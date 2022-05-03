package com.br.CalculadoraMacroNutrientes.services;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import com.br.CalculadoraMacroNutrientes.controllers.forms.ExercicioDominioUpdateForm;
import com.br.CalculadoraMacroNutrientes.exceptions.ExercicioNaoEncontradoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.br.CalculadoraMacroNutrientes.controllers.dtos.ExercicioDominioDetalharDto;
import com.br.CalculadoraMacroNutrientes.controllers.dtos.ExercicioDominioDto;
import com.br.CalculadoraMacroNutrientes.controllers.forms.ExercicioDominioForm;
import com.br.CalculadoraMacroNutrientes.models.dominios.ExercicioDominio;
import com.br.CalculadoraMacroNutrientes.repositories.ExercicioDominioRepository;

@Slf4j
@Service
public class ExercicioDominioService {
		
	private ExercicioDominioRepository exercicioDominioRepository;
	
	public ExercicioDominioService(ExercicioDominioRepository exercicioDominioRepository) {
		this.exercicioDominioRepository = exercicioDominioRepository;
	}

	public ResponseEntity<ExercicioDominioDto> cadastraExercicioDominio(ExercicioDominioForm form, UriComponentsBuilder uriBuilder) {
		ExercicioDominio exercicioDominio = form.converter();
		log.info("Cadastrando exercício de domínio");
		exercicioDominioRepository.save(exercicioDominio);
		URI uri = uriBuilder.path("/exerciciosDominio/{id}").buildAndExpand(exercicioDominio.getId()).toUri();
		return ResponseEntity.created(uri).body(new ExercicioDominioDto(exercicioDominio));
	}

	public ResponseEntity<?> detalhaExercicioDominio(Long idExercicioDominio) {
		try{
			ExercicioDominio exercicio = exercicioDominioRepository.findById(idExercicioDominio)
					.orElseThrow(() -> new ExercicioNaoEncontradoException("Exercício de id " + idExercicioDominio + " não encontrado"));
			return ResponseEntity.ok(new ExercicioDominioDetalharDto(exercicio));
		} catch (ExercicioNaoEncontradoException e) {
			log.error(e.getMessage());
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}

	public ResponseEntity<List<ExercicioDominioDto>> listaExerciciosDominio(String modalidade) {
		if(modalidade == null){
			List<ExercicioDominio> exercicios = exercicioDominioRepository.findAll();
			log.info("Exibindo lista de exercícios de domínio");
			return ResponseEntity.ok(ExercicioDominioDto.converter(exercicios));
		} else {
			List<ExercicioDominio> exercicios = exercicioDominioRepository.findByModalidadeContaining(modalidade);
			log.info("Buscando exercício de domínio de nome {}",modalidade);
			return ResponseEntity.ok(ExercicioDominioDto.converter(exercicios));
		}

	}

	public ResponseEntity<ExercicioDominioDto> atualizaExercicioDominio(ExercicioDominioUpdateForm form,UriComponentsBuilder uri) {
		try {
			exercicioDominioRepository.findById(form.getId())
					.orElseThrow(() -> new ExercicioNaoEncontradoException("Exercício de id " + form.getId() + " não encontrado"));
			ExercicioDominio exercicioDominio = form.converter();
			log.info("Atualizando exercicio de id " + exercicioDominio.getId());
			exercicioDominioRepository.save(exercicioDominio);
			return ResponseEntity.ok(new ExercicioDominioDto(exercicioDominio));
		} catch (ExercicioNaoEncontradoException e) {
			ExercicioDominioForm formCadastro = form.converterParaFormSemId();
			return cadastraExercicioDominio(formCadastro,uri);
		}
	}

	public ResponseEntity<?> deletaExercicioDominio(Long idExercicioDominio) {
		try{
			ExercicioDominio exercicioDominio = exercicioDominioRepository.findById(idExercicioDominio)
					.orElseThrow(() -> new ExercicioNaoEncontradoException("Exercicio de domínio com id " + idExercicioDominio + " não encontrado"));
			log.info("Deletando exercicio de domínio com id {}",idExercicioDominio);
			exercicioDominioRepository.delete(exercicioDominio);
			return ResponseEntity.noContent().build();
		} catch (ExercicioNaoEncontradoException e) {
			log.error(e.getMessage());
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}
}
