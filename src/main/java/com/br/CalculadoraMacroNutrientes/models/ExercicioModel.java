package com.br.CalculadoraMacroNutrientes.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.br.CalculadoraMacroNutrientes.controllers.dtos.ExercicioDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="TB_EXR")
@Data 
@NoArgsConstructor
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

	public static List<ExercicioDto> converterDto(List<ExercicioModel> listExercicios) {
		List<ExercicioDto> listaDto = new ArrayList<ExercicioDto>();
		listExercicios.forEach(exercicio -> {
			listaDto.add(new ExercicioDto(exercicio));
		});
		return listaDto;
	}
}
