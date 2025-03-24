## Desafio – Meios de Pagamentos 💸

PDF do desafio recebido:
[DesafioMPCartoes.pdf](https://github.com/user-attachments/files/19431090/DesafioMPCartoes.pdf)


## Quem eu sou?
Olá, me chamo Kaique e trabalho com desenvolvimento de software a mais de 4 anos e atualmente 
sou engenheiro de software na FIEMG (Federação das Indústrias do Estado de Minas Gerais).
Trabalhei no desenvolvimento de soluções de pagamento, integrando sistemas às
APIs do Itaú para geração de chaves PIX e processamento de transações. Além disso, contribuí para a
criação de sistemas de gestão utilizados por grandes empresas, como Vale e Stellantis.

#### 🔗 Linkedin https://www.linkedin.com/in/kaique-mendes-9b61381a5/
#### 📞 Telefone 31.9.9332=8321

## Como rodar o projeto: 🚩

 ````sh 
 ./gradlew clean build
 ./gradlew run
````` 

## Requisitos técnicos: 🚩
- Criar APIs seguindo os padrões REST ✔
- Utilizar Swagger 3.0 para documentação ✔
- Usar Java 11 ou superior ✔ ( Java 21 )
- Micronaut ✔
- Banco de dados H2 ✔
- Criar testes unitários ✔

## Rotas 🚩

#### 
````
✔ Swagger Documentação
GET: http://localhost:8080/swagger-ui#

✔ Criar nova transação 
POST: http://localhost:8080/card/transaction

✔ Fechar fatura 
PATCH: http://localhost:8080/card/statement/{statementId}/account/{accountNumber}

✔ Listar trasações da fatura por mes 
GET: http://localhost:8080/card/transaction
````

## **Pontos importantes e dúvidas durante a leitura do desafio 🤔**

### 📌 **O que identifica uma compra à vista?**
Interpretei que **uma compra é à vista** quando **há apenas uma parcela**, e o valor dessa parcela é igual ao total da compra.

### 📅 **Como funciona o fechamento da fatura e a abertura para o mês seguinte?**
- Quando uma fatura é fechada, a **próxima fatura fica em aberto**, e todas as novas transações (compras) serão associadas a ela.
- A data de início da nova fatura **continua sendo o proximo mês**.
 
### **Cron JOB ⏳**
- Foi implementado um **cron job** para fechar as faturas no final de cada mês.

## **Estrutura do banco de dados 🗃️**
O banco de dados possui **5 tabelas principais**:
- - 1 **Card** → Representa o cartão, vinculado a um número de conta.
- - 2 **CardStatement** → Representa uma fatura do cartão.
- - 3 **CardTransaction** → Representa uma transação (compra).
- - 4 **CardTransactionInstallment** → Representa as parcelas das compras.
- - 5 **CardStatementTransaction** → Tabela de associação entre faturas e transações (compras).

![image](https://github.com/user-attachments/assets/ed38f809-4691-4a53-bda4-9f0a6cbb8292)
