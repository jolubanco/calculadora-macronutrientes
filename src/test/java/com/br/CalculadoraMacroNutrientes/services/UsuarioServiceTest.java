package com.br.CalculadoraMacroNutrientes.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.br.CalculadoraMacroNutrientes.models.InformacoesUsuarioModel;
import com.br.CalculadoraMacroNutrientes.models.SexoEnum;
import com.br.CalculadoraMacroNutrientes.models.UsuarioModel;

public class UsuarioServiceTest {
	
	private UsuarioService service;
	
	@BeforeEach
	public void setup() {
		this.service = new UsuarioService();
	}
	
	@Test
	void deveRetornarTaxaMetabolismoBasal() {

		InformacoesUsuarioModel info = new InformacoesUsuarioModel();
		info.setAltura(175);
		info.setIdade(27);
		info.setPeso(80);
		info.setSexo(SexoEnum.MASCULINO);
		
		UsuarioModel usuario = new UsuarioModel();
		usuario.setInformacoesUsuario(info);
		
		service.calculaTaxaMetabolismoBasal(usuario);
		
		Assertions.assertEquals(1853.40,usuario.getInformacoesUsuario().getTaxaMetabolismoBasal());

	}

}
