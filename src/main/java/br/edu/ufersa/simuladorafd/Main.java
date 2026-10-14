package br.edu.ufersa.simuladorafd;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Locale.setDefault(new Locale("pt", "BR"));
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        System.setErr(new PrintStream(System.err, true, StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(System.in);

        try {
            AFD automato = LeitorAutomato.lerConsole(scanner);
            System.out.println("\nAFD válido: " + automato + "\n");
            executarLoopDeValidacao(automato, scanner);
        } catch (IllegalArgumentException e) {
            System.err.println("Erro ao criar o autômato: " + e.getMessage());
        }
    }

    private static void executarLoopDeValidacao(AFD automato, Scanner scanner) {
        while (true) {
            System.out.println("Digite uma cadeia para testar (ou 'sair' para encerrar):");
            System.out.flush();
            if (!scanner.hasNextLine()) {
                break;
            }

            String entrada = scanner.nextLine();

            String normalizada = entrada.trim();
            if (normalizada.equalsIgnoreCase("sair") || normalizada.equalsIgnoreCase("exit")) {
                break;
            }

            try {
                boolean aceita = automato.aceita(normalizada);
                System.out.println(aceita ? "ACEITA" : "REJEITA");
                System.out.println();
            } catch (IllegalArgumentException e) {
                System.out.println("Cadeia inválida: " + e.getMessage());
                System.out.println();
            }
        }
    }
}