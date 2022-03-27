package com.br.CalculadoraMacroNutrientes.controllers.dtos;

import java.time.LocalDate;
import java.util.List;

import com.br.CalculadoraMacroNutrientes.models.AlimentoModel;
import com.br.CalculadoraMacroNutrientes.models.RefeicaoModel;

import lombok.Data;

@Data
public class RefeicaoDto {

	private Long id;
	private String nome;
	private double caloriasTotais;
	private double proteinasTotais;
	private double carboidratosTotais;
	private double gordurasTotais;
	private LocalDate dataCriacao;
	private List<AlimentoModel> alimentos; //trocar para AlimentoDto
	
	public RefeicaoDto(RefeicaoModel refeicao) {
		this.id= refeicao.getId();
		this.nome = refeicao.getNome();
		this.caloriasTotais = refeicao.getCaloriasTotais();
		this.carboidratosTotais = refeicao.getCarboidratosTotais();
		this.gordurasTotais = refeicao.getGordurasTotais();
		this.proteinasTotais = refeicao.getProteinasTotais();
		this.dataCriacao = refeicao.getDataCriacao();
		this.alimentos = refeicao.getAlimentos();
	}

	public RefeicaoDto(String nome, double caloriasTotais, double proteinasTotais, double carboidratosTotais,
			double gordurasTotais, LocalDate dataCriacao, List<AlimentoModel> alimentos) {
		this.nome = nome;
		this.caloriasTotais = caloriasTotais;
		this.proteinasTotais = proteinasTotais;
		this.carboidratosTotais = carboidratosTotais;
		this.gordurasTotais = gordurasTotais;
		this.dataCriacao = dataCriacao;
		this.alimentos = alimentos;
	}

	public RefeicaoDto() {
		// TODO Auto-generated constructor stub
	}
	
	
	
}
