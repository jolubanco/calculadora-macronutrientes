package com.br.CalculadoraMacroNutrientes.services;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import com.br.CalculadoraMacroNutrientes.controllers.forms.AlimentoUpdateForm;
import com.br.CalculadoraMacroNutrientes.exceptions.AlimentoNaoEncontradoException;
import com.br.CalculadoraMacroNutrientes.exceptions.ExercicioNaoEncontradoException;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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
		try {
			AlimentoDominio alimentoReferencia = alimentoDominioRepository.findById(idAlimento)
					.orElseThrow(() -> new AlimentoNaoEncontradoException("Alimento de id " + idAlimento + " não encontrado"));

			String nome = alimentoReferencia.getNome();
			double carboidrato = (alimentoReferencia.getCarboidrato()*quantidadeInformada)/(alimentoReferencia.getQuantidade());
			double proteina = (alimentoReferencia.getProteina()*quantidadeInformada)/(alimentoReferencia.getQuantidade());
			double gordura = (alimentoReferencia.getGordura()*quantidadeInformada)/(alimentoReferencia.getQuantidade());
			double calorias = (alimentoReferencia.getCalorias()*quantidadeInformada)/(alimentoReferencia.getQuantidade());

			return new AlimentoModel(nome,quantidadeInformada,carboidrato,proteina,gordura,calorias);
		} catch (AlimentoNaoEncontradoException e) {
			log.error(e.getMessage());
			return null;
		}
	}
	
	
	public ResponseEntity<AlimentoDto> cadastraAlimento(AlimentoForm form, UriComponentsBuilder uriBuilder){
		//nao resolveu esse try catch
		try {
			AlimentoModel alimento = form.converter(this);
			log.info("Cadastrando alimento");
			alimentoRepository.save(alimento);
			URI uri = uriBuilder.path("/alimentos/{id}").buildAndExpand(alimento.getId()).toUri();
			return ResponseEntity.created(uri).body(new AlimentoDto(alimento));
		} catch (AlimentoNaoEncontradoException e) {
			log.info(e.getMessage());
			return ResponseEntity.notFound().build();
		}

	}

	public ResponseEntity<?> detalhaAlimento(Long idAlimento) {
		try{
			AlimentoModel alimento = alimentoRepository.findById(idAlimento)
					.orElseThrow(() -> new AlimentoNaoEncontradoException("Alimento de id " + idAlimento + " não encontrado"));
			return ResponseEntity.ok(new AlimentoDetalharDto(alimento));
		} catch (AlimentoNaoEncontradoException e) {
			log.error(e.getMessage());
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	public ResponseEntity<List<AlimentoDto>> listaAlimentos() {
		List<AlimentoModel> alimentos = alimentoRepository.findAll();
		return ResponseEntity.ok(AlimentoDto.converter(alimentos));
	}

    public ResponseEntity<?> deletaAlimento(Long idAlimento) {
		try {
			AlimentoModel alimento = alimentoRepository.findById(idAlimento)
					.orElseThrow(() -> new AlimentoNaoEncontradoException("Alimento de id " + idAlimento + " não encontrado"));
			log.info("Deletando alimento de id {}",alimento.getId());
			alimentoRepository.delete(alimento);
			return ResponseEntity.noContent().build();
		} catch (AlimentoNaoEncontradoException e) {
			log.error(e.getMessage());
			return ResponseEntity.badRequest().body(e.getMessage());
		}
    }

	public ResponseEntity<?> atualizaAlimento(AlimentoUpdateForm form) {
		try {
			alimentoRepository.findById(form.getId())
					.orElseThrow(() -> new AlimentoNaoEncontradoException("Alimento de id " + form.getId() + " não encontrado"));
			AlimentoModel alimento = form.converter();
			alimentoRepository.save(alimento);
			return ResponseEntity.noContent().build();
		} catch (AlimentoNaoEncontradoException e) {
			log.error(e.getMessage());
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
