## 💳 Payment API

Depois de desenvolver a **Transaction API**, quis criar um projeto para continuar praticando conceitos de backend além de um CRUD tradicional.

Enquanto a Transaction API foi importante para consolidar conhecimentos como **DTOs, validações, tratamento de exceções, testes unitários e documentação com Swagger**, nesta Payment API incluí **regras de negócio mais completas, diferentes estados para um mesmo recurso e geração de QR Code**.

A ideia foi simular um fluxo de pagamentos com criação, pagamento, cancelamento e expiração de cobranças, além do tratamento dos diferentes cenários de erro.

Neste projeto, além das operações CRUD, implementei:

* Controle de status com `PENDING`, `PAID`, `CANCELED` e `EXPIRED`
* Regras para pagamento, cancelamento e expiração
* Controle de tempo utilizando `LocalDateTime`
* Geração de QR Code para cada nova cobrança
* Exceções customizadas e tratamento global de erros
* Refatoração para reduzir repetição de código
* Testes unitários para diferentes cenários e regras de negócio

A ideia é simular o fluxo de uma cobrança, desde sua criação até o pagamento, cancelamento ou expiração, sem realizar nenhuma transação financeira real.


## 🚀 Funcionalidades

A API permite:

* Criar pagamentos
* Listar pagamentos
* Buscar pagamento por ID
* Atualizar pagamentos pendentes
* Excluir pagamentos
* Simular o pagamento de uma cobrança
* Cancelar pagamentos
* Controlar expiração de pagamentos
* Gerar QR Code para novos pagamentos

Os pagamentos podem possuir os seguintes status:

```text
PENDING
PAID
CANCELED
EXPIRED
```

---

## 📌 Regras de negócio

Ao criar um pagamento:

* O status inicial é `PENDING`
* A data de criação é registrada automaticamente
* O pagamento expira após 15 minutos
* Um QR Code é gerado com informações da cobrança

Um pagamento:

* Não pode ser pago duas vezes
* Não pode ser pago se estiver cancelado
* Não pode ser pago se estiver expirado
* Só pode ser atualizado enquanto estiver `PENDING`
* Não pode ser cancelado depois de pago
* Não pode ser cancelado depois de expirado

Caso o tempo de expiração tenha passado, a API atualiza o status para `EXPIRED` ao tentar realizar uma operação sobre o pagamento.

---

## 📱 QR Code

Ao criar um pagamento, a API gera um QR Code contendo informações como:

```text
paymentId
amount
description
createdAt
expiresAt
```

O QR Code é gerado utilizando **ZXing**, convertido para PNG e retornado pela API como uma String em **Base64**.

---

## 🛠 Tecnologias

* Java 21
* Spring Boot
* Spring Web
* Spring Data JPA
* PostgreSQL
* Docker
* Bean Validation
* ZXing
* JUnit 5
* Mockito
* Springdoc OpenAPI / Swagger
* Maven
* Git & GitHub

---

## 📂 Estrutura do projeto

```text
src/main/java/com/soares/payment_api

├── controller
├── dto
├── entity
├── enums
├── exception
├── repository
└── service
```

A aplicação utiliza DTOs para separar os dados recebidos e retornados pela API das entidades persistidas no banco.

---
## 🌐 Endpoints

```text
POST   /payments
GET    /payments
GET    /payments/{id}
PUT    /payments/{id}
DELETE /payments/{id}
POST   /payments/{id}/pay
POST   /payments/{id}/cancel
```

A documentação completa e os testes dos endpoints estão disponíveis pelo Swagger UI.


---

## ⚠️ Tratamento de exceções

A API utiliza exceções customizadas para representar diferentes situações de negócio:

* `PaymentNotFoundException`
* `PaymentAlreadyPaidException`
* `PaymentCanceledException`
* `PaymentExpiredException`
* `PaymentUpdateNotAllowedException`
* `QrCodeGenerationException`

O `GlobalExceptionHandler` centraliza o tratamento dessas exceções e as transforma em respostas HTTP adequadas.

Exemplos:

```text
404 NOT FOUND
→ pagamento não encontrado

400 BAD REQUEST
→ operação não permitida para o estado atual do pagamento

500 INTERNAL SERVER ERROR
→ erro interno na geração do QR Code
```

---

## 🧪 Testes

O projeto possui testes unitários para o `PaymentService` utilizando **JUnit 5 e Mockito**.

Entre os cenários testados estão:

* Criação de pagamento
* Pagamento de cobrança pendente
* Tentativa de pagar cobrança já paga
* Tentativa de pagar cobrança cancelada
* Expiração de pagamento
* Cancelamento de pagamento pendente
* Tentativa de cancelar pagamento pago
* Tentativa de cancelar pagamento já cancelado
* Tentativa de cancelar pagamento expirado
* Busca por pagamento inexistente

As dependências externas do Service, como `PaymentRepository` e `QrCodeService`, são mockadas durante os testes unitários.

---

## 📖 Documentação com Swagger

Com a aplicação em execução, a documentação pode ser acessada em:

```text
http://localhost:8080/swagger-ui.html
```

O Swagger permite visualizar e testar os endpoints diretamente pelo navegador.

A especificação OpenAPI também fica disponível em:

```text
http://localhost:8080/v3/api-docs
```

---

## 🐘 Banco de dados

O projeto utiliza **PostgreSQL**, executado localmente com Docker.

As credenciais de conexão são configuradas por meio de **variáveis de ambiente**, evitando expor informações sensíveis diretamente no código.

---

## 🐳 PostgreSQL com Docker

Exemplo para subir um PostgreSQL local:

```bash
docker run --name payment-db \
  -e POSTGRES_DB=payment_db \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=your_password \
  -p 5433:5432 \
  -d postgres:17
```

---

## ▶️ Executando o projeto

Com o PostgreSQL em execução e as variáveis de ambiente configuradas:

```bash
./mvnw spring-boot:run
```

A API ficará disponível em:

```text
http://localhost:8080
```
---

Até o próximo projeto 👋
