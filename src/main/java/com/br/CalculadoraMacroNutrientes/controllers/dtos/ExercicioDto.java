package com.br.CalculadoraMacroNutrientes.controllers.dtos;

import com.br.CalculadoraMacroNutrientes.models.ExercicioModel;

import lombok.Data;

@Data
public class ExercicioDto {
	
	private Long id;
	private String modalidade;
	private double caloriaGasta;

	public ExercicioDto(ExercicioModel exercicio) {
		this.id = exercicio.getId();
		this.modalidade = exercicio.getModalidade();
		this.caloriaGasta = exercicio.getCaloriasGastas();
	}

}
