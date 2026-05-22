package com.autobots.automanager.servicos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.dto.VeiculoDto;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.repositorios.RepositorioVeiculo;

@Service
public class VeiculoServico {

    @Autowired
    private RepositorioVeiculo repositorio;

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    public List<Veiculo> obterVeiculos() {
        return repositorio.findAll();
    }

    public Veiculo obterVeiculo(long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Veiculo nao encontrado: " + id));
    }

    public Veiculo cadastrar(VeiculoDto dto) {
        Usuario proprietario = repositorioUsuario.findById(dto.getProprietarioId())
                .orElseThrow(() -> new RuntimeException("Usuario nao encontrado: " + dto.getProprietarioId()));
        Veiculo veiculo = new Veiculo();
        veiculo.setTipo(dto.getTipo());
        veiculo.setModelo(dto.getModelo());
        veiculo.setPlaca(dto.getPlaca());
        veiculo.setProprietario(proprietario);
        repositorio.save(veiculo);
        proprietario.getVeiculos().add(veiculo);
        repositorioUsuario.save(proprietario);
        return veiculo;
    }

    public Veiculo atualizar(long id, VeiculoDto dto) {
        Veiculo veiculo = obterVeiculo(id);
        if (dto.getTipo() != null) {
            veiculo.setTipo(dto.getTipo());
        }
        if (dto.getModelo() != null) {
            veiculo.setModelo(dto.getModelo());
        }
        if (dto.getPlaca() != null) {
            veiculo.setPlaca(dto.getPlaca());
        }
        return repositorio.save(veiculo);
    }

    public void excluir(long id) {
        repositorio.delete(obterVeiculo(id));
    }
}
