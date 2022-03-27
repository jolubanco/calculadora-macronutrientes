package com.br.CalculadoraMacroNutrientes.controllers.dtos;

import com.br.CalculadoraMacroNutrientes.models.DistribuicaoMacrosModel;

import lombok.Data;

@Data
public class DistribuicaoMacrosDto {
	
	private double carboidrato;
	private double proteina;
	private double gordura;
	private double calorias = 0.0; 
	
	public DistribuicaoMacrosDto(DistribuicaoMacrosModel distruicaoMacros) {
		this.carboidrato = distruicaoMacros.getCarboidrato();
		this.proteina = distruicaoMacros.getProteina();
		this.gordura = distruicaoMacros.getGordura();
	}

	public DistribuicaoMacrosDto(double carboidrato, double proteina, double gordura, double calorias) {
		this.carboidrato = carboidrato;
		this.proteina = proteina;
		this.gordura = gordura;
		this.calorias = calorias;
	}
	
	

}
