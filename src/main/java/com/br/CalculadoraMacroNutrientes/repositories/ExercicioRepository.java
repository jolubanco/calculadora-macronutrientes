package com.br.CalculadoraMacroNutrientes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.CalculadoraMacroNutrientes.models.ExercicioModel;

public interface ExercicioRepository extends JpaRepository<ExercicioModel, Long> {

}
