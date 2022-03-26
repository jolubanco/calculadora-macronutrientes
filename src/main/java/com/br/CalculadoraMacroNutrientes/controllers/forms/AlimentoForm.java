package com.br.CalculadoraMacroNutrientes.controllers.forms;

import com.br.CalculadoraMacroNutrientes.models.AlimentoModel;
import com.br.CalculadoraMacroNutrientes.services.AlimentoService;

import lombok.Data;

@Data
public class AlimentoForm {
	
	private Long id;
	private double quantidadeInformada;
	
	public AlimentoModel converter(AlimentoService alimentoService) {	
		return alimentoService.calcularMacrosDoAlimento(id, quantidadeInformada);
	}

}
