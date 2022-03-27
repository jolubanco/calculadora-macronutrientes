package com.br.CalculadoraMacroNutrientes.controllers.dtos;

import com.br.CalculadoraMacroNutrientes.models.AlimentoModel;

import lombok.Data;

@Data
public class AlimentoDetalharDto {

	private Long id;
	private String nome;
	private double quantidade;
	private double carboidrato;
	private double proteina;
	private double gordura;
	private double calorias;

	public AlimentoDetalharDto(AlimentoModel alimento) {
		this.id = alimento.getId();
		this.nome = alimento.getNome();
		this.quantidade = alimento.getQuantidade();
		this.carboidrato = alimento.getCarboidrato();
		this.proteina = alimento.getProteina();
		this.gordura = alimento.getGordura();
		this.calorias = alimento.getCalorias();
	}
}
