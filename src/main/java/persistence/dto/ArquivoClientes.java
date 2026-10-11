package persistence.dto;

import java.util.List;

public class ArquivoClientes {
    private List<ClienteDados> clientes;

    public List<ClienteDados> getClientes() {
        return clientes;
    }

    public ArquivoClientes() {}

    public ArquivoClientes(
        List<ClienteDados> clientes
    ) {
        this.clientes = clientes;
    }
}
