package com.br.CalculadoraMacroNutrientes.controllers;

import java.util.List;

import com.br.CalculadoraMacroNutrientes.controllers.forms.AlimentoUpdateForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import com.br.CalculadoraMacroNutrientes.controllers.dtos.AlimentoDetalharDto;
import com.br.CalculadoraMacroNutrientes.controllers.dtos.AlimentoDto;
import com.br.CalculadoraMacroNutrientes.controllers.forms.AlimentoForm;
import com.br.CalculadoraMacroNutrientes.services.AlimentoService;

import io.swagger.annotations.ApiOperation;

import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@RequestMapping("/alimentos")
public class AlimentoController {
	
	@Autowired
	private AlimentoService alimentoService;
	
	@ApiOperation(value = "Lista todos os alimentos cadastrados")
	@GetMapping
	public ResponseEntity<List<AlimentoDto>> listaAlimentos(){
		return alimentoService.listaAlimentos();	
	}
	
	@ApiOperation(value = "Detalha um alimento cadastrado")
	@GetMapping("/{idAlimento}")
	public ResponseEntity<?> detalhaAlimento(@PathVariable Long idAlimento) {
		return alimentoService.detalhaAlimento(idAlimento);
	}
	
	@ApiOperation(value = "Cadastra um alimento a partir dos alimentos de domínio")
	@Transactional
	@PostMapping
	public ResponseEntity<AlimentoDto> cadastraAlimento(@RequestBody AlimentoForm form,UriComponentsBuilder uriBuilder) {
		return alimentoService.cadastraAlimento(form, uriBuilder);
	}

	@ApiOperation(value = "Atualiza um alimento")
	@PutMapping("/update")
	public ResponseEntity<?> atualizaAlimento(@RequestBody @Valid AlimentoUpdateForm form){
		return alimentoService.atualizaAlimento(form);
	}

	@ApiOperation(value = "Deleta um alimento")
	@DeleteMapping("/delete/{idAlimento}")
	public ResponseEntity<?> deletaAlimento(@PathVariable("idAlimento") Long idAlimento){
		return alimentoService.deletaAlimento(idAlimento);
	}
}
