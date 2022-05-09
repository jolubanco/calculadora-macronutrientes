package com.br.CalculadoraMacroNutrientes.controllers.forms;

import com.br.CalculadoraMacroNutrientes.models.*;
import com.br.CalculadoraMacroNutrientes.models.enums.FatorAtividadeFisicaEnum;
import com.br.CalculadoraMacroNutrientes.models.enums.ObjetivoEnumModel;
import com.br.CalculadoraMacroNutrientes.models.enums.SexoEnum;
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
	@NotNull 
	//@Length(min = 3,max = 6)
	private double altura;
	@NotNull 
	//@Length(min = 1,max = 2)
	private int idade;
	@NotNull @NotBlank
	private String sexo;
	@NotNull @NotBlank
	private String fatorAtividade;

	public UsuarioModel converter(InformacoesUsuarioRepository informacoesUsuarioRepository) {
		InformacoesUsuarioModel infoUsuario = new InformacoesUsuarioModel(altura,idade, SexoEnum.valueOf(sexo), FatorAtividadeFisicaEnum.valueOf(fatorAtividade));
		informacoesUsuarioRepository.save(infoUsuario);
		return new UsuarioModel(nome, infoUsuario);
	}

	public InformacoesUsuarioModel converteInfomacoesUsuario() {
		return new InformacoesUsuarioModel(altura,idade,SexoEnum.valueOf(sexo),FatorAtividadeFisicaEnum.valueOf(fatorAtividade));
	}

	public UsuarioModel converterUsuario() {
		return new UsuarioModel(id,nome);
	}
}