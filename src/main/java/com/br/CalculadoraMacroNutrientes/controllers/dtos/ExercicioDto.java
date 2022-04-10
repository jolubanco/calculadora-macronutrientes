package com.br.CalculadoraMacroNutrientes.controllers.dtos;

import java.util.ArrayList;
import java.util.List;

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

	public static List<ExercicioDto> converter(List<ExercicioModel> exercicios) {
		List<ExercicioDto> exerciciosDto = new ArrayList<>();
		exercicios.forEach(exercicio -> {
			exerciciosDto.add(new ExercicioDto(exercicio));
		});
		return exerciciosDto;
	}

}
