package com.autobots.automanager.dto;

import java.util.Date;

import lombok.Data;

@Data
public class CredencialDto {

    private String nomeUsuario;
    private String senha;
    private Date criacao;
    private Date ultimoAcesso;
    private boolean inativo;
    private Long usuarioId;
}
