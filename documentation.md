# Documentacao do projeto

## Visao geral

Este projeto e um jogo desktop feito com Java e libGDX, gerado a partir do template do gdx-liftoff. O nome configurado do projeto e `FruitNinjaMicrocontrolador`.

A ideia atual do jogo e inspirada em um estilo de pontuacao por acerto: o jogador sai de um menu inicial, entra na fase principal e interage com personagens que sobem pela tela. Acertar fantasmas comuns aumenta a pontuacao, enquanto acertar fantasmas vermelhos ou bombas reduz a pontuacao.

## Historico de alteracoes

### Alteracoes feitas no projeto

- O projeto foi estruturado como uma aplicacao Java com libGDX.
- Foram mantidos dois modulos principais:
  - `core`, com a logica do jogo.
  - `lwjgl3`, com a execucao desktop.
- Foi configurado o Gradle Wrapper para compilar, executar e gerar o `.jar` do jogo.
- Foram adicionados assets para o jogo:
  - logo do menu
  - fundo da fase
  - fantasma comum
  - fantasma vermelho
  - bomba
- Foi criada a tela de menu inicial.
- Foi adicionado o botao textual `INICIAR`.
- Foi implementada a mudanca de estado do menu para o jogo.
- Foi criada a primeira fase do jogo.
- Foram adicionados objetos que sobem pela tela.
- Foi criado o sistema de pontuacao.
- Foi adicionada penalidade para fantasma vermelho.
- Foi adicionada penalidade maior para bomba.
- Foi adicionada exibicao da pontuacao na tela.
- Foi implementado aumento de velocidade conforme a pontuacao cresce.
- Foi configurado o jogo para abrir em tela cheia no launcher desktop.
- Foi criada esta documentacao em `documentation.md`.

### Atualizacao da documentacao

Este arquivo foi criado para registrar tudo o que ja existe no projeto ate agora. Depois, foi atualizado para incluir esta secao de historico de alteracoes, deixando mais facil acompanhar o que ja foi feito e o que ainda pode ser melhorado.

### Ideia analisada para alteracao futura

Foi avaliada a possibilidade de usar GIFs no lugar das imagens estaticas do jogo. A recomendacao tecnica para libGDX e nao usar o GIF animado diretamente como `Texture`, mas converter o GIF para frames `.png` ou para uma spritesheet e usar `Animation<TextureRegion>`.

Essa alteracao ainda nao foi aplicada no codigo. Ela fica registrada como possibilidade futura para animar os personagens e outros elementos visuais.

## Estrutura do projeto

- `README.md`: documentacao original gerada pelo template libGDX.
- `settings.gradle`: declara os modulos `core` e `lwjgl3`.
- `build.gradle`: configuracao Gradle geral do projeto, incluindo repositorios, compatibilidade Java 8 e geracao automatica da lista de assets.
- `gradle.properties`: define versoes e propriedades principais do projeto, como `gdxVersion=1.14.0`, `lwjgl3Version=3.4.1` e `projectVersion=1.0.0`.
- `core/`: modulo principal, com a logica compartilhada do jogo.
- `lwjgl3/`: modulo desktop, responsavel por iniciar o jogo usando LWJGL3.
- `assets/`: imagens usadas no menu e na fase.

## Assets adicionados

Os assets atuais ficam na pasta `assets/`:

- `assets/logo/logo.png`: logo exibida no menu inicial e usada como icone da janela.
- `assets/fazes/fundo1.png`: imagem de fundo da primeira fase.
- `assets/personas/fantasma.png`: fantasma comum, usado como alvo positivo.
- `assets/personas/fantasmaVermelho.png`: fantasma vermelho, usado como alvo negativo.
- `assets/personas/bomba.png`: bomba, usada como alvo negativo com penalidade maior.

## Modulo core

O modulo `core` contem as classes principais do jogo:

- `Main.java`
- `Menu.java`
- `faze1.java`

### Main.java

A classe `Main` estende `ApplicationAdapter`, que e a base da aplicacao libGDX.

O que ja foi implementado:

- Criacao de um `SpriteBatch` para desenhar os elementos na tela.
- Criacao do menu inicial por meio da classe `Menu`.
- Criacao da primeira fase por meio da classe `faze1`.
- Controle simples de estado do jogo com o enum `GameState`, contendo:
  - `MENU`
  - `GAME`
- Inicio do jogo no estado `MENU`.
- Troca do estado `MENU` para `GAME` quando o jogador clica no botao do menu.
- Renderizacao da pontuacao durante a fase principal.
- Liberacao de recursos no metodo `dispose()`.

### Menu.java

A classe `Menu` controla a tela inicial.

O que ja foi implementado:

- Carregamento da textura `logo/logo.png`.
- Criacao de uma fonte branca com escala maior.
- Texto do botao definido como `INICIAR`.
- Calculo da posicao do botao para centraliza-lo horizontalmente.
- Desenho da logo na parte superior da tela.
- Desenho do texto `INICIAR`.
- Metodo `isButtonClicked()` para detectar clique no texto do botao.
- Conversao da coordenada Y do mouse da tela para coordenada do mundo libGDX.
- Liberacao da textura e da fonte no metodo `dispose()`.

### faze1.java

A classe `faze1` concentra a logica da primeira fase.

O que ja foi implementado:

- Carregamento do fundo da fase: `fazes/fundo1.png`.
- Carregamento dos personagens:
  - fantasma comum
  - fantasma vermelho
  - bomba
