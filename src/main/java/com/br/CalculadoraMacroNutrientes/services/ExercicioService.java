package com.br.CalculadoraMacroNutrientes.services;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import com.br.CalculadoraMacroNutrientes.controllers.forms.ExercicioUpdateForm;
import com.br.CalculadoraMacroNutrientes.exceptions.ExercicioNaoEncontradoException;
import com.br.CalculadoraMacroNutrientes.exceptions.RefeicaoNaoEncontradoException;
import io.swagger.models.Response;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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
		try{
			ExercicioDominio exercicioReferencia = exercicioDominioRepository.findById(idExercicio)
					.orElseThrow(() -> new ExercicioNaoEncontradoException("Exercício de id " + idExercicio + " não encontrado"));

			String modalidade = exercicioReferencia.getModalidade();
			double caloriasGastas = (exercicioReferencia.getCaloriasGastas()*tempoExecucao)/(exercicioReferencia.getTempo());
			return new ExercicioModel(modalidade, tempoExecucao, caloriasGastas);
		} catch (ExercicioNaoEncontradoException e) {
			log.error(e.getMessage());
			return null;
		}

	}

	public ResponseEntity<?> cadastraExercicio(ExercicioForm form, UriComponentsBuilder uriBuilder) {
		try {
			ExercicioModel exercicio = form.converter(this);
			exercicioRepository.save(exercicio);
	        URI uri = uriBuilder.path("/exercicios/{id}").buildAndExpand(exercicio.getId()).toUri();
	        return ResponseEntity.created(uri).body(new ExercicioDto(exercicio));
		} catch(Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	public ResponseEntity<?> detalhaExercicio(Long idExercicio) {
		try {
			ExercicioModel exercicio = exercicioRepository.findById(idExercicio)
					.orElseThrow(() -> new ExercicioNaoEncontradoException("Exercício de id " + idExercicio + " não encontrado"));
			log.info("Detalhando exercicio de id {}",exercicio.getId());
			return ResponseEntity.ok(new ExercicioDetalharDto(exercicio));
		} catch (ExercicioNaoEncontradoException e) {
			log.error(e.getMessage());
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}

	public ResponseEntity<List<ExercicioDto>> listaExercicios() {
		List<ExercicioModel> exercicios = exercicioRepository.findAll();
		return ResponseEntity.ok(ExercicioDto.converter(exercicios));
	}

	public ResponseEntity<?> deletaExercicio(Long idExercicio) {
		try {
			ExercicioModel exercicio = exercicioRepository.findById(idExercicio)
					.orElseThrow(() -> new ExercicioNaoEncontradoException("Exercício de id " + idExercicio + " não encontrado"));
			log.info("Deletando exercício de id {}",exercicio.getId());
			exercicioRepository.delete(exercicio);
			return ResponseEntity.noContent().build();
		} catch(ExercicioNaoEncontradoException e){
			log.error(e.getMessage());
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	public ResponseEntity<?> atualizarExercicio(ExercicioUpdateForm form, UriComponentsBuilder uri) {
		try {
			exercicioRepository.findById(form.getId())
					.orElseThrow(() -> new ExercicioNaoEncontradoException("Exercício de id " + form.getId() + " não encontrado"));

			ExercicioModel exercicio = form.converter();
			log.info("Atualizando exercicio de id {}",form.getId());
			exercicioRepository.save(exercicio);
			return ResponseEntity.noContent().build();
		} catch (ExercicioNaoEncontradoException e) {
			log.error(e.getMessage());
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
