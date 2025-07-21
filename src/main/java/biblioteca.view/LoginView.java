package biblioteca.view;

import biblioteca.controller.LoginController;
import biblioteca.model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {

    private JTextField campoMatricula;
    private JPasswordField campoSenha;
    private JButton botaoLogin;
    private JLabel mensagemErro;

    private LoginController loginController;

    public LoginView() {
        super("Login - Sistema de Gerenciamento de Biblioteca");

        this.loginController = new LoginController();

        // --- Configurações da Janela ---
        setSize(450, 300); // Ajuste o tamanho
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a janela

        // --- Painel Principal para organizar tudo verticalmente ---
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS)); // Organiza componentes em coluna
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Adiciona um padding interno

        // --- Componente: Título ---
        JLabel tituloLabel = new JLabel("Bem-vindo(a)!");
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 20));
        tituloLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Centraliza o título horizontalmente
        painelPrincipal.add(tituloLabel);
        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 15))); // Espaçamento vertical

        // --- Painel para Matrícula ---
        JPanel painelMatricula = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Alinha os componentes à direita
        painelMatricula.add(new JLabel("Matrícula:"));
        campoMatricula = new JTextField(15);
        painelMatricula.add(campoMatricula);
        painelPrincipal.add(painelMatricula);

        // --- Painel para Senha ---
        JPanel painelSenha = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Alinha os componentes à direita
        painelSenha.add(new JLabel("Senha:"));
        campoSenha = new JPasswordField(15);
        painelSenha.add(campoSenha);
        painelPrincipal.add(painelSenha);

        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 10))); // Espaçamento vertical

        // --- Painel para Botão de Login ---
        JPanel painelBotao = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Centraliza o botão
        botaoLogin = new JButton("Login");
        painelBotao.add(botaoLogin);
        painelPrincipal.add(painelBotao);

        // --- Mensagem de Erro/Sucesso ---
        mensagemErro = new JLabel("");
        mensagemErro.setForeground(Color.RED);
        mensagemErro.setAlignmentX(Component.CENTER_ALIGNMENT); // Centraliza a mensagem horizontalmente
        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 10))); // Espaçamento vertical
        painelPrincipal.add(mensagemErro);

        // Adiciona o painel principal ao JFrame
        add(painelPrincipal);

        // --- Adição do Listener de Eventos ao Botão (mantém o que você já tinha) ---
        botaoLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                autenticarUsuario();
            }
        });

        setVisible(true);
    }

    private void autenticarUsuario() {
        try {
            int matricula = Integer.parseInt(campoMatricula.getText());
            String senha = new String(campoSenha.getPassword());

            // Chama o método de autenticação do controlador
            Usuario usuarioAutenticado = loginController.autenticar(matricula, senha);

            if (usuarioAutenticado != null) {
                mensagemErro.setForeground(new Color(0, 128, 0)); // Cor verde para sucesso
                mensagemErro.setText("Login bem-sucedido!");
                JOptionPane.showMessageDialog(this, "Bem-vindo(a), " + usuarioAutenticado.getNome() + "!");

                TelaPrincipalView telaPrincipal = new TelaPrincipalView(usuarioAutenticado);
                telaPrincipal.setVisible(true);

                dispose();
            } else {
                mensagemErro.setForeground(Color.RED); // Cor vermelha para erro
                mensagemErro.setText("Matrícula ou senha inválidos.");
            }
        } catch (NumberFormatException ex) {
            mensagemErro.setForeground(Color.RED);
            mensagemErro.setText("Matrícula deve ser um número válido.");
        }
    }


    public static void main(String[] args) {
        // Garante que a interface gráfica seja criada e atualizada na Thread de Despacho de Eventos (EDT)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginView(); // Cria e exibe a janela de login
            }
        });
    }
}