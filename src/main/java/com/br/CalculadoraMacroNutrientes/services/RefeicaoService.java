package com.br.CalculadoraMacroNutrientes.services;

import java.net.URI;
import java.util.List;

import com.br.CalculadoraMacroNutrientes.controllers.forms.RefeicaoUpdateForm;
import com.br.CalculadoraMacroNutrientes.exceptions.AlimentoNaoEncontradoException;
import com.br.CalculadoraMacroNutrientes.exceptions.RefeicaoNaoEncontradoException;
import com.br.CalculadoraMacroNutrientes.exceptions.UsuarioNaoEncontradoException;
import com.br.CalculadoraMacroNutrientes.models.UsuarioModel;
import com.br.CalculadoraMacroNutrientes.repositories.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.br.CalculadoraMacroNutrientes.controllers.dtos.RefeicaoDetalharDto;
import com.br.CalculadoraMacroNutrientes.controllers.dtos.RefeicaoDto;
import com.br.CalculadoraMacroNutrientes.controllers.forms.RefeicaoForm;
import com.br.CalculadoraMacroNutrientes.models.AlimentoModel;
import com.br.CalculadoraMacroNutrientes.models.RefeicaoModel;
import com.br.CalculadoraMacroNutrientes.repositories.AlimentoRepository;
import com.br.CalculadoraMacroNutrientes.repositories.RefeicaoRepository;

@Slf4j
@Service
public class RefeicaoService {
	
	private RefeicaoRepository refeicaoRepository;
	private AlimentoRepository alimentoRepository;
	//ver se é uma boa prática
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	public RefeicaoService(RefeicaoRepository refeicaoRepository, AlimentoRepository alimentoRepository,UsuarioRepository usuarioRepository ) {
		this.refeicaoRepository = refeicaoRepository;
		this.alimentoRepository = alimentoRepository;
		this.usuarioRepository = usuarioRepository;
	}

	public ResponseEntity<RefeicaoDto> cadastraRefeicao(RefeicaoForm form, UriComponentsBuilder uriBuilder) {
		RefeicaoModel refeicao = form.converter();
		log.info("Cadastrando nova refeição");
		refeicaoRepository.save(refeicao);
		
        URI uri = uriBuilder.path("/refeicoes/{id}").buildAndExpand(refeicao.getId()).toUri();
		return ResponseEntity.created(uri).body(new RefeicaoDto(refeicao));
	}

