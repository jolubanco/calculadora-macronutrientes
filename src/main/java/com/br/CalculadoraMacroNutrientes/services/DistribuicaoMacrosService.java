package com.br.CalculadoraMacroNutrientes.services;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.br.CalculadoraMacroNutrientes.controllers.dtos.DistribuicaoMacrosDto;
import com.br.CalculadoraMacroNutrientes.controllers.forms.MacrosForm;
import com.br.CalculadoraMacroNutrientes.models.DistribuicaoMacrosModel;
import com.br.CalculadoraMacroNutrientes.repositories.DistribuicaoMacrosRepository;

@Service
public class DistribuicaoMacrosService {
	
	DistribuicaoMacrosRepository distribuicaoMacrosRepository;
	
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

}
