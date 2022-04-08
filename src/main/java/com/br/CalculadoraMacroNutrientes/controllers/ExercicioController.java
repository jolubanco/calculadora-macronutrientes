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

import com.br.CalculadoraMacroNutrientes.controllers.dtos.ExercicioDetalharDto;
import com.br.CalculadoraMacroNutrientes.controllers.dtos.ExercicioDto;
import com.br.CalculadoraMacroNutrientes.controllers.forms.ExercicioForm;
import com.br.CalculadoraMacroNutrientes.services.ExercicioService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/exercicios")
public class ExercicioController {
	
	@Autowired
	private ExercicioService exercicioService;
	
	@ApiOperation(value = "Lista todos os exercícios")
	@GetMapping
	public List<ExercicioDto> listaExercicios(){
		return null;
	}
	
	@ApiOperation(value = "Detalha exercício cadastrado")
	@GetMapping("/{idExercicio}")
	public ResponseEntity<ExercicioDetalharDto> detalhaExercicio(@PathVariable("idExercicio") Long idExercicio) {
		return exercicioService.detalhaExercicio(idExercicio);
	}
	
	@ApiOperation(value = "Cadastra exercício a partir de um exercício de domínio")
	@PostMapping
	public ResponseEntity<ExercicioDto> cadastraExercicio(@RequestBody ExercicioForm form, UriComponentsBuilder uriBuilder){
		return exercicioService.cadastraExercicio(form,uriBuilder);
	}

}
