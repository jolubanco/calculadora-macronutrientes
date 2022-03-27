package com.br.CalculadoraMacroNutrientes.controllers.dtos;

import com.br.CalculadoraMacroNutrientes.models.dominios.ExercicioDominio;

import lombok.Data;

@Data
public class ExercicioDominioDetalharDto {
	
	private Long id;
	private String modalidade;
	private double tempo;
	private double caloriasGastas;
	
	public ExercicioDominioDetalharDto(ExercicioDominio exercicioDominio) {
		this.id = exercicioDominio.getId();
		this.modalidade = exercicioDominio.getModalidade();
		this.tempo = exercicioDominio.getTempo();
		this.caloriasGastas = exercicioDominio.getCaloriasGastas();
	}


}
