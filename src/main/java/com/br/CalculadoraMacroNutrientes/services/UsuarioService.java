package com.br.CalculadoraMacroNutrientes.services;

import java.net.URI;
import java.util.List;

import com.br.CalculadoraMacroNutrientes.controllers.forms.UsuarioUpdateForm;
import com.br.CalculadoraMacroNutrientes.exceptions.DistribuicaoMacrosNaoEncontradoException;
import com.br.CalculadoraMacroNutrientes.exceptions.ExercicioNaoEncontradoException;
import com.br.CalculadoraMacroNutrientes.exceptions.RefeicaoNaoEncontradoException;
import com.br.CalculadoraMacroNutrientes.exceptions.UsuarioNaoEncontradoException;
import com.br.CalculadoraMacroNutrientes.history.HistoricoPeso;
import com.br.CalculadoraMacroNutrientes.models.*;
import com.br.CalculadoraMacroNutrientes.models.enums.ObjetivoEnumModel;
import com.br.CalculadoraMacroNutrientes.models.enums.SexoEnum;
import com.br.CalculadoraMacroNutrientes.repositories.*;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.br.CalculadoraMacroNutrientes.controllers.dtos.UsuarioDetalharDto;
import com.br.CalculadoraMacroNutrientes.controllers.dtos.UsuarioDto;
import com.br.CalculadoraMacroNutrientes.controllers.forms.UsuarioForm;

@Slf4j
@NoArgsConstructor
@Service
public class UsuarioService {
	
	private UsuarioRepository usuarioRepository;
	private InformacoesUsuarioRepository informacoesUsuarioRepository;
	private RefeicaoRepository refeicaoRepository; 
	private DistribuicaoMacrosRepository distribuicaoMacrosRepository;
	private ExercicioRepository exercicioRepository;

	private HistoricoPesoRepository historicoPesoRepository;
	
	@Autowired
	public UsuarioService(
			UsuarioRepository usuarioRepository,
			InformacoesUsuarioRepository informacoesUsuarioRepository,
			RefeicaoRepository refeicaoRepository,
			DistribuicaoMacrosRepository distribuicaoMacrosRepository,
			ExercicioRepository exercicioRepository,
			HistoricoPesoRepository historicoPesoRepository
	)
	{
		this.usuarioRepository = usuarioRepository;
		this.informacoesUsuarioRepository = informacoesUsuarioRepository;
		this.refeicaoRepository = refeicaoRepository;
		this.distribuicaoMacrosRepository = distribuicaoMacrosRepository;
		this.exercicioRepository = exercicioRepository;
		this.historicoPesoRepository = historicoPesoRepository;
	}
	
	public void calculaTaxaMetabolismoBasal(UsuarioModel usuario) {
		double altura = usuario.getInformacoesUsuario().getAltura();
		double peso = usuario.getInformacoesUsuario().getPeso();
		int idade = usuario.getInformacoesUsuario().getIdade();

		if (usuario.getInformacoesUsuario().getSexo().equals(SexoEnum.MASCULINO)) {
			double tmbMasculino = 66 + ((13.7 * peso) + (5 * altura) - (6.8 * idade));
			usuario.getInformacoesUsuario().setTaxaMetabolismoBasal(tmbMasculino);
		} else if (usuario.getInformacoesUsuario().getSexo().equals(SexoEnum.FEMININO)) {
			double tmbFeminino = 655 + ((9.6 * peso) + (1.8 * altura) - (4.7 * idade));
			usuario.getInformacoesUsuario().setTaxaMetabolismoBasal(tmbFeminino);
		}
	}

	public Page<UsuarioDto> listaUsuarios(Pageable paginacao) {
		Page<UsuarioModel> usuarios = usuarioRepository.findAll(paginacao);
		return UsuarioDto.converter(usuarios);
	}
	
