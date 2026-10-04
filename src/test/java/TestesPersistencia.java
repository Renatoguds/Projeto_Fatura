import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import model.Estoque;
import persistence.RepositorioEstoqueJson;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestesPersistencia {

    @TempDir
    Path pastaTemporaria;

    @Test
    void deveSalvarEstoqueEmJson() throws IOException {
        Estoque estoque = new Estoque();
        estoque.cadastrarProduto(
            "Café",
            "09012100",
            "Marca Teste",
            new BigDecimal("15.50"),
            4
        );

        Path caminhoArquivo =
            pastaTemporaria.resolve("estoque.json");

        RepositorioEstoqueJson repositorio =
            new RepositorioEstoqueJson();

        repositorio.salvar(estoque, caminhoArquivo);

        assertTrue(Files.exists(caminhoArquivo));

        String conteudo = Files.readString(caminhoArquivo);
        JsonObject dadosJson =
            JsonParser.parseString(conteudo).getAsJsonObject();

        assertEquals(
            2,
            dadosJson.get("proximoCodigo").getAsInt()
        );

        JsonArray produtosJson =
            dadosJson.getAsJsonArray("produtos");

        assertEquals(1, produtosJson.size());

        JsonObject produtoJson =
            produtosJson.get(0).getAsJsonObject();

        assertEquals(
            "Marca Teste",
            produtoJson.get("marca").getAsString()
        );

        Estoque dadosCarregados =
            repositorio.carregarEstoque(caminhoArquivo);

        assertEquals(2, dadosCarregados.getProximoCodigo());
        assertEquals(1, dadosCarregados.getProdutos().size());
        assertEquals(
            "Marca Teste",
            dadosCarregados.getProdutos().get(0).getMarca()
);
    }
}