package ui;

import module java.base;

import model.Cliente;
import repository.RepositorioClientes;

public class ClienteMenu {

    private static final String COMANDO_CNPJ_TESTE = "DEBUG";
    private static final String CNPJ_TESTE = "12.345.678/0001-95";
    private final RepositorioClientes clientes;
    private final Scanner scanner;

    public ClienteMenu(
        RepositorioClientes clientes,
        Scanner scanner
    ){
        this.clientes = clientes;
        this.scanner = scanner;
    }

    public void executar() {
        boolean continuar = true;

        while (continuar) {
            Terminal.limpar();
            exibirOpcoes();

            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    cadastrarCliente();
                    break;

                case "2":
                    listarClientes();
                    break;

                case "0":
                    continuar = false;
                    Terminal.limpar();
                    break;

                default:
                    IO.println();
                    IO.println("Opção inválida.");
                    Terminal.pausar(scanner);
            }
        }
    }

    private void exibirOpcoes() {
        IO.println("=== MENU DE CLIENTES ===");
        IO.println("1 - Cadastrar cliente");
        IO.println("2 - Listar clientes");
        IO.println("0 - Voltar");
        IO.print("Escolha uma opção: ");
    }

    // Ações do usuário //

    // Lê, confirma e encaminha o cadastro de cliente. //
    private void cadastrarCliente() {
        Terminal.limpar();

        try {
            IO.println("=== CADASTRO DE CLIENTE ===");

            IO.print("CNPJ: ");
            String cnpjInformado = scanner.nextLine().trim();

            String cnpj = cnpjInformado.equalsIgnoreCase(COMANDO_CNPJ_TESTE)
                ? CNPJ_TESTE
                : cnpjInformado;
            // Verificação de cliente existente //
            
            // Cadastro do cliente //

            IO.print("Razão Social: ");
            String razaoSocial = scanner.nextLine();

            IO.print("Inscrição Estadual: ");
            String inscricaoEstadual = scanner.nextLine();

            IO.print("Endereço: ");
            String endereco = scanner.nextLine();

            Terminal.pausar(scanner);
            
            String email = null;
            String celular = null;
            boolean contatosConcluidos = false;
            
            while (!contatosConcluidos) {
                Terminal.limpar();
                IO.println("Formas de contato:");
                IO.println("1 - Informar telefone/celular");
                IO.println("2 - Informar e-mail");
                IO.println("0 - Finalizar");

                String opcaoContato = scanner.nextLine();

                switch (opcaoContato) {
                    case "1":
                        IO.print("Informe o telefone/celular: ");
                        String celularInformado = scanner.nextLine();

                        if (celularInformado.isBlank()) {
                            IO.println("O telefone/celular não pode ficar vazio.");
                        } else {
                            celular = celularInformado;
                        }
                        break;

                    case "2":
                        IO.print("Informe o e-mail: ");
                        String emailInformado = scanner.nextLine();

                        if (emailInformado.isBlank()) {
                            IO.println("O e-mail não pode ficar vazio.");
                        } else {
                            email = emailInformado;
                        }
                        break;

                    case "0":
                        if (email == null && celular == null) {
                            IO.println("Informe pelo menos uma forma de contato.");
                        } else {
                            contatosConcluidos = true;
                        }
                        Terminal.pausar(scanner);
                        break;

                    default:
                        IO.println("Opção inválida.");
                        Terminal.pausar(scanner);
                }
            }
                
            Terminal.limpar();

            IO.println("Confirme os dados:");
            System.out.printf("Razão Social: %16s%n", razaoSocial);
            System.out.printf("Cnpj: %24s%n", cnpj);
            System.out.printf("Inscrição Estadual: %10s%n", inscricaoEstadual);
            System.out.printf("Endereço: %20s%n", endereco);
            System.out.printf("Telefone/Celular: %12s%n", celular);
            System.out.printf("E-mail: %22s%n", email);
            IO.println();

            if (!confirmar("Deseja cadastrar este cliente?")) {
                IO.println("Cadastro cancelado.");
                Terminal.pausar(scanner);
                return;
            }

            Cliente cliente = new Cliente(
                razaoSocial,
                cnpj,
                inscricaoEstadual,
                endereco,
                email,
                celular
            );

            clientes.cadastrar(cliente);

            IO.println(
                "\nCliente cadastrado com sucesso."
            );

        } catch (IllegalArgumentException erro) {
            IO.println();
            IO.println(
                "\nNão foi possível cadastrar: "
                + erro.getMessage()
            );
        }

        Terminal.pausar(scanner);
    }

    // Exibe os clientes cadastrados //
    private void listarClientes() {
        Terminal.limpar();

        IO.println("=== CLIENTES CADASTRADOS ===");
        IO.println();
        
        System.out.printf(
            "%-20s | %-18s | %-18s | %-25s | %-18s | %25s%n",
            "CNPJ",
            "Razão Social",
            "Inscrição Estadual",
            "Endereço",
            "E-mail",
            "Telefone/Celular"
        );

        IO.println(
            "---------------------------------------------------------------------"
        );

        for (Cliente cliente : clientes.listarTodos()) {
            String inscricaoEstadual = Objects.toString(
                cliente.getInscricaoEstadual(),
                "Não informado"
            );

            String endereco = Objects.toString(
                cliente.getEndereco(),
                "Não informado"
            );
            
            String celular = Objects.toString(
                cliente.getCelular(),
                "Não informado"
            );
            
            String email = Objects.toString(
                cliente.getEmail(),
                "Não informado"
            );
            
            System.out.printf(
                "%-20s | %-18s | %-18s | %-25s | %-18s | %25s%n",
                cliente.getCnpj(),
                cliente.getRazaoSocial(),
                inscricaoEstadual.trim(),
                endereco.trim(),
                email.trim(),
                celular.trim()
                );
        }

        IO.println();

        Terminal.pausar(scanner);
    }

    // Entrada e confirmação //

    // Solicita uma resposta S/N e retorna a opção confirmada. //
    private boolean confirmar(String mensagem) {
        while (true) {
            System.out.printf("%s (S/N): ", mensagem);

            String resposta =
                scanner.nextLine().trim();

            if (resposta.equalsIgnoreCase("S")) {
                return true;
            }

            if (resposta.equalsIgnoreCase("N")) {
                return false;
            }

            IO.println(
                "Digite somente S ou N."
            );
        }
    }
    
}
