package com.br.CalculadoraMacroNutrientes.controllers.forms;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.br.CalculadoraMacroNutrientes.models.InformacoesUsuarioModel;
import com.br.CalculadoraMacroNutrientes.models.ObjetivoEnumModel;
import com.br.CalculadoraMacroNutrientes.models.SexoEnum;
import com.br.CalculadoraMacroNutrientes.models.UsuarioModel;
import com.br.CalculadoraMacroNutrientes.repositories.InformacoesUsuarioRepository;

import lombok.Getter;

@Getter
public class UsuarioForm {
	
	private String nome;
	@NotNull @NotBlank
	private String objetivo;
	@NotNull @Length(min = 2,max = 4)
	private double peso;
	@NotNull @Length(min = 3,max = 6)
	private double altura;
	@NotNull @Length(min = 2,max = 2)
	private int idade;
	@NotNull @NotBlank
	private String sexo;

	public UsuarioModel converter(InformacoesUsuarioRepository informacoesUsuarioRepository) {
		
		InformacoesUsuarioModel infoUsuario = new InformacoesUsuarioModel(peso,altura,idade,SexoEnum.valueOf(sexo));
		informacoesUsuarioRepository.save(infoUsuario);
		return new UsuarioModel(nome, ObjetivoEnumModel.valueOf(objetivo), infoUsuario);
		
	}

}
