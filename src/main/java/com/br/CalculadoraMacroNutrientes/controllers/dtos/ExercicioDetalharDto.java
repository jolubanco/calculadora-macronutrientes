package com.br.CalculadoraMacroNutrientes.controllers.dtos;

import com.br.CalculadoraMacroNutrientes.models.ExercicioModel;

import lombok.Data;

@Data
public class ExercicioDetalharDto {
	
	private Long id;
	private String modalidade;
	private double tempo;
	private double caloriasGastas;

	public ExercicioDetalharDto(ExercicioModel exercicio) {
		this.id = exercicio.getId();
		this.modalidade = exercicio.getModalidade();
		this.tempo = exercicio.getTempo();
		this.caloriasGastas = exercicio.getCaloriasGastas();
	}


}
