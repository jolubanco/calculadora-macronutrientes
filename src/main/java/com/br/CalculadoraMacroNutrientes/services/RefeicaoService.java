package com.br.CalculadoraMacroNutrientes.services;

import java.net.URI;
import java.util.List;
import java.util.Optional;

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
		refeicaoRepository.save(refeicao);
		
        URI uri = uriBuilder.path("/refeicoes/{id}").buildAndExpand(refeicao.getId()).toUri();
		return ResponseEntity.created(uri).body(new RefeicaoDto(refeicao));
	}

	public ResponseEntity<RefeicaoDto> adicionaAlimentoNaRefeicao(Long idRefeicao, Long idAlimento) {
		
		RefeicaoModel refeicao = refeicaoRepository.findById(idRefeicao)
				.orElseThrow(() -> new RefeicaoNaoEncontradoException("Refeicao de id " + idRefeicao + " não encontrada"));
		AlimentoModel alimento = alimentoRepository.findById(idAlimento)
				.orElseThrow(() -> new AlimentoNaoEncontradoException("Alimento de id " + idAlimento + " não encontrado"));
			
		refeicao.getAlimentos().add(alimento);
		atualizaMacroDaRefeicao(refeicao,alimento);
		return ResponseEntity.ok(new RefeicaoDto(refeicao));
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

	public ResponseEntity<RefeicaoDetalharDto> detalhaRefeicao(Long idRefeicao) {
		RefeicaoModel refeicao = refeicaoRepository.findById(idRefeicao)
				.orElseThrow(() -> new RefeicaoNaoEncontradoException("Refeicao de id " + idRefeicao + " não encontrada"));

		return ResponseEntity.ok(new RefeicaoDetalharDto(refeicao));
	}

	public ResponseEntity<?> removeAlimento(Long idRefeicao, Long idAlimento) {

		RefeicaoModel refeicao = refeicaoRepository.findById(idRefeicao)
				.orElseThrow(() -> new RefeicaoNaoEncontradoException("Refeicao de id " + idRefeicao + " não encontrada"));

			List<AlimentoModel> alimentos = refeicao.getAlimentos();
			try{
				alimentos.removeIf(alimento -> alimento.getId().equals(idAlimento));
				refeicaoRepository.save(refeicao);
				return ResponseEntity.noContent().build();
			} catch (Exception e) {
				e.getMessage();
				System.out.println("Alimento não encontrado na refeição"); //trocar por log
				return ResponseEntity.notFound().build();
			}
	}

    public ResponseEntity<?> atualizaRefeicao(Long idRefeicao, RefeicaoForm form) {
		RefeicaoModel refeicao = refeicaoRepository.findById(idRefeicao)
				.orElseThrow(() -> new RefeicaoNaoEncontradoException("Refeicao de id " + idRefeicao + " não encontrada"));

		refeicao.setNome(form.getNome());
		log.info("Atualizando a refeição de id {}",idRefeicao);
		refeicaoRepository.save(refeicao);
		return ResponseEntity.noContent().build();
	}

	public ResponseEntity<?> deletaRefeicao(Long idRefeicao) {
		RefeicaoModel refeicao = refeicaoRepository.findById(idRefeicao)
				.orElseThrow(() -> new RefeicaoNaoEncontradoException("Refeicao de id " + idRefeicao + " não encontrada"));

		log.info("Deletando a refeição de id {}", idRefeicao);
		refeicaoRepository.delete(refeicao);
		return ResponseEntity.ok().build();
	}
}
