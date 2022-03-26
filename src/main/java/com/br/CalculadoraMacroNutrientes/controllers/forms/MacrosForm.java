package com.br.CalculadoraMacroNutrientes.controllers.forms;

import com.br.CalculadoraMacroNutrientes.models.DistribuicaoMacrosModel;

import lombok.Data;

@Data
public class MacrosForm {
	
	private double carboidrato;
	private double proteina;
	private double gordura;

	public DistribuicaoMacrosModel converter() {
		return new DistribuicaoMacrosModel(carboidrato,proteina,gordura);
	}

}
