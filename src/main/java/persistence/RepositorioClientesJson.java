package persistence;

import module java.base;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import model.Cliente;
import persistence.dto.ArquivoClientes;
import persistence.dto.ClienteDados;
import repository.RepositorioClientes;

public class RepositorioClientesJson {
    private ArquivoClientes converterParaArquivo(RepositorioClientes repositorioClientes) {
        List<ClienteDados> clienteDados = new ArrayList<>();

        for (Cliente cliente : repositorioClientes.getClientes()) {
            clienteDados.add(new ClienteDados(cliente));
        }

        return new ArquivoClientes(
            clienteDados
        );       
    }

    public void salvar(RepositorioClientes repositorioCliente, Path caminhoArquivo) throws IOException {
        ArquivoClientes dados = converterParaArquivo(repositorioCliente);

        Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

        String json = gson.toJson(dados);

        Path caminhoAbsoluto = caminhoArquivo.toAbsolutePath();
        Path pastaDados = caminhoAbsoluto.getParent();

        Files.createDirectories(pastaDados);

        Path arquivoTemporario = Files.createTempFile(
            pastaDados, 
            "clientes-", 
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

    public RepositorioClientes carregarClientes(Path caminhoArquivo) throws IOException {
        String json = Files.readString(
            caminhoArquivo,
            StandardCharsets.UTF_8
        );

        Gson gson = new Gson();

        ArquivoClientes dados =
            gson.fromJson(json, ArquivoClientes.class);

        return converterParaClientes(dados);
    }

    private RepositorioClientes converterParaClientes(ArquivoClientes dados) {
        List<Cliente> clientes = new ArrayList<>();

        for (ClienteDados clienteDados : dados.getClientes()) {
            clientes.add(clienteDados.paraCliente());
        }

        return new RepositorioClientes(
            clientes
        );
    }
}
