# Sistema de Faturas e Controle de Estoque

Aplicação Java via terminal para cadastrar produtos, controlar quantidades em estoque e compor faturas associadas a um cliente. O projeto nasceu de uma atividade acadêmica de Programação Orientada a Objetos e está evoluindo como parte de um portfólio de desenvolvimento.

O foco é aplicar modelagem de domínio, encapsulamento, validações e testes automatizados em um fluxo comercial simples, documentando as decisões e a evolução da solução.

**Status:** aplicação funcional em memória, com menus de estoque e fatura. A próxima entrega planejada é a persistência dos cadastros dinâmicos de clientes e estoque em JSON.

> A aplicação simula faturas comerciais para fins de estudo. Não realiza emissão de documentos fiscais oficiais.

## Funcionalidades atuais

- Cadastro de produtos com código sequencial, nome, NCM, valor e quantidade.
- Confirmação dos dados antes da inclusão de um novo produto.
- Reposição de produtos existentes pelo nome, desconsiderando diferenças entre maiúsculas e minúsculas.
- Listagem do estoque e resumo de produtos distintos e unidades disponíveis.
- Composição de fatura com seleção de produtos por código e quantidade.
- Agrupamento do mesmo produto em um único item da fatura.
- Retirada do estoque ao adicionar itens à fatura.
- Remoção parcial ou total da quantidade de um item, com devolução ao estoque.
- Cálculo de subtotais e do valor total da fatura.
- Impressão no terminal com dados do cliente, produtos, quantidades e valores.
- Validação e formatação de CNPJ numérico, incluindo dígitos verificadores.
- Validação de razão social obrigatória e exigência de pelo menos um contato.
- Testes automatizados com JUnit.

A validação de CNPJ é local e matemática; não consulta a existência ou a situação cadastral da empresa.

## Tecnologias e requisitos

| Tecnologia | Uso |
|---|---|
| Java / JDK 25 | Referência de ambiente para compilação e execução |
| Java Collections | Armazenamento de produtos e itens em memória |
| JUnit Jupiter | Testes automatizados |
| JUnit Platform Console Standalone 1.14.2 | Biblioteca disponível em `lib` para compilar e executar testes |
| Git e GitHub | Versionamento e apresentação do projeto |
| VS Code | Editor opcional, com configurações incluídas no repositório |

O projeto ainda não utiliza Maven ou Gradle. Os comandos abaixo consideram Windows com PowerShell, JDK disponível no `PATH` e arquivos salvos em UTF-8. A limpeza de tela utiliza sequências ANSI; prefira um terminal compatível.

## Como executar

Clone ou baixe o repositório e abra um terminal na pasta `Projeto_Fatura`, onde estão `src` e `lib`.

Confira o JDK:

```powershell
java -version
javac -version
```

Compile a aplicação:

```powershell
javac -encoding UTF-8 -d bin src/model/*.java src/ui/*.java src/Main.java
```

Execute:

```powershell
java -cp bin Main
```

No VS Code, abra a pasta raiz do projeto, utilize as extensões de Java e execute o método `main` de `src/Main.java` no terminal integrado.

### Fluxo de uso

1. No menu de estoque, cadastre um produto e confirme os dados.
2. Consulte a listagem ou o resumo do estoque.
3. Escolha `0 - Voltar` para sair do menu de estoque e avançar ao menu da fatura.
4. Adicione produtos à fatura informando código e quantidade.
5. Visualize a fatura ou remova quantidades dos itens.
6. Escolha `0 - Voltar` no menu da fatura para encerrar a execução atual.

No fluxo atual, `Main` cria um cliente de demonstração e uma fatura com código e data fixos. Ainda não há menu de cadastro de clientes ou gerenciamento de múltiplas faturas.

### Exemplo de operação

Exemplo ilustrativo das regras implementadas:

| Operação | Saldo em estoque | Quantidade na fatura | Total da fatura |
|---|---:|---:|---:|
| Cadastrar 5 unidades a R$ 8,00 | 5 | 0 | R$ 0,00 |
| Adicionar 3 unidades à fatura | 2 | 3 | R$ 24,00 |
| Remover 1 unidade da fatura | 3 | 2 | R$ 16,00 |

## Testes automatizados

A suíte atual está em `src/TestesSistema.java` e contém 14 testes. Os cenários abrangem validação de cliente e CNPJ, valor de produto, agrupamento, geração de códigos, retirada e devolução ao estoque, remoção parcial e cópias de produtos.

