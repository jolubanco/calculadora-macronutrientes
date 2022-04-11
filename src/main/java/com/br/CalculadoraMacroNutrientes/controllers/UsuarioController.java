package com.br.CalculadoraMacroNutrientes.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.br.CalculadoraMacroNutrientes.controllers.dtos.UsuarioDetalharDto;
import com.br.CalculadoraMacroNutrientes.controllers.dtos.UsuarioDto;
import com.br.CalculadoraMacroNutrientes.controllers.forms.UsuarioForm;
import com.br.CalculadoraMacroNutrientes.services.UsuarioService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
	
	@Autowired
	private UsuarioService usuarioService;
	
	@ApiOperation(value = "Lista todos os usuários cadastrados")
	@GetMapping
	public Page<UsuarioDto> listaUsuarios(Pageable paginacao) {
		return usuarioService.listaUsuarios(paginacao);	
	}
	
	@ApiOperation(value = "Detalha informações do usuário")
	@GetMapping("/{idUsuario}")
	public ResponseEntity<UsuarioDetalharDto> detalhaUsuario(@PathVariable Long idUsuario) {
		return usuarioService.detalhaUsuario(idUsuario);
	}
	
	@ApiOperation(value = "Cadastra um usuário e calcula a taxa de matabolismo basal e a distribuição de macronutrientes de acordo com o objetivo")
	@PostMapping
	public ResponseEntity<UsuarioDto> cadastraUsuario(@RequestBody @Valid UsuarioForm form, UriComponentsBuilder uriBuilder) {
		return usuarioService.cadastraUsuario(form, uriBuilder);
	}
	
	@ApiOperation(value = "Associa uma refeição ao usuário a partir da lista já cadastrada, e atualiza as calorias restantes")
	@PatchMapping("/{idUsuario}/addRefeicao/{idRefeicao}")
	public ResponseEntity<UsuarioDto> cadastraRefeicao(@PathVariable("idUsuario") Long idUsuario, @PathVariable("idRefeicao") Long idRefeicao) {
		return usuarioService.cadastraRefeicaoParaUsuario(idUsuario,idRefeicao);
	}
	
	@ApiOperation(value = "Associa os macronutrientes informados pelo usuário a partir da lista já cadastrada")
	@PatchMapping("/{idUsuario}/addMacros/{idMacros}")
	public ResponseEntity<UsuarioDto> cadastraMacros(@PathVariable("idUsuario") Long idUsuario,@PathVariable("idMacros") Long idMacros){
		return usuarioService.cadastraMacros(idUsuario,idMacros);
	}
	
	@ApiOperation(value = "Associa um exercício ao usuário a partir da lista já cadastrada")
	@PatchMapping("/{idUsuario}/addExercicio/{idExercicio}")
	public ResponseEntity<UsuarioDto> cadastraExercicio(@PathVariable("idUsuario") Long idUsuario, @PathVariable("idExercicio") Long idExercicio){
		return usuarioService.cadastraExercicio(idUsuario,idExercicio);
	}
	
	public ResponseEntity<?> listaRefeicoesDoUsuario(){
		return null;
	}
}
