package com.br.CalculadoraMacroNutrientes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.CalculadoraMacroNutrientes.models.dominios.ExercicioDominio;

import java.util.List;

public interface ExercicioDominioRepository extends JpaRepository<ExercicioDominio, Long> {

    List<ExercicioDominio> findByModalidadeContaining(String modalidade);
}
