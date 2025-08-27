# 📚 BookFlow - Sistema de Gerenciamento de Biblioteca

**Projeto de uma Biblioteca desenvolvido em Java, aplicando os conceitos de Programação Orientada a Objetos (POO).**

---

## 🚀 Funcionalidades
O sistema foi construído para gerenciar de forma completa os processos de uma biblioteca escolar, incluindo as seguintes funcionalidades:
* Gestão de Obras Literárias: O sistema permite o cadastro, a edição e a exclusão de obras, como Livros, Revistas e Artigos. Cada obra possui atributos como código, título, autor, ano de publicação e status (disponível ou emprestado).
* Gestão de Usuários e Perfis de Acesso: É possível cadastrar, editar e excluir usuários, com diferentes tipos de perfis de acesso: Administrador, Bibliotecário e Estagiário.
* Gerenciamento de Empréstimos e Devoluções: O sistema associa uma obra a um usuário elegível. Calcula a data de devolução prevista com base no tipo da obra e registra o status da obra. Na devolução, é calculado se a devolução foi feita no prazo e, se necessário, é aplicada uma multa por atraso.
* Registro de Pagamentos de Multas: Permite registrar o pagamento de multas. Cada pagamento inclui ID, valor pago, data, método de pagamento e está associado a um usuário.
* Relatórios Gerenciais: O sistema é capaz de gerar relatórios em PDF para análises gerenciais, como relatórios de empréstimos do mês, obras mais emprestadas e usuários com mais atrasos.

---

## 🛠️ Tecnologias Utilizadas
* Java SE 17+: Linguagem principal para o desenvolvimento do sistema.
* Java Swing: Utilizado para a construção da interface gráfica do usuário (GUI).
* Gson: Biblioteca para a persistência de dados em formato JSON
* iText: Biblioteca para a geração dos relatórios gerenciais em formato PDF.
---

## 📦 Estrutura do Projeto
O projeto segue o padrão de arquitetura MVC (Model-View-Controller) e está organizado nos seguintes pacotes lógicos:
* model: Contém as classes que representam os dados e a lógica de negócio, como Usuario, Obra, Emprestimo e PagamentoMulta.
* dao: Camada de acesso a dados (Data Access Objects), responsável por salvar e recuperar os objetos em arquivos JSON.
* controller: Camada de controle, que atua como a ponte entre a interface do usuário (view) e a lógica do negócio (model), processando as requisições.
* view: Camada de visualização, com todas as classes de interface gráfica desenvolvidas com Java Swing.
---


## 💡 Conceitos de POO aplicados

* Classes e Objetos: O projeto é modelado em torno de classes como Usuario, Obra e Emprestimo, que representam as entidades do sistema.
* Herança: A classe Obra é abstrata, e Livro, Revista e Artigo herdam dela, implementando o método getTempoEmprestimo().
* Encapsulamento: O acesso aos atributos das classes é controlado por meio de modificadores de acesso (private), e a manipulação dos dados é feita através de métodos públicos (getters e setters).
* Polimorfismo: O conceito foi aplicado para tratar as obras de forma genérica em processos como empréstimos e listagens.

---

## 📝 Imagens 
<img width="992" height="695" alt="image" src="https://github.com/user-attachments/assets/55e0490c-2e4d-4115-8b38-4b4a302c1b95" />
<img width="985" height="693" alt="image" src="https://github.com/user-attachments/assets/dfa1757e-9ac6-4412-8f80-99a847ff25c6" />
---
> Projeto desenvolvido para fins acadêmicos na disciplina de Programação Orientada a Objetos.

