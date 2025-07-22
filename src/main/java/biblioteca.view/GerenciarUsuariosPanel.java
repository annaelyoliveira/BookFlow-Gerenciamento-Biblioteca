package biblioteca.view;

import biblioteca.controller.UsuarioController;
import biblioteca.model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GerenciarUsuariosPanel extends JPanel {
    private UsuarioController usuarioController;
    private JTable tabelaUsuarios;
    private DefaultTableModel modeloTabela;
    private JTextField campoPesquisa;
    private JButton botaoPesquisar;
    private JButton botaoEditar;
    private JButton botaoExcluir;
    private JLabel mensagemFeedback;

    private JButton btnMostrarLeitores;
    private JButton btnMostrarUsuariosSistema;

    private boolean mostrandoLeitores = false;

    public GerenciarUsuariosPanel() {
        this.usuarioController = new UsuarioController();
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel tituloPagina = new JLabel("Gerenciar Usuários", SwingConstants.CENTER);
        tituloPagina.setFont(new Font("Arial", Font.BOLD, 24));
        add(tituloPagina, BorderLayout.NORTH);

        JPanel painelControles = new JPanel();
        painelControles.setLayout(new BoxLayout(painelControles, BoxLayout.Y_AXIS));

        JPanel painelSelecao = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnMostrarLeitores = new JButton("Mostrar Leitores");
        btnMostrarUsuariosSistema = new JButton("Mostrar Usuários do Sistema");
        painelSelecao.add(btnMostrarLeitores);
        painelSelecao.add(btnMostrarUsuariosSistema);
        painelControles.add(painelSelecao);

        JPanel painelBarraPesquisa = new JPanel(new FlowLayout(FlowLayout.CENTER));
        campoPesquisa = new JTextField(20);
        painelBarraPesquisa.add(campoPesquisa);
        botaoPesquisar = new JButton("Pesquisar");
        painelBarraPesquisa.add(botaoPesquisar);
        painelControles.add(painelBarraPesquisa);

        JPanel painelBotoesAcao = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botaoEditar = new JButton("Editar");
        botaoExcluir = new JButton("Excluir");
        painelBotoesAcao.add(botaoEditar);
        painelBotoesAcao.add(botaoExcluir);
        painelControles.add(painelBotoesAcao);

        mensagemFeedback = new JLabel("");
        mensagemFeedback.setHorizontalAlignment(SwingConstants.CENTER);
        painelControles.add(mensagemFeedback);

        add(painelControles, BorderLayout.NORTH);

        modeloTabela = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaUsuarios = new JTable(modeloTabela);
        tabelaUsuarios.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(tabelaUsuarios);
        add(scrollPane, BorderLayout.CENTER);

        // Listeners para os botões de seleção
        btnMostrarLeitores.addActionListener(e -> {
            mostrandoLeitores = true;
            carregarUsuariosNaTabela(campoPesquisa.getText());
        });
        btnMostrarUsuariosSistema.addActionListener(e -> {
            mostrandoLeitores = false;
            carregarUsuariosNaTabela(campoPesquisa.getText());
        });

        botaoPesquisar.addActionListener(e -> carregarUsuariosNaTabela(campoPesquisa.getText()));
        botaoEditar.addActionListener(e -> editarUsuario());
        botaoExcluir.addActionListener(e -> excluirUsuario());

        botaoEditar.setEnabled(false);
        botaoExcluir.setEnabled(false);

        tabelaUsuarios.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabelaUsuarios.getSelectedRow() != -1) {
                botaoEditar.setEnabled(true);
                botaoExcluir.setEnabled(true);
            } else {
                botaoEditar.setEnabled(false);
                botaoExcluir.setEnabled(false);
            }
        });
        mostrandoLeitores = false;
        carregarUsuariosNaTabela(null);
    }

    private void carregarUsuariosNaTabela(String termoPesquisa) {
        modeloTabela.setRowCount(0);
        List<Usuario> todosUsuarios = usuarioController.listarTodosUsuarios();
        List<Usuario> usuariosFiltrados = new ArrayList<>();

        if (mostrandoLeitores) {
            String[] colunasLeitores = {"Matrícula", "Nome", "Categoria Leitor", "Telefone", "Email"};
            modeloTabela.setColumnIdentifiers(colunasLeitores);

            for (Usuario u : todosUsuarios) {
                if (u.getMatricula() > 0 && u.getCategoriaLeitor() != null && !u.getCategoriaLeitor().isEmpty()) {
                    boolean match = true;
                    if (termoPesquisa != null && !termoPesquisa.trim().isEmpty()) {
                        String termoLower = termoPesquisa.toLowerCase();
                        try {
                            int matriculaPesquisa = Integer.parseInt(termoPesquisa);
                            if (u.getMatricula() != matriculaPesquisa) {
                                match = false;
                            }
                        } catch (NumberFormatException e) {
                            if (!u.getNome().toLowerCase().contains(termoLower)) {
                                match = false;
                            }
                        }
                    }
                    if (match) {
                        usuariosFiltrados.add(u);
                    }
                }
            }

            for (Usuario usuario : usuariosFiltrados) {
                Object[] linha = {
                        usuario.getMatricula(),
                        usuario.getNome(),
                        usuario.getCategoriaLeitor(),
                        usuario.getTelefone(),
                        usuario.getEmail()
                };
                modeloTabela.addRow(linha);
            }

        } else {
            String[] colunasUsuariosSistema = {"Login", "Nome", "Telefone", "Email", "Perfil Acesso"};
            modeloTabela.setColumnIdentifiers(colunasUsuariosSistema);

            for (Usuario u : todosUsuarios) {
                if (u.getMatricula() < 0 || "Administrador".equals(u.getPerfilAcesso()) || "Bibliotecario".equals(u.getPerfilAcesso()) || "Estagiario".equals(u.getPerfilAcesso())) {
                    boolean match = true;
                    if (termoPesquisa != null && !termoPesquisa.trim().isEmpty()) {
                        String termoLower = termoPesquisa.toLowerCase();
                        if (!u.getNome().toLowerCase().contains(termoLower) &&
                                (u.getLogin() == null || !u.getLogin().toLowerCase().contains(termoLower))) {
                            match = false;
                        }
                    }
                    if (match) {
                        usuariosFiltrados.add(u);
                    }
                }
            }

            for (Usuario usuario : usuariosFiltrados) {
                Object[] linha = {
                        usuario.getLogin(),
                        usuario.getNome(),
                        usuario.getTelefone(),
                        usuario.getEmail(),
                        usuario.getPerfilAcesso()
                };
                modeloTabela.addRow(linha);
            }
        }
    }

    private void editarUsuario() {
        int selectedRow = tabelaUsuarios.getSelectedRow();
        if (selectedRow == -1) {
            mensagemFeedback.setText("Selecione um usuário para editar.");
            mensagemFeedback.setForeground(Color.RED);
            return;
        }

        Usuario usuarioParaEditar = null;

        if (mostrandoLeitores) {
            // Edição de Leitores
            int matricula = (int) modeloTabela.getValueAt(selectedRow, 0); // Matrícula é a primeira coluna
            usuarioParaEditar = usuarioController.buscarLeitorPorMatricula(matricula);

            if (usuarioParaEditar == null) {
                mensagemFeedback.setText("Erro: Leitor não encontrado para edição.");
                mensagemFeedback.setForeground(Color.RED);
                return;
            }

            JTextField txtNome = new JTextField(usuarioParaEditar.getNome());
            JTextField txtMatricula = new JTextField(String.valueOf(usuarioParaEditar.getMatricula()));
            JTextField txtTelefone = new JTextField(usuarioParaEditar.getTelefone());
            JTextField txtEmail = new JTextField(usuarioParaEditar.getEmail());
            String[] categorias = {"Aluno", "Professor", "Servidor"};
            JComboBox<String> comboCategoria = new JComboBox<>(categorias);
            comboCategoria.setSelectedItem(usuarioParaEditar.getCategoriaLeitor());

            txtMatricula.setEnabled(false);

            JPanel painelEdicao = new JPanel(new GridLayout(0, 2, 5, 5));
            painelEdicao.add(new JLabel("Nome:"));
            painelEdicao.add(txtNome);
            painelEdicao.add(new JLabel("Matrícula:"));
            painelEdicao.add(txtMatricula);
            painelEdicao.add(new JLabel("Categoria Leitor:"));
            painelEdicao.add(comboCategoria);
            painelEdicao.add(new JLabel("Telefone:"));
            painelEdicao.add(txtTelefone);
            painelEdicao.add(new JLabel("Email:"));
            painelEdicao.add(txtEmail);

            int result = JOptionPane.showConfirmDialog(this, painelEdicao,
                    "Editar Leitor: " + usuarioParaEditar.getNome(), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String novoNome = txtNome.getText();
                String novaCategoria = (String) comboCategoria.getSelectedItem();
                String novoTelefone = txtTelefone.getText();
                String novoEmail = txtEmail.getText();

                Usuario leitorAtualizado = new Usuario(
                        novoNome,
                        usuarioParaEditar.getMatricula(),
                        novaCategoria,
                        novoTelefone,
                        novoEmail
                );

                boolean sucesso = usuarioController.atualizarUsuario(leitorAtualizado);
                if (sucesso) {
                    mensagemFeedback.setText("Leitor " + leitorAtualizado.getNome() + " atualizado com sucesso!");
                    mensagemFeedback.setForeground(new Color(0, 128, 0));
                    carregarUsuariosNaTabela(campoPesquisa.getText());
                } else {
                    mensagemFeedback.setText("Erro ao atualizar leitor.");
                    mensagemFeedback.setForeground(Color.RED);
                }
            }

        } else {
            // Edição de Usuários do Sistema
            String login = (String) modeloTabela.getValueAt(selectedRow, 0);
            usuarioParaEditar = usuarioController.buscarUsuarioPorLogin(login);

            if (usuarioParaEditar == null) {
                mensagemFeedback.setText("Erro: Usuário do sistema não encontrado para edição.");
                mensagemFeedback.setForeground(Color.RED);
                return;
            }

            JTextField txtNome = new JTextField(usuarioParaEditar.getNome());
            JTextField txtLogin = new JTextField(usuarioParaEditar.getLogin());
            JTextField txtTelefone = new JTextField(usuarioParaEditar.getTelefone());
            JTextField txtEmail = new JTextField(usuarioParaEditar.getEmail());
            JPasswordField txtSenha = new JPasswordField();

            String[] perfis = {"Administrador", "Bibliotecario", "Estagiario"};
            JComboBox<String> comboPerfil = new JComboBox<>(perfis);
            comboPerfil.setSelectedItem(usuarioParaEditar.getPerfilAcesso());

            txtLogin.setEnabled(false);

            JPanel painelEdicao = new JPanel(new GridLayout(0, 2, 5, 5));
            painelEdicao.add(new JLabel("Nome:"));
            painelEdicao.add(txtNome);
            painelEdicao.add(new JLabel("Login:"));
            painelEdicao.add(txtLogin);
            painelEdicao.add(new JLabel("Telefone:"));
            painelEdicao.add(txtTelefone);
            painelEdicao.add(new JLabel("Email:"));
            painelEdicao.add(txtEmail);
            painelEdicao.add(new JLabel("Perfil Acesso:"));
            painelEdicao.add(comboPerfil);
            painelEdicao.add(new JLabel("Senha (deixe vazio para não alterar):"));
            painelEdicao.add(txtSenha);

            int result = JOptionPane.showConfirmDialog(this, painelEdicao,
                    "Editar Usuário do Sistema: " + usuarioParaEditar.getNome(), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String novoNome = txtNome.getText();
                String novoTelefone = txtTelefone.getText();
                String novoEmail = txtEmail.getText();
                String novoPerfil = (String) comboPerfil.getSelectedItem();
                String novaSenha = new String(txtSenha.getPassword());

                Usuario usuarioAtualizado = new Usuario(
                        novoNome,
                        usuarioParaEditar.getLogin(),
                        usuarioParaEditar.getMatricula(),
                        usuarioParaEditar.getCategoriaLeitor(),
                        novoTelefone,
                        novoEmail,
                        novoPerfil,
                        novaSenha.isEmpty() ? usuarioParaEditar.getSenha() : novaSenha
                );

                boolean sucesso = usuarioController.atualizarUsuario(usuarioAtualizado);
                if (sucesso) {
                    mensagemFeedback.setText("Usuário " + usuarioAtualizado.getNome() + " atualizado com sucesso!");
                    mensagemFeedback.setForeground(new Color(0, 128, 0));
                    carregarUsuariosNaTabela(campoPesquisa.getText());
                } else {
                    mensagemFeedback.setText("Erro ao atualizar usuário.");
                    mensagemFeedback.setForeground(Color.RED);
                }
            }
        }
    }

    private void excluirUsuario() {
        int selectedRow = tabelaUsuarios.getSelectedRow();
        if (selectedRow == -1) {
            mensagemFeedback.setText("Selecione um usuário para excluir.");
            mensagemFeedback.setForeground(Color.RED);
            return;
        }

        int matriculaParaExcluir = -1;
        String nomeParaExcluir = "";

        if (mostrandoLeitores) {
            matriculaParaExcluir = (int) modeloTabela.getValueAt(selectedRow, 0);
            nomeParaExcluir = (String) modeloTabela.getValueAt(selectedRow, 1);
        } else {
            // Para usuários do sistema, precisamos buscar a matrícula pelo login
            String login = (String) modeloTabela.getValueAt(selectedRow, 0);
            nomeParaExcluir = (String) modeloTabela.getValueAt(selectedRow, 1);
            Usuario user = usuarioController.buscarUsuarioPorLogin(login);
            if (user != null) {
                matriculaParaExcluir = user.getMatricula();
            } else {
                mensagemFeedback.setText("Erro: Usuário do sistema não encontrado para exclusão.");
                mensagemFeedback.setForeground(Color.RED);
                return;
            }
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir " + (mostrandoLeitores ? "o leitor " : "o usuário do sistema ") + nomeParaExcluir + "?",
                "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean sucesso = usuarioController.removerUsuario(matriculaParaExcluir);
            if (sucesso) {
                mensagemFeedback.setText((mostrandoLeitores ? "Leitor " : "Usuário ") + nomeParaExcluir + " excluído com sucesso!");
                mensagemFeedback.setForeground(new Color(0, 128, 0));
                carregarUsuariosNaTabela(campoPesquisa.getText());
            } else {
                mensagemFeedback.setText("Erro ao excluir " + (mostrandoLeitores ? "leitor" : "usuário") + ".");
                mensagemFeedback.setForeground(Color.RED);
            }
        }
    }
}