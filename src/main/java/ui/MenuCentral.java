package ui;

import module java.base;

import model.*;
import persistence.RepositorioEstoqueJson;
import repository.RepositorioClientes;
import persistence.RepositorioClientesJson;

public class MenuCentral {

    private final Scanner scanner;

    public MenuCentral(Scanner scanner) {
        this.scanner = scanner;
    }

    public void executar() {
        Path caminhoEstoque = Path.of("data", "estoque.json");
        Path caminhoRepositorioClientes = Path.of("data", "clientes.json");
        
        RepositorioEstoqueJson repositorioEstoque =
            new RepositorioEstoqueJson();

        RepositorioClientesJson repositorioClientes =
            new RepositorioClientesJson();

        Estoque estoque;
        RepositorioClientes clientes;
        

        try {
            if (Files.exists(caminhoEstoque)) {
                estoque = repositorioEstoque.carregarEstoque(caminhoEstoque);
            } else {
                estoque = new Estoque();
            }
        } catch (IOException erro) {
            System.err.println(
                "Não foi possível carregar o estoque: "
                + erro.getMessage()
            );
            return;
        }

        try {
            if (Files.exists(caminhoRepositorioClientes)) {
                clientes = repositorioClientes.carregarClientes(caminhoRepositorioClientes);
            } else {
                clientes = new RepositorioClientes();
            }
        } catch (IOException erro) {
            System.err.println(
                "Não foi possível carregar os clientes: "
                + erro.getMessage()
            );
            return;
        }

        Cliente cliente = new Cliente(
            "Deolane Bezzera.",
            "04.252.011/0001-10",
            "123456789",
            "Rua de Teste, 100",
            "cliente@teste.com",
            "(11) 99999-9999"
        );

        Fatura fatura = new Fatura(
            1,
            cliente,
            LocalDate.of(2026, 9, 25),
            estoque
        );
        

        // Inicializa os objetos do sistema e executa os menus do terminal. //
        ClienteMenu clienteMenu = new ClienteMenu(clientes, scanner);
        EstoqueMenu estoqueMenu = new EstoqueMenu(estoque, scanner);
        FaturaMenu faturaMenu =
            new FaturaMenu(
                fatura, 
                estoque, 
                scanner
            );

        boolean continuar = true;

        
        while (continuar) {

            Terminal.limpar();
            
            /**
             * 1 - Menu Clientes
             * 2 - Menu Estoque
             * 3 - Menu Fatura
            */
           
           IO.println("=== GERENCIAMENTO DE ESTOQUE ===");
           IO.println("1 - Menu clientes");
           IO.println("2 - Menu estoque");
           IO.println("3 - Menu fatura");
           IO.println("0 - Sair");
           
           String opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    clienteMenu.executar();
                    salvarClientes(
                        repositorioClientes, 
                        clientes, 
                        caminhoRepositorioClientes
                    );
                    Terminal.pausar(scanner);
                    break;

                case "2":
                    estoqueMenu.executar();
                    salvarEstoque(
                        repositorioEstoque, 
                        estoque, 
                        caminhoEstoque
                    );
                    Terminal.pausar(scanner);
                    break;

                case "3":
                    faturaMenu.executar();
                    salvarEstoque(
                        repositorioEstoque, 
                        estoque, 
                        caminhoEstoque
                    );
                    Terminal.pausar(scanner);
                    break;

                case "0":
                    Terminal.limpar();
                    IO.println("Menu encerrado!");
                    salvarEstoque(
                        repositorioEstoque,
                        estoque, 
                        caminhoEstoque
                    );
                    salvarClientes(
                        repositorioClientes, 
                        clientes, 
                        caminhoRepositorioClientes
                    );
                    Terminal.pausar(scanner);
                    Terminal.limpar();
                    continuar = false;
                    break;
                
                default:
                    IO.println();
                    IO.println("Opção Inválida!");
                    Terminal.pausar(scanner);
                    Terminal.limpar();
            }
        }
    }

    private static void salvarEstoque(
        RepositorioEstoqueJson repositorio,
        Estoque estoque,
        Path caminhoEstoque
    ) {
        try {
            repositorio.salvar(estoque, caminhoEstoque);
            System.out.println("Estoque salvo.");
        } catch (IOException erro) {
            System.err.println(
                "Não foi possível salvar o estoque: "
                + erro.getMessage()
            );
        }
    }

    private static void salvarClientes(
        RepositorioClientesJson repositorio,
        RepositorioClientes rClientes,
        Path caminhoClientes
    ) {
        try {
            repositorio.salvar(rClientes, caminhoClientes);
            System.out.println("Clientes salvos.");
        } catch (IOException erro) {
            System.err.println(
                "Não foi possível salvar os clientes: "
                + erro.getMessage()
            );
        }
    }
}
