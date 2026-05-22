package com.autobots.automanager.servicos;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.dto.VendaDto;
import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.entitades.Servico;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import com.autobots.automanager.repositorios.RepositorioMercadoria;
import com.autobots.automanager.repositorios.RepositorioServico;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.repositorios.RepositorioVeiculo;
import com.autobots.automanager.repositorios.RepositorioVenda;

@Service
public class VendaServico {

    @Autowired
    private RepositorioVenda repositorio;
    @Autowired
    private RepositorioUsuario repositorioUsuario;
    @Autowired
    private RepositorioVeiculo repositorioVeiculo;
    @Autowired
    private RepositorioMercadoria repositorioMercadoria;
    @Autowired
    private RepositorioServico repositorioServico;
    @Autowired
    private RepositorioEmpresa repositorioEmpresa;

    public List<Venda> obterVendas() {
        return repositorio.findAll();
    }

    public Venda obterVenda(long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Venda nao encontrada: " + id));
    }

    public Venda cadastrar(VendaDto dto) {
        Usuario cliente = repositorioUsuario.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente nao encontrado: " + dto.getClienteId()));
        Usuario funcionario = repositorioUsuario.findById(dto.getFuncionarioId())
                .orElseThrow(() -> new RuntimeException("Funcionario nao encontrado: " + dto.getFuncionarioId()));
        Veiculo veiculo = repositorioVeiculo.findById(dto.getVeiculoId())
                .orElseThrow(() -> new RuntimeException("Veiculo nao encontrado: " + dto.getVeiculoId()));
        Empresa empresa = repositorioEmpresa.findById(dto.getEmpresaId())
                .orElseThrow(() -> new RuntimeException("Empresa nao encontrada: " + dto.getEmpresaId()));
        Set<Mercadoria> mercadorias = new HashSet<>(repositorioMercadoria.findAllById(dto.getMercadoriaIds()));
        Set<Servico> servicos = new HashSet<>(repositorioServico.findAllById(dto.getServicoIds()));
        Venda venda = new Venda();
        venda.setCadastro(dto.getCadastro());
        venda.setIdentificacao(dto.getIdentificacao());
        venda.setCliente(cliente);
        venda.setFuncionario(funcionario);
        venda.setVeiculo(veiculo);
        venda.setMercadorias(mercadorias);
        venda.setServicos(servicos);
        repositorio.save(venda);
        empresa.getVendas().add(venda);
        repositorioEmpresa.save(empresa);
        cliente.getVendas().add(venda);
        repositorioUsuario.save(cliente);
        veiculo.getVendas().add(venda);
        repositorioVeiculo.save(veiculo);
        return venda;
    }

    public Venda atualizar(long id, VendaDto dto) {
        Venda venda = obterVenda(id);
        if (dto.getIdentificacao() != null) {
            venda.setIdentificacao(dto.getIdentificacao());
        }
        if (dto.getCadastro() != null) {
            venda.setCadastro(dto.getCadastro());
        }
        if (dto.getClienteId() != null) {
            Usuario cliente = repositorioUsuario.findById(dto.getClienteId())
                    .orElseThrow(() -> new RuntimeException("Cliente nao encontrado: " + dto.getClienteId()));
            venda.setCliente(cliente);
        }
        if (dto.getFuncionarioId() != null) {
            Usuario funcionario = repositorioUsuario.findById(dto.getFuncionarioId())
                    .orElseThrow(() -> new RuntimeException("Funcionario nao encontrado: " + dto.getFuncionarioId()));
            venda.setFuncionario(funcionario);
        }
        if (dto.getMercadoriaIds() != null) {
            venda.setMercadorias(new HashSet<>(repositorioMercadoria.findAllById(dto.getMercadoriaIds())));
        }
        if (dto.getServicoIds() != null) {
            venda.setServicos(new HashSet<>(repositorioServico.findAllById(dto.getServicoIds())));
        }
        return repositorio.save(venda);
    }

    public void excluir(long id) {
        repositorio.delete(obterVenda(id));
    }
}
