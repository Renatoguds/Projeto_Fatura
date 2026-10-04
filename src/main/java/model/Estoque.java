package model;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// Mantém os produtos cadastrados e coordena as movimentações do estoque. //
public class Estoque {
    // Atributos //

    private List<Produto> produtos;
    private int proximoCodigo;

    // Retorna uma lista imutável contendo cópias dos produtos cadastrados. //
    public List<Produto> getProdutos() {
        ArrayList <Produto> copias = new ArrayList<>();

        for (Produto produto : produtos) {
            Produto copia = produto.criarCopia();
            copias.add(copia);
        }

        return List.copyOf(copias);
    }

    // Retorna a quantidade de produtos distintos cadastrados. //
    public int getQuantidadeDeProdutosDiferentes(){
        return produtos.size();
    }

    // Soma e retorna as unidades de todos os produtos cadastrados. //
    public int getQuantidadeTotalEmEstoque(){
        int quantidadeTotal = 0;
        
        for (Produto produto : produtos){
            quantidadeTotal += produto.getQuantidade();
        }
        
        return quantidadeTotal;
    }

    public int getProximoCodigo() {
        return proximoCodigo;
    }

    // Construtor //

    // Cria um estoque vazio e inicia a sequência de códigos em um. //
    public Estoque() {
        this.produtos = new ArrayList<>();
        this.proximoCodigo = 1;
    }

    public Estoque(
        List<Produto> produtos, 
        int proximoCodigo
    ) {
        this.produtos = new ArrayList<>();

        for (Produto produto : produtos) {
            this.produtos.add(produto.criarCopia());
        }

        this.proximoCodigo = proximoCodigo;
    }

    // Validações //
    
    private void validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException(
                "O nome do produto é obrigatório."
            );
        }
    }
    
    // Consultas //

    // Busca interna por nome e marca; consultas públicas expõem cópias dos produtos. //

    private Produto buscarProdutoPorNomeEMarca (String nome, String marca) {
        for (Produto produto : produtos) {
            boolean mesmoNome = 
                produto.getNome().equalsIgnoreCase(nome.trim());
            
            boolean mesmaMarca = 
                produto.getMarca().equalsIgnoreCase(marca.trim());
        
            if (mesmoNome && mesmaMarca) {
                return produto;
            }
        }
        
        return null;
    }

    // Busca pelo código e retorna uma cópia independente do produto. //
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
    
    
    // Movimentações e cadastro //
    
    // Informa se existe um produto com o nome informado. //
    public boolean possuiProduto(String nome, String marca) {        
        return buscarProdutoPorNomeEMarca(nome, marca) != null;
    }

    // Acrescenta unidades ao produto identificado pelo nome. //
    public void adicionarQuantidade(
            String nome,
            String marca,
            int quantidade
    ) {
        validarNome(nome);

        Produto produto = buscarProdutoPorNomeEMarca(nome, marca);

        if (produto == null) {
            throw new IllegalArgumentException(
                "Produto não encontrado."
            );
        }

        produto.adicionarQuantidade(quantidade);
    }

    // Cadastra um produto ou acrescenta saldo ao produto de mesmo nome. //
    public void cadastrarProduto(
            String nome,
            String ncm,
            String marca,
            BigDecimal valor,
            int quantidade
    ){
        // Um nome já cadastrado representa o mesmo item e recebe apenas novo saldo. //
        validarNome(nome);

        Produto produtoExistente = 
            buscarProdutoPorNomeEMarca(nome, marca);

        if (produtoExistente != null) {
            produtoExistente.adicionarQuantidade(quantidade);
            return;
        }

        Produto novoProduto = new Produto(
            proximoCodigo, 
            nome.trim(), 
            ncm, 
            marca,
            valor, 
            quantidade
        );

        produtos.add(novoProduto);
        proximoCodigo++;
    }
    
    // Retira unidades do produto correspondente ao código informado. //
    public void retirarProduto(
        Produto produto,
        int quantidade
    ) {
        Produto produtoCadastrado = 
            buscarProduto(produto);
        
        produtoCadastrado.removerQuantidade(quantidade);
    }

    // Devolve ao estoque unidades previamente retiradas do produto. //
    public void devolverProduto(
        Produto produto,
        int quantidade
    ) {
        Produto produtoCadastrado = 
            buscarProduto(produto);
        
        produtoCadastrado.adicionarQuantidade(quantidade);
    }
    
}
