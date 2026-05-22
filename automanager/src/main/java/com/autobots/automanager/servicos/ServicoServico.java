package com.autobots.automanager.servicos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.dto.ServicoDto;
import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.entitades.Servico;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import com.autobots.automanager.repositorios.RepositorioServico;

@Service
public class ServicoServico {

    @Autowired
    private RepositorioServico repositorio;

    @Autowired
    private RepositorioEmpresa repositorioEmpresa;

    public List<Servico> obterServicos() {
        return repositorio.findAll();
    }

    public Servico obterServico(long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Servico nao encontrado: " + id));
    }

    public Servico cadastrar(ServicoDto dto) {
        Empresa empresa = repositorioEmpresa.findById(dto.getEmpresaId())
                .orElseThrow(() -> new RuntimeException("Empresa nao encontrada: " + dto.getEmpresaId()));
        Servico servico = new Servico();
        servico.setNome(dto.getNome());
        servico.setValor(dto.getValor());
        servico.setDescricao(dto.getDescricao());
        repositorio.save(servico);
        empresa.getServicos().add(servico);
        repositorioEmpresa.save(empresa);
        return servico;
    }

    public Servico atualizar(long id, ServicoDto dto) {
        Servico servico = obterServico(id);
        if (dto.getNome() != null) {
            servico.setNome(dto.getNome());
        }
        if (dto.getValor() > 0) {
            servico.setValor(dto.getValor());
        }
        if (dto.getDescricao() != null) {
            servico.setDescricao(dto.getDescricao());
        }
        return repositorio.save(servico);
    }

    public void excluir(long id) {
        repositorio.delete(obterServico(id));
    }
}
