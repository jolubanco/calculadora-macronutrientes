package com.br.CalculadoraMacroNutrientes.controllers.dtos;

import com.br.CalculadoraMacroNutrientes.models.DistribuicaoMacrosModel;

import lombok.Data;

@Data
public class DistribuicaoMacrosDto {
	
	private double carboidrato;
	private double proteina;
	private double gordura;
	private double caloriasNecessarias; 
	private double caloriasRestantes;
	
	//ver onde é utilizado
	public DistribuicaoMacrosDto(DistribuicaoMacrosModel distruicaoMacros) {
		this.carboidrato = distruicaoMacros.getCarboidrato();
		this.proteina = distruicaoMacros.getProteina();
		this.gordura = distruicaoMacros.getGordura();
		this.caloriasNecessarias = distruicaoMacros.getCaloriasNecessarias();
		this.caloriasRestantes = distruicaoMacros.getCaloriasRestantes();
	}

	public DistribuicaoMacrosDto(double carboidrato, double proteina, double gordura, double caloriasNecessarias, double caloriasRestantes) {
		this.carboidrato = carboidrato;
		this.proteina = proteina;
		this.gordura = gordura;
		this.caloriasNecessarias = caloriasNecessarias;
		this.caloriasRestantes = caloriasRestantes;
	}
	
	

}
