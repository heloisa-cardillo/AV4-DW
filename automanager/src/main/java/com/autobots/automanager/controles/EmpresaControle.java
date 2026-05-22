package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.dto.EmpresaDto;
import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.servicos.EmpresaServico;

@RestController
@RequestMapping("/empresa")
public class EmpresaControle {

    @Autowired
    private EmpresaServico servico;

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR', 'CLIENTE')")
    @GetMapping("/{id}")
    public ResponseEntity<Empresa> obterEmpresa(@PathVariable long id) {
        Empresa empresa = servico.obterEmpresa(id);
        adicionarLinks(empresa);
        return new ResponseEntity<>(empresa, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR', 'CLIENTE')")
    @GetMapping
    public ResponseEntity<List<Empresa>> obterEmpresas() {
        List<Empresa> empresas = servico.obterEmpresas();
        if (empresas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        empresas.forEach(e -> {
            Link self = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class).obterEmpresa(e.getId())).withSelfRel();
            e.add(self);
        });
        return new ResponseEntity<>(empresas, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/cadastro")
    public ResponseEntity<Empresa> cadastrar(@RequestBody EmpresaDto dto) {
        Empresa empresa = servico.cadastrar(dto);
        adicionarLinks(empresa);
        return new ResponseEntity<>(empresa, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Empresa> atualizar(@PathVariable long id, @RequestBody EmpresaDto dto) {
        Empresa empresa = servico.atualizar(id, dto);
        adicionarLinks(empresa);
        return new ResponseEntity<>(empresa, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<?> excluir(@PathVariable long id) {
        servico.excluir(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void adicionarLinks(Empresa empresa) {
        empresa.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class).obterEmpresa(empresa.getId())).withSelfRel());
        empresa.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class).obterEmpresas()).withRel("empresas"));
    }
}
