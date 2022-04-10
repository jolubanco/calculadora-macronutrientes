package com.br.CalculadoraMacroNutrientes.controllers.dtos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.br.CalculadoraMacroNutrientes.models.RefeicaoModel;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RefeicaoDto {

	private Long id;
	private String nome;
	private double caloriasTotais;
	private LocalDate dataCriacao;

	
	public RefeicaoDto(RefeicaoModel refeicao) {
		this.id= refeicao.getId();
		this.nome = refeicao.getNome();
		this.caloriasTotais = refeicao.getCaloriasTotais();
		this.dataCriacao = refeicao.getDataCriacao();
	}

	public RefeicaoDto(String nome, double caloriasTotais, LocalDate dataCriacao) {
		this.nome = nome;
		this.caloriasTotais = caloriasTotais;
		this.dataCriacao = dataCriacao;
	}

	public static List<RefeicaoDto> converter(List<RefeicaoModel> refeicoes) {
		List<RefeicaoDto> refeicoesDto = new ArrayList<>();
		refeicoes.forEach(refeicao -> {
			refeicoesDto.add(new RefeicaoDto(refeicao));
		});
		return refeicoesDto;
	}
	
	
	
}
