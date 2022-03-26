package com.br.CalculadoraMacroNutrientes.controllers.forms;

import com.br.CalculadoraMacroNutrientes.models.dominios.AlimentoDominio;

import lombok.Data;

@Data
public class AlimentoDominioForm {
	
	private String nome;
	private double quantidade;
	private double carboidrato;
	private double proteina;
	private double gordura;
	private double calorias;

	public AlimentoDominio converter() {
		return new AlimentoDominio(nome,quantidade,carboidrato,proteina,gordura,calorias);
	}

}
