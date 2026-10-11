package repository;

import module java.base;

import model.Cliente;
public class RepositorioClientes {

    private final List<Cliente> clientes;
        
    public List<Cliente> getClientes() {
        ArrayList <Cliente> copias = new ArrayList<>();

        for (Cliente cliente : clientes) {
            Cliente copia = cliente.criarCopia();
            copias.add(copia);
        }

        return List.copyOf(copias);
    }

    public RepositorioClientes() {
        this.clientes = new ArrayList<>();
    }

    public RepositorioClientes(List<Cliente> clientes){
        this.clientes = new ArrayList<>(clientes);
    }

    public void cadastrar(Cliente cliente) {
        for (Cliente clienteLista : clientes) {
            if (clienteLista.mesmoCnpj(cliente)) {
                throw new IllegalArgumentException("Cliente já cadastrado!");
            }
        }

        clientes.add(cliente);
    }

    public List<Cliente> listarTodos() {
        return List.copyOf(clientes);
    }
}
