package com.br.CalculadoraMacroNutrientes.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="TB_ALM")
@Data
@NoArgsConstructor
public class AlimentoModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private double quantidade;
	private double carboidrato;
	private double proteina;
	private double gordura;
	private double calorias;
	
	public AlimentoModel(String nome, double quantidade, double carboidrato, double proteina, double gordura, double calorias) {
		this.nome = nome;
		this.quantidade = quantidade;
		this.carboidrato = carboidrato;
		this.proteina = proteina;
		this.gordura = gordura;
		this.calorias = calorias;
	}
	
	public AlimentoModel(Long id, double quantidade) {
		this.id = id;
		this.quantidade = quantidade;
	}

    public AlimentoModel(Long id, String nome, double quantidade, double carboidrato, double proteina, double gordura, double calorias) {
		this.id = id;
		this.nome = nome;
		this.quantidade = quantidade;
		this.carboidrato = carboidrato;
		this.proteina = proteina;
		this.gordura = gordura;
		this.calorias = calorias;
    }
}