	public ResponseEntity<UsuarioDto> cadastraUsuario(UsuarioForm form, UriComponentsBuilder uriBuilder) {
		UsuarioModel usuario = form.converter(informacoesUsuarioRepository);
		calculandoDadosDoUsuario(usuario);
		log.info("Cadastrando usu??rio");
		usuarioRepository.save(usuario);

		log.info("Atualizando a tabela de hist??rico de peso");
		HistoricoPeso historicoPeso = HistoricoPeso.builder()
				.peso(usuario.getInformacoesUsuario().getPeso())
				.idUsuario(usuario.getId())
				.build();
		historicoPesoRepository.save(historicoPeso);

        URI uri = uriBuilder.path("/usuarios/{id}").buildAndExpand(usuario.getId()).toUri();
		return ResponseEntity.created(uri).body(new UsuarioDto(usuario));
	}

	private void calculandoDadosDoUsuario(UsuarioModel usuario) {
		log.info("Calculando a taxa de metabolismo basal");
		calculaTaxaMetabolismoBasal(usuario);
		log.info("Calculando a necessidade de calorias di??rias");
		calculaNecessidadeDiariaDeCalorias(usuario);
		log.info("Calculando distribui????o dos macros nutrientes");
		calculaDistribuicaoMacroNutrientes(usuario);
		log.info("Definindo as calorias dispon??veis");
		defineCaloriasRestantes(usuario);
		log.info("Definindo distribui????o dos macros dispon??veis");
		defineMacrosRestantes(usuario);
	}

	private void defineMacrosRestantes(UsuarioModel usuario) {
		usuario.getDistribruicaoMacros().setCarboidratoDisponivel(usuario.getDistribruicaoMacros().getCarboidrato());
		usuario.getDistribruicaoMacros().setProteinaDisponivel(usuario.getDistribruicaoMacros().getProteina());
		usuario.getDistribruicaoMacros().setGorduraDisponivel(usuario.getDistribruicaoMacros().getGordura());
	}

	private void defineCaloriasRestantes(UsuarioModel usuario) {
		usuario.getDistribruicaoMacros().setConsumoCaloriasDisponivel(usuario.getNecessidadeDiariaCalorias());	
	}

	private void calculaDistribuicaoMacroNutrientes(UsuarioModel usuario) {
		double caloriasLimite = usuario.getNecessidadeDiariaCalorias();
		double proteina = 2 * usuario.getInformacoesUsuario().getPeso();
		double gordura = 1 * usuario.getInformacoesUsuario().getPeso();

		double caloriasRestante = caloriasLimite - (proteina * 4 + gordura * 9);
		double carboidrato = caloriasRestante/4;

		DistribuicaoMacrosModel distribuicaoMacros = new DistribuicaoMacrosModel(carboidrato,proteina,gordura);
		distribuicaoMacrosRepository.save(distribuicaoMacros);
		usuario.setDistribruicaoMacros(distribuicaoMacros);
	}

	public ResponseEntity<?> cadastraRefeicaoParaUsuario(Long idUsuario, Long idRefeicao) {
		try {
			UsuarioModel usuario = usuarioRepository.findById(idUsuario)
					.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario de id " + idUsuario + " n??o encontrado"));
			RefeicaoModel refeicao = refeicaoRepository.findById(idRefeicao)
					.orElseThrow(() -> new RefeicaoNaoEncontradoException("Refeicao de id " + idRefeicao + " n??o encontrada"));

			log.info("Adicionando refei????o de id {}",idRefeicao);
			usuario.getRefeicoes().add(refeicao);
			log.info("Atualizando calorias restantes");
			atualizaCaloriasRestantesPorRefeicao(usuario,refeicao);
			log.info("Atualizando os macros dispon??veis");
			atualizaDistribuicaoDosMacros(usuario,refeicao);
			usuarioRepository.save(usuario);
			return ResponseEntity.noContent().build();
		} catch (UsuarioNaoEncontradoException | RefeicaoNaoEncontradoException e) {
			log.error(e.getMessage());
			return ResponseEntity.notFound().build();
		}
	}

	private void atualizaDistribuicaoDosMacros(UsuarioModel usuario, RefeicaoModel refeicao) {
		usuario.getDistribruicaoMacros().subtraiCarboidratoDisponivel(refeicao.getCarboidratosTotais());
		usuario.getDistribruicaoMacros().subtraiProteinaDisponivel(refeicao.getProteinasTotais());
		usuario.getDistribruicaoMacros().subtraiGorduraDisponivel(refeicao.getGordurasTotais());
	}

