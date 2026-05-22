package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.dto.CredencialDto;
import com.autobots.automanager.entitades.Credencial;
import com.autobots.automanager.servicos.CredencialServico;

@RestController
@RequestMapping("/credencial")
public class CredencialControle {

    @Autowired
    private CredencialServico servico;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Credencial> obterCredencial(@PathVariable long id) {
        Credencial credencial = servico.obterCredencial(id);
        adicionarLinks(credencial);
        return new ResponseEntity<>(credencial, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Credencial>> obterCredenciais() {
        List<Credencial> credenciais = servico.obterCredenciais();
        if (credenciais.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        credenciais.forEach(c -> {
            Link self = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CredencialControle.class).obterCredencial(c.getId())).withSelfRel();
            c.add(self);
        });
        return new ResponseEntity<>(credenciais, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @PostMapping("/cadastro")
    public ResponseEntity<Credencial> cadastrar(@RequestBody CredencialDto dto) {
        Credencial credencial = servico.cadastrar(dto);
        adicionarLinks(credencial);
        return new ResponseEntity<>(credencial, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Credencial> atualizar(@PathVariable long id, @RequestBody CredencialDto dto) {
        Credencial credencial = servico.atualizar(id, dto);
        adicionarLinks(credencial);
        return new ResponseEntity<>(credencial, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<?> excluir(@PathVariable long id) {
        servico.excluir(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void adicionarLinks(Credencial credencial) {
        credencial.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CredencialControle.class).obterCredencial(credencial.getId())).withSelfRel());
        credencial.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CredencialControle.class).obterCredenciais()).withRel("credenciais"));
    }
}