Compile aplicação e testes:

```powershell
javac -encoding UTF-8 -cp "lib/*" -d bin src/model/*.java src/ui/*.java src/Main.java src/TestesSistema.java
```

Execute a classe de testes:

```powershell
java -jar lib/junit-platform-console-standalone-1.14.2.jar execute --class-path bin --select-class TestesSistema
```

A seleção explícita de `TestesSistema` evita depender das convenções de nome usadas pela descoberta automática. A suíte cobre os cenários existentes, mas ainda precisa de novos casos para saldo zero, persistência e integridade histórica.

## Organização do projeto

```text
Projeto_Fatura/
├── .vscode/
│   ├── launch.json
│   └── settings.json
├── lib/
│   └── junit-platform-console-standalone-1.14.2.jar
├── src/
│   ├── model/
│   │   ├── Cliente.java
│   │   ├── Cnpj.java
│   │   ├── Estoque.java
│   │   ├── Fatura.java
│   │   ├── ItemFatura.java
│   │   └── Produto.java
│   ├── ui/
│   │   ├── EstoqueMenu.java
│   │   ├── FaturaMenu.java
│   │   └── Terminal.java
│   ├── Main.java
│   └── TestesSistema.java
├── .gitignore
└── README.md
```

A pasta `bin` é gerada pela compilação e deve permanecer fora do versionamento.

### Responsabilidades e decisões

- **`model`:** representa os dados e concentra as regras de cliente, produto, estoque e fatura.
- **`ui`:** recebe entradas e apresenta os menus no terminal.
- **`Main`:** cria os objetos iniciais e coordena a sequência dos menus.
- **`Cnpj`:** objeto de valor imutável com validação, formatação e comparação por conteúdo.
- **`ItemFatura`:** representa o produto e a quantidade da operação, calculando seu subtotal.
- **`Estoque`:** atribui códigos e controla as quantidades; consultas retornam cópias dos produtos.

A separação de responsabilidades está em evolução: atualmente, `Fatura` também imprime dados e coordena movimentações do estoque. SOLID e Object Calisthenics são referências para revisão e aprendizado, sem pressupor adesão integral no estado atual.

## Limitações atuais

- Os dados existem somente durante a execução; não há persistência em arquivo ou banco.
- O CRUD de clientes e produtos ainda não está completo.
- Valores monetários usam `double`, e a data da fatura usa `String`.
- Faturas continuam mutáveis e não possuem etapas formais de emissão e cancelamento ou histórico auditável.
- A lista retornada por `Fatura.getItens()` não permite alterações estruturais, mas seus itens continuam mutáveis.
- O formato de e-mail, celular e NCM ainda não recebe validação específica.
- **Saldo zero:** a retirada pode zerar o produto, mas `criarCopia()` passa pelo construtor que exige quantidade positiva. Consultas posteriores podem falhar; esse caso precisa ser corrigido antes da persistência do estoque.

## Próximas etapas

Os itens abaixo são propostas de evolução, ainda não implementadas. A prioridade combina três ajustes pequenos com a próxima entrega funcional: persistência dinâmica em JSON.

### Prioridade — três melhorias de aplicação simples

- [ ] **Corrigir a normalização de campos opcionais de `Cliente`:** armazenar o resultado de `normalizarCampoOpcional()` em `setEndereco()` e `setInscricaoEstadual()`, mantendo o mesmo comportamento do construtor.
- [ ] **Remover a validação duplicada de contato:** manter uma única validação no caminho de inicialização, pois `definirContato()` já verifica e-mail e celular.
- [ ] **Padronizar nomes e formatação:** substituir `scan` por `scanner` e ajustar a indentação dos menus, sem alterar as regras de negócio.

### Prioridade — próxima entrega: persistência dinâmica em JSON

Salvar e restaurar o estado atual de clientes e estoque será o próximo foco de desenvolvimento. A persistência de faturas históricas e dos eventos de auditoria ficará para uma etapa posterior.

