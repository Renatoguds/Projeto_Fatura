package model;
import java.util.ArrayList;
import java.util.List;

public class Estoque {
    //--------------------Atributos--------------------//

    private ArrayList<Produto> produtos;
    private int proximoCodigo;

    //------------------- Construtor ------------------//

    public Estoque() {
        this.produtos = new ArrayList<>();
        this.proximoCodigo = 1;
    }

    //--------------------- Métodos -------------------//
    
    private void validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException(
                "O nome do produto é obrigatório."
            );
        }
    }
    
    //--------------------- Getters -------------------//

    public List<Produto> getProdutos() {
        ArrayList <Produto> copias = new ArrayList<>();

        for (Produto produto : produtos) {
            Produto copia = produto.criarCopia();
            copias.add(copia);
        }

        return List.copyOf(copias);
    }

    private Produto buscarProdutoPorNome(String nome){
        for (Produto produto : produtos){
            if (produto.getNome().equalsIgnoreCase(nome.trim())){
                return produto;
            }
        }

        return null;
    }

    public Produto buscarProdutoPorCodigo(int codigo) {
        for (Produto produto : produtos) {
            if (produto.getCodigo() == codigo) {
                return produto.criarCopia();
            }
        }

        throw new IllegalArgumentException(
            "Produto não encontrado."
        );
    }

    private Produto buscarProduto(Produto produtoProcurado) {
        for (Produto produto : produtos) {
            if (produto.temMesmoCodigo(produtoProcurado)) {
                return produto;
            }
        }

        throw new IllegalArgumentException(
            "Produto não encontrado no estoque."
        );
    }
    
    public int getQuantidadeDeProdutosDiferentes(){
        return produtos.size();
    }

    public int getQuantidadeTotalEmEstoque(){
        int quantidadeTotal = 0;
        
        for (Produto produto : produtos){
            quantidadeTotal += produto.getQuantidade();
        }
        
        return quantidadeTotal;
    }
    
    //--------------------- Setters -------------------//
    
    public boolean possuiProduto(String nome) {
        validarNome(nome);
        return buscarProdutoPorNome(nome) != null;
    }

    public void adicionarQuantidade(
            String nome,
            int quantidade
    ) {
        validarNome(nome);

        Produto produto = buscarProdutoPorNome(nome);

        if (produto == null) {
            throw new IllegalArgumentException(
                "Produto não encontrado."
            );
        }

        produto.adicionarQuantidade(quantidade);
    }
    public void cadastrarProduto(
            String nome,
            String ncm,
            double valor,
            int quantidade
    ){
        validarNome(nome);

        Produto produtoExistente = 
            buscarProdutoPorNome(nome);

        if (produtoExistente != null) {
            produtoExistente.adicionarQuantidade(quantidade);
            return;
        }

        Produto novoProduto = new Produto(
            proximoCodigo, 
            nome.trim(), 
            ncm, 
            valor, 
            quantidade
        );

        produtos.add(novoProduto);
        proximoCodigo++;
    }
    
    public void retirarProduto(
        Produto produto,
        int quantidade
    ) {
        Produto produtoCadastrado = 
            buscarProduto(produto);
        
        produtoCadastrado.removerQuantidade(quantidade);
    }

    public void devolverProduto(
        Produto produto,
        int quantidade
    ) {
        Produto produtoCadastrado = 
            buscarProduto(produto);
        
        produtoCadastrado.adicionarQuantidade(quantidade);
    }
    
}