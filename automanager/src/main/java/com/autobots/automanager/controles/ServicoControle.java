package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.dto.ServicoDto;
import com.autobots.automanager.entitades.Servico;
import com.autobots.automanager.servicos.ServicoServico;

@RestController
@RequestMapping("/servico")
public class ServicoControle {

    @Autowired
    private ServicoServico servico;

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR', 'CLIENTE')")
    @GetMapping("/{id}")
    public ResponseEntity<Servico> obterServico(@PathVariable long id) {
        Servico s = servico.obterServico(id);
        adicionarLinks(s);
        return new ResponseEntity<>(s, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR', 'CLIENTE')")
    @GetMapping
    public ResponseEntity<List<Servico>> obterServicos() {
        List<Servico> servicos = servico.obterServicos();
        if (servicos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        servicos.forEach(s -> {
            Link self = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ServicoControle.class).obterServico(s.getId())).withSelfRel();
            s.add(self);
        });
        return new ResponseEntity<>(servicos, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @PostMapping("/cadastro")
    public ResponseEntity<Servico> cadastrar(@RequestBody ServicoDto dto) {
        Servico s = servico.cadastrar(dto);
        adicionarLinks(s);
        return new ResponseEntity<>(s, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Servico> atualizar(@PathVariable long id, @RequestBody ServicoDto dto) {
        Servico s = servico.atualizar(id, dto);
        adicionarLinks(s);
        return new ResponseEntity<>(s, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<?> excluir(@PathVariable long id) {
        servico.excluir(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void adicionarLinks(Servico s) {
        s.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ServicoControle.class).obterServico(s.getId())).withSelfRel());
        s.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ServicoControle.class).obterServicos()).withRel("servicos"));
    }
}
