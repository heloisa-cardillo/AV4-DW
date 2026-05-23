# AV4 - Desenvolvimento Web
API REST com Spring Boot, HATEOAS e autenticação/autorização via JWT.
## Requisitos
- Java 17
- Maven
- MySQL 8
## Configuração do banco
Crie um schema chamado `base` no MySQL. Configure sua senha no arquivo `src/main/resources/application.properties`
spring.datasource.password=root
## Como rodar
Acesse a pasta do projeto: cd automanager
Execute: .\mvnw spring-boot:run
O servidor sobe na porta 8080. O banco é populado automaticamente na primeira execução.
## Autenticação
Faça login para obter o token JWT via POST em http://localhost:8080/login com o body:
    {
        "nomeUsuario": "josericardo",
        "senha": "123456"
    }
O token JWT virá no header Authorization da resposta. Use-o nas demais requisições via Bearer Token.
## Perfis e credenciais
| Perfil     | Usuário     | Senha  |
|------------|-------------|--------|
| Vendedor   | josericardo | 123456 |
| Fornecedor | bosch       | 123456 |
| Cliente    | daniele     | 123456 |
| Cliente    | hanna       | 123456 |
| Cliente    | frida       | 123456 |
## Permissões por perfil
| Perfil   | Autorizações                                                                    |
|----------|---------------------------------------------------------------------------------|
| ADMIN    | CRUD total                                                                      |
| GERENTE  | CRUD de usuários, serviços, vendas e mercadorias                                |
| VENDEDOR | CRUD de clientes, ler mercadorias/serviços, criar vendas                        |
| CLIENTE  | Ver próprio cadastro em GET /usuario/meu e próprias vendas em GET /venda/minhas |
