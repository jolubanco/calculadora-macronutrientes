package com.br.CalculadoraMacroNutrientes.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="TB_REF")
@Data
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
	@OneToMany
	private List<AlimentoModel> alimentos = new ArrayList<>();
	
	public RefeicaoModel() {}
	
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

}
