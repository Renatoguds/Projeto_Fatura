package model;

import java.math.BigDecimal;

// Representa uma linha da fatura, com produto, quantidade e subtotal. //
public class ItemFatura {

    // Atributos //

    private Produto produto;
    private int quantidade;

    // Métodos de acesso //

    // Retorna o produto associado a esta linha da fatura. //
    public Produto getProduto() {
        return produto;
    }

    // Retorna a quantidade atualmente registrada na linha. //
    public int getQuantidade() {
        return quantidade;
    }

    // Métodos de alteração //

    // Define a quantidade após validar que ela é positiva. //
    public void setQuantidade(int quantidade) {
        validarQuantidade(quantidade);
        this.quantidade = quantidade;
    }

    // Construtor //

    // Cria uma linha de fatura para um produto e uma quantidade inicial. //
    public ItemFatura(
            Produto produto,
            int quantidade
    ) {
        this.produto = produto;
        setQuantidade(quantidade);
    }

    // Operações //

    private void validarQuantidade(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException(
                "A quantidade do item deve ser maior que zero."
            );
        }
    }

    // Aumenta a quantidade desta linha usando um acréscimo positivo. //
    public void adicionarQuantidade(int quantidade) {
        validarQuantidade(quantidade);
        this.quantidade += quantidade;
    }

    // Calcula o subtotal usando o preço atual do produto e a quantidade. //
    public BigDecimal calcularSubtotal() {
        return produto.getValor()
            .multiply(BigDecimal.valueOf(quantidade));
    }

    // Reduz a quantidade após validar que ela não excede o saldo da linha. //
    public void removerQuantidade(int quantidade) {
        validarQuantidade(quantidade);

        if (quantidade > this.quantidade) {
            throw new IllegalArgumentException(
                "Quantidade maior que a presente na fatura."
            );
        }

        this.quantidade -= quantidade;
    }

    // Informa se a quantidade registrada nesta linha é zero. //
    public boolean estaVazio() {
        return quantidade == 0;
    }

    // Compara o código do produto desta linha ao produto informado. //
    public boolean correspondeAo(Produto produto){
        return this.produto.temMesmoCodigo(produto);
    }

}
