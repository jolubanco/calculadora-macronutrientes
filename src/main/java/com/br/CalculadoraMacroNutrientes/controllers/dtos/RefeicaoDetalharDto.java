package com.br.CalculadoraMacroNutrientes.controllers.dtos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.br.CalculadoraMacroNutrientes.models.RefeicaoModel;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RefeicaoDetalharDto {
	
	private Long id;
	private String nome;
	private double caloriasTotais = 0;
	private double proteinasTotais = 0;
	private double carboidratosTotais = 0;
	private double gordurasTotais = 0;
	private LocalDate dataCriacao = LocalDate.now();
	private List<AlimentoDto> alimentos = new ArrayList<>();

	public RefeicaoDetalharDto(RefeicaoModel refeicao) {
		this.id = refeicao.getId();
		this.nome = refeicao.getNome();
		this.caloriasTotais = Math.round(refeicao.getCaloriasTotais());
		this.proteinasTotais = Math.round(refeicao.getProteinasTotais());
		this.carboidratosTotais = Math.round(refeicao.getCarboidratosTotais());
		this.gordurasTotais = Math.round(refeicao.getGordurasTotais());
		this.dataCriacao = refeicao.getDataCriacao();
		this.alimentos = AlimentoDto.converter(refeicao.getAlimentos());
	}

}