	private void atualizaCaloriasRestantesPorRefeicao(UsuarioModel usuario, RefeicaoModel refeicao) {
		usuario.getDistribruicaoMacros().subtraiCaloriaDisponivel(refeicao.getCaloriasTotais());
	}

	public ResponseEntity<?> cadastraMacros(Long idUsuario,Long idMacros) {
		try {
			UsuarioModel usuario = usuarioRepository.findById(idUsuario)
					.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario de id " + idUsuario + " n??o encontrado"));
			DistribuicaoMacrosModel distribuicaoMacrosAtualizada = distribuicaoMacrosRepository.findById(idMacros)
					.orElseThrow(() -> new DistribuicaoMacrosNaoEncontradoException("Distribui????o de macros, de id " + idMacros + " n??o encontrada"));
			log.info("Definindo distribui????o de macros, de id {}, informada pelo usu??rio",idMacros);
			usuario.setDistribruicaoMacros(distribuicaoMacrosAtualizada);
			log.info("Calculando os macros dispon??veis");
			calculaMacrosRestantesAPartirDosMacrosDefinidos(usuario);
			usuarioRepository.save(usuario);
			return ResponseEntity.noContent().build();
		} catch (UsuarioNaoEncontradoException | DistribuicaoMacrosNaoEncontradoException e) {
			log.error(e.getMessage());
			return ResponseEntity.notFound().build();
		}
	}

	private void calculaMacrosRestantesAPartirDosMacrosDefinidos(UsuarioModel usuario) {
		double consumoCaloriasDisponiveis = 4 * usuario.getDistribruicaoMacros().getCarboidrato()
				+ 4 * usuario.getDistribruicaoMacros().getProteina()
				+ 9 * usuario.getDistribruicaoMacros().getGordura();
		usuario.getDistribruicaoMacros().setConsumoCaloriasDisponivel(consumoCaloriasDisponiveis);
		atualizandoMacrosDoUsuarioPorRefeicao(usuario);
		atualizandoMacrosDoUsuarioPorExercicio(usuario);
	}

	private void atualizandoMacrosDoUsuarioPorExercicio(UsuarioModel usuario) {
		usuario.getExercicios().forEach(exercicio -> {
			log.info("Atualizando as calorias restantes");
			atualizaCaloriasRestantesPorExercicio(usuario, exercicio);
			log.info("Atualizando os macros");
			atualizaCarboidratos(usuario, exercicio);
		});
	}

	private void atualizandoMacrosDoUsuarioPorRefeicao(UsuarioModel usuario) {
		usuario.getRefeicoes().forEach(refeicao -> {
			log.info("Atualizando calorias restantes");
			atualizaCaloriasRestantesPorRefeicao(usuario,refeicao);
			log.info("Atualizando os macros dispon??veis");
			atualizaDistribuicaoDosMacros(usuario,refeicao);
		});
	}

	public ResponseEntity<?> detalhaUsuario(Long idUsuario) {
		try {
			UsuarioModel usuario = usuarioRepository.findById(idUsuario)
					.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario de id " + idUsuario + " n??o encontrado"));
			log.info("Detalhando usuario de id {}",idUsuario);
			return ResponseEntity.ok(new UsuarioDetalharDto(usuario));
		} catch (UsuarioNaoEncontradoException e) {
			log.error(e.getMessage());
			return ResponseEntity.notFound().build();
		}
	}

	public ResponseEntity<?> cadastraExercicio(Long idUsuario, Long idExercicio) {
		try{
			UsuarioModel usuario = usuarioRepository.findById(idUsuario)
					.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario de id " + idUsuario + " n??o encontrado"));
			ExercicioModel exercicio = exercicioRepository.findById(idExercicio)
					.orElseThrow(() -> new ExercicioNaoEncontradoException("Exerc??cio de id " + idExercicio + " n??o encontrado"));

			atualizandoInformacoesAPartirDoCadastroDoExercicio(idExercicio, usuario, exercicio);
			usuarioRepository.save(usuario);
			return ResponseEntity.noContent().build();
		} catch (UsuarioNaoEncontradoException | ExercicioNaoEncontradoException e) {
			log.error(e.getMessage());
			return ResponseEntity.notFound().build();
		}
	}

