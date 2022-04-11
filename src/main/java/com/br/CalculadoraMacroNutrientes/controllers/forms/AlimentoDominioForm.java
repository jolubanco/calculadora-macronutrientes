package com.br.CalculadoraMacroNutrientes.controllers.forms;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.br.CalculadoraMacroNutrientes.models.dominios.AlimentoDominio;

import lombok.Getter;

@Getter
public class AlimentoDominioForm {
	
	@NotNull @NotBlank
	private String nome;
	@NotNull @Length(min = 1,max = 6)
	private double quantidade;
	@NotNull @Length(min = 1,max = 4)
	private double carboidrato;
	@NotNull @Length(min = 1,max = 4)
	private double proteina;
	@NotNull @Length(min = 1,max = 4)
	private double gordura;
	@NotNull @Length(min = 1,max = 6)
	private double calorias;

	public AlimentoDominio converter() {
		return new AlimentoDominio(nome,quantidade,carboidrato,proteina,gordura,calorias);
	}

}
