package com.br.CalculadoraMacroNutrientes.services;

import java.net.URI;
import java.util.List;

import com.br.CalculadoraMacroNutrientes.controllers.forms.UsuarioUpdateForm;
import com.br.CalculadoraMacroNutrientes.exceptions.DistribuicaoMacrosNaoEncontradoException;
import com.br.CalculadoraMacroNutrientes.exceptions.ExercicioNaoEncontradoException;
import com.br.CalculadoraMacroNutrientes.exceptions.RefeicaoNaoEncontradoException;
import com.br.CalculadoraMacroNutrientes.exceptions.UsuarioNaoEncontradoException;
import com.br.CalculadoraMacroNutrientes.models.*;
import com.br.CalculadoraMacroNutrientes.models.enums.ObjetivoEnumModel;
import com.br.CalculadoraMacroNutrientes.models.enums.SexoEnum;
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
import com.br.CalculadoraMacroNutrientes.repositories.DistribuicaoMacrosRepository;
import com.br.CalculadoraMacroNutrientes.repositories.ExercicioRepository;
import com.br.CalculadoraMacroNutrientes.repositories.InformacoesUsuarioRepository;
import com.br.CalculadoraMacroNutrientes.repositories.RefeicaoRepository;
import com.br.CalculadoraMacroNutrientes.repositories.UsuarioRepository;

@Slf4j
@NoArgsConstructor
@Service
public class UsuarioService {
	
	private UsuarioRepository usuarioRepository;
	private InformacoesUsuarioRepository informacoesUsuarioRepository;
	private RefeicaoRepository refeicaoRepository; 
	private DistribuicaoMacrosRepository distribuicaoMacrosRepository;
	private ExercicioRepository exercicioRepository;
	
	@Autowired
	public UsuarioService(UsuarioRepository usuarioRepository,
			InformacoesUsuarioRepository informacoesUsuarioRepository,
			RefeicaoRepository refeicaoRepository,
			DistribuicaoMacrosRepository distribuicaoMacrosRepository,
			ExercicioRepository exercicioRepository
			)
	{
		this.usuarioRepository = usuarioRepository;
		this.informacoesUsuarioRepository = informacoesUsuarioRepository;
		this.refeicaoRepository = refeicaoRepository;
		this.distribuicaoMacrosRepository = distribuicaoMacrosRepository;
		this.exercicioRepository = exercicioRepository;
	}
	
	public void calculaTaxaMetabolismoBasal(UsuarioModel usuario) {
	
			double altura = usuario.getInformacoesUsuario().getAltura();
			double peso = usuario.getInformacoesUsuario().getPeso();
			int idade = usuario.getInformacoesUsuario().getIdade();
			
			if (usuario.getInformacoesUsuario().getSexo().equals(SexoEnum.MASCULINO)) {
				double tmbMasculino = 66 + ((13.7 * peso) + (5 * altura) - (6.8 * idade));			
				usuario.getInformacoesUsuario().setTaxaMetabolismoBasal(Math.round(tmbMasculino));
			} else if (usuario.getInformacoesUsuario().getSexo().equals(SexoEnum.FEMININO)) {
				double tmbFeminino = 655 + ((9.6 * peso) + (1.8 * altura) - (4.7 * idade));			
				usuario.getInformacoesUsuario().setTaxaMetabolismoBasal(Math.round(tmbFeminino));
			}
	}

	public Page<UsuarioDto> listaUsuarios(Pageable paginacao) {
		Page<UsuarioModel> usuarios = usuarioRepository.findAll(paginacao);
		return UsuarioDto.converter(usuarios);
	}
	
	public ResponseEntity<UsuarioDto> cadastraUsuario(UsuarioForm form, UriComponentsBuilder uriBuilder) {

		UsuarioModel usuario = form.converter(informacoesUsuarioRepository);

		calculandoDadosDoUsuario(usuario);

		log.info("Cadastrando usuário");
		usuarioRepository.save(usuario);
		
        URI uri = uriBuilder.path("/usuarios/{id}").buildAndExpand(usuario.getId()).toUri();
        
		return ResponseEntity.created(uri).body(new UsuarioDto(usuario));
	}

	private void calculandoDadosDoUsuario(UsuarioModel usuario) {
		log.info("Calculando a taxa de metabolismo basal");
		calculaTaxaMetabolismoBasal(usuario);
		log.info("Calculando a necessidade de calorias diárias");
		calculaNecessidadeDiariaDeCalorias(usuario);
		log.info("Calculando distribuição dos macros nutrientes");
		calculaDistribuicaoMacroNutrientes(usuario);
		log.info("Definindo as calorias disponíveis");
		defineCaloriasRestantes(usuario);
		log.info("Definindo distribuição dos macros disponíveis");
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
		double carboidrato = Math.round(caloriasRestante/4);

		DistribuicaoMacrosModel distribuicaoMacros = new DistribuicaoMacrosModel(carboidrato,proteina,gordura);
		distribuicaoMacrosRepository.save(distribuicaoMacros);
		usuario.setDistribruicaoMacros(distribuicaoMacros);
	}

