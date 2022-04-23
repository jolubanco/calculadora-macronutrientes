package com.br.CalculadoraMacroNutrientes.controllers;

import java.util.List;

import com.br.CalculadoraMacroNutrientes.controllers.forms.RefeicaoUpdateForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import com.br.CalculadoraMacroNutrientes.controllers.dtos.RefeicaoDetalharDto;
import com.br.CalculadoraMacroNutrientes.controllers.dtos.RefeicaoDto;
import com.br.CalculadoraMacroNutrientes.controllers.forms.RefeicaoForm;
import com.br.CalculadoraMacroNutrientes.services.RefeicaoService;

import io.swagger.annotations.ApiOperation;

import javax.validation.Valid;

@RestController
@RequestMapping("/refeicoes")
public class RefeicaoController {
	
	@Autowired
	private RefeicaoService refeicaoService;
	
	//verificar se faz sentido esse endpoint
	@ApiOperation(value = "Lista todas as refeições cadastradas")
	@GetMapping
	public ResponseEntity<List<RefeicaoDto>> listaRefeicoesPorUsuario(){
		return refeicaoService.listaRefeicoes();
	}
	
	@ApiOperation(value = "Detalha a refeição cadastrada")
	@GetMapping("/{idRefeicao}")
	public ResponseEntity<?> detalhaRefeicao(@PathVariable("idRefeicao") Long idRefeicao) {
		return refeicaoService.detalhaRefeicao(idRefeicao);
	}
	
	@ApiOperation(value = "Cadastra uma refeição")//está com problema na conversao do form
	@PostMapping
	public ResponseEntity<RefeicaoDto> cadastraReifeicao(@RequestBody @Valid RefeicaoForm form, UriComponentsBuilder uriBuilder) {
		return refeicaoService.cadastraRefeicao(form, uriBuilder);
	}
	@ApiOperation(value = "Atualiza uma refeição se existir, caso contrário cria uma nova refeição")
	@PutMapping("/update")
	public ResponseEntity<?> atualizaRefeicao(@RequestBody @Valid RefeicaoUpdateForm form,UriComponentsBuilder uriBuilder) {
		return refeicaoService.atualizaRefeicao(form,uriBuilder);
	}
	
	@ApiOperation(value = "Adiciona alimentos cadastrados em uma refeição já cadastrada")
	@PatchMapping("/{idRefeicao}/addAlimento/{idAlimento}")
	public ResponseEntity<?> adicionaAlimentoNaRefeicao(@PathVariable("idRefeicao") Long idRefeicao, @PathVariable("idAlimento") Long idAlimento) {
		return refeicaoService.adicionaAlimentoNaRefeicao(idRefeicao,idAlimento);
	}

	@ApiOperation(value="Remove um alimento da refeição")
	@PatchMapping("/{idRefeicao}/removeAlimento/{idAlimento}")
	public ResponseEntity<?> removeAlimentoDaRefeicao(@PathVariable("idRefeicao") Long idRefeicao, @PathVariable("idAlimento") Long idAlimento){
		return refeicaoService.removeAlimento(idRefeicao,idAlimento);
	}

	@ApiOperation(value="Deleta uma refeição")
	@DeleteMapping("/delete/{idRefeicao}")
	public ResponseEntity<?> deletaRefeicao(@PathVariable("idRefeicao") Long idRefeicao) {
		return refeicaoService.deletaRefeicao(idRefeicao);
	}



}
