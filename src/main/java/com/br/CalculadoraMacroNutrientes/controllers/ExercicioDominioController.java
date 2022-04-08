package com.br.CalculadoraMacroNutrientes.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
	public List<ExercicioDominioDto> listaExerciciosDominio(){
		return null;
	}
	
	@ApiOperation(value = "Detalha exercício de domínio cadastrado")
	@GetMapping("/{idExercicioDominio}")
	public ResponseEntity<ExercicioDominioDetalharDto> detalharExercicioDominio(@PathVariable("idExercicioDominio") Long idExercicioDominio){
		return exercicioDominioService.detalhaExercicioDominio(idExercicioDominio);
	}
	
	@ApiOperation(value = "Cadastra um exercício de domínio")
	@PostMapping
	public ResponseEntity<ExercicioDominioDto> cadastraExercicioDominio(@RequestBody ExercicioDominioForm form, UriComponentsBuilder uriBuilder){
		return exercicioDominioService.cadastraExercicioDominio(form,uriBuilder);
		
	}

}
