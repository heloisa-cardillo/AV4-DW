package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.dto.VeiculoDto;
import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.servicos.VeiculoServico;

@RestController
@RequestMapping("/veiculo")
public class VeiculoControle {

    @Autowired
    private VeiculoServico servico;

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
    @GetMapping("/{id}")
    public ResponseEntity<Veiculo> obterVeiculo(@PathVariable long id) {
        Veiculo veiculo = servico.obterVeiculo(id);
        adicionarLinks(veiculo);
        return new ResponseEntity<>(veiculo, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
    @GetMapping
    public ResponseEntity<List<Veiculo>> obterVeiculos() {
        List<Veiculo> veiculos = servico.obterVeiculos();
        if (veiculos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        veiculos.forEach(v -> {
            Link self = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VeiculoControle.class).obterVeiculo(v.getId())).withSelfRel();
            v.add(self);
        });
        return new ResponseEntity<>(veiculos, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @PostMapping("/cadastro")
    public ResponseEntity<Veiculo> cadastrar(@RequestBody VeiculoDto dto) {
        Veiculo veiculo = servico.cadastrar(dto);
        adicionarLinks(veiculo);
        return new ResponseEntity<>(veiculo, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Veiculo> atualizar(@PathVariable long id, @RequestBody VeiculoDto dto) {
        Veiculo veiculo = servico.atualizar(id, dto);
        adicionarLinks(veiculo);
        return new ResponseEntity<>(veiculo, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<?> excluir(@PathVariable long id) {
        servico.excluir(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void adicionarLinks(Veiculo veiculo) {
        veiculo.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VeiculoControle.class).obterVeiculo(veiculo.getId())).withSelfRel());
        veiculo.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VeiculoControle.class).obterVeiculos()).withRel("veiculos"));
    }
}
