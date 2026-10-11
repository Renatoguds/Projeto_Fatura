import module java.base;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import model.Estoque;
import model.Cliente;
import persistence.RepositorioEstoqueJson;
import repository.RepositorioClientes;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    @Test
    void deveRejeitarClienteComCnpjJaCadastradoMesmoSemPontuacao() {
        RepositorioClientes repositorio =
            new RepositorioClientes(new ArrayList<>());

        Cliente primeiro = criarCliente("12.345.678/0001-95");
        Cliente duplicado = criarCliente("12345678000195");

        repositorio.cadastrar(primeiro);

        assertThrows(
            IllegalArgumentException.class,
            () -> repositorio.cadastrar(duplicado)
        );

        assertEquals(1, repositorio.listarTodos().size());
    }

    @Test
    void deveAceitarClientesComCnpjsDiferentes() {
        RepositorioClientes repositorio =
            new RepositorioClientes(new ArrayList<>());

        repositorio.cadastrar(
            criarCliente("12.345.678/0001-95")
        );

        repositorio.cadastrar(
            criarCliente("04.252.011/0001-10")
        );

        assertEquals(2, repositorio.listarTodos().size());
    }

    @Test
    void repositorioDeveCopiarListaRecebidaNoConstrutor() {
        List<Cliente> clientesIniciais = new ArrayList<>();
        clientesIniciais.add(criarCliente("12.345.678/0001-95"));

        RepositorioClientes repositorio =
            new RepositorioClientes(clientesIniciais);

        clientesIniciais.clear();

        assertEquals(1, repositorio.listarTodos().size());
    }

    @Test
    void listaRetornadaNaoDevePermitirAlteracoes() {
        RepositorioClientes repositorio =
            new RepositorioClientes(new ArrayList<>());

        repositorio.cadastrar(
            criarCliente("12.345.678/0001-95")
        );

        List<Cliente> clientesConsultados =
            repositorio.listarTodos();

        assertThrows(
            UnsupportedOperationException.class,
            () -> clientesConsultados.clear()
        );

        assertEquals(1, repositorio.listarTodos().size());
    }

    private Cliente criarCliente(String cnpj) {
        return new Cliente(
            "Empresa de teste",
            cnpj,
            null,
            null,
            "teste@exemplo.com",
            null
        );
    }
}