package com.br.CalculadoraMacroNutrientes.controllers.forms;

import com.br.CalculadoraMacroNutrientes.models.AlimentoModel;
import com.br.CalculadoraMacroNutrientes.services.AlimentoService;

import lombok.Data;

@Data
public class AlimentoForm {
	
	private String id;
	private double quantidade;
	
	public AlimentoModel converter(AlimentoService alimentoService) {	
		return alimentoService.calcularMacrosDoAlimento(Long.parseLong(id), quantidade);
	}

}