	public ResponseEntity<?> adicionaAlimentoNaRefeicao(Long idRefeicao, Long idAlimento, Long idUsuario) {
		try {
			RefeicaoModel refeicao = refeicaoRepository.findById(idRefeicao)
					.orElseThrow(() -> new RefeicaoNaoEncontradoException("Refeicao de id " + idRefeicao + " não encontrada"));
			AlimentoModel alimento = alimentoRepository.findById(idAlimento)
					.orElseThrow(() -> new AlimentoNaoEncontradoException("Alimento de id " + idAlimento + " não encontrado"));

			log.info("Adicionando alimento na refeição");
			refeicao.getAlimentos().add(alimento);
			adicionaMacrosDaRefeicao(refeicao,alimento);

			if(idUsuario != null) {
				subtraiMacrosDisponiveisDoUsuario(idUsuario,alimento);
			}
			return ResponseEntity.ok(new RefeicaoDto(refeicao));
		} catch (RefeicaoNaoEncontradoException | AlimentoNaoEncontradoException e) {
			log.error(e.getMessage());
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}

	private void adicionaMacrosDisponiveisDoUsuario(Long idUsuario, AlimentoModel alimento) {
		try{
			UsuarioModel usuario = usuarioRepository.findById(idUsuario)
					.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário de id " + idUsuario + " não encontrado"));
			log.info("Atualizando os macros disponíveis do usuário após a remoção de um alimento");

			usuario.getDistribruicaoMacros().adicionaCaloriaConsumo(alimento.getCalorias());
			usuario.getDistribruicaoMacros().adicionaCarboidratoDisponivel(alimento.getCarboidrato());
			usuario.getDistribruicaoMacros().adicionaProteinaDisponivel(alimento.getProteina());
			usuario.getDistribruicaoMacros().adicionaGorguraDisponivel(alimento.getGordura());

			log.info("Salvando os dados do usuário atualizados");
			usuarioRepository.save(usuario);
		} catch (UsuarioNaoEncontradoException e) {
			log.info(e.getMessage());
		}
	}

	private void subtraiMacrosDisponiveisDoUsuario(Long idUsuario, AlimentoModel alimento) {
		try{
			UsuarioModel usuario = usuarioRepository.findById(idUsuario)
					.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário de id " + idUsuario + " não encontrado"));
			log.info("Atualizando os macros disponíveis do usuário após a inserção de um alimento");

			usuario.getDistribruicaoMacros().subtraiCaloriaConsumo(alimento.getCalorias());
			usuario.getDistribruicaoMacros().subtraiCarboidratoDisponivel(alimento.getCarboidrato());
			usuario.getDistribruicaoMacros().subtraiProteinaDisponivel(alimento.getProteina());
			usuario.getDistribruicaoMacros().subtraiGorduraDisponivel(alimento.getGordura());

			log.info("Salvando os dados do usuário atualizados");
			usuarioRepository.save(usuario);
		} catch (UsuarioNaoEncontradoException e) {
			log.info(e.getMessage());
		}
	}

	private void adicionaMacrosDaRefeicao(RefeicaoModel refeicao, AlimentoModel alimento) {
		refeicao.adicionaCarboidratos(alimento.getCarboidrato());
		refeicao.adicionaCalorias(alimento.getCalorias());
		refeicao.adicionaProteinas(alimento.getProteina());
		refeicao.adicionaGorduras(alimento.getGordura());
		refeicaoRepository.save(refeicao);
	}

	private void subtraiMacrosDaRefeicao(RefeicaoModel refeicao, AlimentoModel alimento) {
		refeicao.subtraiCarboitradosTotais(alimento.getCarboidrato());
		refeicao.subtraiCaloriasTotais(alimento.getCalorias());
		refeicao.subtraiProteinasTotais(alimento.getProteina());
		refeicao.subtraiGordurasTotais(alimento.getGordura());
		refeicaoRepository.save(refeicao);
	}

	public ResponseEntity<List<RefeicaoDto>> listaRefeicoes() {
		List<RefeicaoModel> refeicoes = refeicaoRepository.findAll();
		return ResponseEntity.ok(RefeicaoDto.converter(refeicoes));
	}

	public ResponseEntity<?> detalhaRefeicao(Long idRefeicao) {
		try {
			RefeicaoModel refeicao = refeicaoRepository.findById(idRefeicao)
					.orElseThrow(() -> new RefeicaoNaoEncontradoException("Refeicao de id " + idRefeicao + " não encontrada"));
			return ResponseEntity.ok(new RefeicaoDetalharDto(refeicao));
		} catch (RefeicaoNaoEncontradoException e) {
			log.error(e.getMessage());
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}

	public ResponseEntity<?> removeAlimentoNaRefeicao(Long idRefeicao, Long idAlimento, Long idUsuario) {
		try{
			RefeicaoModel refeicao = refeicaoRepository.findById(idRefeicao)
					.orElseThrow(() -> new RefeicaoNaoEncontradoException("Refeicao de id " + idRefeicao + " não encontrada"));

			AlimentoModel alimento = refeicao.getAlimentos()
					.stream()
					.filter(alimentoConsultado -> alimentoConsultado.getId().equals(idAlimento))
					.findFirst().orElseThrow(() -> new AlimentoNaoEncontradoException("Alimento de id " + idAlimento + " não encontrado"));

			List<AlimentoModel> alimentos = refeicao.getAlimentos();
			log.info("Removendo alimento de id {}, caso exista",idAlimento);
			alimentos.removeIf(alimentoASerRemovido -> alimentoASerRemovido.getId().equals(idAlimento));

			subtraiMacrosDaRefeicao(refeicao,alimento);

			if(idUsuario != null) {
				adicionaMacrosDisponiveisDoUsuario(idUsuario,alimento);
			}

			refeicaoRepository.save(refeicao);
			return ResponseEntity.noContent().build();
		} catch (RefeicaoNaoEncontradoException | AlimentoNaoEncontradoException e) {
			log.error(e.getMessage());
			return ResponseEntity.notFound().build();
		}

	}

    public ResponseEntity<?> atualizaRefeicao(RefeicaoUpdateForm form, UriComponentsBuilder uri) {
		try {
			refeicaoRepository.findById(form.getId())
					.orElseThrow(() -> new RefeicaoNaoEncontradoException("Refeicao de id " + form.getId() + " não encontrada"));
			RefeicaoModel refeicao = form.converter();
			log.info("Atualizando a refeição de id {}",form.getId());
			refeicaoRepository.save(refeicao);
			return ResponseEntity.ok(new RefeicaoDto(refeicao));
		} catch (RefeicaoNaoEncontradoException e) {
			RefeicaoForm formCadastro = form.converteParaFormSemId();
			return cadastraRefeicao(formCadastro,uri);
		}

	}

	public ResponseEntity<?> deletaRefeicao(Long idRefeicao) {
		try{
			RefeicaoModel refeicao = refeicaoRepository.findById(idRefeicao)
					.orElseThrow(() -> new RefeicaoNaoEncontradoException("Refeicao de id " + idRefeicao + " não encontrada"));

			log.info("Deletando a refeição de id {}", refeicao.getId());
			refeicaoRepository.delete(refeicao);
			return ResponseEntity.noContent().build();
		} catch (RefeicaoNaoEncontradoException e) {
			log.error(e.getMessage());
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}
}
