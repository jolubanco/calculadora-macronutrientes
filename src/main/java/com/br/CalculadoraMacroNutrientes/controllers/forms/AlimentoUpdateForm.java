package com.br.CalculadoraMacroNutrientes.controllers.forms;

import com.br.CalculadoraMacroNutrientes.models.AlimentoModel;
import com.br.CalculadoraMacroNutrientes.models.dominios.AlimentoDominio;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class AlimentoUpdateForm {

	@NotNull
	private Long id;
	@NotNull @NotBlank
	private String nome;
	@NotNull //@Length(min = 1,max = 6)
	private double quantidade;
	@NotNull //@Length(min = 1,max = 4)
	private double carboidrato;
	@NotNull //@Length(min = 1,max = 4)
	private double proteina;
	@NotNull //@Length(min = 1,max = 4)
	private double gordura;
	@NotNull //@Length(min = 1,max = 6)
	private double calorias;

	//public AlimentoUpdateForm(String nome, double quantidade, double carboidrato, double proteina, double gordura, double calorias) {
	//	this.nome = nome;
	//	this.quantidade = quantidade;
	//	this.carboidrato = carboidrato;
	//	this.proteina = proteina;
	//	this.gordura = gordura;
	//	this.calorias = calorias;
	//}

	public AlimentoModel converter() {
		return new AlimentoModel(id,nome,quantidade,carboidrato,proteina,gordura,calorias);
	}

}
