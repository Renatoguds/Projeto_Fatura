import module java.base;

import model.Cliente;
import persistence.RepositorioClientesJson;
import repository.RepositorioClientes;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestesPersistenciaClientes {

    @TempDir
    Path pastaTemporaria;

    @Test
    void deveSalvarECarregarClientesComDadosOpcionais() throws IOException {
        RepositorioClientes clientes = new RepositorioClientes();
        clientes.cadastrar(
            new Cliente(
                "Padaria do Centro Ltda.",
                "12.345.678/0001-95",
                null,
                null,
                "contato@padaria.com",
                null
            )
        );
        clientes.cadastrar(
            new Cliente(
                "Oficina Dois Irmãos",
                "04.252.011/0001-10",
                "123456789",
                "Rua das Flores, 100",
                null,
                "(11) 99999-9999"
            )
        );

        Path caminhoArquivo = pastaTemporaria.resolve("clientes.json");
        RepositorioClientesJson repositorioJson =
            new RepositorioClientesJson();

        repositorioJson.salvar(clientes, caminhoArquivo);

        assertTrue(Files.exists(caminhoArquivo));

        RepositorioClientes clientesCarregados =
            repositorioJson.carregarClientes(caminhoArquivo);
        List<Cliente> listaCarregada = clientesCarregados.listarTodos();

        assertEquals(2, listaCarregada.size());

        Cliente primeiro = listaCarregada.get(0);
        assertEquals("Padaria do Centro Ltda.", primeiro.getRazaoSocial());
        assertEquals("12.345.678/0001-95", primeiro.getCnpj().toString());
        assertNull(primeiro.getInscricaoEstadual());
        assertNull(primeiro.getEndereco());
        assertEquals("contato@padaria.com", primeiro.getEmail());
        assertNull(primeiro.getCelular());

        Cliente segundo = listaCarregada.get(1);
        assertEquals("Oficina Dois Irmãos", segundo.getRazaoSocial());
        assertEquals("04.252.011/0001-10", segundo.getCnpj().toString());
        assertEquals("123456789", segundo.getInscricaoEstadual());
        assertEquals("Rua das Flores, 100", segundo.getEndereco());
        assertNull(segundo.getEmail());
        assertEquals("(11) 99999-9999", segundo.getCelular());
    }

    @Test
    void deveSalvarECarregarRepositorioDeClientesVazio() throws IOException {
        RepositorioClientes clientes = new RepositorioClientes();
        Path caminhoArquivo = pastaTemporaria.resolve("clientes-vazio.json");
        RepositorioClientesJson repositorioJson =
            new RepositorioClientesJson();

        repositorioJson.salvar(clientes, caminhoArquivo);

        RepositorioClientes clientesCarregados =
            repositorioJson.carregarClientes(caminhoArquivo);

        assertTrue(Files.exists(caminhoArquivo));
        assertTrue(clientesCarregados.listarTodos().isEmpty());
    }
}
