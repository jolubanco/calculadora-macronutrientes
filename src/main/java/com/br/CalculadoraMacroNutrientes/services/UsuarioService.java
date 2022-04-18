package com.br.CalculadoraMacroNutrientes.services;

import java.net.URI;
import java.util.Optional;

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
import com.br.CalculadoraMacroNutrientes.models.DistribuicaoMacrosModel;
import com.br.CalculadoraMacroNutrientes.models.ExercicioModel;
import com.br.CalculadoraMacroNutrientes.models.ObjetivoEnumModel;
import com.br.CalculadoraMacroNutrientes.models.RefeicaoModel;
import com.br.CalculadoraMacroNutrientes.models.SexoEnum;
import com.br.CalculadoraMacroNutrientes.models.UsuarioModel;
import com.br.CalculadoraMacroNutrientes.repositories.DistribuicaoMacrosRepository;
import com.br.CalculadoraMacroNutrientes.repositories.ExercicioRepository;
import com.br.CalculadoraMacroNutrientes.repositories.InformacoesUsuarioRepository;
import com.br.CalculadoraMacroNutrientes.repositories.RefeicaoRepository;
import com.br.CalculadoraMacroNutrientes.repositories.UsuarioRepository;

@Slf4j
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
	
	public UsuarioService() {}
	
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
			//usuarioRepository.save(usuario);
	}

	public Page<UsuarioDto> listaUsuarios(Pageable paginacao) {
		Page<UsuarioModel> usuarios = usuarioRepository.findAll(paginacao);
		return UsuarioDto.converter(usuarios);
	}
	
	public ResponseEntity<UsuarioDto> cadastraUsuario(UsuarioForm form, UriComponentsBuilder uriBuilder) {

		UsuarioModel usuario = form.converter(informacoesUsuarioRepository);

		log.info("Calculando distribuição dos macros nutrientes");
        calculaDistribuicaoMacroNutrientes(usuario);
		log.info("Calculando a taxa de metabolismo basal");
		calculaTaxaMetabolismoBasal(usuario);
		log.info("Calculando a necessidade de calorias diárias");
		calculaNecessidadeDiariaDeCalorias(usuario);//arredondar
		log.info("Definindo as calorias disponíveis");
		defineCaloriasRestantes(usuario);
		log.info("Definindo distribuição dos macros disponíveis");
		defineMacrosRestantes(usuario);
		
		usuarioRepository.save(usuario);
		
        URI uri = uriBuilder.path("/usuarios/{id}").buildAndExpand(usuario.getId()).toUri();
        
		return ResponseEntity.created(uri).body(new UsuarioDto(usuario));
	}

	private void defineMacrosRestantes(UsuarioModel usuario) {
		usuario.getDistribruicaoMacros().setCarboidratoDisponivel(usuario.getDistribruicaoMacros().getCarboidrato());
		usuario.getDistribruicaoMacros().setProteinaDisponivel(usuario.getDistribruicaoMacros().getProteina());
		usuario.getDistribruicaoMacros().setGorduraDisponivel(usuario.getDistribruicaoMacros().getGordura());
		//usuarioRepository.save(usuario);
	}

	private void defineCaloriasRestantes(UsuarioModel usuario) {
		usuario.getDistribruicaoMacros().setConsumoCaloriasDisponivel(usuario.getNecessidadeDiariaCalorias());	
	}

	private void calculaDistribuicaoMacroNutrientes(UsuarioModel usuario) {
		
		if(usuario.getObjetivo() == ObjetivoEnumModel.GANHO_PESO) {
			
			double carboidrato = 5 * usuario.getInformacoesUsuario().getPeso();
			double proteina = 2 * usuario.getInformacoesUsuario().getPeso();
			double gordura = 1 * usuario.getInformacoesUsuario().getPeso();
			
			DistribuicaoMacrosModel distribuicaoMacros = new DistribuicaoMacrosModel(carboidrato,proteina,gordura);
			distribuicaoMacrosRepository.save(distribuicaoMacros);
			usuario.setDistribruicaoMacros(distribuicaoMacros);
			//usuarioRepository.save(usuario);
			
		} else if(usuario.getObjetivo() == ObjetivoEnumModel.PERDA_PESO) {
			
			double carboidrato = 3 * usuario.getInformacoesUsuario().getPeso();
			double proteina = 2 * usuario.getInformacoesUsuario().getPeso();
			double gordura = 1 * usuario.getInformacoesUsuario().getPeso();
			
			DistribuicaoMacrosModel distribuicaoMacros = new DistribuicaoMacrosModel(carboidrato,proteina,gordura);
			distribuicaoMacrosRepository.save(distribuicaoMacros);
			usuario.setDistribruicaoMacros(distribuicaoMacros);
			//usuarioRepository.save(usuario);
			
		}
		
	}

	public ResponseEntity<UsuarioDto> cadastraRefeicaoParaUsuario(Long idUsuario, Long idRefeicao) {
		
		Optional<UsuarioModel> usuario = usuarioRepository.findById(idUsuario);
		Optional<RefeicaoModel> refeicao = refeicaoRepository.findById(idRefeicao);
		
		if(usuario.isPresent() && refeicao.isPresent()) {
			
			usuario.get().getRefeicoes().add(refeicao.get());
			atualizaCaloriasRestantesPorRefeicao(usuario.get(),refeicao.get());
			atualizaDistribuicaoDosMacros(usuario.get(),refeicao.get());
			usuarioRepository.save(usuario.get());
			return ResponseEntity.ok(new UsuarioDto(usuario.get()));
			
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	private void atualizaDistribuicaoDosMacros(UsuarioModel usuario, RefeicaoModel refeicao) {
		usuario.getDistribruicaoMacros().subtraiCarboidrato(refeicao.getCarboidratosTotais());
		usuario.getDistribruicaoMacros().subtraiProteina(refeicao.getProteinasTotais());
		usuario.getDistribruicaoMacros().subtraiGordura(refeicao.getGordurasTotais());
	}

	private void atualizaCaloriasRestantesPorRefeicao(UsuarioModel usuario, RefeicaoModel refeicao) {
		usuario.getDistribruicaoMacros().subtraiCaloriaConsumo(refeicao.getCaloriasTotais());
	}

	public ResponseEntity<UsuarioDto> cadastraMacros(Long idUsuario,Long idMacros) {
		Optional<UsuarioModel> usuario = usuarioRepository.findById(idUsuario);
		Optional<DistribuicaoMacrosModel> distribuicaoMacros = distribuicaoMacrosRepository.findById(idMacros);
		
		if(usuario.isPresent() && distribuicaoMacros.isPresent()) {
			usuario.get().setDistribruicaoMacros(distribuicaoMacros.get());
			usuarioRepository.save(usuario.get());
			return ResponseEntity.ok(new UsuarioDto(usuario.get()));
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	public ResponseEntity<UsuarioDetalharDto> detalhaUsuario(Long idUsuario) {
		Optional<UsuarioModel> usuario = usuarioRepository.findById(idUsuario);
		if(usuario.isPresent()) {
			return ResponseEntity.ok(new UsuarioDetalharDto(usuario.get()));
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	public ResponseEntity<UsuarioDto> cadastraExercicio(Long idUsuario, Long idExercicio) {
		
		Optional<UsuarioModel> usuario = usuarioRepository.findById(idUsuario);
		Optional<ExercicioModel> exercicio = exercicioRepository.findById(idExercicio);
		
		if(usuario.isPresent() && exercicio.isPresent()) {
			usuario.get().getExercicios().add(exercicio.get());
			atualizaCaloriasRestantesPorExercicio(usuario.get(),exercicio.get());
			usuarioRepository.save(usuario.get());
			return ResponseEntity.ok(new UsuarioDto(usuario.get()));
		} else {
			log.info("Usuário ou refeição não encontrados");
			return ResponseEntity.notFound().build();
		}
	}

	private void atualizaCaloriasRestantesPorExercicio(UsuarioModel usuario, ExercicioModel exercicio) {
		usuario.getDistribruicaoMacros().adicionaCaloriaConsumo(exercicio.getCaloriasGastas());
	}
	
	private void calculaNecessidadeDiariaDeCalorias(UsuarioModel usuario) {
		
		double basal = usuario.getInformacoesUsuario().getTaxaMetabolismoBasal();
		double ndc = basal * usuario.getInformacoesUsuario().getFatorAtividadeFisica().getFator();
		usuario.setNecessidadeDiariaCalorias(ndc);
		
		if(usuario.getObjetivo() == ObjetivoEnumModel.GANHO_PESO) {
			usuario.adicionaCaloriaNdc(500);
		} else if (usuario.getObjetivo() == ObjetivoEnumModel.PERDA_PESO) {
			usuario.adicionaCaloriaNdc(-500);
		} else if (usuario.getObjetivo() == ObjetivoEnumModel.MANTER_PESO) {
			usuario.adicionaCaloriaNdc(0);
		}
	}
}
