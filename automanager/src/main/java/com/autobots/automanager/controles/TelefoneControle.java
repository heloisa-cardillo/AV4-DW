package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.dto.TelefoneDto;
import com.autobots.automanager.entitades.Telefone;
import com.autobots.automanager.servicos.TelefoneServico;

@RestController
@RequestMapping("/telefone")
public class TelefoneControle {

    @Autowired
    private TelefoneServico servico;

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
    @GetMapping("/{id}")
    public ResponseEntity<Telefone> obterTelefone(@PathVariable long id) {
        Telefone telefone = servico.obterTelefone(id);
        adicionarLinks(telefone);
        return new ResponseEntity<>(telefone, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
    @GetMapping
    public ResponseEntity<List<Telefone>> obterTelefones() {
        List<Telefone> telefones = servico.obterTelefones();
        if (telefones.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        telefones.forEach(t -> {
            Link self = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TelefoneControle.class).obterTelefone(t.getId())).withSelfRel();
            t.add(self);
        });
        return new ResponseEntity<>(telefones, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @PostMapping("/cadastro")
    public ResponseEntity<Telefone> cadastrar(@RequestBody TelefoneDto dto) {
        Telefone telefone = servico.cadastrar(dto);
        adicionarLinks(telefone);
        return new ResponseEntity<>(telefone, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Telefone> atualizar(@PathVariable long id, @RequestBody TelefoneDto dto) {
        Telefone telefone = servico.atualizar(id, dto);
        adicionarLinks(telefone);
        return new ResponseEntity<>(telefone, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<?> excluir(@PathVariable long id) {
        servico.excluir(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void adicionarLinks(Telefone telefone) {
        telefone.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TelefoneControle.class).obterTelefone(telefone.getId())).withSelfRel());
        telefone.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TelefoneControle.class).obterTelefones()).withRel("telefones"));
    }
}
