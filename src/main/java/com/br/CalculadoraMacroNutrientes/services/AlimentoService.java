package com.br.CalculadoraMacroNutrientes.services;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import com.br.CalculadoraMacroNutrientes.exceptions.AlimentoNaoEncontradoException;
import com.br.CalculadoraMacroNutrientes.exceptions.ExercicioNaoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.br.CalculadoraMacroNutrientes.controllers.dtos.AlimentoDetalharDto;
import com.br.CalculadoraMacroNutrientes.controllers.dtos.AlimentoDto;
import com.br.CalculadoraMacroNutrientes.controllers.forms.AlimentoForm;
import com.br.CalculadoraMacroNutrientes.models.AlimentoModel;
import com.br.CalculadoraMacroNutrientes.models.dominios.AlimentoDominio;
import com.br.CalculadoraMacroNutrientes.repositories.AlimentoDominioRepository;
import com.br.CalculadoraMacroNutrientes.repositories.AlimentoRepository;

@Service
public class AlimentoService {
	
	private AlimentoDominioRepository alimentoDominioRepository;
	private AlimentoRepository alimentoRepository;
	
	@Autowired
	public AlimentoService (AlimentoDominioRepository alimentoDominioRepository,AlimentoRepository alimentoRepository) {
		this.alimentoDominioRepository = alimentoDominioRepository;
		this.alimentoRepository = alimentoRepository;
	}
	
	public AlimentoModel calcularMacrosDoAlimento(Long idAlimento, double quantidadeInformada) {
		
		AlimentoDominio alimentoReferencia = alimentoDominioRepository.findById(idAlimento)
				.orElseThrow(() -> new AlimentoNaoEncontradoException("Alimento de id " + idAlimento + " não encontrado"));
			
		String nome = alimentoReferencia.getNome();
		//necessario tratamento para não dividir por 0
		double carboidrato = (alimentoReferencia.getCarboidrato()*quantidadeInformada)/(alimentoReferencia.getQuantidade());
		double proteina = (alimentoReferencia.getProteina()*quantidadeInformada)/(alimentoReferencia.getQuantidade());
		double gordura = (alimentoReferencia.getGordura()*quantidadeInformada)/(alimentoReferencia.getQuantidade());
		double calorias = (alimentoReferencia.getCalorias()*quantidadeInformada)/(alimentoReferencia.getQuantidade());

		return new AlimentoModel(nome,quantidadeInformada,carboidrato,proteina,gordura,calorias);
	}
	
	
	public ResponseEntity<AlimentoDto> cadastraAlimento(AlimentoForm form, UriComponentsBuilder uriBuilder){
		AlimentoModel alimento = form.converter(this);
		alimentoRepository.save(alimento);
		
        URI uri = uriBuilder.path("/alimentos/{id}").buildAndExpand(alimento.getId()).toUri();
        
		return ResponseEntity.created(uri).body(new AlimentoDto(alimento));
	}

	public ResponseEntity<AlimentoDetalharDto> detalhaAlimento(Long idAlimento) {	
		AlimentoModel alimento = alimentoRepository.findById(idAlimento)
				.orElseThrow(() -> new AlimentoNaoEncontradoException("Alimento de id " + idAlimento + " não encontrado"));
		return ResponseEntity.ok(new AlimentoDetalharDto(alimento));
	}

	public ResponseEntity<List<AlimentoDto>> listaAlimentos() {
		List<AlimentoModel> alimentos = alimentoRepository.findAll();
		return ResponseEntity.ok(AlimentoDto.converter(alimentos));
	}
}
