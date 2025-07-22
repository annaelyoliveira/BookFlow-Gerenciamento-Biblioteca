package biblioteca.view;

import biblioteca.controller.UsuarioController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class CadastroUsuarioPanel extends JPanel {
    private UsuarioController usuarioController;

    private JTextField campoNome;
    public JTextField campoLogin;
    private JTextField campoTelefone;
    private JTextField campoEmail;
    public JComboBox<String> comboPerfilAcesso;
    public JPasswordField campoSenha;
    private JButton botaoSalvar;
    private JLabel mensagemFeedback;

    private List<ActionListener> cadastroSuccessListeners = new ArrayList<>();

    public CadastroUsuarioPanel() {
        this(false);
    }
    public CadastroUsuarioPanel(boolean isFirstAdmin) {
        this.usuarioController = new UsuarioController();
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel tituloPagina = new JLabel("Cadastrar Novo Usuário do Sistema", SwingConstants.CENTER);
        if (isFirstAdmin) {
            tituloPagina.setText("Cadastro do Primeiro Administrador");
        }
        tituloPagina.setFont(new Font("Arial", Font.BOLD, 24));
        add(tituloPagina, BorderLayout.NORTH);

        JPanel painelFormulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; painelFormulario.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; campoNome = new JTextField(20); painelFormulario.add(campoNome, gbc);

        gbc.gridx = 0; gbc.gridy = 1; painelFormulario.add(new JLabel("Login:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; campoLogin = new JTextField(20); painelFormulario.add(campoLogin, gbc);


        int currentGridY = 2;

        gbc.gridx = 0; gbc.gridy = currentGridY;
        painelFormulario.add(new JLabel("Telefone:"), gbc);
        gbc.gridx = 1; gbc.gridy = currentGridY++;
        campoTelefone = new JTextField(20); painelFormulario.add(campoTelefone, gbc);

        gbc.gridx = 0; gbc.gridy = currentGridY;
        painelFormulario.add(new JLabel("E-mail:"), gbc);
        gbc.gridx = 1; gbc.gridy = currentGridY++;
        campoEmail = new JTextField(20); painelFormulario.add(campoEmail, gbc);

        gbc.gridx = 0; gbc.gridy = currentGridY;
        painelFormulario.add(new JLabel("Perfil de Acesso:"), gbc);
        gbc.gridx = 1; gbc.gridy = currentGridY++;
        String[] perfis = {"Administrador", "Bibliotecario", "Estagiario"};
        comboPerfilAcesso = new JComboBox<>(perfis); painelFormulario.add(comboPerfilAcesso, gbc);

        gbc.gridx = 0; gbc.gridy = currentGridY;
        painelFormulario.add(new JLabel("Senha:"), gbc);
        gbc.gridx = 1; gbc.gridy = currentGridY++;
        campoSenha = new JPasswordField(20); painelFormulario.add(campoSenha, gbc);

        add(painelFormulario, BorderLayout.CENTER);

        JPanel painelRodape = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botaoSalvar = new JButton("Salvar Usuário");
        painelRodape.add(botaoSalvar);
        mensagemFeedback = new JLabel("");
        painelRodape.add(mensagemFeedback);
        add(painelRodape, BorderLayout.SOUTH);

        botaoSalvar.addActionListener(e -> cadastrarUsuario());
    }

    public void addCadastroSuccessListener(ActionListener listener) {
        cadastroSuccessListeners.add(listener);
    }

    private void notifyCadastroSuccessListeners() {
        for (ActionListener listener : cadastroSuccessListeners) {
            listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "cadastro_success"));
        }
    }

    private void cadastrarUsuario() {
        String nome = campoNome.getText();
        String login = campoLogin.getText();
        String telefone = campoTelefone.getText();
        String email = campoEmail.getText();
        String perfilAcesso = (String) comboPerfilAcesso.getSelectedItem();
        String senha = new String(campoSenha.getPassword());

        if (login.trim().isEmpty()) {
            mensagemFeedback.setText("Erro: O campo Login não pode ser vazio.");
            mensagemFeedback.setForeground(Color.RED);
            return;
        }

        String resultadoCadastro = usuarioController.cadastrarUsuarioSistema(nome, login, telefone, email, perfilAcesso, senha);

        if (resultadoCadastro.startsWith("Erro:")) {
            mensagemFeedback.setText(resultadoCadastro);
            mensagemFeedback.setForeground(Color.RED);
        } else {
            mensagemFeedback.setText(resultadoCadastro);
            mensagemFeedback.setForeground(new Color(0, 128, 0));

            notifyCadastroSuccessListeners();

            campoNome.setText("");
            campoLogin.setText("");
            campoTelefone.setText("");
            campoEmail.setText("");
            comboPerfilAcesso.setSelectedIndex(0);
            campoSenha.setText("");
        }
    }

    public void setPerfilAcessoAdminMode() {
        comboPerfilAcesso.setSelectedItem("Administrador");
        comboPerfilAcesso.setEnabled(false);
        revalidate();
        repaint();
    }

    public String getLoginText() {
        return campoLogin.getText();
    }

    public String getSenhaText() {
        return new String(campoSenha.getPassword());
    }

}