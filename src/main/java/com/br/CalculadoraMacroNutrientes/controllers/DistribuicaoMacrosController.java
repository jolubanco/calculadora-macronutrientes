package com.br.CalculadoraMacroNutrientes.controllers;

import com.br.CalculadoraMacroNutrientes.controllers.forms.MacrosUpdateForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import com.br.CalculadoraMacroNutrientes.controllers.dtos.DistribuicaoMacrosDto;
import com.br.CalculadoraMacroNutrientes.controllers.forms.MacrosForm;
import com.br.CalculadoraMacroNutrientes.services.DistribuicaoMacrosService;

import io.swagger.annotations.ApiOperation;

import javax.validation.Valid;

@RestController
@RequestMapping("/macros")
public class DistribuicaoMacrosController {
	
	@Autowired
	private DistribuicaoMacrosService distribuicaoMacrosService; 

	@ApiOperation(value = "Define os macronutrientes informados pelo usuário")
	@PostMapping("/set")
	public ResponseEntity<DistribuicaoMacrosDto> defineMacrosUsuario(@RequestBody @Valid MacrosForm form, UriComponentsBuilder uriBuilder) {
		return distribuicaoMacrosService.defineMacrosUsuario(form,uriBuilder);
	}
	@ApiOperation(value = "Atualiza os macronutrientes informados pelo usuário")
	@PutMapping("/update")
	public ResponseEntity<?> atualizaMacrosUsuario(@RequestBody  @Valid MacrosUpdateForm form) {
		return distribuicaoMacrosService.atualizaMacrosUsuario(form);
	}
	
}
