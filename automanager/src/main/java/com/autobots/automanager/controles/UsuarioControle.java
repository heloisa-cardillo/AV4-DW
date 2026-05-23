package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.dto.UsuarioDto;
import com.autobots.automanager.entitades.CredencialUsuarioSenha;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.servicos.UsuarioServico;

@RestController
@RequestMapping("/usuario")
public class UsuarioControle {

    @Autowired
    private UsuarioServico servico;

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obterUsuario(@PathVariable long id) {
        Usuario usuario = servico.obterUsuario(id);
        adicionarLinks(usuario);
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
    @GetMapping
    public ResponseEntity<List<Usuario>> obterUsuarios() {
        List<Usuario> usuarios = servico.obterUsuarios();
        if (usuarios.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        usuarios.forEach(u -> {
            Link self = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).obterUsuario(u.getId())).withSelfRel();
            u.add(self);
        });
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR', 'CLIENTE')")
    @GetMapping("/meu")
    public ResponseEntity<Usuario> obterMeuUsuario(Authentication authentication) {
        String nomeUsuario = authentication.getName();
        List<Usuario> usuarios = servico.obterUsuarios();
        Usuario meu = usuarios.stream()
                .filter(u -> u.getCredenciais().stream()
                .anyMatch(c -> c instanceof CredencialUsuarioSenha
                && ((CredencialUsuarioSenha) c).getNomeUsuario().equals(nomeUsuario)))
                .findFirst()
                .orElse(null);
        if (meu == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        adicionarLinks(meu);
        return new ResponseEntity<>(meu, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
    @PostMapping("/cadastro")
    public ResponseEntity<Usuario> cadastrar(@RequestBody UsuarioDto dto) {
        Usuario usuario = servico.cadastrar(dto);
        adicionarLinks(usuario);
        return new ResponseEntity<>(usuario, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Usuario> atualizar(@PathVariable long id, @RequestBody UsuarioDto dto) {
        Usuario usuario = servico.atualizar(id, dto);
        adicionarLinks(usuario);
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<?> excluir(@PathVariable long id) {
        servico.excluir(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void adicionarLinks(Usuario usuario) {
        usuario.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).obterUsuario(usuario.getId())).withSelfRel());
        usuario.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).obterUsuarios()).withRel("usuarios"));
    }
}
