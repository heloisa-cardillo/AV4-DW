package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.dto.VendaDto;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.servicos.VendaServico;

@RestController
@RequestMapping("/venda")
public class VendaControle {

    @Autowired
    private VendaServico servico;

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR', 'CLIENTE')")
    @GetMapping("/{id}")
    public ResponseEntity<Venda> obterVenda(@PathVariable long id) {
        Venda venda = servico.obterVenda(id);
        adicionarLinks(venda);
        return new ResponseEntity<>(venda, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR', 'CLIENTE')")
    @GetMapping
    public ResponseEntity<List<Venda>> obterVendas() {
        List<Venda> vendas = servico.obterVendas();
        if (vendas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        vendas.forEach(v -> {
            Link self = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VendaControle.class).obterVenda(v.getId())).withSelfRel();
            v.add(self);
        });
        return new ResponseEntity<>(vendas, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
    @PostMapping("/cadastro")
    public ResponseEntity<Venda> cadastrar(@RequestBody VendaDto dto) {
        Venda venda = servico.cadastrar(dto);
        adicionarLinks(venda);
        return new ResponseEntity<>(venda, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Venda> atualizar(@PathVariable long id, @RequestBody VendaDto dto) {
        Venda venda = servico.atualizar(id, dto);
        adicionarLinks(venda);
        return new ResponseEntity<>(venda, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<?> excluir(@PathVariable long id) {
        servico.excluir(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void adicionarLinks(Venda venda) {
        venda.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VendaControle.class).obterVenda(venda.getId())).withSelfRel());
        venda.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VendaControle.class).obterVendas()).withRel("vendas"));
    }
}
