package com.br.CalculadoraMacroNutrientes.controllers.dtos;

import com.br.CalculadoraMacroNutrientes.models.DistribuicaoMacrosModel;

import lombok.Data;

@Data
public class DistribuicaoMacrosDto {
	private double carboidratoEstipulado;
	private double proteinaEstipulado;
	private double gorduraEstipulado;
	private double consumoCaloriasDisponivel;
	private double carboidratoDisponivel;
	private double proteinaDisponivel;
	private double gorduraDisponivel;
	
	public DistribuicaoMacrosDto(DistribuicaoMacrosModel distruicaoMacros) {
		this.carboidratoEstipulado =Math.round(distruicaoMacros.getCarboidrato());
		this.proteinaEstipulado = Math.round(distruicaoMacros.getProteina());
		this.gorduraEstipulado = Math.round(distruicaoMacros.getGordura());
		this.carboidratoDisponivel = Math.round(distruicaoMacros.getCarboidratoDisponivel());
		this.proteinaDisponivel = Math.round(distruicaoMacros.getProteinaDisponivel());
		this.gorduraDisponivel = Math.round(distruicaoMacros.getGorduraDisponivel());
		this.consumoCaloriasDisponivel = Math.round(distruicaoMacros.getConsumoCaloriasDisponivel());
	}

	public DistribuicaoMacrosDto(double carboidrato,double proteina, double gordura,double carboidratoDisponivel, double proteinaDisponivel, double gorduraDisponivel, double consumoCaloriasDisponivel) {
		this.carboidratoEstipulado =Math.round(carboidrato);
		this.proteinaEstipulado = Math.round(proteina);
		this.gorduraEstipulado = Math.round(gordura);
		this.carboidratoDisponivel = Math.round(carboidratoDisponivel);
		this.proteinaDisponivel = Math.round(proteinaDisponivel);
		this.gorduraDisponivel = Math.round(gorduraDisponivel);
		this.consumoCaloriasDisponivel = Math.round(consumoCaloriasDisponivel);
	}
	
}
