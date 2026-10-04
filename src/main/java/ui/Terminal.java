package ui;
import java.util.Scanner;

// Reúne operações auxiliares de interação com o terminal. //
public final class Terminal {

    private Terminal() {
        // Impede a instanciação. //
    }

    // Limpa o terminal usando sequências ANSI. //
    public static void limpar() {
        // Sequências ANSI limpam a tela e reposicionam o cursor no início. //
        System.out.print(
            "\u001B[2J"
            + "\u001B[3J"
            + "\u001B[H"
        );

        System.out.flush();
    }

    // Aguarda Enter para continuar a interação no terminal. //
    public static void pausar(Scanner scanner) {
        System.out.println();
        System.out.print("Pressione Enter para continuar...");
        scanner.nextLine();
    }
}
