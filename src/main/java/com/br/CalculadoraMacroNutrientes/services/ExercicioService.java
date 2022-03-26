package com.br.CalculadoraMacroNutrientes.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.br.CalculadoraMacroNutrientes.models.ExercicioModel;
import com.br.CalculadoraMacroNutrientes.models.dominios.ExercicioDominio;
import com.br.CalculadoraMacroNutrientes.repositories.ExercicioDominioRepository;
import com.br.CalculadoraMacroNutrientes.repositories.ExercicioRepository;

@Service
public class ExercicioService {
	
	private ExercicioDominioRepository exercicioDominioRepository;
	private ExercicioRepository exercicioRepository;
	
	public ExercicioService (ExercicioDominioRepository exercicioDominioRepository,ExercicioRepository exercicioRepository) {
		this.exercicioDominioRepository = exercicioDominioRepository;
		this.exercicioRepository = exercicioRepository;
	}
	
	public void calcularCaloriasExercicio(Long idExercicio, double tempoExecucao) {
		
		Optional<ExercicioDominio> exercicioReferencia = exercicioDominioRepository.findById(idExercicio);
		
		if (exercicioReferencia.isPresent()) {
			String modalidade = exercicioReferencia.get().getModalidade();
			double caloriasGastas = (exercicioReferencia.get().getCaloriasGastas()*tempoExecucao)/(exercicioReferencia.get().getTempo());
			
			ExercicioModel exercicio = new ExercicioModel(modalidade, tempoExecucao, caloriasGastas);
			exercicioRepository.save(exercicio);
		} else {
			System.out.println("Exercicio não encontrado");
		}
	}

}
