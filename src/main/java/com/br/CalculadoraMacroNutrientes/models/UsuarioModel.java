package com.br.CalculadoraMacroNutrientes.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.br.CalculadoraMacroNutrientes.history.HistoricoPeso;
import com.br.CalculadoraMacroNutrientes.models.enums.ObjetivoEnumModel;
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
	private double necessidadeDiariaCalorias = 0;
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

    public UsuarioModel(Long id, String nome, ObjetivoEnumModel objetivo, double necessidadeDiariaCalorias) {
		this.id = id;
		this.nome = nome;
		this.objetivo = objetivo;
		this.necessidadeDiariaCalorias = necessidadeDiariaCalorias;
    }

	public UsuarioModel(String nome, InformacoesUsuarioModel infoUsuario) {
		this.nome = nome;
		this.informacoesUsuario = infoUsuario;
	}

	public UsuarioModel(Long id, String nome, double necessidadeDiariaCalorias) {
		this.id = id;
		this.nome = nome;
		this.necessidadeDiariaCalorias = necessidadeDiariaCalorias;
	}

	public UsuarioModel(Long id, String nome) {
		this.id = id;
		this.nome = nome;
	}

	public void adicionaCaloriaNdc(double caloria) {
		this.necessidadeDiariaCalorias += caloria;
	}
}
