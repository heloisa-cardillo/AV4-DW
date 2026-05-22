package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.dto.EnderecoDto;
import com.autobots.automanager.entitades.Endereco;
import com.autobots.automanager.servicos.EnderecoServico;

@RestController
@RequestMapping("/endereco")
public class EnderecoControle {

	@Autowired
	private EnderecoServico servico;

	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
	@GetMapping("/{id}")
	public ResponseEntity<Endereco> obterEndereco(@PathVariable long id) {
		Endereco endereco = servico.obterEndereco(id);
		adicionarLinks(endereco);
		return new ResponseEntity<>(endereco, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
	@GetMapping
	public ResponseEntity<List<Endereco>> obterEnderecos() {
		List<Endereco> enderecos = servico.obterEnderecos();
		if (enderecos.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		enderecos.forEach(e -> {
			Link self = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EnderecoControle.class).obterEndereco(e.getId())).withSelfRel();
			e.add(self);
		});
		return new ResponseEntity<>(enderecos, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
	@PostMapping("/cadastro")
	public ResponseEntity<Endereco> cadastrar(@RequestBody EnderecoDto dto) {
		Endereco endereco = servico.cadastrar(dto);
		adicionarLinks(endereco);
		return new ResponseEntity<>(endereco, HttpStatus.CREATED);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
	@PutMapping("/atualizar/{id}")
	public ResponseEntity<Endereco> atualizar(@PathVariable long id, @RequestBody EnderecoDto dto) {
		Endereco endereco = servico.atualizar(id, dto);
		adicionarLinks(endereco);
		return new ResponseEntity<>(endereco, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
	@DeleteMapping("/excluir/{id}")
	public ResponseEntity<?> excluir(@PathVariable long id) {
		servico.excluir(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	private void adicionarLinks(Endereco endereco) {
		endereco.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EnderecoControle.class).obterEndereco(endereco.getId())).withSelfRel());
		endereco.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EnderecoControle.class).obterEnderecos()).withRel("enderecos"));
	}
}