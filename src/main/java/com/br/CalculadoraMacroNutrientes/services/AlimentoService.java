package com.br.CalculadoraMacroNutrientes.services;

import java.net.URI;
import java.util.List;

import com.br.CalculadoraMacroNutrientes.controllers.forms.AlimentoUpdateForm;
import com.br.CalculadoraMacroNutrientes.exceptions.AlimentoNaoEncontradoException;
import com.br.CalculadoraMacroNutrientes.exceptions.RefeicaoNaoEncontradoException;
import com.br.CalculadoraMacroNutrientes.exceptions.UsuarioNaoEncontradoException;
import com.br.CalculadoraMacroNutrientes.models.RefeicaoModel;
import com.br.CalculadoraMacroNutrientes.models.UsuarioModel;
import com.br.CalculadoraMacroNutrientes.repositories.RefeicaoRepository;
import com.br.CalculadoraMacroNutrientes.repositories.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.br.CalculadoraMacroNutrientes.controllers.dtos.AlimentoDetalharDto;
import com.br.CalculadoraMacroNutrientes.controllers.dtos.AlimentoDto;
import com.br.CalculadoraMacroNutrientes.controllers.forms.AlimentoForm;
import com.br.CalculadoraMacroNutrientes.models.AlimentoModel;
import com.br.CalculadoraMacroNutrientes.models.dominios.AlimentoDominio;
import com.br.CalculadoraMacroNutrientes.repositories.AlimentoDominioRepository;
import com.br.CalculadoraMacroNutrientes.repositories.AlimentoRepository;

@Slf4j
@Service
public class AlimentoService {
	
	private AlimentoDominioRepository alimentoDominioRepository;
	private AlimentoRepository alimentoRepository;

	private RefeicaoRepository refeicaoRepository;

	private UsuarioRepository usuarioRepository;
	
	@Autowired
	public AlimentoService (AlimentoDominioRepository alimentoDominioRepository,AlimentoRepository alimentoRepository, RefeicaoRepository refeicaoRepository,UsuarioRepository usuarioRepository) {
		this.alimentoDominioRepository = alimentoDominioRepository;
		this.alimentoRepository = alimentoRepository;
		this.refeicaoRepository = refeicaoRepository;
		this.usuarioRepository = usuarioRepository;
	}
	
	public AlimentoModel calcularMacrosDoAlimento(Long idAlimento, double quantidadeInformada) {
		try {
			AlimentoDominio alimentoReferencia = alimentoDominioRepository.findById(idAlimento)
					.orElseThrow(() -> new AlimentoNaoEncontradoException("Alimento de id " + idAlimento + " não encontrado"));

			String nome = alimentoReferencia.getNome();
			double carboidrato = (alimentoReferencia.getCarboidrato()*quantidadeInformada)/(alimentoReferencia.getQuantidade());
			double proteina = (alimentoReferencia.getProteina()*quantidadeInformada)/(alimentoReferencia.getQuantidade());
			double gordura = (alimentoReferencia.getGordura()*quantidadeInformada)/(alimentoReferencia.getQuantidade());
			double calorias = (alimentoReferencia.getCalorias()*quantidadeInformada)/(alimentoReferencia.getQuantidade());

			return new AlimentoModel(nome,quantidadeInformada,carboidrato,proteina,gordura,calorias);
		} catch (AlimentoNaoEncontradoException e) {
			log.error(e.getMessage());
			return null;
		}
	}
	
	
	public ResponseEntity<AlimentoDto> cadastraAlimento(AlimentoForm form, UriComponentsBuilder uriBuilder){
		//nao resolveu esse try catch
		try {
			AlimentoModel alimento = form.converter(this);
			log.info("Cadastrando alimento");
			alimentoRepository.save(alimento);
			URI uri = uriBuilder.path("/alimentos/{id}").buildAndExpand(alimento.getId()).toUri();
			return ResponseEntity.created(uri).body(new AlimentoDto(alimento));
		} catch (AlimentoNaoEncontradoException e) {
			log.info(e.getMessage());
			return ResponseEntity.notFound().build();
		}

	}

