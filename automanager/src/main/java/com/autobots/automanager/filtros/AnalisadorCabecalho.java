package com.autobots.automanager.filtros;

class AnalisadorCabecalho {

    private String cabecalho;

    public AnalisadorCabecalho(String cabecalho) {
        this.cabecalho = cabecalho;
    }

    public String obterJwt() {
        String[] partes = cabecalho.split(" ");
        return partes[1];
    }
}
