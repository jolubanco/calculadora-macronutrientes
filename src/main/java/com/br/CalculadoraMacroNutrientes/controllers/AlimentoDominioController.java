package com.br.CalculadoraMacroNutrientes.controllers;

import java.util.List;

import com.br.CalculadoraMacroNutrientes.controllers.forms.AlimentoDominioUpdateForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import com.br.CalculadoraMacroNutrientes.controllers.dtos.AlimentoDominioDetalharDto;
import com.br.CalculadoraMacroNutrientes.controllers.dtos.AlimentoDominioDto;
import com.br.CalculadoraMacroNutrientes.controllers.forms.AlimentoDominioForm;
import com.br.CalculadoraMacroNutrientes.services.AlimentoDominioService;

import io.swagger.annotations.ApiOperation;

import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@RequestMapping("/alimentosDominio")
public class AlimentoDominioController {
	
	@Autowired
	private AlimentoDominioService alimentoDominioService;
	
	@ApiOperation(value = "Lista todos os alimentos de domínio cadastrados, e filtra pelo nome do alimento caso seja informado via parâmetro")
	@GetMapping
	public ResponseEntity<List<AlimentoDominioDto>> listaAlimentos(String nome){
		return alimentoDominioService.listaAlimentos(nome);
	}
	
	@ApiOperation(value = "Detalha o alimento de domínio cadastrado")
	@GetMapping("/{idAlimentoDominio}")
	public ResponseEntity<?> detalhaALimentoDominio(@PathVariable("idAlimentoDominio") Long idAlimentoDominio){
		return alimentoDominioService.detalhaAlimentoDominio(idAlimentoDominio);	
	}
	
	@ApiOperation(value = "Cadastra um alimento de domínio")
	@Transactional
	@PostMapping
	public ResponseEntity<AlimentoDominioDetalharDto> cadastraAlimentoDominio(@RequestBody @Valid AlimentoDominioForm form, UriComponentsBuilder uriBuilder){
		return alimentoDominioService.cadastraAlimentoDominio(form,uriBuilder);
	}

	//colocar um perfil especifico que possa alterar (ADMIN)
	@ApiOperation(value = "Atualiza um alimento de domínio se existir, caso contrário cadastra um novo")
	@PutMapping("/update")
	public ResponseEntity<?> atualizaAlimentoDominio(@RequestBody @Valid AlimentoDominioUpdateForm form, UriComponentsBuilder uri) {
		return alimentoDominioService.atualizaAlimentoDominio(form,uri);
	}

	@ApiOperation(value = "Deleta um alimento de domínio")
	@DeleteMapping("/delete/{idAlimentoDominio}")
	public ResponseEntity<?> deletaAlimentoDominio(@PathVariable("idAlimentoDominio") Long idAlimentoDominio){
		return alimentoDominioService.deletaAlimentoDominio(idAlimentoDominio);
	}

}
