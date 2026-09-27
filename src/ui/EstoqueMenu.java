package ui;
import java.util.List;
import java.util.Scanner;

import model.Estoque;
import model.Produto;

public class EstoqueMenu {
    //--------------------Atributos--------------------//
    
    private final Estoque estoque;
    private final Scanner scan;

    //------------------- Construtor ------------------//
    
    public EstoqueMenu(
        Estoque estoque,
        Scanner scan
    ) {
        this.estoque = estoque;
        this.scan = scan;
    }
    
    //--------------------- Métodos -------------------//

    public void executar() {
        boolean continuar = true;
        
        while (continuar) {
            Terminal.limpar();
            exibirOpcoes();

            String opcao = scan.nextLine();

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
                    Terminal.pausar(scan);
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

    private void cadastrarProduto() {
        Terminal.limpar();

        try {
            System.out.println("=== CADASTRO DE PRODUTO ===");

            System.out.print("Nome: ");
            String nome = scan.nextLine();

        if (estoque.possuiProduto(nome)) {
            System.out.println("Produto localizado!");
            System.out.print("Quantidade adicional: ");

            int quantidade = Integer.parseInt(
                scan.nextLine()
            );

            estoque.adicionarQuantidade(
                nome,
                quantidade
            );

            System.out.println(
                "\nQuantidade atualizada com sucesso."
            );

            Terminal.pausar(scan);
            return;
        }
            System.out.println("Produto não localizado!");
            System.out.println("Cadastrando novo produto...");
            System.out.print("NCM: ");
            String ncm = scan.nextLine();

            System.out.print("Valor: R$ ");
            String valorInformado = scan.nextLine();
            valorInformado = valorInformado.replace(",", ".");

            double valor = Double.parseDouble(valorInformado);

            System.out.print("Quantidade: ");
            int quantidade = Integer.parseInt(
                scan.nextLine()
            );

            Terminal.limpar();

            System.out.println("Confirme os dados:");
            System.out.printf("Nome: %15s%n", nome);
            System.out.printf("NCM: %16s%n", ncm);
            System.out.printf("Valor: R$ %11.2f%n", valor);
            System.out.printf("Quantidade: %9d%n", quantidade);
            System.out.println();

            if (!confirmar("Deseja cadastrar este produto?")) {
                System.out.println("Cadastro cancelado.");
                Terminal.pausar(scan);
                return;
            }

            estoque.cadastrarProduto(
                nome,
                ncm,
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

        

        Terminal.pausar(scan);
    }

    private void listarProdutos() {
        Terminal.limpar();

        System.out.println("=== PRODUTOS EM ESTOQUE ===");
        System.out.println();

        List<Produto> produtos = estoque.getProdutos();

        if (produtos.isEmpty()) {
            System.out.println(
                "Nenhum produto cadastrado."
            );

            Terminal.pausar(scan);
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

        Terminal.pausar(scan);
    }

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

        Terminal.pausar(scan);
    }

    private boolean confirmar(String mensagem) {
        while (true) {
            System.out.printf("%s (S/N): ", mensagem);

            String resposta =
                scan.nextLine().trim();

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
