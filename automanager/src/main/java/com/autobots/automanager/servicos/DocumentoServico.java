package com.autobots.automanager.servicos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.dto.DocumentoDto;
import com.autobots.automanager.entitades.Documento;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.repositorios.RepositorioDocumento;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@Service
public class DocumentoServico {

    @Autowired
    private RepositorioDocumento repositorio;

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    public List<Documento> obterDocumentos() {
        return repositorio.findAll();
    }

    public Documento obterDocumento(long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Documento nao encontrado: " + id));
    }

    public Documento cadastrar(DocumentoDto dto) {
        Usuario usuario = repositorioUsuario.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario nao encontrado: " + dto.getUsuarioId()));
        Documento documento = new Documento();
        documento.setTipo(dto.getTipo());
        documento.setNumero(dto.getNumero());
        documento.setDataEmissao(dto.getDataEmissao());
        repositorio.save(documento);
        usuario.getDocumentos().add(documento);
        repositorioUsuario.save(usuario);
        return documento;
    }

    public Documento atualizar(long id, DocumentoDto dto) {
        Documento documento = obterDocumento(id);
        if (dto.getTipo() != null) {
            documento.setTipo(dto.getTipo());
        }
        if (dto.getNumero() != null) {
            documento.setNumero(dto.getNumero());
        }
        if (dto.getDataEmissao() != null) {
            documento.setDataEmissao(dto.getDataEmissao());
        }
        return repositorio.save(documento);
    }

    public void excluir(long id) {
        repositorio.delete(obterDocumento(id));
    }
}
