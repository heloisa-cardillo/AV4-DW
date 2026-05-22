package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.dto.DocumentoDto;
import com.autobots.automanager.entitades.Documento;
import com.autobots.automanager.servicos.DocumentoServico;

@RestController
@RequestMapping("/documento")
public class DocumentoControle {

    @Autowired
    private DocumentoServico servico;

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
    @GetMapping("/{id}")
    public ResponseEntity<Documento> obterDocumento(@PathVariable long id) {
        Documento documento = servico.obterDocumento(id);
        adicionarLinks(documento);
        return new ResponseEntity<>(documento, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
    @GetMapping
    public ResponseEntity<List<Documento>> obterDocumentos() {
        List<Documento> documentos = servico.obterDocumentos();
        if (documentos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        documentos.forEach(d -> {
            Link self = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DocumentoControle.class).obterDocumento(d.getId())).withSelfRel();
            d.add(self);
        });
        return new ResponseEntity<>(documentos, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @PostMapping("/cadastro")
    public ResponseEntity<Documento> cadastrar(@RequestBody DocumentoDto dto) {
        Documento documento = servico.cadastrar(dto);
        adicionarLinks(documento);
        return new ResponseEntity<>(documento, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Documento> atualizar(@PathVariable long id, @RequestBody DocumentoDto dto) {
        Documento documento = servico.atualizar(id, dto);
        adicionarLinks(documento);
        return new ResponseEntity<>(documento, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<?> excluir(@PathVariable long id) {
        servico.excluir(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void adicionarLinks(Documento documento) {
        documento.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DocumentoControle.class).obterDocumento(documento.getId())).withSelfRel());
        documento.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DocumentoControle.class).obterDocumentos()).withRel("documentos"));
    }
}
