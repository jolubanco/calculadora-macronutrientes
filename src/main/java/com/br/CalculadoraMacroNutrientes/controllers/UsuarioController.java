package com.br.CalculadoraMacroNutrientes.controllers;

import javax.validation.Valid;

import com.br.CalculadoraMacroNutrientes.controllers.forms.UsuarioUpdateForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
	public ResponseEntity<?> detalhaUsuario(@PathVariable Long idUsuario) {
		return usuarioService.detalhaUsuario(idUsuario);
	}
	
	@ApiOperation(value = "Cadastra um usuário e calcula a necessidade de calorias diárias e a distribuição de macronutrientes de acordo com o objetivo")
	@PostMapping
	public ResponseEntity<UsuarioDto> cadastraUsuario(@RequestBody @Valid UsuarioForm form, UriComponentsBuilder uriBuilder) {
		return usuarioService.cadastraUsuario(form, uriBuilder);
	}

	@ApiOperation(value = "Atualiza dados do usuário")
	@PutMapping("/update")
	public ResponseEntity<?> atualizaUsuario(@RequestBody @Valid UsuarioUpdateForm form) {
		return usuarioService.atualizaUsuario(form);
	}

	@ApiOperation(value = "Deleta um usuário")
	@DeleteMapping("/delete/{idUsuario}")
	public ResponseEntity<?> deletaUsuario(@PathVariable("idUsuario") Long idUsuario){
		return usuarioService.deletaUsuario(idUsuario);
	}
	
	@ApiOperation(value = "Associa uma refeição ao usuário a partir da lista já cadastrada, e atualiza as calorias restantes")
	@PatchMapping("/{idUsuario}/add/refeicao/{idRefeicao}")
	public ResponseEntity<?> cadastraRefeicao(@PathVariable("idUsuario") Long idUsuario, @PathVariable("idRefeicao") Long idRefeicao) {
		return usuarioService.cadastraRefeicaoParaUsuario(idUsuario,idRefeicao);
	}

	@ApiOperation(value = "Remove uma refeicão da lista cadastrada do usuário")
	@PatchMapping("/{idUsuario}/remove/refeicao/{idRefeicao}")
	public ResponseEntity<?> removeRefeicaoDoUsuario(@PathVariable("idUsuario") Long idUsuario, @PathVariable("idRefeicao") Long idRefeicao){
		return usuarioService.removeRefeicaoDoUsuario(idUsuario,idRefeicao);
	}
	
	@ApiOperation(value = "Associa os macronutrientes informados pelo usuário a partir da lista já cadastrada")
	@PatchMapping("/{idUsuario}/add/macros/{idMacros}")
	public ResponseEntity<?> cadastraMacros(@PathVariable("idUsuario") Long idUsuario,@PathVariable("idMacros") Long idMacros){
		return usuarioService.cadastraMacros(idUsuario,idMacros);
	}

	@ApiOperation(value = "Associa um exercício ao usuário a partir da lista já cadastrada")
	@PatchMapping("/{idUsuario}/add/exercicio/{idExercicio}")
	public ResponseEntity<?> cadastraExercicio(@PathVariable("idUsuario") Long idUsuario, @PathVariable("idExercicio") Long idExercicio){
		return usuarioService.cadastraExercicio(idUsuario,idExercicio);
	}

	@ApiOperation(value = "Remove um exercício da lista cadastrada do usuário")
	@PatchMapping("/{idUsuario}/remove/exercicio/{idExercicio}")
	public ResponseEntity<?> removeExercicioDoUsuario(@PathVariable("idUsuario") Long idUsuario, @PathVariable("idExercicio") Long idExercicio){
		return usuarioService.removeExercicioDoUsuario(idUsuario,idExercicio);
	}

	@ApiOperation(value = "Atualiza peso do usuário e recalcula os dados")
	@PatchMapping("/{idUsuario}/update/peso/{valorPeso}")
	public ResponseEntity<?> atualizarPeso(@PathVariable("idUsuario") Long idUsuario,@PathVariable("valorPeso") double valorPeso){
		return usuarioService.atualizarPeso(idUsuario,valorPeso);
	}

	@ApiOperation(value = "Atualiza objetivo do usuário e recalcula os dados")
	@PatchMapping("/{idUsuario}/update/objetivo/{objetivo}")
	public ResponseEntity<?> atualizarObjetivo(@PathVariable("idUsuario") Long idUsuario,@PathVariable("objetivo") String objetivo){
		return usuarioService.atualizarObjetivo(idUsuario,objetivo);
	}
}
