package com.br.CalculadoraMacroNutrientes.services;

import java.net.URI;
import java.util.List;
import java.util.Optional;

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
		
		Optional<RefeicaoModel> refeicao = refeicaoRepository.findById(idRefeicao);
		Optional<AlimentoModel> alimento = alimentoRepository.findById(idAlimento);
		
		if(refeicao.isPresent() && alimento.isPresent()) {
			
			refeicao.get().getAlimentos().add(alimento.get());
			
			atualizaMacroDaRefeicao(refeicao.get(),alimento.get());
			
			return ResponseEntity.ok(new RefeicaoDto(refeicao.get()));
			
		} else {
			return ResponseEntity.notFound().build();
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

	public ResponseEntity<RefeicaoDetalharDto> detalhaRefeicao(Long idRefeicao) {
		Optional<RefeicaoModel> refeicao = refeicaoRepository.findById(idRefeicao);
		if(refeicao.isPresent()) {
			return ResponseEntity.ok(new RefeicaoDetalharDto(refeicao.get()));
		} else {
			return ResponseEntity.notFound().build();
		}
	}

}
