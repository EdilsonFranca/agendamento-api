# Agendamento de Transferências Financeiras

<section>
  <h2>Estrutura do projeto</h2>

  <pre><code>agendamento-api/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── br/
│   │   │       └── com/
│   │   │           └── agendamentoapi/
│   │   │               ├── AgendamentoApiApplication.java
│   │   │               ├── config/
│   │   │               │   └── ModelMapperConfig.java
│   │   │               ├── controller/
│   │   │               │   └── TransferenciasController.java
│   │   │               ├── dto/
│   │   │               │   ├── ErroResponse.java
│   │   │               │   └── TransferenciaDto.java
│   │   │               ├── exception/
│   │   │               │   └── GlobalExceptionHandler.java
│   │   │               ├── model/
│   │   │               │   └── Transferencia.java
│   │   │               ├── repository/
│   │   │               │   └── TransferenciaRepository.java
│   │   │               ├── service/
│   │   │               │   └── TransferenciaService.java
│   │   │               └── util/
│   │   │                   └── CalculadoraTaxa.java
│   │   └── resources/
│   │       ├── application.yaml
│   │       ├── static/
│   │       └── templates/
│   └── test/
│       └── java/
│           └── br/
│               └── com/
│                   └── agendamentoapi/
│                       ├── AgendamentoApiApplicationTests.java
│                       ├── controller/
│                       │   └── TransferenciasControllerTest.java
│                       ├── service/
│                       │   └── TransferenciaServiceTest.java
│                       └── util/
│                           └── CalculadoraTaxaTest.java
├── pom.xml
└── README.md</code></pre>
</section>


### Justificativa da escolha arquitetural

A arquitetura em camadas foi escolhida por sua simplicidade e rapidez de implementação, considerando que o projeto possui um escopo limitado e não prevê um crescimento significativo de complexidade.

Essa abordagem permite separar as responsabilidades entre Controller, Service e Repository, mantendo o código organizado, testável e fácil de manter, sem adicionar abstrações desnecessárias.

A decisão prioriza a simplicidade e a produtividade, utilizando uma arquitetura adequada ao tamanho e aos objetivos do projeto.


API REST para agendamento de transferências bancárias, desenvolvida com Java e Spring Boot. O sistema permite cadastrar transferências, calcular automaticamente as taxas conforme a antecedência da operação e consultar as transferências agendadas.

## Tecnologias utilizadas

- Java
- Spring Boot
- Spring Web
- Spring Data JPA
- Bean Validation
- H2 Database
- Lombok
- ModelMapper
- Swagger / OpenAPI
- Maven
- SonarQube

## Funcionalidades

- Agendamento de transferências bancárias.
- Validação dos dados recebidos pela API.
- Cálculo automático de taxas conforme a data da transferência.
- Cálculo do valor total da operação.
- Persistência dos dados utilizando Spring Data JPA.
- Consulta das transferências cadastradas, ordenadas pela data de agendamento.
- Tratamento centralizado de exceções.
- Documentação dos endpoints com Swagger.

## Regras de negócio

A taxa é calculada de acordo com a quantidade de dias entre o agendamento e a data da transferência.

| Antecedência | Taxa aplicada |
|---|---|
| 0 a 10 dias | 2,5% do valor + R$ 12,00 |
| 11 a 20 dias | Sem taxa |
| 21 a 30 dias | 8,2% do valor |
| 31 a 40 dias | 6,9% do valor |
| 41 a 50 dias | 4,7% do valor |
| Negativa ou superior a 50 dias | Agendamento não permitido |

O valor total da transferência é calculado somando o valor informado à taxa correspondente. Os valores monetários são arredondados para duas casas decimais.

## Endpoints

Base URL: `http://localhost:8080`

### 1. Agendar transferência

**POST** `/api/v1/transferencias`

Exemplo de requisição:

```json
{
  "contaOrigem": "1234567890",
  "contaDestino": "0987654321",
  "valor": 1000.00,
  "dataTransferencia": "2026-10-20"
}
```

O exemplo considera os nomes usuais dos campos do DTO; ajuste-os caso sejam diferentes na implementação.

Resposta esperada: `201 Created`, quando o agendamento é realizado com sucesso.

### 2. Listar transferências

**GET** `/api/v1/transferencias`

Retorna as transferências cadastradas, ordenadas pela data de agendamento, da mais recente para a mais antiga.

Resposta esperada: `200 OK`.

### Tratamento de erros

A API utiliza validações de entrada e tratamento centralizado de exceções para retornar respostas adequadas em situações como dados inválidos ou datas não permitidas.

## Pré-requisitos

- JDK 11.
- Maven.
- Git.

## Como executar o projeto

Clone o repositório:

```bash
git clone https://github.com/EdilsonFranca/agendamento-api.git
```

Entre na pasta do projeto:

```bash
cd agendamento-api
```

Compile e execute os testes:

```bash
mvn clean verify
```

Inicie a aplicação:

```bash
mvn spring-boot:run
```

Por padrão, a API pode ser acessada em:

`http://localhost:8080`

## Documentação Swagger

Com a aplicação em execução, acesse:

`http://localhost:8080/swagger-ui.html`

A interface permite visualizar os endpoints e testar as requisições diretamente pelo navegador.

## Testes

Execute os testes automatizados com:

```bash
mvn test
```

Para executar os testes e gerar a compilação completa:

```bash
mvn clean verify
```

## Análise de qualidade com SonarQube

Com o SonarQube local em execução na porta 9000, gere um token de acesso na interface web e execute:

```bash
mvn clean verify org.sonarsource.scanner.maven:sonar-maven-plugin:5.2.0.4988:sonar \
  -Dsonar.projectKey=agendamento-api \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.token=SEU_TOKEN
```

Acesse o SonarQube:

`http://localhost:9000`

Substitua `SEU_TOKEN` pelo token gerado na sua instalação. Evite salvar tokens diretamente no repositório.

## Estrutura do projeto

A organização utiliza camadas para separar responsabilidades:

- `controller`: endpoints e comunicação HTTP.
- `service`: regras de negócio e processamento das transferências.
- `repository`: acesso e persistência dos dados.
- `model`: entidades do domínio.
- `dto`: objetos de transferência de dados.
- `exception`: tratamento de exceções.
- `util`: utilitários, incluindo a calculadora de taxas.

Os nomes e pacotes acima representam a organização prevista para o projeto e devem ser conferidos com a estrutura atual do código.