- [ ] Corrigir a representação de saldo zero, distinguindo saldo válido de quantidade positiva exigida em uma movimentação, e adicionar um teste de regressão.
- [ ] Criar uma coleção de clientes identificáveis, substituindo progressivamente o cliente fixo de demonstração.
- [ ] Definir arquivos modulares, como `dados/clientes.json` e `dados/estoque.json`, e uma versão para seu formato.
- [ ] Criar uma camada de persistência separada das entidades e dos menus; escolher e configurar uma biblioteca JSON.
- [ ] Carregar os cadastros na inicialização e definir a gravação das alterações confirmadas.
- [ ] Preservar códigos, saldos e a sequência de identificação dos produtos ao reabrir o sistema.
- [ ] Tratar primeiro uso sem arquivo, conteúdo inválido e falha de gravação sem substituir silenciosamente dados existentes.
- [ ] Usar escrita temporária e substituição segura para reduzir o risco de arquivos incompletos.
- [ ] Testar gravação, leitura e restauração após reiniciar, incluindo estoque zerado e preservação dos identificadores.
- [ ] Manter dados reais de execução fora do Git e fornecer exemplos fictícios quando necessário.

Persistência e CRUD serão desenvolvidos de forma incremental. A primeira entrega não precisa aguardar a implementação de todo o histórico de transações.

### Evolução funcional e arquitetural

- [ ] Completar o CRUD de clientes e produtos, incluindo desativação de registros necessários ao histórico.
- [ ] Separar cadastro de produto e movimentação de estoque, com operações explícitas de entrada, saída e ajuste.
- [ ] Migrar datas para `LocalDate` e valores monetários para `BigDecimal`.
- [ ] Reforçar validações de contato, NCM e identidade das entidades; avaliar suporte a CNPJ alfanumérico.
- [ ] Restringir alterações externas em itens e produtos que possam invalidar os totais ou saldos.
- [ ] Separar impressão de faturas e coordenação das operações de negócio.
- [ ] Adotar Maven ou Gradle e organizar os testes por responsabilidade.
- [ ] Automatizar a execução dos testes com GitHub Actions.
- [ ] Introduzir interfaces de repositório para permitir persistência JSON e, posteriormente, banco de dados sem espalhar decisões de armazenamento pelas entidades.
- [ ] Avaliar e aplicar um design pattern GoF quando houver um problema concreto de extensão; registrar a justificativa, os benefícios e os custos. Repository é uma opção de organização da persistência, mas não pertence ao catálogo GoF.

### Histórico de faturas e auditoria

A direção planejada é manter **cadastros dinâmicos para o estado atual** e **registros históricos para as transações anteriores**.

- [ ] Definir o ciclo de vida da fatura: elaboração, emissão e cancelamento.
- [ ] Permitir alterações durante a elaboração e preservar o conteúdo após a emissão.
- [ ] Guardar snapshots dos dados do cliente e dos produtos usados na transação, incluindo preços, quantidades e totais.
- [ ] Persistir faturas emitidas para consulta histórica, sem depender dos valores atuais dos cadastros.
- [ ] Registrar cancelamentos com motivo e eventual movimentação compensatória de estoque, preservando o documento original.
- [ ] Manter logs de alterações em clientes, produtos e estoque separados dos arquivos que representam o estado atual.
- [ ] Registrar identificador, data e hora, operação, registro afetado, motivo e valores anteriores e novos quando pertinentes; incluir responsável quando houver identificação de usuários.
- [ ] Vincular eventos de auditoria às faturas e movimentações correspondentes.
- [ ] Definir recuperação de falhas entre gravação do estado e do histórico, evitando alterações sem o respectivo evento.

Essa proposta não exige Event Sourcing. O histórico complementará os cadastros; um arquivo de log editável, isoladamente, não garante proteção contra adulteração.

### Persistência em banco de dados

- [ ] Modelar clientes, produtos, saldos, movimentações, faturas, itens e eventos de auditoria.
- [ ] Implementar uma alternativa relacional à persistência JSON, com banco a definir.
- [ ] Usar integridade referencial e transações para manter operações relacionadas consistentes.
- [ ] Planejar migrações de estrutura, importação dos dados JSON e testes de integração.

JSON será a primeira implementação. Banco de dados será uma evolução posterior, sem compromisso inicial de gravar simultaneamente nas duas opções.

## Contexto acadêmico e portfólio

O projeto parte de um exercício de modelagem de cliente, produto e fatura e amplia o escopo com controle de estoque, menus e testes. A evolução busca demonstrar raciocínio sobre regras de negócio, manutenção de código e integridade dos dados.

Os estudos de orientação a objetos, padrões GoF e Git/GitHub acompanham o desenvolvimento. O plano pessoal de estudos é mantido separado deste README; aqui, o foco é o funcionamento do projeto e suas próximas entregas.
