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
	private double carboidrato;
	private double proteina;
	private double gordura;
	private double consumoCaloriasDisponivel;
	private double carboidratoDisponivel;
	private double proteinaDisponivel;
	private double gorduraDisponivel;

	public DistribuicaoMacrosModel(double carboidrato, double proteina, double gordura) {
		this.carboidrato = carboidrato;
		this.proteina = proteina;
		this.gordura = gordura;
	}

	public DistribuicaoMacrosDto converterDto() {
		return new DistribuicaoMacrosDto(carboidratoDisponivel,proteinaDisponivel,gorduraDisponivel,consumoCaloriasDisponivel);
	}
	
	public void adicionaCaloriaConsumo(double caloria) {
		consumoCaloriasDisponivel += caloria;
	}

	public void subtraiCaloriaConsumo(double calorias) {
		consumoCaloriasDisponivel -= calorias;
	}

	public void subtraiCarboidrato(double carboidratos) {
		this.carboidratoDisponivel -= carboidratos;
	}

	public void subtraiProteina(double proteinas) {
		this.proteinaDisponivel -= proteinas;
	}

	public void subtraiGordura(double gorduras) {
		this.gorduraDisponivel -= gorduras;
	}
}
