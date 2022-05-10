package com.br.CalculadoraMacroNutrientes.controllers.dtos;

import java.util.List;

import com.br.CalculadoraMacroNutrientes.models.ExercicioModel;
import com.br.CalculadoraMacroNutrientes.models.RefeicaoModel;
import com.br.CalculadoraMacroNutrientes.models.UsuarioModel;

import lombok.Data;

@Data
public class UsuarioDetalharDto {
	
	private Long id;
	private String nome;
	private String objetivo;
	private double necessidadeDiariaCalorias;
	private InformacoesUsuarioDto informacoesUsuario;
	private DistribuicaoMacrosDto distribruicaoMacros;
	private List<RefeicaoDto> refeicoes;
	private List<ExercicioDto> exercicios;
	
	public UsuarioDetalharDto(UsuarioModel usuario) {
		this.id = usuario.getId();
		this.nome = usuario.getNome();
		this.objetivo = usuario.getObjetivo().name();
		this.necessidadeDiariaCalorias = Math.round(usuario.getNecessidadeDiariaCalorias());
		this.informacoesUsuario = usuario.getInformacoesUsuario().converterDto();
		this.distribruicaoMacros = usuario.getDistribruicaoMacros().converterDto();
		this.refeicoes = RefeicaoModel.converterDto(usuario.getRefeicoes()); //testar
		this.exercicios = ExercicioModel.converterDto(usuario.getExercicios());
	}
	
	

}
