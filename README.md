# Sistema de Faturas e Controle de Estoque

Aplicação de terminal desenvolvida em Java para cadastrar produtos, controlar o estoque e compor faturas associadas a um cliente. O projeto começou como atividade acadêmica de Programação Orientada a Objetos e está sendo desenvolvido também como peça de portfólio.

O foco é aplicar conceitos de orientação a objetos, separação de responsabilidades, validação de regras de negócio, testes automatizados e persistência de dados, evoluindo o sistema em etapas pequenas e verificáveis.

> A aplicação simula operações comerciais para fins de estudo. Não emite documentos fiscais oficiais.

## Funcionalidades

- Cadastro e consulta de produtos em memória, com código sequencial, nome, marca, NCM, valor e quantidade.
- Identificação de produtos pelo par nome e marca, ignorando diferenças entre maiúsculas e minúsculas.
- Reposição de produtos existentes e consulta do resumo do estoque.
- Composição de faturas com seleção de produtos por código e quantidade.
- Agrupamento de linhas referentes ao mesmo produto na fatura.
- Retirada do estoque ao adicionar itens à fatura e devolução ao remover quantidades.
- Cálculo de subtotais e do valor total da fatura.
- Impressão da fatura no terminal com os dados do cliente, produtos e valores.
- Validação e formatação de CNPJ, razão social e contato do cliente.
- Persistência do estoque e dos clientes em JSON: os arquivos são carregados na inicialização e salvos ao sair dos menus correspondentes e ao encerrar o programa.
- Testes automatizados para regras de domínio e gravação/leitura da persistência.

O estoque e os clientes cadastrados são persistidos em JSON. A fatura ainda é criada em memória com um cliente fixo de demonstração; faturas não são armazenadas como histórico. A validação de CNPJ é local e matemática, sem consulta à situação cadastral da empresa.

## Tecnologias e requisitos

| Tecnologia | Uso |
| --- | --- |
| Java / JDK 25 | Compilação e execução |
| Maven | Gerenciamento de dependências e execução dos testes |
| Gson 2.14.0 | Conversão dos DTOs de estoque e clientes para JSON e de volta |
| JUnit Jupiter 5.14.2 | Testes automatizados |
| Git e GitHub | Versionamento e apresentação do projeto |
| VS Code | Editor recomendado, com configurações do workspace incluídas |

É necessário ter um JDK compatível com Java 25 e Maven instalado e disponível no terminal. Confira as instalações com:

```powershell
java -version
mvn -version
```

## Como executar

Clone ou baixe o repositório e abra sua pasta raiz no VS Code. Use a extensão Java para executar `Main.java` pelo comando **Run Java** ou pelo botão **Run** do editor.

O Maven compila as classes e executa os testes com:

```powershell
mvn clean test
```

## Persistência de estoque e clientes

Na inicialização, o sistema procura `data/estoque.json` e `data/clientes.json`. Se um arquivo não existir, o sistema começa com o respectivo cadastro vazio. O estoque é salvo ao sair dos menus de estoque e fatura; os clientes são salvos ao sair do menu de clientes. Ambos também são salvos ao encerrar o programa. Os arquivos ficam na pasta `data/`, ignorada pelo Git.

Para conferir a persistência manualmente, cadastre um produto e um cliente, saia normalmente dos respectivos menus e execute o sistema outra vez; ambos deverão ser carregados dos arquivos JSON.

## Testes

Os testes ficam em `src/test/java`. A suíte cobre regras do modelo e do repositório de clientes, além dos ciclos de gravação e leitura JSON de estoque e clientes. Execute todos os testes a partir da raiz do projeto:

```powershell
mvn clean test
```

## Organização do projeto

```text
Projeto_Fatura/
├── .vscode/
├── data/                         # dados JSON gerados localmente; ignorados pelo Git
├── src/
│   ├── main/java/
│   │   ├── model/                # Cliente, Cnpj, Estoque, Fatura, ItemFatura, Produto
│   │   ├── persistence/          # implementações JSON de estoque e clientes
│   │   │   └── dto/              # objetos de transferência para os arquivos JSON
│   │   ├── repository/           # estrutura em evolução
│   │   ├── ui/                   # menus e operações de terminal
│   │   └── Main.java
│   └── test/java/                # testes JUnit
├── pom.xml
├── .gitignore
└── README.md
```

