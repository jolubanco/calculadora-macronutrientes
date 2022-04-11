package com.br.CalculadoraMacroNutrientes.controllers.forms;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.br.CalculadoraMacroNutrientes.models.AlimentoModel;
import com.br.CalculadoraMacroNutrientes.services.AlimentoService;

import lombok.Getter;

@Getter
public class AlimentoForm {
	
	@NotNull @NotBlank @Length(min = 1,max = 3)
	private String id;
	@NotNull @Length(min = 1,max = 6)
	private double quantidade;
	
	public AlimentoModel converter(AlimentoService alimentoService) {	
		return alimentoService.calcularMacrosDoAlimento(Long.parseLong(id), quantidade);
	}

}
