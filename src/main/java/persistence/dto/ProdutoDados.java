package persistence.dto;

import java.math.BigDecimal;

import model.Produto;

public class ProdutoDados {
    // Atributos //
    
    private int codigo;
    private String nome;
    private String ncm;
    private String marca;
    private BigDecimal valor;
    private int quantidade;

    // Métodos de acesso //

    public int getCodigo() {
        return codigo;
    }

    public String getMarca() {
        return marca;
    }

    public String getNcm() {
        return ncm;
    }

    public String getNome() {
        return nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public BigDecimal getValor() {
        return valor;
    }

    // Construtor //
    
    public ProdutoDados(
        int codigo,
        String nome,
        String ncm,
        String marca,
        BigDecimal valor,
        int quantidade
    ) {
        this.codigo = codigo;
        this.nome = nome;
        this.ncm = ncm;
        this.marca = marca;
        this.valor = valor;
        this.quantidade = quantidade;
    }

    public ProdutoDados() {}

    public ProdutoDados (Produto produto) {
        this(
            produto.getCodigo(),
            produto.getNome(),
            produto.getNcm(),
            produto.getMarca(),
            produto.getValor(),
            produto.getQuantidade()
        );
    }
    
    // Conversão para produto //

    public Produto paraProduto() {
        return new Produto(
            codigo,
            nome,
            ncm,
            marca,
            valor,
            quantidade
        );
    }

}