package com.br.CalculadoraMacroNutrientes.controllers.dtos;

import com.br.CalculadoraMacroNutrientes.models.AlimentoModel;

import lombok.Data;

@Data
public class AlimentoDto {
	
	private Long id;
	private String nome;

	public AlimentoDto(AlimentoModel alimento) {
		this.id = alimento.getId();
		this.nome = alimento.getNome();
	}

}
