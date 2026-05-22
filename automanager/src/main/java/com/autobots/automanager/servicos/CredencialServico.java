package com.autobots.automanager.servicos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.autobots.automanager.dto.CredencialDto;
import com.autobots.automanager.entitades.Credencial;
import com.autobots.automanager.entitades.CredencialUsuarioSenha;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.repositorios.RepositorioCredencial;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@Service
public class CredencialServico {

    @Autowired
    private RepositorioCredencial repositorio;

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    public List<Credencial> obterCredenciais() {
        return repositorio.findAll();
    }

    public Credencial obterCredencial(long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Credencial nao encontrada: " + id));
    }

    public Credencial cadastrar(CredencialDto dto) {
        Usuario usuario = repositorioUsuario.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario nao encontrado: " + dto.getUsuarioId()));
        BCryptPasswordEncoder codificador = new BCryptPasswordEncoder();
        CredencialUsuarioSenha credencial = new CredencialUsuarioSenha();
        credencial.setNomeUsuario(dto.getNomeUsuario());
        credencial.setSenha(codificador.encode(dto.getSenha()));
        credencial.setCriacao(dto.getCriacao());
        credencial.setUltimoAcesso(dto.getUltimoAcesso());
        credencial.setInativo(dto.isInativo());
        repositorio.save(credencial);
        usuario.getCredenciais().add(credencial);
        repositorioUsuario.save(usuario);
        return credencial;
    }

    public Credencial atualizar(long id, CredencialDto dto) {
        Credencial credencial = obterCredencial(id);
        if (credencial instanceof CredencialUsuarioSenha) {
            CredencialUsuarioSenha cus = (CredencialUsuarioSenha) credencial;
            if (dto.getNomeUsuario() != null) {
                cus.setNomeUsuario(dto.getNomeUsuario());
            }
            if (dto.getSenha() != null) {
                BCryptPasswordEncoder codificador = new BCryptPasswordEncoder();
                cus.setSenha(codificador.encode(dto.getSenha()));
            }
        }
        if (dto.getCriacao() != null) {
            credencial.setCriacao(dto.getCriacao());
        }
        if (dto.getUltimoAcesso() != null) {
            credencial.setUltimoAcesso(dto.getUltimoAcesso());
        }
        return repositorio.save(credencial);
    }

    public void excluir(long id) {
        repositorio.delete(obterCredencial(id));
    }
}
