package model;
public class Produto {
    //--------------------Atributos--------------------//
    
    private int codigo;
    private String nome;
    private String ncm;
    private double valor;
    private int quantidade;

    //------------------- Construtor ------------------//
    
    public Produto(
        int codigo,
        String nome,
        String ncm,
        double valor,
        int quantidade
    ) {
        this.codigo = codigo;
        this.nome = nome;
        this.ncm = ncm;
        setValor(valor);
        setQuantidade(quantidade);
    }
    
    //--------------------- Métodos -------------------//
    
    private void validarValor(double valor) {
        if (valor <= 0){
            throw new IllegalArgumentException("Valor deve ser positivo");
        }
    }

    private void validarQuantidade(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException(
                "A quantidade deve ser maior que zero."
            );
        }
    }
    
    public boolean temMesmoCodigo(Produto outrProduto){
        return codigo == outrProduto.codigo;
    }

    public Produto criarCopia() {
        return new Produto(
            codigo, 
            nome, 
            ncm, 
            valor, 
            quantidade
        );
    }

    //--------------------- Getters -------------------//
    
    public int getCodigo() {
        return codigo;
    }
    
    public String getNome() {
        return nome;
    }

    public String getNcm() {
        return ncm;
    }
    
    public double getValor() {
        return valor;
    }

    public int getQuantidade() {
        return quantidade;
    }
    
    //--------------------- Setters -------------------//
    
    public void setNcm(String ncm) {
        this.ncm = ncm;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setValor(double valor) {
        validarValor(valor);
        this.valor = valor;
    }

    public void setQuantidade(int quantidade) {
        validarQuantidade(quantidade);
        this.quantidade = quantidade;
    }
    
    public void removerQuantidade(int quantidade) {
        validarQuantidade(quantidade);

        if (quantidade > this.quantidade) {
            throw new IllegalArgumentException(
                "Quantidade solicitada maior que o estoque disponível."
            );
        }

        this.quantidade -= quantidade;
    }
    
    public void adicionarQuantidade(int quantidade) {
        validarQuantidade(quantidade);
        this.quantidade += quantidade;
    }
}
