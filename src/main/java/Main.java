// Ponto de entrada que prepara os objetos e inicia os menus do sistema. //

import java.nio.charset.Charset;
import java.util.Scanner;

import ui.MenuCentral;

public class Main{
    public static void main(String[] args) {

                // Obtém a codificação do terminal para interpretar corretamente a entrada do usuário. //
        Charset codificacaoEntrada = 
            System.console() == null ? 
            Charset.defaultCharset() : 
            System.console().charset();

        try (Scanner scanner = new Scanner(
            System.in,
            codificacaoEntrada
        )) {
            MenuCentral menu = new MenuCentral(scanner);
            menu.executar();
        }
    }
}
