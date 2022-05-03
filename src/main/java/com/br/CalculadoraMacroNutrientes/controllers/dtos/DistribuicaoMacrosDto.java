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
		this.carboidratoEstipulado =distruicaoMacros.getCarboidrato();
		this.proteinaEstipulado = distruicaoMacros.getProteina();
		this.gorduraEstipulado = distruicaoMacros.getGordura();
		this.carboidratoDisponivel = distruicaoMacros.getCarboidratoDisponivel();
		this.proteinaDisponivel = distruicaoMacros.getProteinaDisponivel();
		this.gorduraDisponivel = distruicaoMacros.getGorduraDisponivel();
		this.consumoCaloriasDisponivel = distruicaoMacros.getConsumoCaloriasDisponivel();
	}

	public DistribuicaoMacrosDto(double carboidrato,double proteina, double gordura,double carboidratoDisponivel, double proteinaDisponivel, double gorduraDisponivel, double consumoCaloriasDisponivel) {
		this.carboidratoEstipulado =carboidrato;
		this.proteinaEstipulado = proteina;
		this.gorduraEstipulado = gordura;
		this.carboidratoDisponivel = carboidratoDisponivel;
		this.proteinaDisponivel = proteinaDisponivel;
		this.gorduraDisponivel = gorduraDisponivel;
		this.consumoCaloriasDisponivel = consumoCaloriasDisponivel;
	}
	
}
