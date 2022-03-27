package com.br.CalculadoraMacroNutrientes.services;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.br.CalculadoraMacroNutrientes.controllers.dtos.ExercicioDetalharDto;
import com.br.CalculadoraMacroNutrientes.controllers.dtos.ExercicioDto;
import com.br.CalculadoraMacroNutrientes.controllers.forms.ExercicioForm;
import com.br.CalculadoraMacroNutrientes.models.ExercicioModel;
import com.br.CalculadoraMacroNutrientes.models.dominios.ExercicioDominio;
import com.br.CalculadoraMacroNutrientes.repositories.ExercicioDominioRepository;
import com.br.CalculadoraMacroNutrientes.repositories.ExercicioRepository;

@Service
public class ExercicioService {
	
	private ExercicioDominioRepository exercicioDominioRepository;
	private ExercicioRepository exercicioRepository;
	
	@Autowired
	public ExercicioService (ExercicioDominioRepository exercicioDominioRepository,ExercicioRepository exercicioRepository) {
		this.exercicioDominioRepository = exercicioDominioRepository;
		this.exercicioRepository = exercicioRepository;
	}
	
	public ExercicioModel calcularCaloriasExercicio(Long idExercicio, double tempoExecucao) {
		
		Optional<ExercicioDominio> exercicioReferencia = exercicioDominioRepository.findById(idExercicio);
		
		if (exercicioReferencia.isPresent()) {
			String modalidade = exercicioReferencia.get().getModalidade();
			double caloriasGastas = (exercicioReferencia.get().getCaloriasGastas()*tempoExecucao)/(exercicioReferencia.get().getTempo());
			
			return new ExercicioModel(modalidade, tempoExecucao, caloriasGastas);
			
		} else {
			System.out.println("Exercicio não encontrado");
			return null;
		}
	}

	public ResponseEntity<ExercicioDto> cadastraExercicio(ExercicioForm form, UriComponentsBuilder uriBuilder) {
		
		try {
			
			ExercicioModel exercicio = form.converter(this);
			exercicioRepository.save(exercicio);
	        URI uri = uriBuilder.path("/exercicios/{id}").buildAndExpand(exercicio.getId()).toUri();
	        return ResponseEntity.created(uri).body(new ExercicioDto(exercicio));	
	        
		} catch(Exception e) {
			
			e.getLocalizedMessage();
			return ResponseEntity.badRequest().build();
			
		}
	}

	public ResponseEntity<ExercicioDetalharDto> detalhaExercicio(Long idExercicio) {
		
		Optional<ExercicioModel> exercicio = exercicioRepository.findById(idExercicio);
		if(exercicio.isPresent()) {
			return ResponseEntity.ok(new ExercicioDetalharDto(exercicio.get()));
		} else {
			return ResponseEntity.notFound().build();
		}
	}

}