	private void atualizandoInformacoesAPartirDoCadastroDoExercicio(Long idExercicio, UsuarioModel usuario, ExercicioModel exercicio) {
		log.info("Cadastrando exerc??cio de id {}", idExercicio);
		usuario.getExercicios().add(exercicio);
		log.info("Atualizando as calorias restantes");
		atualizaCaloriasRestantesPorExercicio(usuario, exercicio);
		log.info("Atualizando os macros");
		atualizaCarboidratos(usuario, exercicio);
	}

	private void atualizaCarboidratos(UsuarioModel usuario, ExercicioModel exercicio) {
		usuario.getDistribruicaoMacros().adicionaCarboidratosDisponivelAPartirDaCaloria(exercicio.getCaloriasGastas());
	}

	private void atualizaCaloriasRestantesPorExercicio(UsuarioModel usuario, ExercicioModel exercicio) {
		usuario.getDistribruicaoMacros().adicionaCaloriaDisponivel(exercicio.getCaloriasGastas());
	}
	
	private void calculaNecessidadeDiariaDeCalorias(UsuarioModel usuario) {
		
		double basal = usuario.getInformacoesUsuario().getTaxaMetabolismoBasal();
		double ndc = basal * usuario.getInformacoesUsuario().getFatorAtividadeFisica().getFator();
		usuario.setNecessidadeDiariaCalorias(ndc);
		
		if(usuario.getObjetivo().equals(ObjetivoEnumModel.GANHAR_PESO)) {
			usuario.adicionaCaloriaNdc(500);
		} else if (usuario.getObjetivo().equals(ObjetivoEnumModel.PERDER_PESO)) {
			usuario.adicionaCaloriaNdc(-500);
		} else if (usuario.getObjetivo().equals(ObjetivoEnumModel.MANTER_PESO)) {
			usuario.adicionaCaloriaNdc(0);
		}
	}

	public ResponseEntity<?> deletaUsuario(Long idUsuario) {
		try {
			UsuarioModel usuario = usuarioRepository.findById(idUsuario)
					.orElseThrow(() -> new UsuarioNaoEncontradoException("Usu??rio de id " + idUsuario + " n??o encontrado"));
			log.info("Deletando usu??rio de id {}", idUsuario);
			usuarioRepository.delete(usuario);
			return ResponseEntity.noContent().build();
		} catch (UsuarioNaoEncontradoException e) {
			log.error(e.getMessage());
			return ResponseEntity.notFound().build();
		}
	}

	public ResponseEntity<?> atualizaUsuario(UsuarioUpdateForm form) {
		try{
			UsuarioModel usuario = usuarioRepository.findById(form.getId())
					.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario de id " + form.getId() + " n??o encontrado"));
			UsuarioModel usuarioConvertido = cadastraDadosUsuarioQueEstaSendoAtualizado(form, usuario);

			log.info("Recalculado as informa????es do usu??rio");
			calculandoDadosDoUsuario(usuarioConvertido);
			atualizandoMacrosDoUsuarioPorRefeicao(usuarioConvertido);
			atualizandoMacrosDoUsuarioPorExercicio(usuarioConvertido);

			log.info("Atualizando usu??rio de id {}",usuario.getId());
			usuarioRepository.save(usuarioConvertido);

			log.info("Atualizando a tabela de hist??rico de peso");
			HistoricoPeso historicoPeso = HistoricoPeso.builder()
					.peso(usuarioConvertido.getInformacoesUsuario().getPeso())
					.idUsuario(usuarioConvertido.getId())
					.build();
			historicoPesoRepository.save(historicoPeso);

			return ResponseEntity.noContent().build();
		} catch (UsuarioNaoEncontradoException e) {
			log.error(e.getMessage());
			return ResponseEntity.notFound().build();
		}
	}

