package com.br.CalculadoraMacroNutrientes.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="TB_USR")
@Data
@NoArgsConstructor
public class UsuarioModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	@Enumerated(EnumType.STRING)
	private ObjetivoEnumModel objetivo;
	private double necessidadeDiariaCalorias;
	@OneToOne
	private InformacoesUsuarioModel informacoesUsuario;
	@OneToOne
	private DistribuicaoMacrosModel distribruicaoMacros;
	@OneToMany
	private List<RefeicaoModel> refeicoes = new ArrayList<>();
	@OneToMany
	private List<ExercicioModel> exercicios = new ArrayList<>();
	
	public UsuarioModel(String nome,ObjetivoEnumModel objetivo,InformacoesUsuarioModel informacoesUsuario) {
		this.nome = nome;
		this.objetivo = objetivo;
		this.informacoesUsuario = informacoesUsuario;
	}
}
