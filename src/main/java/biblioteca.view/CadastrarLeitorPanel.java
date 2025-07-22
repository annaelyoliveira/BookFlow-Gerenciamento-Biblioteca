package biblioteca.view;

import biblioteca.controller.UsuarioController;

import javax.swing.*;
import java.awt.*;

public class CadastrarLeitorPanel extends JPanel {
    private UsuarioController usuarioController;

    private JTextField campoNome;
    private JTextField campoMatricula;
    private JComboBox<String> comboCategoriaLeitor;
    private JTextField campoTelefone;
    private JTextField campoEmail;
    // Sem campo de senha ou perfil de acesso para o Leitor, se ele não for logar no sistema
    private JButton botaoSalvar;
    private JLabel mensagemFeedback;

    public CadastrarLeitorPanel() {
        this.usuarioController = new UsuarioController();
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel tituloPagina = new JLabel("Cadastrar Novo Leitor", SwingConstants.CENTER);
        tituloPagina.setFont(new Font("Arial", Font.BOLD, 24));
        add(tituloPagina, BorderLayout.NORTH);

        JPanel painelFormulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; painelFormulario.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; campoNome = new JTextField(20); painelFormulario.add(campoNome, gbc);

        gbc.gridx = 0; gbc.gridy = 1; painelFormulario.add(new JLabel("Matrícula:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; campoMatricula = new JTextField(20); painelFormulario.add(campoMatricula, gbc);

        gbc.gridx = 0; gbc.gridy = 2; painelFormulario.add(new JLabel("Categoria Leitor:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; String[] categorias = {"Aluno", "Professor", "Servidor"};
        comboCategoriaLeitor = new JComboBox<>(categorias); painelFormulario.add(comboCategoriaLeitor, gbc);

        gbc.gridx = 0; gbc.gridy = 3; painelFormulario.add(new JLabel("Telefone:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; campoTelefone = new JTextField(20); painelFormulario.add(campoTelefone, gbc);

        gbc.gridx = 0; gbc.gridy = 4; painelFormulario.add(new JLabel("E-mail:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; campoEmail = new JTextField(20); painelFormulario.add(campoEmail, gbc);

        add(painelFormulario, BorderLayout.CENTER);

        JPanel painelRodape = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botaoSalvar = new JButton("Salvar Leitor");
        painelRodape.add(botaoSalvar);
        mensagemFeedback = new JLabel("");
        painelRodape.add(mensagemFeedback);
        add(painelRodape, BorderLayout.SOUTH);

        botaoSalvar.addActionListener(e -> cadastrarLeitor());
    }

    private void cadastrarLeitor() {
        try {
            String nome = campoNome.getText();
            int matricula;
            try {
                matricula = Integer.parseInt(campoMatricula.getText());
            } catch (NumberFormatException ex) {
                mensagemFeedback.setText("Erro: Matrícula deve ser um número válido.");
                mensagemFeedback.setForeground(Color.RED);
                return;
            }

            String categoriaLeitor = (String) comboCategoriaLeitor.getSelectedItem();
            String telefone = campoTelefone.getText();
            String email = campoEmail.getText();

            String resultadoCadastro = usuarioController.cadastrarLeitor(nome, matricula, categoriaLeitor, telefone, email);

            if (resultadoCadastro.startsWith("Erro:")) {
                mensagemFeedback.setText(resultadoCadastro);
                mensagemFeedback.setForeground(Color.RED);
            } else {
                mensagemFeedback.setText(resultadoCadastro);
                mensagemFeedback.setForeground(new Color(0, 128, 0));
                // Limpar campos
                campoNome.setText("");
                campoMatricula.setText("");
                comboCategoriaLeitor.setSelectedIndex(0);
                campoTelefone.setText("");
                campoEmail.setText("");
            }
        } catch (Exception ex) {
            mensagemFeedback.setText("Erro inesperado: " + ex.getMessage());
            mensagemFeedback.setForeground(Color.RED);
            ex.printStackTrace();
        }
    }
}