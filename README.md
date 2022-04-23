# Calculadora de MacroNutrientes para Dietas

## Sobre
Esta é uma API REST desenvolvida com o objetivo de contabilizar tanto as calorias gastas no dia-a-dia, como a a distribuição entre os macronutrientes:
carboidratos, proteínas e gorduras. A partir de informações inicias como altura, peso e idade a API calculará a Taxa de Metabolismo Basal do indivíduo
e irá sugerir uma distribuição dos macrosnutrientes levando em consideração o objetivo do mesmo.

## Funcionalidades
O usuáriio poderá cadastrar:
  - um perfil
  - informar seu objetivo com o treinamento e dieta
  - uma lista de refeições diárias
  - uma lista de alimentos
  - uma lista de exercícios
  - utilizar a sugestão de macronutrientes um definir a sua própria distribuição
  
## Fator de Atividade Física  

  - Sedentários (pouco ou nenhum exercício) fator = 1.2
  - Levemente ativo (exercício leve 1 a 3 dias por semana) fator = 1.375
  - Moderadamente ativo (exercício moderado, faz esportes 3 a 5 dias por semana) fator = 1.55
  - Altamente ativo (exercício pesado de 5 a 6 dias por semana) fator = 1.725
  - Extremamente ativo (exercício pesado diariamente e até 2 vezes por dia) fator = 1.9

## Novas Funcionalidades
 - Refatorar a forma como é calculada a quantidade de calorias diárias necessárias para levar em consideração o fator de exercicio e ter um valor mais 
fiel . [Exemplo](https://www.tuasaude.com/como-calcular-o-gasto-calorico/#:~:text=Exemplo%20de%20c%C3%A1lculo%20de%20ingest%C3%A3o%20cal%C3%B3rica%20di%C3%A1ria&text=Gasto%20energ%C3%A9tico%20basal%3A%20])

## Payload das requisições disponíveis

### Json completo do usuário
```json
{
    "id": 1,
    "nome": "Joao",
    "objetivo": "PERDA_PESO",
    "necessidadeDiariaCalorias": 2383.3875000000003,
    "informacoesUsuario": {
        "peso": 80.5,
        "altura": 175.0,
        "idade": 27,
        "sexo": "MASCULINO"
    },
    "distribruicaoMacros": {
        "carboidratoDisponiveis": 241.5,
        "proteinaDisponiveis": 161.0,
        "gorduraDisponiveis": 80.5,
        "consumoCaloriasDisponivel": 2583.3875000000003
    },
    "refeicoes": [
        {
            "id": 1,
            "nome": "jantar",
            "caloriasTotais": 0.0,
            "dataCriacao": "2022-04-22"
        }
    ],
    "exercicios": [
        {
            "id": 1,
            "modalidade": "musculacao",
            "caloriaGasta": 200.0
        }
    ]
}
```

### Cadastro de um usuario 
#### Request
```
POST: /usuarios
```
```json
{
    "nome":"Joao",
    "objetivo":"PERDA_PESO",
    "peso":"80.5",
    "altura":"175",
    "idade":"27",
    "sexo":"MASCULINO"
}
```
#### Response
```json
{
    "id": 1,
    "nome": "joao",
    "endereco": {
        "id": 1,
        "cep": "86046002",
        "logradouro": "Avenida Inglaterra",
        "complemento": "lado par",
        "bairro": "Igapó",
        "localidade": "Londrina",
        "uf": "PR"
    },
    "tarefas": []
}
```

### Cadastro de alimento de domínio
#### Request
```
POST: /alimentosDominio
```
```json
{
    "nome":"Frango Cozido",
    "quantidade":"100",
    "carboidrato":"0",
    "proteina":"31.5",
    "gordura":"4.3",
    "calorias":"162.9"
}
```
#### Response
```json
{
    "id": 1,
    "nome": "joao",
    "endereco": {
        "id": 1,
        "cep": "86046002",
        "logradouro": "Avenida Inglaterra",
        "complemento": "lado par",
        "bairro": "Igapó",
        "localidade": "Londrina",
        "uf": "PR"
    },
    "tarefas": []
}
```

### Cadastro de alimento do usuário
#### Request
```
POST: /alimentos
```
```json
{
    "id":1,
    "quantidadeInformada":300
}
```
#### Response
```json
{
    "id": 1,
    "nome": "joao",
    "endereco": {
        "id": 1,
        "cep": "86046002",
        "logradouro": "Avenida Inglaterra",
        "complemento": "lado par",
        "bairro": "Igapó",
        "localidade": "Londrina",
        "uf": "PR"
    },
    "tarefas": []
}
```

### Cadastro de refeição
#### Request
```
POST: /refeicoes
```
```json
{
    "nome":"Almoço"
}
```
#### Response
```json
{
    "id": 1,
    "nome": "joao",
    "endereco": {
        "id": 1,
        "cep": "86046002",
        "logradouro": "Avenida Inglaterra",
        "complemento": "lado par",
        "bairro": "Igapó",
        "localidade": "Londrina",
        "uf": "PR"
    },
    "tarefas": []
}
```

### Adicionando um alimento cadastrado em uma refeição cadastrada
#### Request
```
POST: /refeicoes/{idRefeicao}/addAlimento/{idAlimento}
```

#### Response
```json
{
    "id": 1,
    "nome": "joao",
    "endereco": {
        "id": 1,
        "cep": "86046002",
        "logradouro": "Avenida Inglaterra",
        "complemento": "lado par",
        "bairro": "Igapó",
        "localidade": "Londrina",
        "uf": "PR"
    },
    "tarefas": []
}
```

### Adicionando uma refeição cadastrada para um usuario cadastrado
#### Request
```
POST: /usuarios/{idUsuario}/addRefeicao/{idRefeicao}
```

#### Response
```json
{
    "id": 1,
    "nome": "joao",
    "endereco": {
        "id": 1,
        "cep": "86046002",
        "logradouro": "Avenida Inglaterra",
        "complemento": "lado par",
        "bairro": "Igapó",
        "localidade": "Londrina",
        "uf": "PR"
    },
    "tarefas": []
}
```