	public ResponseEntity<?> detalhaAlimento(Long idAlimento) {
		try{
			AlimentoModel alimento = alimentoRepository.findById(idAlimento)
					.orElseThrow(() -> new AlimentoNaoEncontradoException("Alimento de id " + idAlimento + " não encontrado"));
			return ResponseEntity.ok(new AlimentoDetalharDto(alimento));
		} catch (AlimentoNaoEncontradoException e) {
			log.error(e.getMessage());
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	public ResponseEntity<List<AlimentoDto>> listaAlimentos() {
		List<AlimentoModel> alimentos = alimentoRepository.findAll();
		return ResponseEntity.ok(AlimentoDto.converter(alimentos));
	}

    public ResponseEntity<?> deletaAlimento(Long idAlimento) {
		try {
			AlimentoModel alimento = alimentoRepository.findById(idAlimento)
					.orElseThrow(() -> new AlimentoNaoEncontradoException("Alimento de id " + idAlimento + " não encontrado"));
			log.info("Deletando alimento de id {}",alimento.getId());
			alimentoRepository.delete(alimento);
			return ResponseEntity.noContent().build();
		} catch (AlimentoNaoEncontradoException e) {
			log.error(e.getMessage());
			return ResponseEntity.badRequest().body(e.getMessage());
		}
    }

	public ResponseEntity<?> atualizaAlimento(AlimentoUpdateForm form, Long idUsuario, Long idRefeicao) {
		try {
			alimentoRepository.findById(form.getId())
					.orElseThrow(() -> new AlimentoNaoEncontradoException("Alimento de id " + form.getId() + " não encontrado"));
			AlimentoModel alimentoAtualizado = form.converter();
			if(idRefeicao != null && idUsuario != null) {
				try {
					AlimentoModel alimentoASerAtualizado = alimentoRepository.findById(alimentoAtualizado.getId())
							.orElseThrow(() -> new AlimentoNaoEncontradoException("Alimento de id " + alimentoAtualizado.getId() + " não encontrado"));
					log.info("Recalculando a distribuição dos macrosnutrientes da refeição");
					atualizarMacrosDaRefeicaoAPartirDaAtualizacaoDoAlimento(idRefeicao,alimentoAtualizado,alimentoASerAtualizado);
					log.info("Recalculando a distribuição dos macrosnutrientes do usuário");
					atualizarMacrosDoUsuarioAPartirDaAtualizacaoDoAlimento(idUsuario,alimentoAtualizado,alimentoASerAtualizado);
				} catch (UsuarioNaoEncontradoException | AlimentoNaoEncontradoException | RefeicaoNaoEncontradoException e) {
					log.error(e.getMessage());
					return ResponseEntity.notFound().build();
				}
			}
			alimentoRepository.save(alimentoAtualizado);
			return ResponseEntity.noContent().build();
		} catch (AlimentoNaoEncontradoException e) {
			log.error(e.getMessage());
			return ResponseEntity.notFound().build();
		}
	}

	private void atualizarMacrosDoUsuarioAPartirDaAtualizacaoDoAlimento(Long idUsuario, AlimentoModel alimentoAtualizado, AlimentoModel alimentoASerAtualizado) {
		UsuarioModel usuario = usuarioRepository.findById(idUsuario)
				.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário de id " + idUsuario + " não encontrado"));
		//adicionar os macros do alimento antigo e caloriasRestantes
		usuario.getDistribruicaoMacros().adicionaCarboidratoDisponivel(alimentoASerAtualizado.getCarboidrato());
		usuario.getDistribruicaoMacros().adicionaProteinaDisponivel(alimentoASerAtualizado.getProteina());
		usuario.getDistribruicaoMacros().adicionaGorguraDisponivel(alimentoASerAtualizado.getGordura());
		usuario.getDistribruicaoMacros().adicionaCaloriaDisponivel(alimentoASerAtualizado.getCalorias());
		//subtrair os macros do alimento novo e caloriasRestantes
		usuario.getDistribruicaoMacros().subtraiCarboidratoDisponivel(alimentoAtualizado.getCarboidrato());
		usuario.getDistribruicaoMacros().subtraiProteinaDisponivel(alimentoAtualizado.getProteina());
		usuario.getDistribruicaoMacros().subtraiGorduraDisponivel(alimentoAtualizado.getGordura());
		usuario.getDistribruicaoMacros().subtraiCaloriaDisponivel(alimentoAtualizado.getCalorias());
		usuarioRepository.save(usuario);
	}

	private void atualizarMacrosDaRefeicaoAPartirDaAtualizacaoDoAlimento(Long idRefeicao, AlimentoModel alimentoAtualizado, AlimentoModel alimentoASerAtualizado) {
		RefeicaoModel refeicao = refeicaoRepository.findById(idRefeicao)
				.orElseThrow(() -> new RefeicaoNaoEncontradoException("Refeição de id " + idRefeicao + " não encontrada"));
		//subtrai macros e caloriasTotais antigos
		refeicao.subtraiCarboitradosTotais(alimentoASerAtualizado.getCarboidrato());
		refeicao.subtraiProteinasTotais(alimentoASerAtualizado.getProteina());
		refeicao.subtraiGordurasTotais(alimentoASerAtualizado.getGordura());
		refeicao.subtraiCaloriasTotais(alimentoASerAtualizado.getCalorias());
		//adiciona macros e caloriasDisponiveis novos
		refeicao.adicionaCarboidratosTotais(alimentoAtualizado.getCarboidrato());
		refeicao.adicionaProteinasTotais(alimentoAtualizado.getProteina());
		refeicao.adicionaGordurasTotais(alimentoAtualizado.getGordura());
		refeicao.adicionaCaloriasTotais(alimentoAtualizado.getCalorias());
		refeicaoRepository.save(refeicao);
	}
}
