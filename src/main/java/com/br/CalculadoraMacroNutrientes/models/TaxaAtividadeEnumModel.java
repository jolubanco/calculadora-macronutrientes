package com.br.CalculadoraMacroNutrientes.models;

public enum TaxaAtividadeEnumModel {

	SEDENTARIO(1.2),
	LEVEMENTE_ATIVO(1.375),
	MODERADAMENTO_ATIVO(1.55),
	ALTAMENTE_ATIVO(1.725),
	EXTREMAMENTE_ATIVO(1.9);
	
	private final double taxa;
	
	private TaxaAtividadeEnumModel(double taxa) {
		this.taxa = taxa;
	}
	
	public double getTaxa() {
		return this.taxa;
	}
	
}
