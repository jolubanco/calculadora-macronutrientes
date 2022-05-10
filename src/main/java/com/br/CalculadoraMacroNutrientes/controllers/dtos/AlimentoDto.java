package com.br.CalculadoraMacroNutrientes.controllers.dtos;

import java.util.ArrayList;
import java.util.List;

import com.br.CalculadoraMacroNutrientes.models.AlimentoModel;

import lombok.Data;

@Data
public class AlimentoDto {
	
	private Long id;
	private String nome;
	private double calorias;

	public AlimentoDto(AlimentoModel alimento) {
		this.id = alimento.getId();
		this.nome = alimento.getNome();
		this.calorias = Math.round(alimento.getCalorias());
	}

	public static List<AlimentoDto> converter(List<AlimentoModel> alimentos) {
		List<AlimentoDto> alimentoDto = new ArrayList<>();
		alimentos.forEach(alimento -> {
			alimentoDto.add(new AlimentoDto(alimento));
		});
		return alimentoDto;
	}

}
