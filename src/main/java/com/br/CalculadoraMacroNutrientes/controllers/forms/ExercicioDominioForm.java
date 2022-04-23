package com.br.CalculadoraMacroNutrientes.controllers.forms;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import com.br.CalculadoraMacroNutrientes.models.dominios.ExercicioDominio;

import lombok.Getter;

@Getter
@NoArgsConstructor
public class ExercicioDominioForm {
	
	@NotNull @NotBlank
	private String modalidade;
	@NotNull @Length(min = 2,max = 4)
	private double tempo;
	@NotNull @Length(min = 2,max = 5)
	private double caloriasGastas;

    public ExercicioDominioForm(String modalidade, double tempo, double caloriasGastas) {
		this.modalidade = modalidade;
		this.tempo = tempo;
		this.caloriasGastas = caloriasGastas;
    }

    public ExercicioDominio converter() {
		return new ExercicioDominio(modalidade,tempo,caloriasGastas);
	}

}
