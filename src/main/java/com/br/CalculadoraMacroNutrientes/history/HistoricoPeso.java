package com.br.CalculadoraMacroNutrientes.history;

import com.br.CalculadoraMacroNutrientes.models.UsuarioModel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_HIST_PESO")
@Data
@Builder
public class HistoricoPeso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double peso;
    private final LocalDateTime dataPesagem = LocalDateTime.now(); //formatar a data
    private Long idUsuario;
}
