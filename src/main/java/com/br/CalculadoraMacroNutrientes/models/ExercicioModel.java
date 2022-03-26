package com.br.CalculadoraMacroNutrientes.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="TB_EXR")
@Data 
public class ExercicioModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String modalidade;
	private double tempo = 0.0 ;
	private double caloriasGastas = 0.0;
	
	public ExercicioModel (String modalidade, double tempo, double caloriasGastas) {
		this.modalidade = modalidade;
		this.tempo = tempo;
		this.caloriasGastas = caloriasGastas;
	}
}
