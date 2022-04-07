package com.br.CalculadoraMacroNutrientes.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
	
	@GetMapping
	public Page<UsuarioDto> listaUsuarios(Pageable paginacao) {
		return usuarioService.listaUsuarios(paginacao);	
	}
	
	@GetMapping("/{idUsuario}")
	public ResponseEntity<UsuarioDetalharDto> detalhaUsuario(@PathVariable Long idUsuario) {
		return usuarioService.detalhaUsuario(idUsuario);
	}
	
	@PostMapping
	public ResponseEntity<UsuarioDto> cadastraUsuario(@RequestBody UsuarioForm form, UriComponentsBuilder uriBuilder) {
		return usuarioService.cadastraUsuario(form, uriBuilder);
	}
	
	@PostMapping("/{idUsuario}/addRefeicao/{idRefeicao}")
	public ResponseEntity<UsuarioDto> cadastraRefeicao(@PathVariable("idUsuario") Long idUsuario, @PathVariable("idRefeicao") Long idRefeicao) {
		return usuarioService.cadastraRefeicaoParaUsuario(idUsuario,idRefeicao);
	}
	
	@ApiOperation(value = "Cadastra os macronutrientes informados pelo usuário")
	@PostMapping("/{idUsuario}/addMacros/{idMacros}")
	public ResponseEntity<UsuarioDto> cadastraMacros(@PathVariable("idUsuario") Long idUsuario,@PathVariable("idMacros") Long idMacros){
		return usuarioService.cadastraMacros(idUsuario,idMacros);
	}
	
	@PostMapping("/{idUsuario}/addExercicio/{idExercicio}")
	public ResponseEntity<UsuarioDto> cadastraExercicio(@PathVariable("idUsuario") Long idUsuario, @PathVariable("idExercicio") Long idExercicio){
		return usuarioService.cadastraExercicio(idUsuario,idExercicio);
	}
	
	@PostMapping("/calcCalorias/{idUsuario}")
	public ResponseEntity<UsuarioDto> cadastraCaloriasDiarias(@PathVariable("idUsuario") Long idUsuario){
		return usuarioService.cadastraCaloriasDiarias(idUsuario);
		
	}

}
