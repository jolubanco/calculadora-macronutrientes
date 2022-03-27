package com.br.CalculadoraMacroNutrientes.services;

import java.net.URI;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.br.CalculadoraMacroNutrientes.controllers.dtos.ExercicioDominioDetalharDto;
import com.br.CalculadoraMacroNutrientes.controllers.dtos.ExercicioDominioDto;
import com.br.CalculadoraMacroNutrientes.controllers.forms.ExercicioDominioForm;
import com.br.CalculadoraMacroNutrientes.models.dominios.ExercicioDominio;
import com.br.CalculadoraMacroNutrientes.repositories.ExercicioDominioRepository;

@Service
public class ExercicioDominioService {
		
	private ExercicioDominioRepository exercicioDominioRepository;
	
	public ExercicioDominioService(ExercicioDominioRepository exercicioDominioRepository) {
		this.exercicioDominioRepository = exercicioDominioRepository;
	}

	public ResponseEntity<ExercicioDominioDto> cadastraExercicioDominio(ExercicioDominioForm form, UriComponentsBuilder uriBuilder) {
		ExercicioDominio exercicioDominio = form.converter();
		exercicioDominioRepository.save(exercicioDominio);
		URI uri = uriBuilder.path("/exerciciosDominio/{id}").buildAndExpand(exercicioDominio.getId()).toUri();
		return ResponseEntity.created(uri).body(new ExercicioDominioDto(exercicioDominio));
	}

	public ResponseEntity<ExercicioDominioDetalharDto> detalhaExercicioDominio(Long idExercicioDominio) {
		Optional<ExercicioDominio> exercicio = exercicioDominioRepository.findById(idExercicioDominio);
		if(exercicio.isPresent()) {
			return ResponseEntity.ok(new ExercicioDominioDetalharDto(exercicio.get()));
		} else {
			return ResponseEntity.notFound().build();
		}
	}

}
