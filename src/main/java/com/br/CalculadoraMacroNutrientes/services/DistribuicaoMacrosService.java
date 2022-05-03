package com.br.CalculadoraMacroNutrientes.services;

import java.net.URI;

import com.br.CalculadoraMacroNutrientes.controllers.forms.MacrosUpdateForm;
import com.br.CalculadoraMacroNutrientes.exceptions.DistribuicaoMacrosNaoEncontradoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.br.CalculadoraMacroNutrientes.controllers.dtos.DistribuicaoMacrosDto;
import com.br.CalculadoraMacroNutrientes.controllers.forms.MacrosForm;
import com.br.CalculadoraMacroNutrientes.models.DistribuicaoMacrosModel;
import com.br.CalculadoraMacroNutrientes.repositories.DistribuicaoMacrosRepository;

@Slf4j
@Service
public class DistribuicaoMacrosService {
	
	private DistribuicaoMacrosRepository distribuicaoMacrosRepository;
	
	@Autowired
	public DistribuicaoMacrosService(DistribuicaoMacrosRepository distribuicaoMacrosRepository) {
		this.distribuicaoMacrosRepository = distribuicaoMacrosRepository;
	}

	public ResponseEntity<DistribuicaoMacrosDto> defineMacrosUsuario(MacrosForm form, UriComponentsBuilder uriBuilder) {
		DistribuicaoMacrosModel distruicaoMacros = form.converter();
		distribuicaoMacrosRepository.save(distruicaoMacros);
		URI uri = uriBuilder.path("/macros/{id}").buildAndExpand(distruicaoMacros.getId()).toUri();
		return ResponseEntity.created(uri).body(new DistribuicaoMacrosDto(distruicaoMacros));
	}

	public ResponseEntity<?> atualizaMacrosUsuario(MacrosUpdateForm form) {
		try{
			distribuicaoMacrosRepository.findById(form.getId())
					.orElseThrow(() -> new DistribuicaoMacrosNaoEncontradoException("Distribuição de macros de id " + form.getId() + " não encontrada"));
			DistribuicaoMacrosModel dist = form.converter();
			log.info("Atualizando distribuição de macros de id {}",form.getId());
			distribuicaoMacrosRepository.save(dist);
			return ResponseEntity.noContent().build();
		} catch (DistribuicaoMacrosNaoEncontradoException e) {
			log.error(e.getMessage());
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
