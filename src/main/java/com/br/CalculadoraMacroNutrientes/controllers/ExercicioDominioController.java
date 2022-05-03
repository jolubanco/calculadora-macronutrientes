package com.br.CalculadoraMacroNutrientes.controllers;

import java.util.List;

import com.br.CalculadoraMacroNutrientes.controllers.forms.ExercicioDominioUpdateForm;
import com.br.CalculadoraMacroNutrientes.models.dominios.ExercicioDominio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import com.br.CalculadoraMacroNutrientes.controllers.dtos.ExercicioDominioDetalharDto;
import com.br.CalculadoraMacroNutrientes.controllers.dtos.ExercicioDominioDto;
import com.br.CalculadoraMacroNutrientes.controllers.forms.ExercicioDominioForm;
import com.br.CalculadoraMacroNutrientes.services.ExercicioDominioService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/exerciciosDominio")
public class ExercicioDominioController {
	
	@Autowired
	private ExercicioDominioService exercicioDominioService;
	
	@ApiOperation(value = "Lista todos os exercícios de domínio")
	@GetMapping
	public ResponseEntity<List<ExercicioDominioDto>> listaExerciciosDominio(String modalidade){
		return exercicioDominioService.listaExerciciosDominio(modalidade);
	}
	
	@ApiOperation(value = "Detalha exercício de domínio cadastrado")
	@GetMapping("/{idExercicioDominio}")
	public ResponseEntity<?> detalharExercicioDominio(@PathVariable("idExercicioDominio") Long idExercicioDominio){
		return exercicioDominioService.detalhaExercicioDominio(idExercicioDominio);
	}
	//Se for uma boa pratica, adicionar a atualizacao aqui tambem
	@ApiOperation(value = "Cadastra um exercício de domínio")
	@PostMapping
	public ResponseEntity<ExercicioDominioDto> cadastraExercicioDominio(@RequestBody ExercicioDominioForm form, UriComponentsBuilder uriBuilder){
		return exercicioDominioService.cadastraExercicioDominio(form,uriBuilder);
		
	}
	//verificar se é uma boa pratica cadastrar caso nao exista
	@ApiOperation(value = "Atualiza um exercício de domínio caso exista, caso contrário cria um novo exercicio")
	@PutMapping("/update")
	public ResponseEntity<ExercicioDominioDto> atualizaExercicioDominio(@RequestBody ExercicioDominioUpdateForm form,UriComponentsBuilder uri){
		return exercicioDominioService.atualizaExercicioDominio(form,uri);
	}
	@ApiOperation(value = "Deleta um exercício de domínio")
	@DeleteMapping("/delete/{idExercicioDominio}")
	public ResponseEntity<?> deletaExercicioDominio(@PathVariable("idExercicioDominio") Long idExercicioDominio){
		return exercicioDominioService.deletaExercicioDominio(idExercicioDominio);
	}

}
