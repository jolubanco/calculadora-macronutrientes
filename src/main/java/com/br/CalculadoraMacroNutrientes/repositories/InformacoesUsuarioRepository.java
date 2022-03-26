package com.br.CalculadoraMacroNutrientes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.CalculadoraMacroNutrientes.models.InformacoesUsuarioModel;

public interface InformacoesUsuarioRepository extends JpaRepository<InformacoesUsuarioModel, Long> {

}
