package br.edu.ufersa.simuladorafd;

import java.util.Objects;

public class Transicao {
    private final String estadoOrigem;
    private final String simbolo;
    private final String estadoDestino;

    public Transicao(String estadoOrigem, String simbolo, String estadoDestino) {
        this.estadoOrigem = normalizarEstado(estadoOrigem, "estado de origem");
        this.simbolo = normalizarSimbolo(simbolo);
        this.estadoDestino = normalizarEstado(estadoDestino, "estado de destino");
    }

    public String getEstadoOrigem() {
        return estadoOrigem;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public String getEstadoDestino() {
        return estadoDestino;
    }

    private static String normalizarEstado(String valor, String nomeCampo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("O " + nomeCampo + " não pode ser vazio.");
        }
        return valor.trim();
    }

    private static String normalizarSimbolo(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("O símbolo da transição não pode ser vazio.");
        }
        String simbolo = valor.trim();
        if (simbolo.length() != 1) {
            throw new IllegalArgumentException("Cada símbolo do alfabeto deve conter apenas um caractere: '" + simbolo + "'.");
        }
        return simbolo;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Transicao outra)) {
            return false;
        }
        return Objects.equals(estadoOrigem, outra.estadoOrigem)
                && Objects.equals(simbolo, outra.simbolo)
                && Objects.equals(estadoDestino, outra.estadoDestino);
    }

    @Override
    public int hashCode() {
        return Objects.hash(estadoOrigem, simbolo, estadoDestino);
    }

    @Override
    public String toString() {
        return estadoOrigem + " --" + simbolo + "--> " + estadoDestino;
    }
}