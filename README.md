# Análise estática com SonarQube

Este repositório reúne uma atividade de análise estática de código feita com o SonarQube Community Edition. Foram analisados dois projetos: o framework Flask, em Python, e uma API REST com Spring Boot, em Java.

Os relatórios registram a execução das análises, as métricas iniciais e finais, as correções realizadas e uma estratégia para reduzir as issues restantes.

## Projetos analisados

| Projeto | Tecnologia | Chave no SonarQube | Código-fonte |
|---|---|---|---|
| Minicurso Flask | Python | `br.edu.fatec:minicurso-flask` | [`projeto-1-flask`](./projeto-1-flask) |
| lgn-spring-api | Java e Spring Boot | `br.edu.fatec:sprint-boot-sample` | [`projeto-2-spring-rest-data-security`](./projeto-2-spring-rest-data-security) |

Os projetos foram incorporados ao repositório principal como diretórios comuns. Portanto, não é necessário inicializar submódulos após o clone.

## Relatórios

| Projeto | DOCX | HTML | PDF |
|---|---|---|---|
| Flask | [relatorio-flask.docx](./relatorio-flask.docx) | [relatorio-flask.html](./relatorio-flask.html) | [relatorio-flask.pdf](./relatorio-flask.pdf) |
| Java | [relatorio-java.docx](./relatorio-java.docx) | [relatorio-java.html](./relatorio-java.html) | [relatorio-java.pdf](./relatorio-java.pdf) |

Cada relatório contém três capturas do SonarQube: a visão Overall inicial, a visão New Code após as correções e a visão Overall final.

## Pré-requisitos

- Docker com Docker Compose
- Git
- Python 3 para o projeto Flask
- Java 21 e Maven para o projeto Spring Boot
- Um token de análise criado no SonarQube

## Iniciar o SonarQube

Na raiz do repositório, execute:

```bash
docker compose up -d
```

Confira o estado dos contêineres:

```bash
docker compose ps
```

Quando o serviço estiver pronto, abra [http://localhost:29000](http://localhost:29000). Os dados do SonarQube e do PostgreSQL ficam em volumes do Docker e permanecem disponíveis após a reinicialização dos contêineres.

Para encerrar os serviços sem apagar os dados:

```bash
docker compose down
```

## Analisar o projeto Flask

Entre no diretório do projeto e prepare o ambiente Python:

```bash
cd projeto-1-flask
python3 -m venv .venv
source .venv/bin/activate
python -m pip install -e ".[test]"
```

Execute os testes:

```bash
pytest
```

Defina o token somente na sessão atual do terminal e envie a análise:

```bash
export SONAR_TOKEN="seu_token"
pysonar \
  --sonar-host-url=http://localhost:29000 \
  --sonar-token="$SONAR_TOKEN" \
  --sonar-project-key=br.edu.fatec:minicurso-flask
```

As propriedades do projeto estão em [`projeto-1-flask/sonar-project.properties`](./projeto-1-flask/sonar-project.properties).

## Analisar o projeto Java

Entre no diretório do projeto e execute o build com os testes:

```bash
cd projeto-2-spring-rest-data-security
mvn clean verify -Ptest -Dserver.port=0 -Djacoco.skip=true
```

O relatório da atividade usa `jacoco.skip=true` porque o JaCoCo 0.8.8 configurado no projeto apresentou incompatibilidade com o Java 21 do ambiente analisado. Para obter cobertura, atualize o JaCoCo para uma versão compatível antes de remover essa opção.

Envie a análise ao SonarQube:

```bash
export SONAR_TOKEN="seu_token"
mvn sonar:sonar \
  -Dsonar.host.url=http://localhost:29000 \
  -Dsonar.projectKey=br.edu.fatec:sprint-boot-sample \
  -Dsonar.token="$SONAR_TOKEN"
```

As propriedades usadas na análise estão em [`projeto-2-spring-rest-data-security/.sonarcloud.properties`](./projeto-2-spring-rest-data-security/.sonarcloud.properties).

## Estrutura do repositório

```text
.
├── docker-compose.yml
├── projeto-1-flask/
├── projeto-2-spring-rest-data-security/
├── relatorio-flask.docx
├── relatorio-flask.html
├── relatorio-flask.pdf
├── relatorio-java.docx
├── relatorio-java.html
└── relatorio-java.pdf
```

Arquivos locais com tokens, caches, ambientes virtuais, builds e dados do scanner são excluídos pelo [`.gitignore`](./.gitignore).
