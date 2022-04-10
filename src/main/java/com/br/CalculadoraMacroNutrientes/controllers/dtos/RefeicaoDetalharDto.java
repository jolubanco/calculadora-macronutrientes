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
		this.caloriasTotais = refeicao.getCaloriasTotais();
		this.proteinasTotais = refeicao.getProteinasTotais();
		this.carboidratosTotais = refeicao.getCarboidratosTotais();
		this.gordurasTotais = refeicao.getGordurasTotais();
		this.dataCriacao = refeicao.getDataCriacao();
		this.alimentos = AlimentoDto.converter(refeicao.getAlimentos());
	}

}