- Criacao de arrays para controlar posicao e velocidade dos objetos.
- Quatro fantasmas comuns aparecem na fase.
- Dois fantasmas vermelhos aparecem na fase.
- Duas bombas aparecem na fase.
- Os elementos sobem pela tela com velocidade baseada em `deltaTime`.
- Quando um elemento sai pelo topo da tela, ele volta para uma posicao abaixo da tela.
- O fundo e desenhado ocupando toda a tela.
- O tamanho dos personagens e calculado com base na largura da tela.
- Existe um sistema de pontuacao.
- Existe aumento gradual de velocidade conforme a pontuacao cresce.

## Regras de pontuacao

As regras atuais sao:

- Acertar um fantasma comum aumenta a pontuacao em `+1`.
- Acertar um fantasma vermelho reduz a pontuacao em `-5`.
- Acertar uma bomba reduz a pontuacao em `-12`.
- A pontuacao nunca fica abaixo de zero, pois as penalidades usam `Math.max(0, score - valor)`.
- A velocidade aumenta a cada 15 pontos:
  - multiplicador base: `1.0`
  - acrescimo: `+0.2` a cada 15 pontos

## Colisoes e interacao

Atualmente a interacao acontece pela posicao do mouse.

O metodo `checkMouseCollision()` verifica se o mouse esta dentro da area retangular de cada objeto:

- Se estiver sobre um fantasma comum, o fantasma e reposicionado e a pontuacao aumenta.
- Se estiver sobre um fantasma vermelho, ele e reposicionado e a pontuacao diminui.
- Se estiver sobre uma bomba, ela e reposicionada e a pontuacao diminui mais.

Observacao: a verificacao usa a posicao do mouse continuamente durante o jogo, nao apenas no momento exato de clique.

## Posicionamento dos objetos

Os fantasmas comuns sao posicionados aleatoriamente dentro da largura da tela.

Os fantasmas vermelhos e as bombas usam o metodo `randomSafeX()`, que tenta escolher uma posicao horizontal sem ficar muito perto de outros objetos. O metodo faz ate 50 tentativas para evitar um loop infinito.

## Modulo desktop LWJGL3

O modulo `lwjgl3` e responsavel por executar o jogo no desktop.

### Lwjgl3Launcher.java

O que ja foi implementado:

- Inicializacao da aplicacao desktop com `Lwjgl3Application`.
- Uso da classe `Main` como aplicacao principal.
- Titulo da janela configurado como `FruitNinjaMicrocontrolador`.
- VSync ativado.
- FPS limitado com base na taxa de atualizacao do monitor.
- Modo tela cheia configurado com o display atual.
- Icone da janela configurado usando `logo/logo.png`.
- Uso do `StartupHelper` antes de iniciar a aplicacao.

### StartupHelper.java

Arquivo auxiliar gerado pelo template para melhorar compatibilidade do LWJGL3 em diferentes sistemas.

Ele trata casos especificos como:

- Windows: evita problemas de carregamento de bibliotecas nativas em caminhos com caracteres problematicos.
- macOS: reinicia a JVM com `-XstartOnFirstThread`, quando necessario.
- Linux com NVIDIA: ajusta a variavel `__GL_THREADED_OPTIMIZATIONS`, quando necessario.

## Build e execucao

O projeto usa Gradle Wrapper.

Comandos principais:

```bat
gradlew.bat lwjgl3:run
```

Executa o jogo desktop.

```bat
gradlew.bat build
```

Compila o projeto.

```bat
gradlew.bat lwjgl3:jar
```

Gera um arquivo `.jar` executavel na pasta `lwjgl3/build/libs`.

Tambem existem tarefas especificas para gerar JARs por sistema:

- `jarWin`
- `jarLinux`
- `jarMac`

## O que esta pronto ate agora

- Projeto libGDX configurado com Gradle.
- Modulo `core` criado para a logica principal.
- Modulo `lwjgl3` configurado para rodar no desktop.
- Tela inicial com logo e botao `INICIAR`.
- Transicao do menu para a fase principal.
- Primeira fase com fundo proprio.
- Fantasmas comuns com movimento vertical.
- Fantasmas vermelhos com movimento vertical e penalidade.
- Bombas com movimento vertical e penalidade maior.
- Sistema basico de pontuacao.
- Exibicao da pontuacao na tela.
- Aumento de dificuldade por velocidade conforme a pontuacao aumenta.
- Reposicionamento automatico dos objetos quando saem da tela.
- Liberacao de recursos graficos com `dispose()`.
- Configuracao para execucao em tela cheia.
- Assets organizados em pastas.

## Pontos que ainda podem ser melhorados

- Padronizar o nome da classe `faze1` para seguir a convencao Java, por exemplo `Fase1`.
- Melhorar o botao do menu com uma area visual maior, nao apenas o texto.
- Decidir se a colisao deve acontecer apenas com clique/toque ou enquanto o mouse passa por cima dos objetos.
- Adicionar tela de fim de jogo ou condicao de vitoria/derrota.
- Adicionar vidas, tempo limite ou outra regra de progressao.
- Adicionar sons e efeitos visuais de acerto.
- Adicionar testes, se o projeto crescer em logica.
- Verificar se o modo tela cheia e desejado para todas as execucoes ou se deve haver uma resolucao em janela durante o desenvolvimento.

## Observacoes finais

No estado atual, o projeto ja possui a base jogavel: menu, fase, alvos positivos, obstaculos negativos e pontuacao. A proxima etapa natural e refinar a interacao do jogador, melhorar o feedback visual/sonoro e organizar melhor a classe da fase para facilitar futuras fases ou novos tipos de objetos.
