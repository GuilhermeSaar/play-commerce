# 🎮 Plataforma de Venda e Gerenciamento de Jogos Digitais

> **Status do Projeto:** 🚧 *Em desenvolvimento (atualmente no RF05)*

---

## 🧾 Descrição Geral

O sistema proposto é uma plataforma de **venda e gerenciamento de jogos digitais**, desenvolvida em **Java com Spring Boot**.  
O objetivo é oferecer aos clientes uma experiência completa de **compra, download e gerenciamento** de seus jogos adquiridos.

A aplicação permitirá:
- Cadastro e autenticação de clientes;
- Registro e controle dos jogos disponíveis;
- Processamento de compras e pagamentos;
- Criação de uma biblioteca digital pessoal com downloads ilimitados.

O sistema também gerenciará informações detalhadas sobre cada jogo, incluindo:
- Nome
- Descrição
- Desenvolvedor
- Publicadora
- Data de lançamento
- Preço
- Categoria
- Classificação indicativa
- Link de download

As transações de compra poderão conter múltiplos jogos e estarão vinculadas a um único pagamento.

---

## 📋 Requisitos Funcionais

| Código | Descrição | Status |
|--------|------------|--------|
| **RF01** | O sistema deve permitir o cadastro de clientes | ✅ Concluído |
| **RF02** | O sistema deve permitir a atualização dos dados cadastrais do cliente | ✅ Concluído |
| **RF03** | O sistema deve permitir a autenticação de clientes (login) para acesso à conta | ✅ Concluído |
| **RF04** | O sistema deve armazenar informações sobre os jogos disponíveis para venda | ✅ Concluído |
| **RF05** | O sistema deve permitir a inclusão, atualização e exclusão de jogos da plataforma | ✅ Concluído |
| **RF06** | Cada compra deve estar associada a um único cliente | ✅ Concluído |
| **RF07** | O sistema deve permitir que um cliente realize uma compra de um ou mais jogos disponíveis | ✅ Concluído |
| **RF08** | O sistema deve permitir o registro de um pagamento para cada compra | ✅ Concluído |
| **RF09** | Cada compra deve possuir apenas um pagamento associado | ✅ Concluído |
| **RF10** | O sistema deve armazenar informações sobre o pagamento (forma, status e data) | ✅ Concluído |
| **RF11** | O sistema deve vincular o pagamento à compra e ao cliente correspondente | 🟦 Não iniciado |
| **RF12** | O sistema deve garantir que todas as compras e pagamentos estejam devidamente registrados | 🟦 Não iniciado |
| **RF13** | O sistema deve manter uma biblioteca de jogos vinculada a cada cliente, contendo todos os jogos adquiridos | 🟦 Não iniciado |
| **RF14** | O sistema deve permitir que o cliente baixe qualquer jogo da sua biblioteca quantas vezes quiser | 🟦 Não iniciado |
| **RF15** | O sistema deve permitir que um jogo esteja presente na biblioteca de vários clientes | 🟦 Não iniciado |
| **RF16** | O sistema deve permitir consultar o histórico de compras de um cliente | 🟦 Não iniciado |
| **RF17** | O sistema deve permitir visualizar os detalhes de cada compra, incluindo jogos e status | 🟦 Não iniciado |
| **RF18** | O sistema deve permitir consultar a biblioteca de jogos de um cliente autenticado | 🟦 Não iniciado |


---

## 🏗️ Tecnologias Utilizadas

- ☕ **Java 17**
- 🌱 **Spring Boot 3**
- 🧩 **Spring Data JPA**
- 🧾 **PostgreSQL**
- 🔐 **Spring Security + JWT**
- 🧰 **Maven**
- 🧪 **JUnit / Mockito** *(planejado)*

---

## 🗂️ Modelagem de Dados

A modelagem do banco de dados foi criada com base nos requisitos funcionais.  
Ela representa as principais entidades do sistema.

![Modelagem de Dados](docs/modelagem.jpg)

---

## ⚙️ Como Executar o Projeto

1. **Clonar o repositório**
   ```bash
   git clone https://github.com/GuilhermeSaar/nome-do-projeto.git
