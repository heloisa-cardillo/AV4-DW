package com.autobots.automanager.servicos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.dto.EmailDto;
import com.autobots.automanager.entitades.Email;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.repositorios.RepositorioEmail;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@Service
public class EmailServico {

    @Autowired
    private RepositorioEmail repositorio;

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    public List<Email> obterEmails() {
        return repositorio.findAll();
    }

    public Email obterEmail(long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Email nao encontrado: " + id));
    }

    public Email cadastrar(EmailDto dto) {
        Usuario usuario = repositorioUsuario.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario nao encontrado: " + dto.getUsuarioId()));
        Email email = new Email();
        email.setEndereco(dto.getEndereco());
        repositorio.save(email);
        usuario.getEmails().add(email);
        repositorioUsuario.save(usuario);
        return email;
    }

    public Email atualizar(long id, EmailDto dto) {
        Email email = obterEmail(id);
        if (dto.getEndereco() != null) {
            email.setEndereco(dto.getEndereco());
        }
        return repositorio.save(email);
    }

    public void excluir(long id) {
        repositorio.delete(obterEmail(id));
    }
}
