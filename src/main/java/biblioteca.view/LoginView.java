package biblioteca.view;

import biblioteca.controller.LoginController;
import biblioteca.controller.UsuarioController;
import biblioteca.model.Usuario;

import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {

    private JTextField campoLogin;
    private JPasswordField campoSenha;
    private JButton botaoLogin;
    private JLabel mensagemErro;

    private LoginController loginController;

    public LoginView() {
        super("Login - Sistema de Gerenciamento de Biblioteca");

        this.loginController = new LoginController();

        // --- Configurações da Janela ---
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // --- Painel Principal para organizar tudo verticalmente ---
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // --- Componente: Título ---
        JLabel tituloLabel = new JLabel("Bem-vindo(a)!");
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 20));
        tituloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelPrincipal.add(tituloLabel);
        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 25)));

        JPanel painelLogin = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel labelLogin = new JLabel("Login:");
        labelLogin.setPreferredSize(new Dimension(80, labelLogin.getPreferredSize().height));
        painelLogin.add(labelLogin);
        campoLogin = new JTextField(15); // Campo para o login (String)
        campoLogin.setMaximumSize(new Dimension(200, campoLogin.getPreferredSize().height));
        campoLogin.setAlignmentX(Component.LEFT_ALIGNMENT);
        painelLogin.add(campoLogin);
        painelLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelPrincipal.add(painelLogin);
        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 10)));

        // --- Painel para Senha ---
        JPanel painelSenha = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel labelSenha = new JLabel("Senha:");
        labelSenha.setPreferredSize(new Dimension(80, labelSenha.getPreferredSize().height));
        painelSenha.add(labelSenha);
        campoSenha = new JPasswordField(15);
        campoSenha.setMaximumSize(new Dimension(200, campoSenha.getPreferredSize().height));
        campoSenha.setAlignmentX(Component.LEFT_ALIGNMENT);
        painelSenha.add(campoSenha);
        painelSenha.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelPrincipal.add(painelSenha);

        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- Painel para Botão de Login ---
        JPanel painelBotao = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botaoLogin = new JButton("Login");
        botaoLogin.setPreferredSize(new Dimension(120, 35));
        painelBotao.add(botaoLogin);
        painelPrincipal.add(painelBotao);

        mensagemErro = new JLabel("");
        mensagemErro.setForeground(Color.RED);
        mensagemErro.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 15)));
        painelPrincipal.add(mensagemErro);

        add(painelPrincipal);


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
            String login = campoLogin.getText();
            String senha = new String(campoSenha.getPassword());

            Usuario usuarioAutenticado = loginController.autenticar(login, senha);

            if (usuarioAutenticado != null) {
                mensagemErro.setForeground(new Color(0, 128, 0));
                mensagemErro.setText("Login bem-sucedido!");
                JOptionPane.showMessageDialog(this, "Bem-vindo(a), " + usuarioAutenticado.getNome() + "!");

                TelaPrincipalView telaPrincipal = new TelaPrincipalView(usuarioAutenticado);
                telaPrincipal.setVisible(true);

                dispose();
            } else {
                mensagemErro.setForeground(Color.RED);
                mensagemErro.setText("Login ou senha inválidos.");
            }
        }catch (Exception ex) {
            mensagemErro.setText("Erro inesperado: " + ex.getMessage());
            mensagemErro.setForeground(Color.RED);
            ex.printStackTrace();
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                UsuarioController usuarioController = new UsuarioController();
                List<Usuario> todosUsuarios = usuarioController.listarTodosUsuarios();
                boolean adminExiste = false;
                for (Usuario u : todosUsuarios) {
                    if ("Administrador".equals(u.getPerfilAcesso())) {
                        adminExiste = true;
                        break;
                    }
                }

                if (!adminExiste) {
                    JFrame cadastroAdminFrame = new JFrame("Cadastro do Primeiro Administrador");
                    cadastroAdminFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    cadastroAdminFrame.setSize(600, 450);
                    cadastroAdminFrame.setLocationRelativeTo(null);

                    CadastroUsuarioPanel cadastroAdminPanel = new CadastroUsuarioPanel(true);

                    cadastroAdminPanel.comboPerfilAcesso.setSelectedItem("Administrador");
                    cadastroAdminPanel.comboPerfilAcesso.setEnabled(false);

                    cadastroAdminPanel.addCadastroSuccessListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String login = cadastroAdminPanel.getLoginText();
                            String senha = cadastroAdminPanel.getSenhaText();

                            LoginController loginController = new LoginController();
                            Usuario adminCriado = loginController.autenticar(login, senha);

                            if (adminCriado != null) {
                                JOptionPane.showMessageDialog(null, "Administrador cadastrado e login realizado com sucesso!");
                                TelaPrincipalView telaPrincipal = new TelaPrincipalView(adminCriado);
                                telaPrincipal.setVisible(true);
                                cadastroAdminFrame.dispose();
                            } else {
                                JOptionPane.showMessageDialog(null, "Erro inesperado no login automático após cadastro.", "Erro", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    });
                    cadastroAdminFrame.add(cadastroAdminPanel);
                    cadastroAdminFrame.setVisible(true);

                    return;
                }

                new LoginView();
            }
        });
    }
}