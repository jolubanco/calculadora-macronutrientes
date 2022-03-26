package com.br.CalculadoraMacroNutrientes.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.br.CalculadoraMacroNutrientes.controllers.dtos.AlimentoDominioDetalharDto;
import com.br.CalculadoraMacroNutrientes.controllers.dtos.AlimentoDominioDto;
import com.br.CalculadoraMacroNutrientes.controllers.forms.AlimentoDominioForm;
import com.br.CalculadoraMacroNutrientes.services.AlimentoDominioService;

@RestController
@RequestMapping("/alimentosDominio")
public class AlimentoDominioController {
	
	@Autowired
	private AlimentoDominioService alimentoDominioService;
	
	@GetMapping
	public List<AlimentoDominioDto> listaAlimentos(){
		return null;	
	}
	
	@GetMapping("/{idAlimentoDominio}")
	public ResponseEntity<AlimentoDominioDetalharDto> detalhaALimentoDominio(@PathVariable Long idAlimentoDominio){
		return alimentoDominioService.detalhaAlimentoDominio(idAlimentoDominio);	
	}
	
	@PostMapping
	public ResponseEntity<AlimentoDominioDetalharDto> cadastraAlimentoDominio(@RequestBody AlimentoDominioForm form, UriComponentsBuilder uriBuilder){
		return alimentoDominioService.cadastraAlimentoDominio(form,uriBuilder);
		
	}

}