- **`model`:** representa os conceitos do sistema e concentra regras de domínio e validações.
- **`ui`:** recebe entradas e apresenta informações no terminal.
- **`persistence`:** converte os dados de estoque e clientes para DTOs e usa Gson para salvar e carregar JSON.
- **`persistence.dto`:** contém os dados usados na transferência entre o modelo e o arquivo.
- **`Main`:** inicia os menus; `MenuCentral` carrega os dados antes da navegação e solicita sua gravação ao sair dos menus e do programa.
- **`repository`:** área em evolução para separar contratos de repositório da implementação de armazenamento.

## Limitações atuais

- Estoque e clientes são persistidos; faturas ainda não são mantidas entre execuções.
- A fatura usa um cliente fixo de demonstração, ainda sem seleção a partir dos clientes cadastrados.
- O CRUD de produtos e clientes ainda não está completo.
- A fatura permanece mutável e não tem ciclo formal de emissão, cancelamento ou histórico auditável.
- Valores monetários são representados com `BigDecimal`, e a data da fatura com `LocalDate`.
- Validações específicas de e-mail, celular e NCM ainda podem ser ampliadas.
- O projeto ainda precisa definir recuperação para arquivos JSON inválidos e tornar a escrita mais resistente a interrupções.

## Próximas etapas

A implementação continua de forma incremental, com testes a cada etapa.

### Persistência e cadastros

- [ ] Adicionar testes de gravação e leitura JSON de clientes, incluindo dados opcionais e CNPJ.
- [ ] Concluir o CRUD de clientes e produtos, tratando nomes e marcas duplicados e definindo as regras de identificação dos registros.
- [ ] Implementar a exclusão definitiva de um produto e renumerar os códigos dos produtos posteriores, atualizando as referências relacionadas para preservar a integridade.
- [ ] Adicionar testes para arquivo inexistente, JSON inválido, falhas de leitura/gravação e restauração dos códigos.
- [ ] Avaliar escrita temporária e substituição segura do arquivo para reduzir o risco de dados incompletos.
- [ ] Separar os contratos de repositório das implementações concretas e manter os menus independentes do formato de armazenamento.

### Faturas e auditoria

- [ ] Definir o ciclo de vida da fatura: elaboração, emissão e cancelamento.
- [ ] Persistir faturas emitidas em JSON e carregá-las na inicialização, mantendo os registros históricos conforme as regras de imutabilidade que forem definidas.
- [ ] Armazenar faturas emitidas como registros históricos imutáveis, com cópias dos dados de cliente e produto usados na operação.
- [ ] Registrar alterações de clientes e estoque em logs de auditoria separados dos arquivos que representam o estado atual.
- [ ] Registrar operação, data/hora, registro afetado, motivo e valores anteriores e novos quando aplicável.

### Evolução técnica

- [ ] Validar os formatos de e-mail e telefone/celular informados nos cadastros.
- [ ] Ampliar outras validações e restringir alterações que possam invalidar saldos e totais.
- [ ] Separar a apresentação da fatura das regras de negócio.
- [ ] Automatizar testes no GitHub Actions.
- [ ] Avaliar persistência em banco de dados como alternativa posterior ao JSON.
- [ ] Aplicar um padrão GoF quando houver um problema concreto que justifique sua complexidade, documentando benefícios e custos.

O histórico de faturas e os logs de auditoria são uma evolução planejada; não implicam Event Sourcing. O Repository é uma opção arquitetural para organizar o acesso aos dados, mas não faz parte do catálogo GoF.

## Contexto acadêmico e portfólio

O sistema parte de um exercício de modelagem de cliente, produto e fatura e evolui com controle de estoque, menus, testes e persistência. O README registra o comportamento atual e as próximas entregas; o plano pessoal de estudos de orientação a objetos, padrões GoF e Git/GitHub é mantido separadamente.
