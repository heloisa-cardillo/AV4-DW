package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.dto.EmailDto;
import com.autobots.automanager.entitades.Email;
import com.autobots.automanager.servicos.EmailServico;

@RestController
@RequestMapping("/email")
public class EmailControle {

    @Autowired
    private EmailServico servico;

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
    @GetMapping("/{id}")
    public ResponseEntity<Email> obterEmail(@PathVariable long id) {
        Email email = servico.obterEmail(id);
        adicionarLinks(email);
        return new ResponseEntity<>(email, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
    @GetMapping
    public ResponseEntity<List<Email>> obterEmails() {
        List<Email> emails = servico.obterEmails();
        if (emails.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        emails.forEach(e -> {
            Link self = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmailControle.class).obterEmail(e.getId())).withSelfRel();
            e.add(self);
        });
        return new ResponseEntity<>(emails, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @PostMapping("/cadastro")
    public ResponseEntity<Email> cadastrar(@RequestBody EmailDto dto) {
        Email email = servico.cadastrar(dto);
        adicionarLinks(email);
        return new ResponseEntity<>(email, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Email> atualizar(@PathVariable long id, @RequestBody EmailDto dto) {
        Email email = servico.atualizar(id, dto);
        adicionarLinks(email);
        return new ResponseEntity<>(email, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<?> excluir(@PathVariable long id) {
        servico.excluir(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void adicionarLinks(Email email) {
        email.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmailControle.class).obterEmail(email.getId())).withSelfRel());
        email.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmailControle.class).obterEmails()).withRel("emails"));
    }
}
