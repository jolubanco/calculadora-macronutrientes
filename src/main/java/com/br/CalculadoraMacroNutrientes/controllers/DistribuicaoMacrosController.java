package com.br.CalculadoraMacroNutrientes.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.br.CalculadoraMacroNutrientes.controllers.dtos.DistribuicaoMacrosDto;
import com.br.CalculadoraMacroNutrientes.controllers.forms.MacrosForm;
import com.br.CalculadoraMacroNutrientes.services.DistribuicaoMacrosService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/macros")
public class DistribuicaoMacrosController {
	
	@Autowired
	private DistribuicaoMacrosService distribuicaoMacrosService; 

	@ApiOperation(value = "Define os macronutrientes informados pelo usuário")
	@PostMapping("/macros")
	public ResponseEntity<DistribuicaoMacrosDto> defineMacrosUsuario(@RequestBody MacrosForm form, UriComponentsBuilder uriBuilder) {
		return distribuicaoMacrosService.defineMacrosUsuario(form,uriBuilder);
	}
	
}
