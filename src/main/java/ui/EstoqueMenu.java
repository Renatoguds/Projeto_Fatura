package ui;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

import model.Estoque;
import model.Produto;

// Apresenta as opções de cadastro e consulta do estoque no terminal. //
public class EstoqueMenu {
    // Atributos //

    private final Estoque estoque;
    private final Scanner scanner;

    // Construtor //

    // Prepara o menu com o estoque compartilhado e o leitor de entrada. //
    public EstoqueMenu(
        Estoque estoque,
        Scanner scanner
    ) {
        this.estoque = estoque;
        this.scanner = scanner;
    }

    // Fluxo do menu //

    // Mantém o menu do estoque ativo até o usuário escolher voltar. //
    public void executar() {
        boolean continuar = true;

        while (continuar) {
            Terminal.limpar();
            exibirOpcoes();

            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    cadastrarProduto();
                    break;

                case "2":
                    listarProdutos();
                    break;

                case "3":
                    exibirResumo();
                    break;

                case "0":
                    continuar = false;
                    Terminal.limpar();
                    break;

                default:
                    System.out.println();
                    System.out.println("Opção inválida.");
                    Terminal.pausar(scanner);
            }
        }
    }

    private void exibirOpcoes() {
        System.out.println();
        System.out.println("=== MENU DO ESTOQUE ===");
        System.out.println("1 - Cadastrar produto");
        System.out.println("2 - Listar produtos");
        System.out.println("3 - Exibir resumo");
        System.out.println("0 - Voltar");
        System.out.print("Escolha uma opção: ");
    }

    // Ações do usuário //

    // Lê, confirma e encaminha o cadastro ou a reposição de produto. //
    private void cadastrarProduto() {
        Terminal.limpar();

        try {
            System.out.println("=== CADASTRO DE PRODUTO ===");

            System.out.print("Nome: ");
            String nome = scanner.nextLine();

            System.out.print("Marca: ");
            String marca = scanner.nextLine();


            if (estoque.possuiProduto(nome, marca)) {
                System.out.println("Produto localizado!");
                System.out.print("Quantidade adicional: ");

                int quantidade = Integer.parseInt(
                    scanner.nextLine()
                );

                estoque.adicionarQuantidade(
                    nome,
                    marca,
                    quantidade
                );

                System.out.println(
                    "\nQuantidade atualizada com sucesso."
                );

                Terminal.pausar(scanner);
                return;
            }

            System.out.println("Produto não localizado!");
            System.out.println("Cadastrando novo produto...");
            System.out.print("NCM: ");
            String ncm = scanner.nextLine();

            System.out.print("Valor: R$ ");
            String valorInformado = scanner.nextLine();
            valorInformado = valorInformado.replace(",", ".");

            BigDecimal valor = new BigDecimal(valorInformado);

            System.out.print("Quantidade: ");
            int quantidade = Integer.parseInt(
                scanner.nextLine()
            );

            Terminal.limpar();

            System.out.println("Confirme os dados:");
            System.out.printf("Nome: %15s%n", nome);
            System.out.printf("NCM: %16s%n", ncm);
            System.out.printf("Marca: %14s%n", marca);
            System.out.printf("Valor: R$ %11.2f%n", valor);
            System.out.printf("Quantidade: %9d%n", quantidade);
            System.out.println();

            if (!confirmar("Deseja cadastrar este produto?")) {
                System.out.println("Cadastro cancelado.");
                Terminal.pausar(scanner);
                return;
            }

            estoque.cadastrarProduto(
                nome,
                ncm,
                marca,
                valor,
                quantidade
            );

            System.out.println(
                "\nProduto cadastrado com sucesso."
            );

        } catch (NumberFormatException erro) {
            System.out.println();
            System.out.println(
                "\nValor ou quantidade em formato inválido."
            );
        } catch (IllegalArgumentException erro) {
            System.out.println();
            System.out.println(
                "\nNão foi possível cadastrar: "
                + erro.getMessage()
            );
        }

        Terminal.pausar(scanner);
    }

    // Exibe os produtos cadastrados e os totais do estoque. //
    private void listarProdutos() {
        Terminal.limpar();

        System.out.println("=== PRODUTOS EM ESTOQUE ===");
        System.out.println();

        List<Produto> produtos = estoque.getProdutos();

        if (produtos.isEmpty()) {
            System.out.println(
                "Nenhum produto cadastrado."
            );

            Terminal.pausar(scanner);
            return;
        }

        System.out.printf(
            "%-6s | %-20s | %-8s | %-10s | %10s%n",
            "Código",
            "Produto",
            "NCM",
            "Valor",
            "Quantidade"
        );

        System.out.println(
            "---------------------------------------------------------------------"
        );

        for (Produto produto : produtos) {
            System.out.printf(
                "%-6d | %-20.20s | %-8.8s | R$ %7.2f | %10d%n",
                produto.getCodigo(),
                produto.getNome(),
                produto.getNcm(),
                produto.getValor(),
                produto.getQuantidade()
            );
        }

        System.out.println();

        System.out.printf(
            "Produtos diferentes: %d%n",
            estoque.getQuantidadeDeProdutosDiferentes()
        );

        System.out.printf(
            "Total de unidades: %d%n",
            estoque.getQuantidadeTotalEmEstoque()
        );

        Terminal.pausar(scanner);
    }

    // Exibe os totais resumidos de produtos e unidades. //
    private void exibirResumo() {
        Terminal.limpar();

        System.out.println("=== RESUMO DO ESTOQUE ===");
        System.out.println();

        System.out.printf(
            "Produtos diferentes: %d%n",
            estoque.getQuantidadeDeProdutosDiferentes()
        );

        System.out.printf(
            "Total de unidades: %d%n",
            estoque.getQuantidadeTotalEmEstoque()
        );

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

            System.out.println(
                "Digite somente S ou N."
            );
        }
    }

}
