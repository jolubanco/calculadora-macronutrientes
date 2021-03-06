package com.br.CalculadoraMacroNutrientes.models;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.br.CalculadoraMacroNutrientes.controllers.dtos.InformacoesUsuarioDto;

import com.br.CalculadoraMacroNutrientes.models.enums.FatorAtividadeFisicaEnum;
import com.br.CalculadoraMacroNutrientes.models.enums.SexoEnum;
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
	private SexoEnum sexo;
	@Enumerated(EnumType.STRING)
	private FatorAtividadeFisicaEnum fatorAtividadeFisica;
	private double taxaMetabolismoBasal;
	
	public InformacoesUsuarioModel(double peso, double altura, int idade, SexoEnum sexo, FatorAtividadeFisicaEnum fatorAtividadeFisica) {
		this.peso = peso;
		this.altura = altura;
		this.idade = idade;
		this.sexo = sexo;
		this.fatorAtividadeFisica = fatorAtividadeFisica;
	}

	public InformacoesUsuarioModel(double peso, double altura, int idade, SexoEnum sexo, FatorAtividadeFisicaEnum fatorAtividadeFisica,double taxaMetabolismoBasal) {
		this.peso = peso;
		this.altura = altura;
		this.idade = idade;
		this.sexo = sexo;
		this.fatorAtividadeFisica = fatorAtividadeFisica;
		this.taxaMetabolismoBasal = taxaMetabolismoBasal;
	}

    public InformacoesUsuarioModel(double altura, int idade, SexoEnum sexo, FatorAtividadeFisicaEnum fatorAtividadeFisica) {
		this.altura = altura;
		this.idade = idade;
		this.sexo = sexo;
		this.fatorAtividadeFisica = fatorAtividadeFisica;
    }

	public InformacoesUsuarioModel(double altura, int idade, SexoEnum valueOf, FatorAtividadeFisicaEnum fatorAtividadeFisica, double taxaMetabolismoBasal) {
		this.altura = altura;
		this.idade = idade;
		this.fatorAtividadeFisica = fatorAtividadeFisica;
		this.taxaMetabolismoBasal = taxaMetabolismoBasal;
	}

	public InformacoesUsuarioDto converterDto() {
		return new InformacoesUsuarioDto(peso,altura,idade,sexo.name(),fatorAtividadeFisica.name(),taxaMetabolismoBasal);
	}

}
