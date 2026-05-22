package com.autobots.automanager.servicos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.dto.MercadoriaDto;
import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import com.autobots.automanager.repositorios.RepositorioMercadoria;

@Service
public class MercadoriaServico {

    @Autowired
    private RepositorioMercadoria repositorio;

    @Autowired
    private RepositorioEmpresa repositorioEmpresa;

    public List<Mercadoria> obterMercadorias() {
        return repositorio.findAll();
    }

    public Mercadoria obterMercadoria(long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Mercadoria nao encontrada: " + id));
    }

    public Mercadoria cadastrar(MercadoriaDto dto) {
        Empresa empresa = repositorioEmpresa.findById(dto.getEmpresaId())
                .orElseThrow(() -> new RuntimeException("Empresa nao encontrada: " + dto.getEmpresaId()));
        Mercadoria mercadoria = new Mercadoria();
        mercadoria.setNome(dto.getNome());
        mercadoria.setValidade(dto.getValidade());
        mercadoria.setFabricao(dto.getFabricao());
        mercadoria.setCadastro(dto.getCadastro());
        mercadoria.setQuantidade(dto.getQuantidade());
        mercadoria.setValor(dto.getValor());
        mercadoria.setDescricao(dto.getDescricao());
        repositorio.save(mercadoria);
        empresa.getMercadorias().add(mercadoria);
        repositorioEmpresa.save(empresa);
        return mercadoria;
    }

    public Mercadoria atualizar(long id, MercadoriaDto dto) {
        Mercadoria mercadoria = obterMercadoria(id);
        if (dto.getNome() != null) {
            mercadoria.setNome(dto.getNome());
        }
        if (dto.getValidade() != null) {
            mercadoria.setValidade(dto.getValidade());
        }
        if (dto.getFabricao() != null) {
            mercadoria.setFabricao(dto.getFabricao());
        }
        if (dto.getCadastro() != null) {
            mercadoria.setCadastro(dto.getCadastro());
        }
        if (dto.getQuantidade() > 0) {
            mercadoria.setQuantidade(dto.getQuantidade());
        }
        if (dto.getValor() > 0) {
            mercadoria.setValor(dto.getValor());
        }
        if (dto.getDescricao() != null) {
            mercadoria.setDescricao(dto.getDescricao());
        }
        return repositorio.save(mercadoria);
    }

    public void excluir(long id) {
        repositorio.delete(obterMercadoria(id));
    }
}
