package com.br.CalculadoraMacroNutrientes.controllers.dtos;

import com.br.CalculadoraMacroNutrientes.models.dominios.AlimentoDominio;

import lombok.Data;

@Data
public class AlimentoDominioDetalharDto {
	
	private Long id;
	private String nome;
	private double quantidade;
	private double carboidrato;
	private double proteina;
	private double gordura;
	private double calorias;

	public AlimentoDominioDetalharDto(AlimentoDominio alimentoDominio) {
		this.id= alimentoDominio.getId();
		this.nome = alimentoDominio.getNome();
		this.quantidade = alimentoDominio.getQuantidade();
		this.carboidrato = alimentoDominio.getCarboidrato();
		this.proteina = alimentoDominio.getProteina();
		this.gordura = alimentoDominio.getGordura();
		this.calorias = alimentoDominio.getCalorias();
	}

}
