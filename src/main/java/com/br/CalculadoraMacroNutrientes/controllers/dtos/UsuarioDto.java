package com.br.CalculadoraMacroNutrientes.controllers.dtos;

import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.springframework.data.domain.Page;

import com.br.CalculadoraMacroNutrientes.models.ObjetivoEnumModel;
import com.br.CalculadoraMacroNutrientes.models.RefeicaoModel;
import com.br.CalculadoraMacroNutrientes.models.UsuarioModel;

import lombok.Data;

@Data
public class UsuarioDto {
	
	private Long id;
	private String nome;
	@Enumerated(EnumType.STRING)
	private ObjetivoEnumModel objetivo;
	
	public UsuarioDto(UsuarioModel usuario) {
		this.id = usuario.getId();
		this.nome = usuario.getNome();
		this.objetivo = usuario.getObjetivo();
	}

	public static Page<UsuarioDto> converter(Page<UsuarioModel> usuarios) {
		return usuarios.map(UsuarioDto::new);
	}
	
}
