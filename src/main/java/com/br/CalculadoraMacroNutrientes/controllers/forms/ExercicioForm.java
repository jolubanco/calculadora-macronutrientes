package com.br.CalculadoraMacroNutrientes.controllers.forms;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.br.CalculadoraMacroNutrientes.models.ExercicioModel;
import com.br.CalculadoraMacroNutrientes.services.ExercicioService;

import lombok.Getter;

@Getter
public class ExercicioForm {
	
	@NotNull @NotBlank //@Length(min = 1,max = 2)
	private String id;
	@NotNull //@Length(min = 2,max = 4)
	private double tempo;
	
	public ExercicioModel converter(ExercicioService exercicioService) {
		return exercicioService.calcularCaloriasExercicio(Long.parseLong(id),tempo);
	}
	
}
