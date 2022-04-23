package com.br.CalculadoraMacroNutrientes.controllers.forms;

import com.br.CalculadoraMacroNutrientes.models.RefeicaoModel;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class RefeicaoUpdateForm {

	@NotNull
	private Long id;
	@NotNull @NotBlank
	private String nome;

	public RefeicaoModel converter() {
		return new RefeicaoModel(id,nome);
	}

	public RefeicaoForm converteParaFormSemId() {
		return new RefeicaoForm(nome);
	}
}
