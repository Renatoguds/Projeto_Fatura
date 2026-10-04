import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Scanner;
import persistence.RepositorioEstoqueJson;

import model.*;
import ui.*;


// Ponto de entrada que prepara os objetos e inicia os menus do sistema. //
public class Main{
    // Inicializa os objetos do sistema e executa os menus do terminal. //
    
    public static void main(String[] args) {
        // Obtém a codificação do terminal para interpretar corretamente a entrada do usuário. //
        Charset codificacaoEntrada = System.console() == null ? Charset.defaultCharset() : System.console().charset();
        
        Path caminhoEstoque = Path.of("data", "estoque.json");
        
        RepositorioEstoqueJson repositorio =
            new RepositorioEstoqueJson();

        Estoque estoque;
        

        try {

            if (Files.exists(caminhoEstoque)) {
                estoque = repositorio.carregarEstoque(caminhoEstoque);
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

        try (Scanner scanner = new Scanner(
            System.in,
            codificacaoEntrada
        )) {
        
            EstoqueMenu estoqueMenu = new EstoqueMenu(estoque, scanner);

            estoqueMenu.executar();
            salvarEstoque(repositorio, estoque, caminhoEstoque);

            FaturaMenu faturaMenu =
                new FaturaMenu(
                    fatura, 
                    estoque, 
                    scanner
                );
            
            faturaMenu.executar();
            salvarEstoque(repositorio, estoque, caminhoEstoque);

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
}