	public ResponseEntity<?> cadastraRefeicaoParaUsuario(Long idUsuario, Long idRefeicao) {
		try {
			UsuarioModel usuario = usuarioRepository.findById(idUsuario)
					.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario de id " + idUsuario + " não encontrado"));
			RefeicaoModel refeicao = refeicaoRepository.findById(idRefeicao)
					.orElseThrow(() -> new RefeicaoNaoEncontradoException("Refeicao de id " + idRefeicao + " não encontrada"));

			log.info("Adicionando refeição de id {}",idRefeicao);
			usuario.getRefeicoes().add(refeicao);
			atualizaCaloriasRestantesPorRefeicao(usuario,refeicao);
			//preciso atualizar os macros disponiveis
			atualizaDistribuicaoDosMacros(usuario,refeicao);
			usuarioRepository.save(usuario);
			return ResponseEntity.ok().build();
		} catch (UsuarioNaoEncontradoException | RefeicaoNaoEncontradoException e) {
			log.error(e.getMessage());
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	private void atualizaDistribuicaoDosMacros(UsuarioModel usuario, RefeicaoModel refeicao) {
		usuario.getDistribruicaoMacros().subtraiCarboidratoDisponivel(refeicao.getCarboidratosTotais());
		usuario.getDistribruicaoMacros().subtraiProteinaDisponivel(refeicao.getProteinasTotais());
		usuario.getDistribruicaoMacros().subtraiGorduraDisponivel(refeicao.getGordurasTotais());
	}

	private void atualizaCaloriasRestantesPorRefeicao(UsuarioModel usuario, RefeicaoModel refeicao) {
		usuario.getDistribruicaoMacros().subtraiCaloriaConsumo(refeicao.getCaloriasTotais());
	}

	public ResponseEntity<?> cadastraMacros(Long idUsuario,Long idMacros) {
		try {
			UsuarioModel usuario = usuarioRepository.findById(idUsuario)
					.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario de id " + idUsuario + " não encontrado"));
			DistribuicaoMacrosModel distribuicaoMacros = distribuicaoMacrosRepository.findById(idMacros)
					.orElseThrow(() -> new DistribuicaoMacrosNaoEncontradoException("Distribuição de macros, de id " + idMacros + " não encontrada"));

			log.info("Definindo distribuição de macros, de id {}, informada pelo usuário",idMacros);
			usuario.setDistribruicaoMacros(distribuicaoMacros);
			usuarioRepository.save(usuario);
			return ResponseEntity.ok().build();
		} catch (UsuarioNaoEncontradoException | DistribuicaoMacrosNaoEncontradoException e) {
			log.error(e.getMessage());
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	public ResponseEntity<?> detalhaUsuario(Long idUsuario) {
		try {
			UsuarioModel usuario = usuarioRepository.findById(idUsuario)
					.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario de id " + idUsuario + " não encontrado"));
			log.info("Detalhando usuario de id {}",idUsuario);
			return ResponseEntity.ok(new UsuarioDetalharDto(usuario));
		} catch (UsuarioNaoEncontradoException e) {
			log.error(e.getMessage());
			//TROCAR PARA NOTFOUND
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	public ResponseEntity<?> cadastraExercicio(Long idUsuario, Long idExercicio) {
		try{
			UsuarioModel usuario = usuarioRepository.findById(idUsuario)
					.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario de id " + idUsuario + " não encontrado"));
			ExercicioModel exercicio = exercicioRepository.findById(idExercicio)
					.orElseThrow(() -> new ExercicioNaoEncontradoException("Exercício de id " + idExercicio + " não encontrado"));
			log.info("Cadastrando exercício de id {}",idExercicio);
			usuario.getExercicios().add(exercicio);
			atualizaCaloriasRestantesPorExercicio(usuario,exercicio);
			atualizaCarboidratos(usuario,exercicio);
			usuarioRepository.save(usuario);
			return ResponseEntity.ok().build();
		} catch (UsuarioNaoEncontradoException | ExercicioNaoEncontradoException e) {
			log.error(e.getMessage());
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	private void atualizaCarboidratos(UsuarioModel usuario, ExercicioModel exercicio) {
		usuario.getDistribruicaoMacros().adicionaCarboidrato(exercicio.getCaloriasGastas());
		usuario.getDistribruicaoMacros().adicionaCarboidratoDisponivelAPartirDaCaloria(exercicio.getCaloriasGastas());
	}

	private void atualizaCaloriasRestantesPorExercicio(UsuarioModel usuario, ExercicioModel exercicio) {
		usuario.getDistribruicaoMacros().adicionaCaloriaConsumo(exercicio.getCaloriasGastas());
	}
	
	private void calculaNecessidadeDiariaDeCalorias(UsuarioModel usuario) {
		
		double basal = usuario.getInformacoesUsuario().getTaxaMetabolismoBasal();
		double ndc = basal * usuario.getInformacoesUsuario().getFatorAtividadeFisica().getFator();
		usuario.setNecessidadeDiariaCalorias(Math.round(ndc));
		
		if(usuario.getObjetivo().equals(ObjetivoEnumModel.GANHO_PESO)) {
			usuario.adicionaCaloriaNdc(500);
		} else if (usuario.getObjetivo().equals(ObjetivoEnumModel.PERDA_PESO)) {
			usuario.adicionaCaloriaNdc(-500);
		} else if (usuario.getObjetivo().equals(ObjetivoEnumModel.MANTER_PESO)) {
			usuario.adicionaCaloriaNdc(0);
		}
	}

	public ResponseEntity<?> deletaUsuario(Long idUsuario) {
		try {
			UsuarioModel usuario = usuarioRepository.findById(idUsuario)
					.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário de id " + idUsuario + " não encontrado"));
			log.info("Deletando usuário de id {}", idUsuario);
			usuarioRepository.delete(usuario);
			return ResponseEntity.noContent().build();
		} catch (UsuarioNaoEncontradoException e) {
			log.error(e.getMessage());
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	public ResponseEntity<?> atualizaUsuario(UsuarioUpdateForm form) {
		try{
			UsuarioModel usuario = usuarioRepository.findById(form.getId())
					.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario de id " + form.getId() + " não encontrado"));
			UsuarioModel usuarioConvertido = cadastraDadosUsuarioQueEstaSendoAtualizado(form, usuario);
			log.info("Atualizando usuário de id {}",usuario.getId());
			usuarioRepository.save(usuarioConvertido);
			return ResponseEntity.noContent().build();
		} catch (UsuarioNaoEncontradoException e) {
			log.error(e.getMessage());
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	private UsuarioModel cadastraDadosUsuarioQueEstaSendoAtualizado(UsuarioUpdateForm form, UsuarioModel usuario) {
		InformacoesUsuarioModel info = form.converteInfomacoesUsuario(); //verificar se nao vai dar conflito pelo id
		informacoesUsuarioRepository.save(info);
		UsuarioModel usuarioConvertido = form.converterUsuario();
		log.info("Definindo informaçõs do usuário");
		usuarioConvertido.setInformacoesUsuario(info);
		log.info("Definindo distribuição dos macros");
		usuarioConvertido.setDistribruicaoMacros(usuario.getDistribruicaoMacros());
		log.info("Definindo refeições");
		usuarioConvertido.setRefeicoes(usuario.getRefeicoes());
		log.info("Definindo exercícios");
		usuarioConvertido.setExercicios(usuario.getExercicios());
		return usuarioConvertido;
	}

	public ResponseEntity<?> removeRefeicaoDoUsuario(Long idUsuario, Long idRefeicao) {
		try {
			UsuarioModel usuario = usuarioRepository.findById(idUsuario)
					.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario de id " + idUsuario + " não encontrado"));
			List<RefeicaoModel> refeicoes = usuario.getRefeicoes();
			log.info("Removendo refeicão de id {}, caso exista",idRefeicao);
			refeicoes.removeIf(refeicao -> refeicao.getId().equals(idRefeicao));
			usuarioRepository.save(usuario);
			return ResponseEntity.noContent().build();
		} catch (UsuarioNaoEncontradoException e) {
			log.error(e.getMessage());
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	public ResponseEntity<?> removeExercicioDoUsuario(Long idUsuario, Long idExercicio) {
		try {
			UsuarioModel usuario = usuarioRepository.findById(idUsuario)
					.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario de id " + idUsuario + " não encontrado"));
			List<ExercicioModel> exercicios = usuario.getExercicios();
			log.info("Removendo exercicio de id {}, caso exista",idExercicio);
			exercicios.removeIf(exercicio -> exercicio.getId().equals(idExercicio));
			usuarioRepository.save(usuario);
			return ResponseEntity.noContent().build();
		} catch (UsuarioNaoEncontradoException e) {
			log.info(e.getMessage());
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

    public ResponseEntity<?> atualizarPeso(Long idUsuario, double valorPeso) {
		try{
			UsuarioModel usuario = usuarioRepository.findById(idUsuario)
					.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario de id " + idUsuario + " não encontrado"));
			log.info("Recalculando as informações do usuário");
			recalculandoDadosDoUsuario(usuario,valorPeso);
			usuarioRepository.save(usuario);
			return ResponseEntity.ok().build();
		} catch (UsuarioNaoEncontradoException e) {
			log.info(e.getMessage());
			return ResponseEntity.notFound().build();
		}
    }

	private void recalculandoDadosDoUsuario(UsuarioModel usuario, double valorPeso) {
		log.info("Atualizando o peso do usuário {} para {}",usuario.getId(),valorPeso);
		atualizaPeso(usuario,valorPeso);
		log.info("Recalculando a taxa de metabolismo basal");
		calculaTaxaMetabolismoBasal(usuario);
		log.info("Recalculando a necessidade de calorias diárias");
		calculaNecessidadeDiariaDeCalorias(usuario);
		log.info("Recalculando distribuição dos macros nutrientes");
		recalculaDistribuicaoMacroNutrientes(usuario);
		log.info("Redefinindo as calorias disponíveis");
		redefineCaloriasRestantes(usuario);
		log.info("redefinindo distribuição dos macros disponíveis");
		redefineMacrosRestantes(usuario);
	}

	private void atualizaPeso(UsuarioModel usuario, double valorPeso) {
		usuario.getInformacoesUsuario().setPeso(valorPeso);
	}

	private void redefineCaloriasRestantes(UsuarioModel usuario) {
		usuario.getDistribruicaoMacros().setConsumoCaloriasDisponivel(usuario.getNecessidadeDiariaCalorias());
		List<ExercicioModel> exercicios = usuario.getExercicios();
		exercicios.forEach(exercicio -> usuario.getDistribruicaoMacros().adicionaCaloriaConsumo(exercicio.getCaloriasGastas()));

		List<RefeicaoModel> refeicoes = usuario.getRefeicoes();
		refeicoes.forEach(refeicao -> usuario.getDistribruicaoMacros().subtraiCaloriaConsumo(refeicao.getCaloriasTotais()));
	}

	private void redefineMacrosRestantes(UsuarioModel usuario) {

		//ver a necessidade dessa parte
		usuario.getDistribruicaoMacros().setCarboidratoDisponivel(usuario.getDistribruicaoMacros().getCarboidrato());
		usuario.getDistribruicaoMacros().setProteinaDisponivel(usuario.getDistribruicaoMacros().getProteina());
		usuario.getDistribruicaoMacros().setGorduraDisponivel(usuario.getDistribruicaoMacros().getGordura());
		//

		List<ExercicioModel> exercicios = usuario.getExercicios();
		exercicios.forEach(exercicio -> usuario.getDistribruicaoMacros().adicionaCarboidratoDisponivelAPartirDaCaloria(exercicio.getCaloriasGastas()));

		List<RefeicaoModel> refeicoes = usuario.getRefeicoes();
		refeicoes.forEach(refeicao -> {
			usuario.getDistribruicaoMacros().subtraiCarboidratoDisponivel(refeicao.getCarboidratosTotais());
			usuario.getDistribruicaoMacros().subtraiProteinaDisponivel(refeicao.getProteinasTotais());
			usuario.getDistribruicaoMacros().subtraiGorduraDisponivel(refeicao.getGordurasTotais());
		});
	}


	private void recalculaDistribuicaoMacroNutrientes(UsuarioModel usuario) {

		double ndc = usuario.getNecessidadeDiariaCalorias();
		double proteina = 2 * usuario.getInformacoesUsuario().getPeso();
		double gordura = 1 * usuario.getInformacoesUsuario().getPeso();

		double caloriasDisponiveis = ndc - (proteina * 4 + gordura * 9);
		double carboidrato = Math.round(caloriasDisponiveis/4);

		usuario.getDistribruicaoMacros().setCarboidrato(carboidrato);
		usuario.getDistribruicaoMacros().setProteina(proteina);
		usuario.getDistribruicaoMacros().setGordura(gordura);
	}

	public ResponseEntity<?> atualizarObjetivo(Long idUsuario, String objetivo) {
		try {
			UsuarioModel usuario = usuarioRepository.findById(idUsuario)
					.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario de id " + idUsuario + " não encontrado"));
			log.info("Atualizando o objetivo do usuário {} para {}",idUsuario,objetivo);
			String objetivoTratado = objetivo.toUpperCase();
			usuario.setObjetivo(ObjetivoEnumModel.valueOf(objetivoTratado));
			log.info("Recalculando as informações do usuário");
			calculandoDadosDoUsuario(usuario);
			return ResponseEntity.ok().build();
		} catch (UsuarioNaoEncontradoException e) {
			log.info(e.getMessage());
			return ResponseEntity.notFound().build();
		}
	}
}
