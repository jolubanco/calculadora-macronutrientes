package com.br.CalculadoraMacroNutrientes.services;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import com.br.CalculadoraMacroNutrientes.controllers.dtos.ExercicioDominioDto;
import com.br.CalculadoraMacroNutrientes.controllers.forms.AlimentoDominioUpdateForm;
import com.br.CalculadoraMacroNutrientes.exceptions.AlimentoNaoEncontradoException;
import io.swagger.models.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.br.CalculadoraMacroNutrientes.controllers.dtos.AlimentoDominioDetalharDto;
import com.br.CalculadoraMacroNutrientes.controllers.dtos.AlimentoDominioDto;
import com.br.CalculadoraMacroNutrientes.controllers.forms.AlimentoDominioForm;
import com.br.CalculadoraMacroNutrientes.models.dominios.AlimentoDominio;
import com.br.CalculadoraMacroNutrientes.repositories.AlimentoDominioRepository;

@Slf4j
@Service
public class AlimentoDominioService {
	
	private AlimentoDominioRepository alimentoDominioRepository;
	
	@Autowired
	public AlimentoDominioService(AlimentoDominioRepository alimentoDominioRepository) {
		this.alimentoDominioRepository = alimentoDominioRepository;
	}

	public ResponseEntity<AlimentoDominioDetalharDto> cadastraAlimentoDominio(AlimentoDominioForm form,UriComponentsBuilder uriBuilder) {
		AlimentoDominio alimentoDominio = form.converter();
		log.info("Cadastrando alimento de domínio");
		alimentoDominioRepository.save(alimentoDominio);
        URI uri = uriBuilder.path("/alimentosDominio/{id}").buildAndExpand(alimentoDominio.getId()).toUri();
		return ResponseEntity.created(uri).body(new AlimentoDominioDetalharDto(alimentoDominio));
	}

	public ResponseEntity<?> detalhaAlimentoDominio(Long idAlimentoDominio) {
		try{
			AlimentoDominio alimentoDominio = alimentoDominioRepository.findById(idAlimentoDominio)
					.orElseThrow(() -> new AlimentoNaoEncontradoException("Alimento de id " + idAlimentoDominio + " não encontrado"));
			log.info("Detalhando alimento de domínio");
			return ResponseEntity.ok(new AlimentoDominioDetalharDto(alimentoDominio));
		} catch (AlimentoNaoEncontradoException e) {
			log.error(e.getMessage());
			return ResponseEntity.notFound().build();
		}

	}

	public ResponseEntity<List<AlimentoDominioDto>> listaAlimentos(String nome) {
		if (nome == null){
			List<AlimentoDominio> alimentos = alimentoDominioRepository.findAll();
			log.info("Exibindo lista de alimentos de domínio");
			return ResponseEntity.ok(AlimentoDominioDto.converter(alimentos));
		} else {
				List<AlimentoDominio> alimentos = alimentoDominioRepository.findByNomeContaining(nome);
				log.info("Buscando alimento de domínio de nome {}",nome);
				return ResponseEntity.ok(AlimentoDominioDto.converter(alimentos));
		}
	}

    public ResponseEntity<?> deletaAlimentoDominio(Long idAlimentoDominio) {
		try {
			AlimentoDominio alimentoDominio = alimentoDominioRepository.findById(idAlimentoDominio)
					.orElseThrow(() -> new AlimentoNaoEncontradoException("Alimento de id " + idAlimentoDominio + " não encontrado"));
			log.info("Deletando alimento de id {}",idAlimentoDominio);
			alimentoDominioRepository.delete(alimentoDominio);
			return ResponseEntity.noContent().build();
		} catch (AlimentoNaoEncontradoException e){
			log.error(e.getMessage());
			return ResponseEntity.notFound().build();
		}
    }

	public ResponseEntity<?> atualizaAlimentoDominio(AlimentoDominioUpdateForm form, UriComponentsBuilder uri) {
		try {
			alimentoDominioRepository.findById(form.getId())
					.orElseThrow(() -> new AlimentoNaoEncontradoException("Alimento de id " + form.getId() + " não encontrado"));
			AlimentoDominio alimentoDominio = form.converter();
			log.info("Atualizando alimento de domínio com id {}",alimentoDominio.getId());
			alimentoDominioRepository.save(alimentoDominio);
			return ResponseEntity.noContent().build();
		} catch (AlimentoNaoEncontradoException e) {
			AlimentoDominioForm formCadastro = form.converteParaFormSemId();
			log.error(e.getMessage());
			return cadastraAlimentoDominio(formCadastro,uri);
		}
	}
}
