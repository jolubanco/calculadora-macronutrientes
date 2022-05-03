package com.br.CalculadoraMacroNutrientes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.CalculadoraMacroNutrientes.models.dominios.AlimentoDominio;

import java.util.List;
import java.util.Optional;

public interface AlimentoDominioRepository extends JpaRepository<AlimentoDominio,Long> {

    List<AlimentoDominio> findByNomeContaining(String nome);
}
