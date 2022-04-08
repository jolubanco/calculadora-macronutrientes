package com.br.CalculadoraMacroNutrientes.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.br.CalculadoraMacroNutrientes.controllers.dtos.AlimentoDetalharDto;
import com.br.CalculadoraMacroNutrientes.controllers.dtos.AlimentoDto;
import com.br.CalculadoraMacroNutrientes.controllers.forms.AlimentoForm;
import com.br.CalculadoraMacroNutrientes.services.AlimentoService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/alimentos")
public class AlimentoController {
	
	@Autowired
	private AlimentoService alimentoService;
	
	@ApiOperation(value = "Detalha um alimento cadastrado")
	@GetMapping("/{idAlimento}")
	public ResponseEntity<AlimentoDetalharDto> detalhaAlimento(@PathVariable Long idAlimento) {
		return alimentoService.detalhaAlimento(idAlimento);
	}
	
	@ApiOperation(value = "Cadastra um alimento a partir dos alimentos de domínio")
	@PostMapping
	public ResponseEntity<AlimentoDto> cadastraAlimento(@RequestBody AlimentoForm form,UriComponentsBuilder uriBuilder) {
		return alimentoService.cadastraAlimento(form, uriBuilder);
	}

}
