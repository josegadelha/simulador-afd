package br.edu.ufersa.simuladorafd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class AFD {
    private final List<String> estados;
    private final List<String> alfabeto;
    private final String estadoInicial;
    private final Set<String> estadosAceitacao;
    private final Map<String, Map<String, String>> transicoes;

    public AFD(
            List<String> estados,
            List<String> alfabeto,
            String estadoInicial,
            Collection<String> estadosAceitacao,
            Collection<Transicao> transicoes
    ) {
        this.estados = normalizarEstados(estados);
        this.alfabeto = normalizarAlfabeto(alfabeto);
        this.estadoInicial = validarEstadoInicial(estadoInicial);
        this.estadosAceitacao = normalizarEstadosAceitacao(estadosAceitacao);
        this.transicoes = construirMapaTransicoes(transicoes);
        validarAutomato();
    }

    public boolean aceita(String entrada) {
        if (entrada == null) {
            throw new IllegalArgumentException("A cadeia não pode ser nula.");
        }

        String estadoAtual = estadoInicial;
        String entradaNormalizada = entrada.trim();

        if (entradaNormalizada.isEmpty()) {
            return estadosAceitacao.contains(estadoAtual);
        }

        for (int i = 0; i < entradaNormalizada.length(); i++) {
            String simbolo = String.valueOf(entradaNormalizada.charAt(i));
            if (!alfabeto.contains(simbolo)) {
                throw new IllegalArgumentException(
                        "O símbolo '" + simbolo + "' não pertence ao alfabeto definido: " + alfabeto
                );
            }

            Map<String, String> transicoesDoEstado = transicoes.get(estadoAtual);
            if (transicoesDoEstado == null || !transicoesDoEstado.containsKey(simbolo)) {
                return false;
            }
            estadoAtual = transicoesDoEstado.get(simbolo);
        }

        return estadosAceitacao.contains(estadoAtual);
    }

    public List<String> getEstados() {
        return Collections.unmodifiableList(estados);
    }

    public List<String> getAlfabeto() {
        return Collections.unmodifiableList(alfabeto);
    }

    public String getEstadoInicial() {
        return estadoInicial;
    }

    public Set<String> getEstadosAceitacao() {
        return Collections.unmodifiableSet(estadosAceitacao);
    }

    public String proximoEstado(String estadoAtual, String simbolo) {
        if (!estados.contains(estadoAtual)) {
            throw new IllegalArgumentException("Estado inexistente: " + estadoAtual);
        }
        if (!alfabeto.contains(simbolo)) {
            throw new IllegalArgumentException("Símbolo inexistente no alfabeto: " + simbolo);
        }

        Map<String, String> transicoesDoEstado = transicoes.get(estadoAtual);
        if (transicoesDoEstado == null || !transicoesDoEstado.containsKey(simbolo)) {
            throw new IllegalArgumentException(
                    "Não existe transição definida para o estado '" + estadoAtual + "' com o símbolo '" + simbolo + "'."
            );
        }

        return transicoesDoEstado.get(simbolo);
    }

    private List<String> normalizarEstados(List<String> estadosBrutos) {
        if (estadosBrutos == null || estadosBrutos.isEmpty()) {
            throw new IllegalArgumentException("O conjunto de estados não pode estar vazio.");
        }

        List<String> normalizados = new ArrayList<>();
        for (String estado : estadosBrutos) {
            if (estado == null || estado.isBlank()) {
                continue;
            }
            String limpo = estado.trim();
            if (normalizados.contains(limpo)) {
                throw new IllegalArgumentException("Estado duplicado encontrado: " + limpo);
            }
            normalizados.add(limpo);
        }

        if (normalizados.isEmpty()) {
            throw new IllegalArgumentException("O conjunto de estados não pode estar vazio.");
        }

        return List.copyOf(normalizados);
    }

    private List<String> normalizarAlfabeto(List<String> alfabetoBruto) {
        if (alfabetoBruto == null || alfabetoBruto.isEmpty()) {
            throw new IllegalArgumentException("O alfabeto não pode estar vazio.");
        }

        List<String> normalizado = new ArrayList<>();
        for (String simbolo : alfabetoBruto) {
            if (simbolo == null || simbolo.isBlank()) {
                continue;
            }
            String limpo = simbolo.trim();
            if (limpo.length() != 1) {
                throw new IllegalArgumentException(
                        "Cada símbolo do alfabeto deve ter um único caractere. Símbolo inválido: '" + limpo + "'."
                );
            }
            if (normalizado.contains(limpo)) {
                throw new IllegalArgumentException("Símbolo duplicado no alfabeto: " + limpo);
            }
            normalizado.add(limpo);
        }

        if (normalizado.isEmpty()) {
            throw new IllegalArgumentException("O alfabeto não pode estar vazio.");
        }
        if (normalizado.size() > 2) {
            throw new IllegalArgumentException("A simplificação permitida restringe o alfabeto a até 2 símbolos.");
        }

        return List.copyOf(normalizado);
    }

    private String validarEstadoInicial(String estadoInicialInformado) {
        if (estadoInicialInformado == null || estadoInicialInformado.isBlank()) {
            throw new IllegalArgumentException("O estado inicial não pode ser vazio.");
        }
        String limpo = estadoInicialInformado.trim();
        if (!estados.contains(limpo)) {
            throw new IllegalArgumentException("O estado inicial '" + limpo + "' não pertence ao conjunto de estados.");
        }
        return limpo;
    }

    private Set<String> normalizarEstadosAceitacao(Collection<String> estadosAceitacaoBrutos) {
        Set<String> normalizado = new LinkedHashSet<>();
        if (estadosAceitacaoBrutos == null) {
            return normalizado;
        }
        for (String estado : estadosAceitacaoBrutos) {
            if (estado == null || estado.isBlank()) {
                continue;
            }
            String limpo = estado.trim();
            if (!estados.contains(limpo)) {
                throw new IllegalArgumentException(
                        "O estado de aceitação '" + limpo + "' não pertence ao conjunto de estados."
                );
            }
            normalizado.add(limpo);
        }
        return Set.copyOf(normalizado);
    }

    private Map<String, Map<String, String>> construirMapaTransicoes(Collection<Transicao> transicoesBrutas) {
        Map<String, Map<String, String>> mapa = new HashMap<>();
        if (transicoesBrutas == null) {
            return mapa;
        }

        for (Transicao transicao : transicoesBrutas) {
            if (!estados.contains(transicao.getEstadoOrigem())) {
                throw new IllegalArgumentException(
                        "Estado de origem inválido na transição: " + transicao.getEstadoOrigem()
                );
            }
            if (!estados.contains(transicao.getEstadoDestino())) {
                throw new IllegalArgumentException(
                        "Estado de destino inválido na transição: " + transicao.getEstadoDestino()
                );
            }
            if (!alfabeto.contains(transicao.getSimbolo())) {
                throw new IllegalArgumentException(
                        "Símbolo inválido em transição: '" + transicao.getSimbolo() + "' não pertence ao alfabeto."
                );
            }

            Map<String, String> transicoesDoEstado = mapa.computeIfAbsent(
                    transicao.getEstadoOrigem(), chave -> new HashMap<>()
            );

            if (transicoesDoEstado.containsKey(transicao.getSimbolo())) {
                throw new IllegalArgumentException(
                        "Autômato inválido: transição duplicada para o estado '" + transicao.getEstadoOrigem()
                                + "' com o símbolo '" + transicao.getSimbolo() + "'."
                );
            }

            transicoesDoEstado.put(transicao.getSimbolo(), transicao.getEstadoDestino());
        }

        return mapa;
    }

    private void validarAutomato() {
        for (String estado : estados) {
            for (String simbolo : alfabeto) {
                Map<String, String> transicoesDoEstado = transicoes.get(estado);
                if (transicoesDoEstado == null || !transicoesDoEstado.containsKey(simbolo)) {
                    throw new IllegalArgumentException(
                            "Autômato inválido: falta a transição do estado '" + estado + "' para o símbolo '" + simbolo + "'."
                    );
                }
                String proximoEstado = transicoesDoEstado.get(simbolo);
                if (!estados.contains(proximoEstado)) {
                    throw new IllegalArgumentException(
                            "Autômato inválido: a transição do estado '" + estado + "' com o símbolo '" + simbolo + "' aponta para um estado inexistente: " + proximoEstado
                    );
                }
            }
        }

        for (Map<String, String> transicoesDoEstado : transicoes.values()) {
            Set<String> simbolos = transicoesDoEstado.keySet();
            if (simbolos.size() != alfabeto.size()) {
                throw new IllegalArgumentException(
                        "Autômato inválido: transições não determinísticas ou incompletas em um estado."
                );
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AFD outro)) {
            return false;
        }
        return Objects.equals(estados, outro.estados)
                && Objects.equals(alfabeto, outro.alfabeto)
                && Objects.equals(estadoInicial, outro.estadoInicial)
                && Objects.equals(estadosAceitacao, outro.estadosAceitacao)
                && Objects.equals(transicoes, outro.transicoes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(estados, alfabeto, estadoInicial, estadosAceitacao, transicoes);
    }

    @Override
    public String toString() {
        return "AFD{" +
                "estados=" + estados +
                ", alfabeto=" + alfabeto +
                ", inicial=" + estadoInicial +
                ", aceitacao=" + estadosAceitacao +
                ", transicoes=" + transicoes +
                '}';
    }
}