	private UsuarioModel cadastraDadosUsuarioQueEstaSendoAtualizado(UsuarioUpdateForm form, UsuarioModel usuario) {
		InformacoesUsuarioModel info = form.converteInfomacoesUsuario();
		informacoesUsuarioRepository.save(info);
		UsuarioModel usuarioConvertido = form.converterUsuario();
		log.info("Definindo informa????s do usu??rio");
		usuarioConvertido.setInformacoesUsuario(info);
		usuarioConvertido.setObjetivo(usuario.getObjetivo());
		usuarioConvertido.getInformacoesUsuario().setPeso(usuario.getInformacoesUsuario().getPeso());
		log.info("Definindo refei????es");
		usuarioConvertido.setRefeicoes(usuario.getRefeicoes());
		log.info("Definindo exerc??cios");
		usuarioConvertido.setExercicios(usuario.getExercicios());
		return usuarioConvertido;
	}

	public ResponseEntity<?> removeRefeicaoDoUsuario(Long idUsuario, Long idRefeicao) {
		try {
			UsuarioModel usuario = usuarioRepository.findById(idUsuario)
					.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario de id " + idUsuario + " n??o encontrado"));

			RefeicaoModel refeicaoASerRemovida = usuario.getRefeicoes()
					.stream()
					.filter(ref -> ref.getId().equals(idRefeicao))
					.findFirst()
					.orElseThrow(() -> new RefeicaoNaoEncontradoException("Refei????o de id " + idRefeicao + " n??o encontrada"));

			List<RefeicaoModel> refeicoes = usuario.getRefeicoes();
			log.info("Removendo refeic??o de id {}, caso exista",idRefeicao);
			refeicoes.removeIf(refeicao -> refeicao.getId().equals(idRefeicao));

			log.info("Atualizando informa????es do usu??rio");
			atualizandoInformacoesAPartirDaRemocaoDaRefeicao(refeicaoASerRemovida,usuario);

			usuarioRepository.save(usuario);
			return ResponseEntity.noContent().build();
		} catch (UsuarioNaoEncontradoException e) {
			log.error(e.getMessage());
			return ResponseEntity.notFound().build();
		}
	}

	private void atualizandoInformacoesAPartirDaRemocaoDaRefeicao(RefeicaoModel refeicaoASerRemovida, UsuarioModel usuario) {
		usuario.getDistribruicaoMacros().adicionaCaloriaDisponivel(refeicaoASerRemovida.getCaloriasTotais());
		usuario.getDistribruicaoMacros().adicionaCarboidratoDisponivel(refeicaoASerRemovida.getCarboidratosTotais());
		usuario.getDistribruicaoMacros().adicionaProteinaDisponivel(refeicaoASerRemovida.getProteinasTotais());
		usuario.getDistribruicaoMacros().adicionaGorguraDisponivel(refeicaoASerRemovida.getGordurasTotais());
	}

	public ResponseEntity<?> removeExercicioDoUsuario(Long idUsuario, Long idExercicio) {
		try {
			UsuarioModel usuario = usuarioRepository.findById(idUsuario)
					.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario de id " + idUsuario + " n??o encontrado"));

			ExercicioModel exercicioASerExcluido = usuario.getExercicios()
					.stream()
					.filter(exer -> exer.getId().equals(idExercicio))
					.findFirst()
					.orElseThrow(() -> new ExercicioNaoEncontradoException("Exercicio de id " + idExercicio + " n??o encontrado"));

			List<ExercicioModel> exercicios = usuario.getExercicios();
			log.info("Removendo exercicio de id {}, caso exista",idExercicio);
			exercicios.removeIf(exercicio -> exercicio.getId().equals(idExercicio));

			log.info("Atualizando informa????es do usu??rio");
			atualizandoInformacoesAPartirDaRemocaoDoExercicio(exercicioASerExcluido,usuario);

			usuarioRepository.save(usuario);
			return ResponseEntity.noContent().build();
		} catch (UsuarioNaoEncontradoException e) {
			log.info(e.getMessage());
			return ResponseEntity.notFound().build();
		}
	}

	private void atualizandoInformacoesAPartirDaRemocaoDoExercicio(ExercicioModel exercicioASerExcluido, UsuarioModel usuario) {
		usuario.getDistribruicaoMacros().subtraiCaloriaDisponivel(exercicioASerExcluido.getCaloriasGastas());
		usuario.getDistribruicaoMacros().subtraiCarboidratosDisponivelAPartirDaCaloria(exercicioASerExcluido.getCaloriasGastas());
	}

