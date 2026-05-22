package com.autobots.automanager.servicos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.dto.TelefoneDto;
import com.autobots.automanager.entitades.Telefone;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.repositorios.RepositorioTelefone;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@Service
public class TelefoneServico {

    @Autowired
    private RepositorioTelefone repositorio;

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    public List<Telefone> obterTelefones() {
        return repositorio.findAll();
    }

    public Telefone obterTelefone(long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Telefone nao encontrado: " + id));
    }

    public Telefone cadastrar(TelefoneDto dto) {
        Usuario usuario = repositorioUsuario.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario nao encontrado: " + dto.getUsuarioId()));
        Telefone telefone = new Telefone();
        telefone.setDdd(dto.getDdd());
        telefone.setNumero(dto.getNumero());
        repositorio.save(telefone);
        usuario.getTelefones().add(telefone);
        repositorioUsuario.save(usuario);
        return telefone;
    }

    public Telefone atualizar(long id, TelefoneDto dto) {
        Telefone telefone = obterTelefone(id);
        if (dto.getDdd() != null) {
            telefone.setDdd(dto.getDdd());
        }
        if (dto.getNumero() != null) {
            telefone.setNumero(dto.getNumero());
        }
        return repositorio.save(telefone);
    }

    public void excluir(long id) {
        repositorio.delete(obterTelefone(id));
    }
}
