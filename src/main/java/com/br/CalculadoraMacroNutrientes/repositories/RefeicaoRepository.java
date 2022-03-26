package com.br.CalculadoraMacroNutrientes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.CalculadoraMacroNutrientes.models.RefeicaoModel;

public interface RefeicaoRepository extends JpaRepository<RefeicaoModel,Long>{

}
