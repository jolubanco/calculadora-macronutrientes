package com.br.CalculadoraMacroNutrientes.controllers.forms;

import com.br.CalculadoraMacroNutrientes.models.*;
import com.br.CalculadoraMacroNutrientes.repositories.InformacoesUsuarioRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class UsuarioUpdateForm {

	@NotNull
	private Long id;
	@NotNull @NotBlank
	private String nome;
	@NotNull @NotBlank
	private String objetivo;
	@NotNull
	private double necessidadeDiariaCalorias;
	@NotNull
	//@Length(min = 2,max = 4)
	private double peso;
	@NotNull 
	//@Length(min = 3,max = 6)
	private double altura;
	@NotNull 
	//@Length(min = 1,max = 2)
	private int idade;
	@NotNull @NotBlank
	private String sexo;
	@NotNull @NotBlank
	private String fatorAtividadeFisica;
	@NotNull
	private double taxaMetabolismoBasal;

	public UsuarioModel converter(InformacoesUsuarioRepository informacoesUsuarioRepository) {
		InformacoesUsuarioModel infoUsuario = new InformacoesUsuarioModel(peso,altura,idade,SexoEnum.valueOf(sexo),FatorAtividadeFisicaEnum.valueOf(fatorAtividadeFisica));
		informacoesUsuarioRepository.save(infoUsuario);
		return new UsuarioModel(nome, ObjetivoEnumModel.valueOf(objetivo), infoUsuario);
	}

	public InformacoesUsuarioModel converteInfomacoesUsuario() {
		return new InformacoesUsuarioModel(peso,altura,idade,SexoEnum.valueOf(sexo),FatorAtividadeFisicaEnum.valueOf(fatorAtividadeFisica),taxaMetabolismoBasal);
	}

	public UsuarioModel converterUsuario() {
		return new UsuarioModel(id,nome,ObjetivoEnumModel.valueOf(objetivo),necessidadeDiariaCalorias);
	}
}