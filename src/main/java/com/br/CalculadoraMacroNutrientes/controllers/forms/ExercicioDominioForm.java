package com.br.CalculadoraMacroNutrientes.controllers.forms;

import com.br.CalculadoraMacroNutrientes.models.dominios.ExercicioDominio;

import lombok.Data;

@Data
public class ExercicioDominioForm {
	
	private String modalidade;
	private double tempo;
	private double caloriasGastas;
	
	public ExercicioDominio converter() {
		return new ExercicioDominio(modalidade,tempo,caloriasGastas);
	}

}
