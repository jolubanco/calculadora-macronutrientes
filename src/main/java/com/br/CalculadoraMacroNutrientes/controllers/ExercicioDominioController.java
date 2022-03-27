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

@RestController
@RequestMapping("/exerciciosDominio")
public class ExercicioDominioController {
	
	@Autowired
	private ExercicioDominioService exercicioDominioService;
	
	@GetMapping
	public List<ExercicioDominioDto> listaExerciciosDominio(){
		return null;
	}
	
	@GetMapping("/{idExercicioDominio}")
	public ResponseEntity<ExercicioDominioDetalharDto> detalharExercicioDominio(@PathVariable("idExercicioDominio") Long idExercicioDominio){
		return exercicioDominioService.detalhaExercicioDominio(idExercicioDominio);
	}
	
	@PostMapping
	public ResponseEntity<ExercicioDominioDto> cadastraExercicioDominio(@RequestBody ExercicioDominioForm form, UriComponentsBuilder uriBuilder){
		return exercicioDominioService.cadastraExercicioDominio(form,uriBuilder);
		
	}

}
