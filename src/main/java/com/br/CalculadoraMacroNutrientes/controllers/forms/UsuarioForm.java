package com.br.CalculadoraMacroNutrientes.controllers.forms;

import com.br.CalculadoraMacroNutrientes.models.InformacoesUsuarioModel;
import com.br.CalculadoraMacroNutrientes.models.ObjetivoEnumModel;
import com.br.CalculadoraMacroNutrientes.models.SexoEnumModel;
import com.br.CalculadoraMacroNutrientes.models.UsuarioModel;
import com.br.CalculadoraMacroNutrientes.repositories.InformacoesUsuarioRepository;

import lombok.Data;

@Data
public class UsuarioForm {
	
	private String nome;
	private String objetivo; //(ENUM) validar o envio
	private double peso;
	private double altura;
	private int idade;
	private String sexo; //(ENUM) validar o envio

	public UsuarioModel converter(InformacoesUsuarioRepository informacoesUsuarioRepository) {
		
		InformacoesUsuarioModel infoUsuario = new InformacoesUsuarioModel(peso,altura,idade,SexoEnumModel.valueOf(sexo));
		informacoesUsuarioRepository.save(infoUsuario);
		return new UsuarioModel(nome, ObjetivoEnumModel.valueOf(objetivo), infoUsuario);
		
	}

}
