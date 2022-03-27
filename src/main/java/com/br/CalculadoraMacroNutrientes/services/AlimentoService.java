package com.br.CalculadoraMacroNutrientes.services;

import java.net.URI;
import java.util.Optional;

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
		
		Optional<AlimentoDominio> alimentoReferencia = alimentoDominioRepository.findById(idAlimento);
		
		if(alimentoReferencia.isPresent()) {
			
			String nome = alimentoReferencia.get().getNome();
			//necessario tratamento para não dividir por 0
			double carboidrato = (alimentoReferencia.get().getCarboidrato()*quantidadeInformada)/alimentoReferencia.get().getQuantidade();
			double proteina = (alimentoReferencia.get().getProteina()*quantidadeInformada)/alimentoReferencia.get().getQuantidade();
			double gordura = (alimentoReferencia.get().getGordura()*quantidadeInformada)/alimentoReferencia.get().getQuantidade();
			double calorias = (alimentoReferencia.get().getCalorias()*quantidadeInformada)/alimentoReferencia.get().getQuantidade();
			
			return new AlimentoModel(nome,quantidadeInformada,carboidrato,proteina,gordura,calorias);
	
		} else {
			return null;
		}
	
	}
	
	
	public ResponseEntity<AlimentoDto> cadastraAlimento(AlimentoForm form, UriComponentsBuilder uriBuilder){
		AlimentoModel alimento = form.converter(this);
		alimentoRepository.save(alimento);
		
        URI uri = uriBuilder.path("/alimentos/{id}").buildAndExpand(alimento.getId()).toUri();
        
		return ResponseEntity.created(uri).body(new AlimentoDto(alimento));
	}

	public ResponseEntity<AlimentoDetalharDto> detalhaAlimento(Long idAlimento) {	
		Optional<AlimentoModel> alimento = alimentoRepository.findById(idAlimento);
		if(alimento.isPresent()) {
			return ResponseEntity.ok(new AlimentoDetalharDto(alimento.get()));
		} else {
			return ResponseEntity.notFound().build();	
		}
	}
}
