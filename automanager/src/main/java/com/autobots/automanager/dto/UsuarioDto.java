package com.autobots.automanager.dto;

import java.util.Set;

import com.autobots.automanager.enumeracoes.PerfilUsuario;

import lombok.Data;

@Data
public class UsuarioDto {

    private String nome;
    private String nomeSocial;
    private Set<PerfilUsuario> perfis;
    private Long empresaId;
}