	public ResponseEntity<?> atualizarPeso(Long idUsuario, double valorPeso) {
		try{
			UsuarioModel usuario = usuarioRepository.findById(idUsuario)
					.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario de id " + idUsuario + " n??o encontrado"));
			log.info("Recalculando as informa????es do usu??rio ap??s atualiza????o do peso");
			recalculandoDadosDoUsuarioAposAtualizacaoDoPeso(usuario,valorPeso);
			usuarioRepository.save(usuario);

			log.info("Atualizando a tabela de hist??rico de peso");
			HistoricoPeso historicoPeso = HistoricoPeso.builder()
					.peso(usuario.getInformacoesUsuario().getPeso())
					.idUsuario(usuario.getId())
					.build();
			historicoPesoRepository.save(historicoPeso);

			return ResponseEntity.noContent().build();
		} catch (UsuarioNaoEncontradoException e) {
			log.info(e.getMessage());
			return ResponseEntity.notFound().build();
		}
    }

	private void recalculandoDadosDoUsuarioAposAtualizacaoDoPeso(UsuarioModel usuario, double valorPeso) {
		log.info("Atualizando o peso do usu??rio {} para {}",usuario.getId(),valorPeso);
		atualizaPeso(usuario,valorPeso);
		log.info("Recalculando a taxa de metabolismo basal");
		calculaTaxaMetabolismoBasal(usuario);
		log.info("Recalculando a necessidade de calorias di??rias");
		calculaNecessidadeDiariaDeCalorias(usuario);
		log.info("Recalculando distribui????o dos macros nutrientes");
		recalculaDistribuicaoMacroNutrientes(usuario);
		log.info("Redefinindo as calorias dispon??veis");
		redefineCaloriasRestantes(usuario);
		log.info("redefinindo distribui????o dos macros dispon??veis");
		redefineMacrosRestantes(usuario);
	}

	private void recalculandoDadosDoUsuarioAposAtualizacaoDoObjetivo(UsuarioModel usuario, String objetivo) {
		log.info("Atualizando o objetivo do usu??rio {} para {}",usuario.getId(),objetivo);
		String objetivoTratado = objetivo.toUpperCase();
		usuario.setObjetivo(ObjetivoEnumModel.valueOf(objetivoTratado));

		log.info("Recalculando a necessidade de calorias di??rias");
		calculaNecessidadeDiariaDeCalorias(usuario);

		log.info("Recalculando distribui????o dos macros nutrientes");
		recalculaDistribuicaoDeCarboidrato(usuario);

		log.info("Redefinindo as calorias dispon??veis");
		redefineCaloriasRestantes(usuario);
		//problema est?? aqui ou na cria????o do usuario
		log.info("Redefinindo distribui????o dos macros dispon??veis");
		redefineCarboidratoRestantes(usuario);
	}



	private void atualizaPeso(UsuarioModel usuario, double valorPeso) {
		usuario.getInformacoesUsuario().setPeso(valorPeso);
	}

	private void redefineCaloriasRestantes(UsuarioModel usuario) {
		usuario.getDistribruicaoMacros().setConsumoCaloriasDisponivel(usuario.getNecessidadeDiariaCalorias());
		List<ExercicioModel> exercicios = usuario.getExercicios();
		exercicios.forEach(exercicio -> usuario.getDistribruicaoMacros().adicionaCaloriaDisponivel(exercicio.getCaloriasGastas()));

		List<RefeicaoModel> refeicoes = usuario.getRefeicoes();
		refeicoes.forEach(refeicao -> usuario.getDistribruicaoMacros().subtraiCaloriaDisponivel(refeicao.getCaloriasTotais()));
	}

	private void redefineCarboidratoRestantes(UsuarioModel usuario) {

		//ver a necessidade dessa parte
		usuario.getDistribruicaoMacros().setCarboidratoDisponivel(usuario.getDistribruicaoMacros().getCarboidrato());
		//

		List<ExercicioModel> exercicios = usuario.getExercicios();
		exercicios.forEach(exercicio -> usuario.getDistribruicaoMacros().adicionaCarboidratosDisponivelAPartirDaCaloria(exercicio.getCaloriasGastas()));

		List<RefeicaoModel> refeicoes = usuario.getRefeicoes();
		refeicoes.forEach(refeicao -> {
			usuario.getDistribruicaoMacros().subtraiCarboidratoDisponivel(refeicao.getCarboidratosTotais());
		});
	}

