package com.br.CalculadoraMacroNutrientes.controllers.dtos;

import com.br.CalculadoraMacroNutrientes.models.DistribuicaoMacrosModel;

import lombok.Data;

@Data
public class DistribuicaoMacrosDto {
	
	private double carboidratoDisponiveis;
	private double proteinaDisponiveis;
	private double gorduraDisponiveis;
	private double consumoCaloriasDisponivel;
	
	public DistribuicaoMacrosDto(DistribuicaoMacrosModel distruicaoMacros) {
		this.carboidratoDisponiveis = distruicaoMacros.getCarboidratoDisponivel();
		this.proteinaDisponiveis = distruicaoMacros.getProteinaDisponivel();
		this.gorduraDisponiveis = distruicaoMacros.getGorduraDisponivel();
		this.consumoCaloriasDisponivel = distruicaoMacros.getConsumoCaloriasDisponivel();
	}

	public DistribuicaoMacrosDto(double carboidrato, double proteina, double gordura, double consumoCaloriasDisponivel) {
		this.carboidratoDisponiveis = carboidrato;
		this.proteinaDisponiveis = proteina;
		this.gorduraDisponiveis = gordura;
		this.consumoCaloriasDisponivel = consumoCaloriasDisponivel;
	}
	
}
