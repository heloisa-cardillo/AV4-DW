package com.autobots.automanager.servicos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.dto.EnderecoDto;
import com.autobots.automanager.entitades.Endereco;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.repositorios.RepositorioEndereco;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@Service
public class EnderecoServico {

    @Autowired
    private RepositorioEndereco repositorio;

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    public List<Endereco> obterEnderecos() {
        return repositorio.findAll();
    }

    public Endereco obterEndereco(long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Endereco nao encontrado: " + id));
    }

    public Endereco cadastrar(EnderecoDto dto) {
        Usuario usuario = repositorioUsuario.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario nao encontrado: " + dto.getUsuarioId()));
        Endereco endereco = new Endereco();
        endereco.setEstado(dto.getEstado());
        endereco.setCidade(dto.getCidade());
        endereco.setBairro(dto.getBairro());
        endereco.setRua(dto.getRua());
        endereco.setNumero(dto.getNumero());
        endereco.setCodigoPostal(dto.getCodigoPostal());
        endereco.setInformacoesAdicionais(dto.getInformacoesAdicionais());
        repositorio.save(endereco);
        usuario.setEndereco(endereco);
        repositorioUsuario.save(usuario);
        return endereco;
    }

    public Endereco atualizar(long id, EnderecoDto dto) {
        Endereco endereco = obterEndereco(id);
        if (dto.getEstado() != null) {
            endereco.setEstado(dto.getEstado());
        }
        if (dto.getCidade() != null) {
            endereco.setCidade(dto.getCidade());
        }
        if (dto.getBairro() != null) {
            endereco.setBairro(dto.getBairro());
        }
        if (dto.getRua() != null) {
            endereco.setRua(dto.getRua());
        }
        if (dto.getNumero() != null) {
            endereco.setNumero(dto.getNumero());
        }
        if (dto.getCodigoPostal() != null) {
            endereco.setCodigoPostal(dto.getCodigoPostal());
        }
        if (dto.getInformacoesAdicionais() != null) {
            endereco.setInformacoesAdicionais(dto.getInformacoesAdicionais());
        }
        return repositorio.save(endereco);
    }

    public void excluir(long id) {
        repositorio.delete(obterEndereco(id));
    }
}
