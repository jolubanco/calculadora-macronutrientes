package com.br.CalculadoraMacroNutrientes.models;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.br.CalculadoraMacroNutrientes.controllers.dtos.InformacoesUsuarioDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="TB_INF_USR")
@Data
@NoArgsConstructor
public class InformacoesUsuarioModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private double peso;
	private double altura;
	private int idade;
	@Enumerated(EnumType.STRING)
	private SexoEnumModel sexo;
	
	public InformacoesUsuarioModel(double peso, double altura, int idade, SexoEnumModel sexo) {
		this.peso = peso;
		this.altura = altura;
		this.idade = idade;
		this.sexo = sexo;
	}

	public InformacoesUsuarioDto converterDto() {
		return new InformacoesUsuarioDto(peso,altura,idade,sexo.name());
	}

}
