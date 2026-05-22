package com.autobots.automanager.servicos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.dto.UsuarioDto;
import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@Service
public class UsuarioServico {

    @Autowired
    private RepositorioUsuario repositorio;

    @Autowired
    private RepositorioEmpresa repositorioEmpresa;

    public List<Usuario> obterUsuarios() {
        return repositorio.findAll();
    }

    public Usuario obterUsuario(long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario nao encontrado: " + id));
    }

    public Usuario cadastrar(UsuarioDto dto) {
        Empresa empresa = repositorioEmpresa.findById(dto.getEmpresaId())
                .orElseThrow(() -> new RuntimeException("Empresa nao encontrada: " + dto.getEmpresaId()));
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setNomeSocial(dto.getNomeSocial());
        usuario.setPerfis(dto.getPerfis());
        repositorio.save(usuario);
        empresa.getUsuarios().add(usuario);
        repositorioEmpresa.save(empresa);
        return usuario;
    }

    public Usuario atualizar(long id, UsuarioDto dto) {
        Usuario usuario = obterUsuario(id);
        if (dto.getNome() != null) {
            usuario.setNome(dto.getNome());
        }
        if (dto.getNomeSocial() != null) {
            usuario.setNomeSocial(dto.getNomeSocial());
        }
        if (dto.getPerfis() != null) {
            usuario.setPerfis(dto.getPerfis());
        }
        return repositorio.save(usuario);
    }

    public void excluir(long id) {
        repositorio.delete(obterUsuario(id));
    }
}


