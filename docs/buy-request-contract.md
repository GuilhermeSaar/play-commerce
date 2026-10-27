# Contrato JSON para criar uma compra

Payload proposto para `POST /api/buy`, baseado nas tabelas `Compra`, `Compra_Jogo` e `Pagamento`:

```json
{
  "clientId": 7,
  "status": "PENDING",
  "items": [
    {
      "gameId": 12,
      "quantity": 2,
      "price": 49.90
    },
    {
      "gameId": 18,
      "quantity": 1,
      "price": 25.00
    }
  ],
  "payment": {
    "paymentMethod": "PIX",
    "paymentStatus": "PENDENTE"
  }
}
```

## Campos

| Campo | Obrigatório | Tabela/coluna | Descrição |
|---|---:|---|---|
| `clientId` | Sim | `Compra.id_cliente` | ID do cliente associado à compra. |
| `status` | Sim | `Compra.status` | Status: `PENDING`, `COMPLETED` ou `CANCELLED`. |
| `items` | Sim | `Compra_Jogo` | Lista de itens relacionados à compra. |
| `items[].gameId` | Sim | `Compra_Jogo.id_jogo` | ID do jogo relacionado. |
| `items[].quantity` | Sim | `Compra_Jogo.quantidade` | Quantidade registrada para o item. |
| `items[].price` | Sim | `Compra_Jogo.preco` | Preço unitário registrado no item. |
| `payment` | Sim | `Pagamento` | Dados do pagamento associado. |
| `payment.paymentMethod` | Sim | `Pagamento.id_forma_pagamento` | Forma: `CARTAO_CREDITO`, `PAYPAL`, `PIX` ou `BOLETO`. |
| `payment.paymentStatus` | Sim | `Pagamento.id_status_pagamento` | Status: `PENDENTE`, `APROVADO`, `RECUSADO` ou `CANCELADO`. |

O identificador da compra é gerado pela aplicação. Como `Pagamento.id_compra` é chave primária e estrangeira, o pagamento usa esse mesmo identificador; por isso não há `paymentId` no payload. As datas são preenchidas pela aplicação.

Este contrato define o formato JSON, sem definir regras para criar ou atualizar registros. Ele está representado pelos DTOs `BuyRequest`, `BuyGameDTO` e `PaymentRequest`.
