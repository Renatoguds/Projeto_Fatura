package persistence.dto;

import java.util.List;


public class ArquivoEstoque {
    private List<ProdutoDados> produtos;
    private int proximoCodigo;

    public List<ProdutoDados> getProdutos() {
        return produtos;
    }

    public int getProximoCodigo() {
        return proximoCodigo;
    }

    public ArquivoEstoque() {}

    public ArquivoEstoque(
        List<ProdutoDados> produtos, 
        int proximoCodigo
    ) {
        this.produtos = produtos;
        this.proximoCodigo = proximoCodigo;
    }
}
