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

import com.br.CalculadoraMacroNutrientes.controllers.dtos.AlimentoDto;
import com.br.CalculadoraMacroNutrientes.controllers.forms.AlimentoForm;
import com.br.CalculadoraMacroNutrientes.services.AlimentoService;

@RestController
@RequestMapping("/alimentos")
public class AlimentoController {
	
	@Autowired
	private AlimentoService alimentoService;
	
	@GetMapping("/{idAlimento}")
	public ResponseEntity<AlimentoDto> detalhaAlimento(@PathVariable Long idAlimento) {
		return alimentoService.detalhaAlimento(idAlimento);
	}
	
	@PostMapping
	public ResponseEntity<AlimentoDto> cadastraAlimento(@RequestBody AlimentoForm form,UriComponentsBuilder uriBuilder) {
		return alimentoService.cadastraAlimento(form, uriBuilder);
	}

}
