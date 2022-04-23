package com.br.CalculadoraMacroNutrientes.controllers.forms;

import com.br.CalculadoraMacroNutrientes.models.dominios.ExercicioDominio;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class ExercicioDominioUpdateForm {

	@NotNull
	private Long id;
	@NotNull @NotBlank
	private String modalidade;
	@NotNull @Length(min = 0,max = 4)
	private double tempo;
	@NotNull @Length(min = 0,max = 5)
	private double caloriasGastas;

	public ExercicioDominio converter() {
		return new ExercicioDominio(id,modalidade,tempo,caloriasGastas);
	}

	public ExercicioDominioForm converterParaFormSemId() {
		return new ExercicioDominioForm(modalidade,tempo,caloriasGastas);
	}
}
