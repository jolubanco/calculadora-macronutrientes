package com.br.CalculadoraMacroNutrientes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.CalculadoraMacroNutrientes.models.dominios.ExercicioDominio;

public interface ExercicioDominioRepository extends JpaRepository<ExercicioDominio, Long> {

}
