package com.br.CalculadoraMacroNutrientes.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.br.CalculadoraMacroNutrientes.controllers.dtos.DistribuicaoMacrosDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="TB_DSTB_MACROS")
@Data
@NoArgsConstructor
public class DistribuicaoMacrosModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private double carboidrato = 0.0;
	private double proteina = 0.0;
	private double gordura = 0.0;
	private double calorias = 0.0; //realizar o calculo a partir do basal e exercicios

	public DistribuicaoMacrosModel(double carboidrato, double proteina, double gordura) {
		this.carboidrato = carboidrato;
		this.proteina = proteina;
		this.gordura = gordura;
	}

	public DistribuicaoMacrosDto converterDto() {
		return new DistribuicaoMacrosDto(carboidrato,proteina,gordura,calorias);
	}
}
