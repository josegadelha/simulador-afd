package br.edu.ufersa.simuladorafd;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class LeitorAutomato {
    private LeitorAutomato() {
    }

    public static AFD lerConsole() {
        return lerConsole(new Scanner(System.in));
    }

    public static AFD lerConsole(Scanner scanner) {
        System.out.println();
        System.out.println("=== Montagem do AFD ===");
        System.out.print("Informe os estados (separados por vírgula): ");
        List<String> estados = parseLista(scanner.nextLine());

        System.out.print("Informe o alfabeto (máx. 2 símbolos, separados por vírgula): ");
        List<String> alfabeto = parseLista(scanner.nextLine());

        System.out.print("Informe o estado inicial: ");
        String estadoInicial = scanner.nextLine().trim();

        System.out.print("Informe os estados de aceitação (separados por vírgula): ");
        Set<String> estadosAceitacao = new LinkedHashSet<>();
        for (String estado : parseLista(scanner.nextLine())) {
            estadosAceitacao.add(estado);
        }

        List<Transicao> transicoes = new ArrayList<>();
        System.out.println("Informe as transições em formato 'estado,simbolo,estado' e digite 'fim' para encerrar.");
        while (true) {
            System.out.print("Transição: ");
            String linha = scanner.nextLine();
            if (linha == null || linha.isBlank()) {
                continue;
            }
            if (linha.equalsIgnoreCase("fim") || linha.equalsIgnoreCase("finalizar")) {
                break;
            }
            transicoes.add(parseTransicao(linha));
        }

        return new AFD(estados, alfabeto, estadoInicial, estadosAceitacao, transicoes);
    }


    private static List<String> parseLista(String valorBruto) {
        if (valorBruto == null || valorBruto.isBlank()) {
            throw new IllegalArgumentException("A lista informada não pode estar vazia.");
        }

        String sanitizado = valorBruto
                .replace("{", " ")
                .replace("}", " ")
                .replace("[", " ")
                .replace("]", " ")
                .replace("(", " ")
                .replace(")", " ")
                .trim();

        String[] valores = sanitizado.split("[;,\\s]+");
        List<String> itens = new ArrayList<>();
        for (String valor : valores) {
            if (!valor.isBlank()) {
                itens.add(valor.trim());
            }
        }

        if (itens.isEmpty()) {
            throw new IllegalArgumentException("A lista informada não contém valores válidos.");
        }

        return itens;
    }

    public static Transicao parseTransicao(String transicaoBruta) {
        if (transicaoBruta == null || transicaoBruta.isBlank()) {
            throw new IllegalArgumentException("A transição não pode estar vazia.");
        }

        String sanitizada = transicaoBruta.trim();
        String estadoOrigem;
        String simbolo;
        String estadoDestino;

        if (sanitizada.contains("->")) {
            String[] partes = sanitizada.split("->");
            if (partes.length != 2) {
                throw new IllegalArgumentException("Formato de transição inválido: " + transicaoBruta);
            }
            String parteEsquerda = partes[0].trim();
            String[] valoresEsquerda = parteEsquerda.split("[ ,]+");
            if (valoresEsquerda.length != 2) {
                throw new IllegalArgumentException("Formato de transição inválido: " + transicaoBruta);
            }
            estadoOrigem = valoresEsquerda[0];
            simbolo = valoresEsquerda[1];
            estadoDestino = partes[1].trim();
        } else {
            String[] partes = sanitizada.split("[ ,]+");
            if (partes.length != 3) {
                throw new IllegalArgumentException("Formato de transição inválido: " + transicaoBruta);
            }
            estadoOrigem = partes[0];
            simbolo = partes[1];
            estadoDestino = partes[2];
        }

        return new Transicao(estadoOrigem, simbolo, estadoDestino);
    }
}