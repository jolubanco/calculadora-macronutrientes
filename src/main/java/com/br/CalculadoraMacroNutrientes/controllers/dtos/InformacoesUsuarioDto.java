package com.br.CalculadoraMacroNutrientes.controllers.dtos;

import lombok.Data;

@Data
public class InformacoesUsuarioDto {
	
	private double peso;
	private double altura;
	private int idade;
	private String sexo;
	
	public InformacoesUsuarioDto(double peso, double altura, int idade, String sexo) {
		this.peso = peso;
		this.altura = altura;
		this.idade = idade;
		this.sexo = sexo;
	}

}