	private void redefineMacrosRestantes(UsuarioModel usuario) {

		//ver a necessidade dessa parte
		usuario.getDistribruicaoMacros().setCarboidratoDisponivel(usuario.getDistribruicaoMacros().getCarboidrato());
		usuario.getDistribruicaoMacros().setProteinaDisponivel(usuario.getDistribruicaoMacros().getProteina());
		usuario.getDistribruicaoMacros().setGorduraDisponivel(usuario.getDistribruicaoMacros().getGordura());
		//

		List<ExercicioModel> exercicios = usuario.getExercicios();
		exercicios.forEach(exercicio -> usuario.getDistribruicaoMacros().adicionaCarboidratosDisponivelAPartirDaCaloria(exercicio.getCaloriasGastas()));

		List<RefeicaoModel> refeicoes = usuario.getRefeicoes();
		refeicoes.forEach(refeicao -> {
			usuario.getDistribruicaoMacros().subtraiCarboidratoDisponivel(refeicao.getCarboidratosTotais());
			usuario.getDistribruicaoMacros().subtraiProteinaDisponivel(refeicao.getProteinasTotais());
			usuario.getDistribruicaoMacros().subtraiGorduraDisponivel(refeicao.getGordurasTotais());
		});
	}


	private void recalculaDistribuicaoDeCarboidrato(UsuarioModel usuario) {
		double ndc = usuario.getNecessidadeDiariaCalorias();
		double caloriasDisponiveis = ndc - (usuario.getDistribruicaoMacros().getProteina() * 4 + usuario.getDistribruicaoMacros().getGordura() * 9);
		double carboidrato = caloriasDisponiveis/4;
		usuario.getDistribruicaoMacros().setCarboidrato(carboidrato);
	}

	private void recalculaDistribuicaoMacroNutrientes(UsuarioModel usuario) {

		double ndc = usuario.getNecessidadeDiariaCalorias();
		double proteina = 2 * usuario.getInformacoesUsuario().getPeso();
		double gordura = 1 * usuario.getInformacoesUsuario().getPeso();

		double caloriasDisponiveis = ndc - (proteina * 4 + gordura * 9);
		double carboidrato = caloriasDisponiveis/4;

		usuario.getDistribruicaoMacros().setCarboidrato(carboidrato);
		usuario.getDistribruicaoMacros().setProteina(proteina);
		usuario.getDistribruicaoMacros().setGordura(gordura);
	}

	public ResponseEntity<?> atualizarObjetivo(Long idUsuario, String objetivo) {
		try {
			UsuarioModel usuario = usuarioRepository.findById(idUsuario)
					.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario de id " + idUsuario + " n??o encontrado"));
			log.info("Recalculando as informa????es do usu??rio ap??s atualiza????o do objetivo");
			recalculandoDadosDoUsuarioAposAtualizacaoDoObjetivo(usuario,objetivo);
			usuarioRepository.save(usuario);
			return ResponseEntity.noContent().build();
		} catch (UsuarioNaoEncontradoException e) {
			log.info(e.getMessage());
			return ResponseEntity.notFound().build();
		}
	}

	public ResponseEntity<?> resetaMacros(Long idUsuario) {
		try{
			UsuarioModel usuario = usuarioRepository.findById(idUsuario)
					.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario de id " + idUsuario + " n??o encontrado"));
			log.info("Recalculando as informa????es do usuario de id {}",usuario.getId());
			//rever, porque est?? setando os valores sem levar em considera????o as refeicoes e exercicios existentes
			calculandoDadosDoUsuario(usuario);
			usuarioRepository.save(usuario);
			return ResponseEntity.noContent().build();
		} catch (UsuarioNaoEncontradoException e) {
			log.error(e.getMessage());
			return ResponseEntity.notFound().build();
		}
	}
}
