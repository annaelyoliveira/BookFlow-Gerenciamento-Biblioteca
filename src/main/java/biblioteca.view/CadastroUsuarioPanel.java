package biblioteca.view;

import biblioteca.controller.UsuarioController;
import biblioteca.model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CadastroUsuarioPanel extends JPanel {
    private UsuarioController usuarioController;

    private JTextField campoNome;
    private JTextField campoMatricula;
    private JComboBox<String> comboTipoUsuario;
    private JTextField campoTelefone;
    private JTextField campoEmail;
    private JPasswordField campoSenha;
    private JButton botaoSalvar;
    private JLabel mensagemFeedback;

    public CadastroUsuarioPanel() {
        this.usuarioController = new UsuarioController();
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel tituloPagina = new JLabel("Cadastro de Usuários", SwingConstants.CENTER);
        tituloPagina.setFont(new Font("Arial", Font.BOLD, 24));
        add(tituloPagina, BorderLayout.NORTH);

        JPanel painelFormulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        painelFormulario.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        campoNome = new JTextField(20);
        painelFormulario.add(campoNome, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        painelFormulario.add(new JLabel("Matrícula:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        campoMatricula = new JTextField(20);
        painelFormulario.add(campoMatricula, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        painelFormulario.add(new JLabel("Tipo Usuário:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        String[] tiposUsuario = {"Administrador", "Bibliotecario", "Estagiario"};
        comboTipoUsuario = new JComboBox<>(tiposUsuario);
        painelFormulario.add(comboTipoUsuario, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        painelFormulario.add(new JLabel("Telefone:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        campoTelefone = new JTextField(20);
        painelFormulario.add(campoTelefone, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        painelFormulario.add(new JLabel("E-mail:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        campoEmail = new JTextField(20);
        painelFormulario.add(campoEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        painelFormulario.add(new JLabel("Senha:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5;
        campoSenha = new JPasswordField(20);
        painelFormulario.add(campoSenha, gbc);

        add(painelFormulario, BorderLayout.CENTER);

        JPanel painelRodape = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botaoSalvar = new JButton("Salvar Usuário");
        painelRodape.add(botaoSalvar);
        mensagemFeedback = new JLabel("");
        painelRodape.add(mensagemFeedback);
        add(painelRodape, BorderLayout.SOUTH);

        botaoSalvar.addActionListener(e -> cadastrarUsuario());
    }

    private void cadastrarUsuario() {
        try {
            String nome = campoNome.getText();
            int matricula = Integer.parseInt(campoMatricula.getText());
            String tipoUsuario = (String) comboTipoUsuario.getSelectedItem();
            String telefone = campoTelefone.getText();
            String email = campoEmail.getText();
            String senha = new String(campoSenha.getPassword());

            boolean sucesso = usuarioController.cadastrarUsuario(nome, matricula, tipoUsuario, telefone, email, senha);

            if (sucesso) {
                mensagemFeedback.setText("Usuário cadastrado com sucesso!");
                mensagemFeedback.setForeground(new Color(0, 128, 0));
                campoNome.setText("");
                campoMatricula.setText("");
                comboTipoUsuario.setSelectedIndex(0);
                campoTelefone.setText("");
                campoEmail.setText("");
                campoSenha.setText("");
            } else {
                mensagemFeedback.setText("Erro ao cadastrar usuário. Verifique a matrícula.");
                mensagemFeedback.setForeground(Color.RED);
            }
        } catch (NumberFormatException ex) {
            mensagemFeedback.setText("Erro: Matrícula deve ser um número válido.");
            mensagemFeedback.setForeground(Color.RED);
        } catch (Exception ex) {
            mensagemFeedback.setText("Erro inesperado: " + ex.getMessage());
            mensagemFeedback.setForeground(Color.RED);
            ex.printStackTrace();
        }
    }
}