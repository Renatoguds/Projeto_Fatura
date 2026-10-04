package model;

import java.math.BigDecimal;

// Representa um produto cadastrado e seu saldo atual em estoque. //
public class Produto {
    // Atributos //

    private int codigo;
    private String nome;
    private String ncm;
    private String marca;
    private BigDecimal valor;
    private int quantidade;

    // Métodos de acesso //

    // Retorna o código identificador do produto no estoque. //
    public int getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public String getNcm() {
        return ncm;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public String getMarca() {
        return marca;
    }

    // Métodos de alteração //

    // Atualiza o NCM do produto. //
    public void setNcm(String ncm) {
        validarStringNormal(ncm, "ncm");
        
        this.ncm = ncm.trim();
    }

    // Atualiza o nome do produto. //
    public void setNome(String nome) {
        validarStringNormal(nome, "nome");

        this.nome = nome.trim();
    }

    // Atualiza a Marca do produto. //
    public void setMarca(String marca) {
        validarStringNormal(marca, "marca");
        
        this.marca = marca.trim();
    }

    // Atualiza o valor unitário após validar que é positivo. //
    public void setValor(BigDecimal valor) {
        validarValor(valor);
        this.valor = valor;
    }

    // Define o saldo; zero é permitido, mas valores negativos não. //
    public void setQuantidade(int quantidade) {
        validarSaldo(quantidade);
        this.quantidade = quantidade;
    }

    // Construtor //

    // Cria um produto e valida os valores monetário e de saldo inicial. //
    public Produto(
        int codigo,
        String nome,
        String ncm,
        String marca,
        BigDecimal valor,
        int quantidade
    ) {
        this.codigo = codigo;
        setNome(nome);
        setNcm(ncm);
        setMarca(marca);
        setValor(valor);
        setQuantidade(quantidade);
    }

    // Validações //

    private void validarStringNormal (String valor,String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("Preenchimento inválido do campo " + campo);
        }
    }

    private void validarValor(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Valor deve ser positivo");
        }
    }

    private void validarSaldo(int quantidade) {
        if (quantidade < 0) {
            throw new IllegalArgumentException(
                "O saldo do produto não pode ser negativo."
            );
        }
    }

    private void validarQuantidade(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException(
                "A quantidade deve ser maior que zero."
            );
        }
    }

    // Identidade e cópia //

    // Compara produtos pelo código de estoque. //
    public boolean temMesmoCodigo(Produto outroProduto){
        return codigo == outroProduto.codigo;
    }

    // Cria uma cópia independente dos valores atuais do produto. //
    public Produto criarCopia() {
        return new Produto(
            codigo,
            nome,
            ncm,
            marca,
            valor,
            quantidade
        );
    }

    // Movimentações de saldo //

    // Retira uma quantidade positiva, desde que haja saldo suficiente. //
    public void removerQuantidade(int quantidade) {
        validarQuantidade(quantidade);

        if (quantidade > this.quantidade) {
            throw new IllegalArgumentException(
                "Quantidade solicitada maior que o estoque disponível."
            );
        }

        this.quantidade -= quantidade;
    }

    // Acrescenta uma quantidade positiva ao saldo atual. //
    public void adicionarQuantidade(int quantidade) {
        validarQuantidade(quantidade);
        this.quantidade += quantidade;
    }
}
