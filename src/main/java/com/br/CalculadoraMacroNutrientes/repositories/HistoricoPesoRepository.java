package com.br.CalculadoraMacroNutrientes.repositories;

import com.br.CalculadoraMacroNutrientes.history.HistoricoPeso;
import com.br.CalculadoraMacroNutrientes.models.dominios.AlimentoDominio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoricoPesoRepository extends JpaRepository<HistoricoPeso,Long> {

}
