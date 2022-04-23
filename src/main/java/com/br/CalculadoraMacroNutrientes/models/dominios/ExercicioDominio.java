package com.br.CalculadoraMacroNutrientes.models.dominios;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="TB_EXR_DOM")
@Data
@NoArgsConstructor
public class ExercicioDominio {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String modalidade;
	private double tempo = 0.0;
	private double caloriasGastas = 0.0;
	
	public ExercicioDominio (String modalidade, double tempo, double caloriasGastas) {
		this.modalidade = modalidade;
		this.tempo = tempo;
		this.caloriasGastas = caloriasGastas;
	}

    public ExercicioDominio(Long id, String modalidade, double tempo, double caloriasGastas) {
		this.id = id;
		this.modalidade = modalidade;
		this.tempo = tempo;
		this.caloriasGastas = caloriasGastas;
    }
}
