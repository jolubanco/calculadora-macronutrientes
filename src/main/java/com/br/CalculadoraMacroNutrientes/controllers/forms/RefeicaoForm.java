package com.br.CalculadoraMacroNutrientes.controllers.forms;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.br.CalculadoraMacroNutrientes.models.RefeicaoModel;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RefeicaoForm {
	
	@NotNull @NotBlank
	private String nome;

	public RefeicaoForm(String nome) {
		this.nome = nome;
	}

	public RefeicaoModel converter() {
		return new RefeicaoModel(nome);
	}

}
