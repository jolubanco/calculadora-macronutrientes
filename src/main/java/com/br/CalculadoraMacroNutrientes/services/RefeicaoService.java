package com.br.CalculadoraMacroNutrientes.services;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import com.br.CalculadoraMacroNutrientes.controllers.forms.RefeicaoUpdateForm;
import com.br.CalculadoraMacroNutrientes.exceptions.AlimentoNaoEncontradoException;
import com.br.CalculadoraMacroNutrientes.exceptions.RefeicaoNaoEncontradoException;
import com.br.CalculadoraMacroNutrientes.exceptions.UsuarioNaoEncontradoException;
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
	
	@Autowired
	public RefeicaoService(RefeicaoRepository refeicaoRepository, AlimentoRepository alimentoRepository) {
		this.refeicaoRepository = refeicaoRepository;
		this.alimentoRepository = alimentoRepository;
	}

	public ResponseEntity<RefeicaoDto> cadastraRefeicao(RefeicaoForm form, UriComponentsBuilder uriBuilder) {
		RefeicaoModel refeicao = form.converter();
		log.info("Cadastrando nova refeição");
		refeicaoRepository.save(refeicao);
		
        URI uri = uriBuilder.path("/refeicoes/{id}").buildAndExpand(refeicao.getId()).toUri();
		return ResponseEntity.created(uri).body(new RefeicaoDto(refeicao));
	}

	public ResponseEntity<?> adicionaAlimentoNaRefeicao(Long idRefeicao, Long idAlimento) {
		try {
			RefeicaoModel refeicao = refeicaoRepository.findById(idRefeicao)
					.orElseThrow(() -> new RefeicaoNaoEncontradoException("Refeicao de id " + idRefeicao + " não encontrada"));
			AlimentoModel alimento = alimentoRepository.findById(idAlimento)
					.orElseThrow(() -> new AlimentoNaoEncontradoException("Alimento de id " + idAlimento + " não encontrado"));

			log.info("Adicionando alimento na refeição");
			refeicao.getAlimentos().add(alimento);
			atualizaMacroDaRefeicao(refeicao,alimento);
			return ResponseEntity.ok(new RefeicaoDto(refeicao));
		} catch (RefeicaoNaoEncontradoException | AlimentoNaoEncontradoException e) {
			log.info(e.getMessage());
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}

	private void atualizaMacroDaRefeicao(RefeicaoModel refeicao, AlimentoModel alimento) {
		refeicao.adicionaCarboidratos(alimento.getCarboidrato());
		refeicao.adicionaCalorias(alimento.getCalorias());
		refeicao.adicionaProteinas(alimento.getProteina());
		refeicao.adicionaGorduras(alimento.getGordura());
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
			log.info(e.getMessage());
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}

	public ResponseEntity<?> removeAlimento(Long idRefeicao, Long idAlimento) {
		try{
			RefeicaoModel refeicao = refeicaoRepository.findById(idRefeicao)
					.orElseThrow(() -> new RefeicaoNaoEncontradoException("Refeicao de id " + idRefeicao + " não encontrada"));

			List<AlimentoModel> alimentos = refeicao.getAlimentos();
			log.info("Removendo alimento de id {}, caso exista",idAlimento);
			alimentos.removeIf(alimento -> alimento.getId().equals(idAlimento));//lança uma exception caso não encontre???? (aparentemente não lança)
			refeicaoRepository.save(refeicao);
			return ResponseEntity.noContent().build();
		} catch (RefeicaoNaoEncontradoException e) {
			log.info(e.getMessage());
			return ResponseEntity.badRequest().body(e.getMessage());
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
			log.info(e.getMessage());
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}
}
