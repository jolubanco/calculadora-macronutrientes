package com.br.CalculadoraMacroNutrientes.controllers.dtos;

import lombok.Data;

@Data
public class InformacoesUsuarioDto {
	
	private double peso;
	private double altura;
	private int idade;
	private String sexo;

	private String fatorAtividade;

	private double taxaMetabolismoBasal;

	public InformacoesUsuarioDto(double peso, double altura, int idade,String sexo,String fatorAtividade, double taxaMetabolismoBasal) {
		this.peso = peso;
		this.altura = altura;
		this.idade = idade;
		this.sexo = sexo;
		this.fatorAtividade =fatorAtividade;
		this.taxaMetabolismoBasal = Math.round(taxaMetabolismoBasal);
	}
}
