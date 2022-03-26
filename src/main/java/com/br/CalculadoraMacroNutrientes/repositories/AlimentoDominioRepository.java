package com.br.CalculadoraMacroNutrientes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.CalculadoraMacroNutrientes.models.dominios.AlimentoDominio;

public interface AlimentoDominioRepository extends JpaRepository<AlimentoDominio,Long> {

}
