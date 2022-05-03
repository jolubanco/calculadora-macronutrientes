package com.br.CalculadoraMacroNutrientes.models.enums;

import lombok.Getter;

@Getter
public enum FatorAtividadeFisicaEnum {
	
	SEDENTARIO(1.2),
	LEVEMENTE_ATIVO(1.375),
	MODERADAMENTE_ATIVO(1.55),
	ALTAMENTE_ATIVO(1.725),
	EXTREMAMENTE_ATIVO(1.9);
	
	private final double fator;
	
	private FatorAtividadeFisicaEnum(double fator) {
		this.fator = fator;
	}

}
