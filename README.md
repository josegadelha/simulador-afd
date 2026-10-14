# Simulador de Autômatos Finitos Determinísticos (AFD)

Este projeto implementa um simulador de AFD em Java, com validação de autômatos e avaliação de cadeias de entrada.

## Objetivo

O programa permite ao usuário informar:

- os estados do autômato;
- o alfabeto;
- a função de transição;
- o estado inicial;
- os estados de aceitação.

## Requisitos

- Java 21 ou superior
- Maven (opcional, só se você quiser usar o projeto com o padrão Maven)

## Estrutura do projeto

O projeto está organizado em classes separadas:

- `Main`: ponto de entrada da aplicação
- `LeitorAutomato`: leitura do autômato pelo console
- `AFD`: representação do AFD e validação
- `Transicao`: representação de uma transição

## Como informar o autômato


```text

=== Montagem do AFD ===
Informe os estados (separados por vírgula): q0,q1
Informe o alfabeto (máx. 2 símbolos, separados por vírgula): 0,1
Informe o estado inicial: q0
Informe os estados de aceitação (separados por vírgula): q1
Informe as transições em formato 'estado,simbolo,estado' e digite 'fim' para encerrar.
Transição: q0,0,q1
Transição: q0,1,q0
Transição: q1,0,q1
Transição: q1,1,q0
Transição: fim

AFD válido: AFD{estados=[q0, q1], alfabeto=[0, 1], inicial=q0, aceitacao=[q1], transicoes={...}}
```

Esse autômato reconhece cadeias que terminam em `0`.

## Como testar cadeias

Depois de montar o AFD, o programa entra em modo de teste:

```text
Digite uma cadeia para testar (ou 'sair' para encerrar):
0
ACEITA

Digite uma cadeia para testar (ou 'sair' para encerrar):
1
REJEITA

Digite uma cadeia para testar (ou 'sair' para encerrar):
010
ACEITA

Digite uma cadeia para testar (ou 'sair' para encerrar):
sair
```

## Observações importantes

- O alfabeto foi limitado a até 2 símbolos.
- O programa rejeita autômatos inválidos, como:
  - estados duplicados;
  - alfabeto inválido;
  - transições duplicadas;
  - estados de origem ou destino inexistentes;
  - ausência de transição para algum símbolo em algum estado.
- O programa considera que uma cadeia é aceita se, ao final da leitura, o estado atual for um estado de aceitação.

## Exemplos de autômatos

### Exemplo 1: aceita cadeias que terminam com 0

```text
ESTADOS: q0,q1
ALFABETO: 0,1
INICIAL: q0
ACEITACAO: q1
TRANSICOES:
q0,0,q1
q0,1,q0
q1,0,q1
q1,1,q0
```

Cadeias aceitas:

- `0`
- `10`
- `010`

Cadeias rejeitadas:

- `1`
- `11`
- `101`

### Exemplo 2: aceita somente a cadeia vazia

```text
ESTADOS: q0
ALFABETO: 0,1
INICIAL: q0
ACEITACAO: q0
TRANSICOES:
q0,0,q0
q0,1,q0
```

Nesse caso, todas as cadeias sobre o alfabeto são aceitas, pois o único estado é de aceitação e as transições permanecem nele.

### Exemplo 3: aceita cadeias com quantidade par de `1`

Informe os dados no console nesta ordem:

```text
Estados: q0,q1
Alfabeto: 0,1
Estado inicial: q0
Estados de aceitação: q0
Transições:
q0,0,q0
q0,1,q1
q1,0,q1
q1,1,q0
fim
```

O estado `q0` representa uma quantidade par de `1` lidos; `q1`, uma quantidade ímpar. O símbolo `0` não altera essa contagem.

Cadeias aceitas:

- `vazia` (pressione Enter sem digitar símbolos)
- `0`
- `11`
- `1010`

Cadeias rejeitadas:

- `1`
- `10`
- `111`

## Dicas de uso

- Use nomes curtos para estados como `q0`, `q1`, `q2`.
- Use símbolos simples do alfabeto, como `0` e `1`.
- Sempre informe todas as transições de cada estado para cada símbolo.
- Para cadeias em branco, o programa aceita apenas se o estado inicial for de aceitação.
