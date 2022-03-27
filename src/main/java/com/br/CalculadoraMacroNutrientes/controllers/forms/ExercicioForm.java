package com.br.CalculadoraMacroNutrientes.controllers.forms;

import com.br.CalculadoraMacroNutrientes.models.ExercicioModel;
import com.br.CalculadoraMacroNutrientes.services.ExercicioService;

import lombok.Data;

@Data
public class ExercicioForm {
	
	private String id;
	private double tempo;
	
	public ExercicioModel converter(ExercicioService exercicioService) {
		return exercicioService.calcularCaloriasExercicio(Long.parseLong(id),tempo);
	}
	
}
