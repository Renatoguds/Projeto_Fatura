package persistence;

import module java.base;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import model.Estoque;
import model.Produto;
import persistence.dto.ArquivoEstoque;
import persistence.dto.ProdutoDados;

public class RepositorioEstoqueJson {
 
    
    private ArquivoEstoque converterParaArquivo(Estoque estoque) {
        List<ProdutoDados> produtosDados = new ArrayList<>();

        for (Produto produto : estoque.getProdutos()) {
            produtosDados.add(new ProdutoDados(produto));
        }

        return new ArquivoEstoque(
            produtosDados,
            estoque.getProximoCodigo()
        );       
    }

    public void salvar(Estoque estoque, Path caminhoArquivo) throws IOException {
        ArquivoEstoque dados = converterParaArquivo(estoque);

        Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

        String json = gson.toJson(dados);

        Path caminhoAbsoluto = caminhoArquivo.toAbsolutePath();
        Path pastaDados = caminhoAbsoluto.getParent();

        Files.createDirectories(pastaDados);

        Path arquivoTemporario = Files.createTempFile(
            pastaDados, 
            "estoque-", 
            ".tmp"
        );

        try {
            Files.writeString(
                arquivoTemporario,
                json,
                StandardCharsets.UTF_8
            );

            Files.move(
                arquivoTemporario, 
                caminhoAbsoluto, 
                StandardCopyOption.ATOMIC_MOVE,
                StandardCopyOption.REPLACE_EXISTING
            );
        } finally {
            Files.deleteIfExists(arquivoTemporario);
        }
    }

    public Estoque carregarEstoque(Path caminhoArquivo) throws IOException {
        String json = Files.readString(
            caminhoArquivo,
            StandardCharsets.UTF_8
        );

        Gson gson = new Gson();

        ArquivoEstoque dados =
            gson.fromJson(json, ArquivoEstoque.class);

        return converterParaEstoque(dados);
    }

    private Estoque converterParaEstoque(ArquivoEstoque dados) {
        List<Produto> produtos = new ArrayList<>();

        for (ProdutoDados produtoDados : dados.getProdutos()) {
            produtos.add(produtoDados.paraProduto());
        }

        return new Estoque(
            produtos, 
            dados.getProximoCodigo()
        );
    }
}
