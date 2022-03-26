package com.br.CalculadoraMacroNutrientes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.CalculadoraMacroNutrientes.models.AlimentoModel;

public interface AlimentoRepository extends JpaRepository<AlimentoModel,Long> {

}
