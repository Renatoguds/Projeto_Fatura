package ui;
import java.util.Scanner;

public final class Terminal {

    private Terminal() {
        // Impede a instanciação.
    }

    public static void limpar() {
        System.out.print(
            "\u001B[2J"  // Limpa a tela
            + "\u001B[3J" // Limpa o histórico
            + "\u001B[H"  // Retorna o cursor ao início
        );

        System.out.flush();
    }

    public static void pausar(Scanner scanner) {
        System.out.println();
        System.out.print("Pressione Enter para continuar...");
        scanner.nextLine();
    }
}