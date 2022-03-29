package com.br.CalculadoraMacroNutrientes.models.dominios;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="TB_ALM_DOM")
@Data
@NoArgsConstructor
public class AlimentoDominio {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ApiModelProperty(value = "Nome do alimento")
	private String nome;
	private double quantidade = 0.0;
	private double carboidrato = 0.0;
	private double proteina = 0.0;
	private double gordura = 0.0;
	private double calorias = 0.0;
	
	public AlimentoDominio(String nome, double quantidade, double carboidrato, double proteina, double gordura, double calorias) {
		this.nome = nome;
		this.quantidade = quantidade;
		this.carboidrato = carboidrato;
		this.proteina = proteina;
		this.gordura = gordura;
		this.calorias = calorias;
	}
}
