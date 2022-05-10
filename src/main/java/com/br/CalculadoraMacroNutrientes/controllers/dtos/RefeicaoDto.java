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
		this.caloriasTotais = Math.round(refeicao.getCaloriasTotais());
		this.dataCriacao = refeicao.getDataCriacao();
	}

	public static List<RefeicaoDto> converter(List<RefeicaoModel> refeicoes) {
		List<RefeicaoDto> refeicoesDto = new ArrayList<>();
		refeicoes.forEach(refeicao -> {
			refeicoesDto.add(new RefeicaoDto(refeicao));
		});
		return refeicoesDto;
	}
	
	
	
}
