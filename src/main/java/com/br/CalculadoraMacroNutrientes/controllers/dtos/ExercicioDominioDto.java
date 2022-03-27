package com.br.CalculadoraMacroNutrientes.controllers.dtos;

import com.br.CalculadoraMacroNutrientes.models.dominios.ExercicioDominio;

import lombok.Data;

@Data
public class ExercicioDominioDto {
	
	private Long id;
	private String modalidade;
	
	public ExercicioDominioDto(ExercicioDominio exercicioDominio) {
		this.id = exercicioDominio.getId();
		this.modalidade = exercicioDominio.getModalidade();
	}

}
