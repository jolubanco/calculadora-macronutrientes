package com.br.CalculadoraMacroNutrientes.controllers.forms;

import com.br.CalculadoraMacroNutrientes.models.RefeicaoModel;

import lombok.Data;

@Data
public class RefeicaoForm {
	
	private String nome;

	public RefeicaoModel converter() {
		return new RefeicaoModel(nome);
	}

}
