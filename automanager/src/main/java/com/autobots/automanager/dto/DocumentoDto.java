package com.autobots.automanager.dto;

import java.util.Date;

import com.autobots.automanager.enumeracoes.TipoDocumento;

import lombok.Data;

@Data
public class DocumentoDto {

    private TipoDocumento tipo;
    private String numero;
    private Date dataEmissao;
    private Long usuarioId;
}
