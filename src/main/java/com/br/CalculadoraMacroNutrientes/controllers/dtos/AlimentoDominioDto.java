package com.br.CalculadoraMacroNutrientes.controllers.dtos;

import java.util.ArrayList;
import java.util.List;

import com.br.CalculadoraMacroNutrientes.models.AlimentoModel;
import com.br.CalculadoraMacroNutrientes.models.dominios.AlimentoDominio;

import lombok.Data;

@Data
public class AlimentoDominioDto {
	
	private Long id;
	private String nome;
	private double calorias;

	public AlimentoDominioDto(AlimentoDominio alimento) {
		this.id = alimento.getId();
		this.nome = alimento.getNome();
		this.calorias = alimento.getCalorias();
	}

	public static List<AlimentoDominioDto> converter(List<AlimentoDominio> alimentos) {
		List<AlimentoDominioDto> alimentosDto = new ArrayList<>();
		alimentos.forEach(alimento -> {
			alimentosDto.add(new AlimentoDominioDto(alimento));
		});
		return alimentosDto;
	}

}
