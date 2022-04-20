package com.br.CalculadoraMacroNutrientes.services;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import com.br.CalculadoraMacroNutrientes.exceptions.AlimentoNaoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.br.CalculadoraMacroNutrientes.controllers.dtos.AlimentoDominioDetalharDto;
import com.br.CalculadoraMacroNutrientes.controllers.dtos.AlimentoDominioDto;
import com.br.CalculadoraMacroNutrientes.controllers.forms.AlimentoDominioForm;
import com.br.CalculadoraMacroNutrientes.models.dominios.AlimentoDominio;
import com.br.CalculadoraMacroNutrientes.repositories.AlimentoDominioRepository;

@Service
public class AlimentoDominioService {
	
	private AlimentoDominioRepository alimentoDominioRepository;
	
	@Autowired
	public AlimentoDominioService(AlimentoDominioRepository alimentoDominioRepository) {
		this.alimentoDominioRepository = alimentoDominioRepository;
	}

	public ResponseEntity<AlimentoDominioDetalharDto> cadastraAlimentoDominio(AlimentoDominioForm form,UriComponentsBuilder uriBuilder) {
		AlimentoDominio alimentoDominio = form.converter();
		alimentoDominioRepository.save(alimentoDominio);
		
        URI uri = uriBuilder.path("/alimentosDominio/{id}").buildAndExpand(alimentoDominio.getId()).toUri();
        
		return ResponseEntity.created(uri).body(new AlimentoDominioDetalharDto(alimentoDominio));
	}

	public ResponseEntity<AlimentoDominioDetalharDto> detalhaAlimentoDominio(Long idAlimentoDominio) {
		AlimentoDominio alimentoDominio = alimentoDominioRepository.findById(idAlimentoDominio)
				.orElseThrow(() -> new AlimentoNaoEncontradoException("Alimento de id " + idAlimentoDominio + " não encontrado"));
		return ResponseEntity.ok(new AlimentoDominioDetalharDto(alimentoDominio));
	}

	public ResponseEntity<List<AlimentoDominioDto>> listaAlimentos() {
		List<AlimentoDominio> alimentos = alimentoDominioRepository.findAll();
		return ResponseEntity.ok(AlimentoDominioDto.converter(alimentos));
	}

}
