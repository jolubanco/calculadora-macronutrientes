# Calculadora de MacroNutrientes para Dietas

## Sobre
Esta é uma API REST desenvolvida com o objetivo de contabilizar tanto as calorias gastas no dia-a-dia, como a a distribuição entre os macronutrientes:
carboidratos, proteínas e gorduras. A partir de informações inicias como altura, peso e idade a API calculará a Taxa de Metabolismo Basal do indivíduo
e irá sugerir uma distribuição dos macrosnutrientes levando em consideração o objetivo do mesmo.

### Fator de Atividade Física  
Na hora de informar qual o fator de atividade física, se basear nas seguintes categorias.

  - <b>Sedentários</b> (pouco ou nenhum exercício) fator: 1.2
  - <b>Levemente ativo</b> (exercício leve 1 a 3 dias por semana) fator: 1.375
  - <b>Moderadamente ativo</b> (exercício moderado, faz esportes 3 a 5 dias por semana) fator: 1.55
  - <b>Altamente ativo</b> (exercício pesado de 5 a 6 dias por semana) fator: 1.725
  - <b>Extremamente ativo</b> (exercício pesado diariamente e até 2 vezes por dia) fator: 1.9

## Funcionalidades
O usuário poderá cadastrar:
  - um perfil
  - informar seu objetivo com o treinamento e dieta
  - uma lista de refeições diárias
  - uma lista de alimentos
  - uma lista de exercícios
  - utilizar a sugestão de macronutrientes ou definir a sua própria distribuição

## Fluxo ideal
Não necessáriamente o fluxo de cadastro deve seguir essa ordem, porém nos casos de cadastro de alimento e exercício, sempre é necessário cadastrar anteriormente os alimentos e exercícios de domínio.

  1. Cadastro do usuário
  2. Cadastro da refeição
  3. Cadastro do alimento de domínio
  4. Cadastro do alimento
  5. Adicionar o alimento cadastrado na refeição
  6. Adicionar a refeição cadastrado no usuário
  7. Cadastro do exercício de domínio
  8. Cadastro do exercicio
  9. Adicionar o exercício cadastrado no usuário

## Novas Funcionalidades
  - Cobertura de testes
  - Adicionar segurança, para que o usuário só possa realizar as ações através de um token
  - Permitir que o usuário informe qual os valores de calorias gostaria de usar para cada tipo de objetivo (atualmente está definido como 500kcal)
  - Permitir que o usuário crie uma plano nutricional diário
