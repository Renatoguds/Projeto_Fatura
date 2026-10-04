package persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

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

        Path pastaDados = caminhoArquivo.getParent();

        if (pastaDados != null) {
            Files.createDirectories(pastaDados);
        }

        Files.writeString(
            caminhoArquivo,
            json,
            StandardCharsets.UTF_8
        );
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
