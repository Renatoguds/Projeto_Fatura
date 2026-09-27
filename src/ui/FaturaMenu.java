package ui;
import java.util.List;
import java.util.Scanner;

import model.Estoque;
import model.Fatura;
import model.ItemFatura;
import model.Produto;

public class FaturaMenu {

    private final Fatura fatura;
    private final Estoque estoque;
    private final Scanner scan;

    public FaturaMenu(
            Fatura fatura,
            Estoque estoque,
            Scanner scan
    ) {
        this.fatura = fatura;
        this.estoque = estoque;
        this.scan = scan;
    }

    public void executar() {
        boolean continuar = true;

        while (continuar) {
            Terminal.limpar();
            exibirOpcoes();

            String opcao = scan.nextLine();

            switch (opcao) {
                case "1":
                    listarProdutosDisponiveis();
                    break;

                case "2":
                    adicionarProduto();
                    break;

                case "3":
                    removerProduto();
                    break;

                case "4":
                    visualizarFatura();
                    break;

                case "0":
                    continuar = false;
                    Terminal.limpar();
                    break;

                default:
                    System.out.println("Opção inválida.");
                    Terminal.pausar(scan);
            }
        }
    }

    private void exibirOpcoes() {
        System.out.println("=== COMPOSIÇÃO DA FATURA ===");
        System.out.println("1 - Listar produtos disponíveis");
        System.out.println("2 - Adicionar produto");
        System.out.println("3 - Remover produto");
        System.out.println("4 - Visualizar fatura");
        System.out.println("0 - Voltar");
        System.out.print("Escolha uma opção: ");
    }

    private void listarProdutosDisponiveis() {
        Terminal.limpar();

        System.out.println("=== PRODUTOS DISPONÍVEIS ===");
        System.out.println();

        imprimirProdutosDisponiveis();

        Terminal.pausar(scan);
    }

    private void adicionarProduto() {
        Terminal.limpar();

        try {
            System.out.println("=== ADICIONAR PRODUTO ===");
            System.out.println();

            imprimirProdutosDisponiveis();

            System.out.println();
            System.out.print("Código do produto: ");

            int codigo = Integer.parseInt(
                scan.nextLine()
            );

            System.out.print("Quantidade desejada: ");

            int quantidade = Integer.parseInt(
                scan.nextLine()
            );

            Produto produto =
                estoque.buscarProdutoPorCodigo(codigo);

            fatura.adicionarProduto(
                produto,
                quantidade
            );

            System.out.println();
            System.out.println(
                "\nProduto adicionado à fatura."
            );
        } catch (NumberFormatException erro) {
            System.out.println();
            System.out.println(
                "\nCódigo ou quantidade inválida."
            );
        } catch (IllegalArgumentException erro) {
            System.out.println();
            System.out.println(
                "\nNão foi possível adicionar: "
                + erro.getMessage()
            );
        }

        Terminal.pausar(scan);
    }

    private void removerProduto() {
        Terminal.limpar();

        try {
            System.out.println("=== REMOVER PRODUTO ===");
            System.out.println();

            if (fatura.getItens().isEmpty()) {
                System.out.println(
                    "\nA fatura não possui produtos."
                );

                Terminal.pausar(scan);
                return;
            }

            imprimirItensDaFatura();

            System.out.println();
            System.out.print("Código do produto: ");

            int codigo = Integer.parseInt(
                scan.nextLine()
            );

            System.out.print("Quantidade a remover: ");

            int quantidade = Integer.parseInt(
                scan.nextLine()
            );

            Produto produto =
                estoque.buscarProdutoPorCodigo(codigo);

            boolean removido = fatura.removerProduto(
                produto,
                quantidade
            );

            System.out.println();

            if (removido) {
                System.out.println(
                    "Produto removido da fatura."
                );
            } else {
                System.out.println(
                    "O produto não está presente na fatura."
                );
            }
        } catch (NumberFormatException erro) {
            System.out.println();
            System.out.println(
                "O código informado é inválido."
            );
        } catch (IllegalArgumentException erro) {
            System.out.println();
            System.out.println(
                "Não foi possível remover: "
                + erro.getMessage()
            );
        }

        Terminal.pausar(scan);
    }

    private void imprimirProdutosDisponiveis() {
        List<Produto> produtos = estoque.getProdutos();

        if (produtos.isEmpty()) {
            System.out.println(
                "Nenhum produto cadastrado."
            );
            return;
        }

        System.out.printf(
            "%-6s | %-20s | %10s | %10s%n",
            "Código",
            "Produto",
            "Valor",
            "Disponível"
        );

        System.out.println(
            "---------------------------------------------------------"
        );

        for (Produto produto : produtos) {
            System.out.printf(
                "%-6d | %-20.20s | R$ %7.2f | %10d%n",
                produto.getCodigo(),
                produto.getNome(),
                produto.getValor(),
                produto.getQuantidade()
            );
        }
    }

    private void imprimirItensDaFatura() {
        List<ItemFatura> itens = fatura.getItens();

        if (itens.isEmpty()) {
            System.out.println(
                "A fatura não possui produtos."
            );
            return;
        }

        System.out.printf(
            "%-6s | %-20s | %10s | %10s%n",
            "Código",
            "Produto",
            "Quantidade",
            "Subtotal"
        );

        System.out.println(
            "---------------------------------------------------------"
        );

        for (ItemFatura item : itens) {
            Produto produto = item.getProduto();

            System.out.printf(
                "%-6d | %-20.20s | %10d | R$ %7.2f%n",
                produto.getCodigo(),
                produto.getNome(),
                item.getQuantidade(),
                item.calcularSubtotal()
            );
        }
    }

    private void visualizarFatura() {
        Terminal.limpar();
        fatura.imprimirFatura();
        Terminal.pausar(scan);
    }
}