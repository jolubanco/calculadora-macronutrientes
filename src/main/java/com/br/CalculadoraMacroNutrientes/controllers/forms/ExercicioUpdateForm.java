package com.br.CalculadoraMacroNutrientes.controllers.forms;

import com.br.CalculadoraMacroNutrientes.models.ExercicioModel;
import com.br.CalculadoraMacroNutrientes.services.ExercicioService;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class ExercicioUpdateForm {
	
	@NotNull //@NotBlank //@Length(min = 1,max = 2)
	private Long id;
	@NotNull //@Length(min = 2,max = 4)
	private double tempo;
	@NotNull @NotBlank
	private String modalidade;
	@NotNull
	private double caloriasGastas;

	public ExercicioModel converter() {
		return new ExercicioModel(id,tempo,modalidade,caloriasGastas);
	}
}
