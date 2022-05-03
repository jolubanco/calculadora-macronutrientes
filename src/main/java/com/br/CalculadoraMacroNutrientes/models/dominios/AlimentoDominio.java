package com.br.CalculadoraMacroNutrientes.models.dominios;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Entity
@Table(name="TB_ALM_DOM")
@Data
@NoArgsConstructor
@Builder //teste
public class AlimentoDominio {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Setter(AccessLevel.NONE) //(TESTE) não permite que seja criado os setter atraves do @Data
	private Long id;
	@ApiModelProperty(value = "Nome do alimento")
	private String nome;
	private double quantidade;
	private double carboidrato;
	private double proteina;
	private double gordura;
	private double calorias;
	
	public AlimentoDominio(String nome, double quantidade, double carboidrato, double proteina, double gordura, double calorias) {
		this.nome = nome;
		this.quantidade = quantidade;
		this.carboidrato = carboidrato;
		this.proteina = proteina;
		this.gordura = gordura;
		this.calorias = calorias;
	}

    public AlimentoDominio(Long id, String nome, double quantidade, double carboidrato, double proteina, double gordura, double calorias) {
		this.id = id;
		this.nome = nome;
		this.quantidade = quantidade;
		this.carboidrato = carboidrato;
		this.proteina = proteina;
		this.gordura = gordura;
		this.calorias = calorias;
    }
}
