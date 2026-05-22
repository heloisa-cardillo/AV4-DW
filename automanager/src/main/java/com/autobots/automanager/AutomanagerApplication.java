package com.autobots.automanager;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.autobots.automanager.entitades.CredencialUsuarioSenha;
import com.autobots.automanager.entitades.Documento;
import com.autobots.automanager.entitades.Email;
import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.entitades.Endereco;
import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.entitades.Servico;
import com.autobots.automanager.entitades.Telefone;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.enumeracoes.PerfilUsuario;
import com.autobots.automanager.enumeracoes.TipoDocumento;
import com.autobots.automanager.enumeracoes.TipoVeiculo;
import com.autobots.automanager.repositorios.RepositorioEmpresa;

@SpringBootApplication
public class AutomanagerApplication implements CommandLineRunner {

    @Autowired
    private RepositorioEmpresa repositorioEmpresa;

    public static void main(String[] args) {
        SpringApplication.run(AutomanagerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (repositorioEmpresa.count() > 0) {
            return;
        }

        BCryptPasswordEncoder codificador = new BCryptPasswordEncoder();

        Empresa empresa = new Empresa();
        empresa.setRazaoSocial("Megazoo Ltda");
        empresa.setNomeFantasia("Megazoo Racao Coelho");
        empresa.setCadastro(new Date());

        Endereco enderecoEmpresa = new Endereco();
        enderecoEmpresa.setEstado("SP");
        enderecoEmpresa.setCidade("Sao Jose dos Campos");
        enderecoEmpresa.setBairro("Centro");
        enderecoEmpresa.setRua("Av. Dr. Nelson D Avila");
        enderecoEmpresa.setNumero("42");
        enderecoEmpresa.setCodigoPostal("12245-070");
        empresa.setEndereco(enderecoEmpresa);

        Telefone telefoneEmpresa = new Telefone();
        telefoneEmpresa.setDdd("12");
        telefoneEmpresa.setNumero("39121000");
        empresa.getTelefones().add(telefoneEmpresa);

        Usuario funcionario = new Usuario();
        funcionario.setNome("Jose Ricardo");
        funcionario.setNomeSocial("O Coelho Ousado");
        funcionario.getPerfis().add(PerfilUsuario.ROLE_VENDEDOR);

        Email emailFuncionario = new Email();
        emailFuncionario.setEndereco("jose@megazoo.com");
        funcionario.getEmails().add(emailFuncionario);

        Endereco enderecoFuncionario = new Endereco();
        enderecoFuncionario.setEstado("SP");
        enderecoFuncionario.setCidade("Sao Jose dos Campos");
        enderecoFuncionario.setBairro("Sao Dimas");
        enderecoFuncionario.setRua("Av. Engenheiro Jose Longo");
        enderecoFuncionario.setNumero("10");
        enderecoFuncionario.setCodigoPostal("12245-000");
        funcionario.setEndereco(enderecoFuncionario);

        Telefone telefoneFuncionario = new Telefone();
        telefoneFuncionario.setDdd("12");
        telefoneFuncionario.setNumero("991111111");
        funcionario.getTelefones().add(telefoneFuncionario);

        Documento cpfFuncionario = new Documento();
        cpfFuncionario.setDataEmissao(new Date());
        cpfFuncionario.setNumero("11111111111");
        cpfFuncionario.setTipo(TipoDocumento.CPF);
        funcionario.getDocumentos().add(cpfFuncionario);

        CredencialUsuarioSenha credencialFuncionario = new CredencialUsuarioSenha();
        credencialFuncionario.setInativo(false);
        credencialFuncionario.setNomeUsuario("josecoelho");
        credencialFuncionario.setSenha(codificador.encode("123456"));
        credencialFuncionario.setCriacao(new Date());
        credencialFuncionario.setUltimoAcesso(new Date());
        funcionario.getCredenciais().add(credencialFuncionario);

        empresa.getUsuarios().add(funcionario);

        Usuario fornecedor = new Usuario();
        fornecedor.setNome("Cobasi Comercio de Animais Ltda");
        fornecedor.setNomeSocial("Cobasi");
        fornecedor.getPerfis().add(PerfilUsuario.ROLE_FORNECEDOR);

        Email emailFornecedor = new Email();
        emailFornecedor.setEndereco("fornecedor@cobasi.com");
        fornecedor.getEmails().add(emailFornecedor);

        CredencialUsuarioSenha credencialFornecedor = new CredencialUsuarioSenha();
        credencialFornecedor.setInativo(false);
        credencialFornecedor.setNomeUsuario("cobasi");
        credencialFornecedor.setSenha(codificador.encode("123456"));
        credencialFornecedor.setCriacao(new Date());
        credencialFornecedor.setUltimoAcesso(new Date());
        fornecedor.getCredenciais().add(credencialFornecedor);

        Documento cnpjFornecedor = new Documento();
        cnpjFornecedor.setDataEmissao(new Date());
        cnpjFornecedor.setNumero("22222222000100");
        cnpjFornecedor.setTipo(TipoDocumento.CNPJ);
        fornecedor.getDocumentos().add(cnpjFornecedor);

        Endereco enderecoFornecedor = new Endereco();
        enderecoFornecedor.setEstado("SP");
        enderecoFornecedor.setCidade("Sao Jose dos Campos");
        enderecoFornecedor.setBairro("Jardim Aquarius");
        enderecoFornecedor.setRua("Av. Cassiopeia");
        enderecoFornecedor.setNumero("100");
        enderecoFornecedor.setCodigoPostal("12246-000");
        fornecedor.setEndereco(enderecoFornecedor);

        empresa.getUsuarios().add(fornecedor);

        Mercadoria feno = new Mercadoria();
        feno.setCadastro(new Date());
        feno.setFabricao(new Date());
        feno.setNome("Feno Timothy Premium");
        feno.setValidade(new Date());
        feno.setQuantidade(100);
        feno.setValor(45.90);
        feno.setDescricao("Feno de alta qualidade para coelhos");
        empresa.getMercadorias().add(feno);
        fornecedor.getMercadorias().add(feno);

        Usuario daniele = new Usuario();
        daniele.setNome("Daniele");
        daniele.setNomeSocial("A Coelha Carinhosa");
        daniele.getPerfis().add(PerfilUsuario.ROLE_CLIENTE);

        Email emailDaniele = new Email();
        emailDaniele.setEndereco("daniele@coelhos.com");
        daniele.getEmails().add(emailDaniele);

        Documento cpfDaniele = new Documento();
        cpfDaniele.setDataEmissao(new Date());
        cpfDaniele.setNumero("33333333333");
        cpfDaniele.setTipo(TipoDocumento.CPF);
        daniele.getDocumentos().add(cpfDaniele);

        CredencialUsuarioSenha credencialDaniele = new CredencialUsuarioSenha();
        credencialDaniele.setInativo(false);
        credencialDaniele.setNomeUsuario("danielecoelha");
        credencialDaniele.setSenha(codificador.encode("123456"));
        credencialDaniele.setCriacao(new Date());
        credencialDaniele.setUltimoAcesso(new Date());
        daniele.getCredenciais().add(credencialDaniele);

        Endereco enderecoDANIELE = new Endereco();
        enderecoDANIELE.setEstado("SP");
        enderecoDANIELE.setCidade("Sao Jose dos Campos");
        enderecoDANIELE.setBairro("Jardim das Industrias");
        enderecoDANIELE.setRua("Rua Monsenhor Joao Batista");
        enderecoDANIELE.setNumero("1");
        enderecoDANIELE.setCodigoPostal("12235-380");
        daniele.setEndereco(enderecoDANIELE);

        Veiculo veiculoDaniele = new Veiculo();
        veiculoDaniele.setPlaca("COE-1111");
        veiculoDaniele.setModelo("Fusca do Coelho");
        veiculoDaniele.setTipo(TipoVeiculo.HATCH);
        veiculoDaniele.setProprietario(daniele);
        daniele.getVeiculos().add(veiculoDaniele);

        empresa.getUsuarios().add(daniele);

        Usuario hanna = new Usuario();
        hanna.setNome("Hanna");
        hanna.setNomeSocial("A Coelha Cautelosa");
        hanna.getPerfis().add(PerfilUsuario.ROLE_CLIENTE);

        Email emailHanna = new Email();
        emailHanna.setEndereco("hanna@coelhos.com");
        hanna.getEmails().add(emailHanna);

        Documento cpfHanna = new Documento();
        cpfHanna.setDataEmissao(new Date());
        cpfHanna.setNumero("44444444444");
        cpfHanna.setTipo(TipoDocumento.CPF);
        hanna.getDocumentos().add(cpfHanna);

        CredencialUsuarioSenha credencialHanna = new CredencialUsuarioSenha();
        credencialHanna.setInativo(false);
        credencialHanna.setNomeUsuario("hannacoelha");
        credencialHanna.setSenha(codificador.encode("123456"));
        credencialHanna.setCriacao(new Date());
        credencialHanna.setUltimoAcesso(new Date());
        hanna.getCredenciais().add(credencialHanna);

        Endereco enderecoHanna = new Endereco();
        enderecoHanna.setEstado("SP");
        enderecoHanna.setCidade("Sao Jose dos Campos");
        enderecoHanna.setBairro("Urbanova");
        enderecoHanna.setRua("Av. Cedral");
        enderecoHanna.setNumero("2");
        enderecoHanna.setCodigoPostal("12223-000");
        hanna.setEndereco(enderecoHanna);

        Veiculo veiculoHanna = new Veiculo();
        veiculoHanna.setPlaca("COE-2222");
        veiculoHanna.setModelo("Kombi da Coelha");
        veiculoHanna.setTipo(TipoVeiculo.SW);
        veiculoHanna.setProprietario(hanna);
        hanna.getVeiculos().add(veiculoHanna);

        empresa.getUsuarios().add(hanna);

        Usuario frida = new Usuario();
        frida.setNome("Frida");
        frida.setNomeSocial("A Coelha Faminta");
        frida.getPerfis().add(PerfilUsuario.ROLE_CLIENTE);

        Email emailFrida = new Email();
        emailFrida.setEndereco("frida@coelhos.com");
        frida.getEmails().add(emailFrida);

        Documento cpfFrida = new Documento();
        cpfFrida.setDataEmissao(new Date());
        cpfFrida.setNumero("55555555555");
        cpfFrida.setTipo(TipoDocumento.CPF);
        frida.getDocumentos().add(cpfFrida);

        CredencialUsuarioSenha credencialFrida = new CredencialUsuarioSenha();
        credencialFrida.setInativo(false);
        credencialFrida.setNomeUsuario("fridacoelha");
        credencialFrida.setSenha(codificador.encode("123456"));
        credencialFrida.setCriacao(new Date());
        credencialFrida.setUltimoAcesso(new Date());
        frida.getCredenciais().add(credencialFrida);

        Endereco enderecoFrida = new Endereco();
        enderecoFrida.setEstado("SP");
        enderecoFrida.setCidade("Sao Jose dos Campos");
        enderecoFrida.setBairro("Vila Adyana");
        enderecoFrida.setRua("Rua Benedito dos Santos");
        enderecoFrida.setNumero("3");
        enderecoFrida.setCodigoPostal("12243-000");
        frida.setEndereco(enderecoFrida);

        Veiculo veiculoFrida = new Veiculo();
        veiculoFrida.setPlaca("COE-3333");
        veiculoFrida.setModelo("Chevette da Coelha");
        veiculoFrida.setTipo(TipoVeiculo.SEDA);
        veiculoFrida.setProprietario(frida);
        frida.getVeiculos().add(veiculoFrida);

        empresa.getUsuarios().add(frida);

        Servico banhoTosa = new Servico();
        banhoTosa.setDescricao("Banho e tosa para coelhos");
        banhoTosa.setNome("Banho e Tosa");
        banhoTosa.setValor(80.0);

        Servico consulta = new Servico();
        consulta.setDescricao("Consulta veterinaria para coelhos");
        consulta.setNome("Consulta Veterinaria");
        consulta.setValor(150.0);

        empresa.getServicos().add(banhoTosa);
        empresa.getServicos().add(consulta);

        Venda venda = new Venda();
        venda.setCadastro(new Date());
        venda.setCliente(daniele);
        venda.getMercadorias().add(feno);
        venda.setIdentificacao("VENDA-0001");
        venda.setFuncionario(funcionario);
        venda.getServicos().add(banhoTosa);
        venda.setVeiculo(veiculoDaniele);
        veiculoDaniele.getVendas().add(venda);
        empresa.getVendas().add(venda);

        repositorioEmpresa.save(empresa);
    }
}
