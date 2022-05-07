package com.br.CalculadoraMacroNutrientes.controllers.forms;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.br.CalculadoraMacroNutrientes.models.DistribuicaoMacrosModel;

import lombok.Getter;

@Getter
public class MacrosForm {
	
	@NotNull //@Length(min = 1,max = 4)
	private double carboidrato;
	@NotNull //@Length(min = 1,max = 4)
	private double proteina;
	@NotNull //@Length(min = 1,max = 4)
	private double gordura;

	public DistribuicaoMacrosModel converter() {
		return new DistribuicaoMacrosModel(carboidrato,proteina,gordura);
	}

}
