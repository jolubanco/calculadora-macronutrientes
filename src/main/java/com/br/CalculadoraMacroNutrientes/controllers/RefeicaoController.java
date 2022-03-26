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

import com.br.CalculadoraMacroNutrientes.controllers.dtos.RefeicaoDetalharDto;
import com.br.CalculadoraMacroNutrientes.controllers.dtos.RefeicaoDto;
import com.br.CalculadoraMacroNutrientes.controllers.forms.RefeicaoForm;
import com.br.CalculadoraMacroNutrientes.services.RefeicaoService;

@RestController
@RequestMapping("/refeicoes")
public class RefeicaoController {
	
	@Autowired
	private RefeicaoService refeicaoService;
	
	@GetMapping("/{idRefeicao}")
	public RefeicaoDetalharDto detalhaRefeicao(@PathVariable Long idRefeicao) {
		return null;
	}
	
	@PostMapping
	public ResponseEntity<RefeicaoDto> cadastraReifeicao(@RequestBody RefeicaoForm form, UriComponentsBuilder uriBuilder) {
		return refeicaoService.cadastraRefeicao(form, uriBuilder);
	}
	
	@PostMapping("/{idRefeicao}/addAlimento/{idAlimento}") //cadastrar o nome
	public ResponseEntity<RefeicaoDto> adicionaAlimentoNaRefeicao(@PathVariable("idRefeicao") Long idRefeicao, @PathVariable("idAlimento") Long idAlimento) {
		return refeicaoService.adicionaAlimentoNaRefeicao(idRefeicao,idAlimento);
	}


}
