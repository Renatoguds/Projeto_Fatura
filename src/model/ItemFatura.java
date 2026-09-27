package model;

public class ItemFatura {

    //--------------------Atributos--------------------//

    private Produto produto;
    private int quantidade;

    //--------------------Construtor-------------------//

    public ItemFatura(
            Produto produto,
            int quantidade
    ) {
        this.produto = produto;
        setQuantidade(quantidade);
    }

    //---------------------Métodos---------------------//

    private void validarQuantidade(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException(
                "A quantidade do item deve ser maior que zero."
            );
        }
    }

    public void adicionarQuantidade(int quantidade) {
        validarQuantidade(quantidade);
        this.quantidade += quantidade;
    }

    public double calcularSubtotal() {
        return produto.getValor() * quantidade;
    }

    public void removerQuantidade(int quantidade) {
        validarQuantidade(quantidade);

        if (quantidade > this.quantidade) {
            throw new IllegalArgumentException(
                "Quantidade maior que a presente na fatura."
            );
        }

        this.quantidade -= quantidade;
    }

    public boolean estaVazio() {
        return quantidade == 0;
    }

    public boolean correspondeAo(Produto produto){
        return this.produto.temMesmoCodigo(produto);
    }

    //----------------------Getters--------------------//

    public Produto getProduto() {
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    //----------------------Setters--------------------//

    public void setQuantidade(int quantidade) {
        validarQuantidade(quantidade);
        this.quantidade = quantidade;
    }
}