package com.br.CalculadoraMacroNutrientes.services;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import com.br.CalculadoraMacroNutrientes.exceptions.ExercicioNaoEncontradoException;
import com.br.CalculadoraMacroNutrientes.exceptions.RefeicaoNaoEncontradoException;
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
		
		ExercicioDominio exercicioReferencia = exercicioDominioRepository.findById(idExercicio)
				.orElseThrow(() -> new ExercicioNaoEncontradoException("Exercício de id " + idExercicio + " não encontrado"));

		String modalidade = exercicioReferencia.getModalidade();
		double caloriasGastas = (exercicioReferencia.getCaloriasGastas()*tempoExecucao)/(exercicioReferencia.getTempo());
		return new ExercicioModel(modalidade, tempoExecucao, caloriasGastas);
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
		ExercicioModel exercicio = exercicioRepository.findById(idExercicio)
				.orElseThrow(() -> new ExercicioNaoEncontradoException("Exercício de id " + idExercicio + " não encontrado"));
		return ResponseEntity.ok(new ExercicioDetalharDto(exercicio));
	}

	public ResponseEntity<List<ExercicioDto>> listaExercicios() {
		List<ExercicioModel> exercicios = exercicioRepository.findAll();
		return ResponseEntity.ok(ExercicioDto.converter(exercicios));
	}

}
