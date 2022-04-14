package com.br.CalculadoraMacroNutrientes.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.br.CalculadoraMacroNutrientes.controllers.dtos.RefeicaoDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="TB_REF")
@Data
@NoArgsConstructor
public class RefeicaoModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private double caloriasTotais = 0;
	private double proteinasTotais = 0;
	private double carboidratosTotais = 0;
	private double gordurasTotais = 0;
	private LocalDate dataCriacao = LocalDate.now();
	@ManyToMany
	@JoinTable(name = "refeicoes_alimentos",
    joinColumns = @JoinColumn(name = "refeicao_id"),
    inverseJoinColumns = @JoinColumn(name = "alimento_id"))
	private List<AlimentoModel> alimentos = new ArrayList<>();
	
	public RefeicaoModel(String nome) {
		this.nome = nome;
	}
	
	public void adicionaCarboidratos(double carboidrato) {
		this.carboidratosTotais += carboidrato;
	}
	
	public void adicionaProteinas(double proteina) {
		this.proteinasTotais += proteina;
	}
	
	public void adicionaGorduras(double gordura) {
		this.gordurasTotais += gordura;
	}
	
	public void adicionaCalorias(double caloria) {
		this.caloriasTotais += caloria;
	}
	
	public static List<RefeicaoDto> converterDto(List<RefeicaoModel> listaRefeicao ) {
		List<RefeicaoDto> listaDto = new ArrayList<RefeicaoDto>();
		listaRefeicao.forEach(refeicao -> {
			listaDto.add(new RefeicaoDto(refeicao));
		});
		return listaDto;
	}

}
