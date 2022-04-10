package com.br.CalculadoraMacroNutrientes.controllers.dtos;

import java.util.ArrayList;
import java.util.List;

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

	public static List<ExercicioDominioDto> converter(List<ExercicioDominio> exercicios) {
		List<ExercicioDominioDto> exerciciosDto = new ArrayList<>();
		exercicios.forEach(exercicio -> {
			exerciciosDto.add(new ExercicioDominioDto(exercicio));
		});
		return exerciciosDto;
	}

}
