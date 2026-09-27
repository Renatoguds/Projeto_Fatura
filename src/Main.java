import java.nio.charset.Charset;
import java.util.Scanner;

import model.*;
import ui.*;

public class Main{
    public static void main(String[] args) {
        Charset codificacaoEntrada = System.console() == null ? Charset.defaultCharset() : System.console().charset();
        
        Estoque estoque = new Estoque();

        Cliente cliente = new Cliente(
            "Cliente de Teste Ltda.",
            "04.252.011/0001-10",
            "123456789",
            "Rua de Teste, 100",
            "cliente@teste.com",
            "(11) 99999-9999"
        );

        Fatura fatura = new Fatura(
            1,
            cliente,
            "25/09/2026",
            estoque
        );

        try (Scanner scanner = new Scanner(
            System.in,
            codificacaoEntrada
        )) {
        
            EstoqueMenu estoqueMenu = new EstoqueMenu(estoque, scanner);

            estoqueMenu.executar();

            FaturaMenu faturaMenu =
                new FaturaMenu(
                    fatura, 
                    estoque, 
                    scanner
                );
            
            faturaMenu.executar();

        }
    }
}