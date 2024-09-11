<p align="center"> 💻 Atualizado em 11 de Setembro de 2024 💻</p>

<h1 align="center"> 📟 Desafio API-Votação 📟</h1>

<p align="center">
  <img alt="GitHub language count" src="https://img.shields.io/github/languages/count/gabrielcoelhox/api-votacao">

  <img alt="Repository size" src="https://img.shields.io/github/repo-size/gabrielcoelhox/api-votacao">

  <a href="https://github.com/gabrielcoelhox/course-angular-java/commits/main">
    <img alt="GitHub last commit" src="https://img.shields.io/github/last-commit/gabrielcoelhox/api-votacao">
  </a>
</p>

[O Projeto](#id1)&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
[Como executar o projeto](#id2)&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
[Regra de negócio](#id3)&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
[Recursos/End-points](#id4)&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
[Demonstração](#id5)&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;

## <a id="id1"> 💻 O Projeto </a>

Este projeto foi desenvolvido como desafio de avaliação para a vaga de desenvolvedor da empresa __*Lifters*__. A proposta do projeto é implementar uma pequena API, que permita cadastro básico dos candidatos, eleitores, dos votos e relatório para identificar a quantidade de votos para cada candidato e o vencedor.

## <a id="id2">:hammer_and_wrench: Como executar o projeto </a> 

### Pré-requisitos

Antes de começar, você vai precisar ter instalado em sua máquina as seguintes ferramentas:
- [Git](https://git-scm.com);
- [Java JDK - __*VERSÃO 17+*__][Java JDK];
- IDE de sua escolha;

<details>
<summary><strong>Rodando o projeto</strong></summary>

```bash
# Clone este repositório
$ git clone https://github.com/gabrielcoelhox/api-votacao.git
# Abra o projeto na IDE de sua escolha
$ Inicie o projeto utilizando o comando mvn spring-boot:run
# Aguarde até o projeto ser completemante buildado
$ Acesse o site do Swagger http://localhost:8080/swagger-ui
```
</details>

## <a id="id3"> 📝 Regra de negócio </a>

1. Validar para permitir apenas um voto por eleitor;
2. Validar para não permitir candidatos e eleitores duplicados;
3. Caso algum candidato ou eleitor já possua um voto, este não poderá ser apagado, ou seja, só permitir apagar caso ele ainda não tenha nenhum voto computado;
4. Só será possível realizar a votação quando a sessão estiver aberta e consequentemente não poderá ser encerrada caso não esteja aberta;
5. Uma sessão pode ser iniciada e encerrada sem um tempo entre elas, contanto que seja respeitada a regra no4;
6. A sessão só poderá ser encerrada caso haja 0 votos ou pelo menos 2 votos, pois garante a anonimidade dos votantes. Entretanto, caso apenas um eleitor tenha votado e seja necessário encerrar a sessão, o voto desse eleitor deverá ser desconsiderado na contabilização e consequentemente não haverá candidato vencedor;
7. Deverá ser possível gerar um Boletim de Urna, que é um relatório com o resultado de cada sessão;
8. Caso durante a geração do boletim de urna haja apenas um votante, a regra num 6 deverá ser mantida e não haverá contabilização ou vencedor;
9. O relatório poderá ser gerado apenas de sessões encerradas, portanto é necessário chamar o endpoint de encerrar sessão para que o relatório esteja disponível para geração;
10. O boletim de urna tem um layout específico de 40 colunas por linha. Um exemplo pode ser visto no retorno do endpoint GET /boletim-urna/{idSessao}.

## <a id="id4"> 📍 Recursos/End-points </a>

- CRUD para /candidatos
- CRUD para /eleitores
- CRUD para /cargos
- POST /abrir-sessao
- PATCH /fechar-sessao
- POST /eleitores/{id}/votar
- GET /boletim-urna/{idSessao}

## <a id="id5"> 🖼️ Demonstração </a>



[Java JDK]: https://www.oracle.com/br/java/technologies/downloads/