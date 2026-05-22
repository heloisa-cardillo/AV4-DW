package com.autobots.automanager.servicos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.dto.EmpresaDto;
import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.repositorios.RepositorioEmpresa;

@Service
public class EmpresaServico {

    @Autowired
    private RepositorioEmpresa repositorio;

    public List<Empresa> obterEmpresas() {
        return repositorio.findAll();
    }

    public Empresa obterEmpresa(long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Empresa nao encontrada: " + id));
    }

    public Empresa cadastrar(EmpresaDto dto) {
        Empresa empresa = new Empresa();
        empresa.setRazaoSocial(dto.getRazaoSocial());
        empresa.setNomeFantasia(dto.getNomeFantasia());
        empresa.setCadastro(dto.getCadastro());
        return repositorio.save(empresa);
    }

    public Empresa atualizar(long id, EmpresaDto dto) {
        Empresa empresa = obterEmpresa(id);
        if (dto.getRazaoSocial() != null) {
            empresa.setRazaoSocial(dto.getRazaoSocial());
        }
        if (dto.getNomeFantasia() != null) {
            empresa.setNomeFantasia(dto.getNomeFantasia());
        }
        if (dto.getCadastro() != null) {
            empresa.setCadastro(dto.getCadastro());
        }
        return repositorio.save(empresa);
    }

    public void excluir(long id) {
        repositorio.delete(obterEmpresa(id));
    }
}
