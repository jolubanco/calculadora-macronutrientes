package com.br.CalculadoraMacroNutrientes.controllers.forms;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.br.CalculadoraMacroNutrientes.models.RefeicaoModel;

import lombok.Getter;

@Getter
public class RefeicaoForm {
	
	@NotNull @NotBlank
	private String nome;

	public RefeicaoModel converter() {
		return new RefeicaoModel(nome);
	}

}
