package com.br.CalculadoraMacroNutrientes.controllers;

import java.util.List;

import com.br.CalculadoraMacroNutrientes.controllers.forms.ExercicioUpdateForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import com.br.CalculadoraMacroNutrientes.controllers.dtos.ExercicioDetalharDto;
import com.br.CalculadoraMacroNutrientes.controllers.dtos.ExercicioDto;
import com.br.CalculadoraMacroNutrientes.controllers.forms.ExercicioForm;
import com.br.CalculadoraMacroNutrientes.services.ExercicioService;

import io.swagger.annotations.ApiOperation;

import javax.validation.Valid;

@RestController
@RequestMapping("/exercicios")
public class ExercicioController {
	
	@Autowired
	private ExercicioService exercicioService;
	
	@ApiOperation(value = "Lista todos os exercícios")
	@GetMapping
	public ResponseEntity<List<ExercicioDto>> listaExercicios(){
		return exercicioService.listaExercicios();
	}
	
	@ApiOperation(value = "Detalha exercício cadastrado")
	@GetMapping("/{idExercicio}")
	public ResponseEntity<?> detalhaExercicio(@PathVariable("idExercicio") Long idExercicio) {
		return exercicioService.detalhaExercicio(idExercicio);
	}
	
	@ApiOperation(value = "Cadastra exercício a partir de um exercício de domínio")
	@PostMapping
	public ResponseEntity<?> cadastraExercicio(@RequestBody @Valid ExercicioForm form, UriComponentsBuilder uriBuilder){
		return exercicioService.cadastraExercicio(form,uriBuilder);
	}
	@ApiOperation(value = "Atualiza um exercício existente, caso o parâmetro idUsuario seja informado atualiza as informações de macros do usuário")
	@PutMapping("/update")
	public ResponseEntity<?> atualizaExercicio(@RequestBody @Valid ExercicioUpdateForm form, Long idUsuario){
		return exercicioService.atualizarExercicio(form,idUsuario);
	}
	@ApiOperation(value = "Deleta um exercício")
	@DeleteMapping("/delete/{idExercicio}")
	public ResponseEntity<?> deletaExercicio(@PathVariable("idExercicio") Long idExercicio){
		return exercicioService.deletaExercicio(idExercicio);
	}

}
