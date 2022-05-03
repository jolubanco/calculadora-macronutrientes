package com.br.CalculadoraMacroNutrientes.controllers.forms;

import com.br.CalculadoraMacroNutrientes.models.dominios.AlimentoDominio;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class AlimentoDominioUpdateForm {

	@NotNull
	private Long id;
	@NotNull @NotBlank
	private String nome;
	@NotNull
	//@Length(min = 1,max = 6)
	private double quantidade;
	@NotNull
	//@Length(min = 1,max = 4)
	private double carboidrato;
	@NotNull
	//@Length(min = 1,max = 4)
	private double proteina;
	@NotNull
	//@Length(min = 1,max = 4)
	private double gordura;
	@NotNull
	//@Length(min = 1,max = 6)
	private double calorias;

	public AlimentoDominio converter() {
		return new AlimentoDominio(id,nome,quantidade,carboidrato,proteina,gordura,calorias);
	}

	public AlimentoDominioForm converteParaFormSemId() {
		return new AlimentoDominioForm(nome,quantidade,carboidrato,proteina,gordura,calorias);
	}
}
