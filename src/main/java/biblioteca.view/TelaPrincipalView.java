package biblioteca.view;

import biblioteca.model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaPrincipalView extends JFrame {

    private Usuario usuarioLogado;
    private JPanel painelConteudo; // Onde as telas de funcionalidade serão exibidas

    private JButton btnCadastrarObra;
    private JButton btnCadastrarUsuarioSistema;
    private JButton btnCadastrarLeitor;
    private JButton btnGerenciarUsuarios;
    private JButton btnEmprestarObra;
    private JButton btnDevolverObra;
    private JButton btnListarObras; // Botão de listar obras
    private JButton btnRegistrarPagamento;
    private JButton btnRelatorios;
    private JButton btnSair;

    public TelaPrincipalView(Usuario usuario) {
        super("Sistema de Gerenciamento de Biblioteca - " + usuario.getNome() + " (" + usuario.getPerfilAcesso() + ")");
        this.usuarioLogado = usuario;

        // --- Configurações da Janela Principal ---
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- Painel Superior (Boas-vindas) ---
        JPanel painelSuperior = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel boasVindasLabel = new JLabel("Bem-vindo(a), " + usuarioLogado.getNome() + "! (" + usuarioLogado.getPerfilAcesso() + ")");
        boasVindasLabel.setFont(new Font("Arial", Font.BOLD, 18));
        painelSuperior.add(boasVindasLabel);
        add(painelSuperior, BorderLayout.NORTH);

        // --- Painel de Menu Lateral ---
        JPanel painelMenu = new JPanel();
        painelMenu.setLayout(new BoxLayout(painelMenu, BoxLayout.Y_AXIS));
        painelMenu.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        painelMenu.add(Box.createRigidArea(new Dimension(0, 10)));

        btnCadastrarObra = new JButton("Cadastrar Obra");
        btnCadastrarUsuarioSistema = new JButton("Cadastrar Usuário");
        btnCadastrarLeitor = new JButton("Cadastrar Leitor");
        btnGerenciarUsuarios = new JButton("Gerenciar Usuários");
        btnEmprestarObra = new JButton("Empréstimo de Obra");
        btnDevolverObra = new JButton("Devolução de Obra");
        btnListarObras = new JButton("Listar / Pesquisar Obras"); // Inicialização do botão
        btnRegistrarPagamento = new JButton("Registrar Pagamento de Multa");
        btnRelatorios = new JButton("Gerar Relatórios");
        btnSair = new JButton("Sair");

        adicionarBotaoAoMenu(painelMenu, btnCadastrarObra);
        adicionarBotaoAoMenu(painelMenu, btnCadastrarUsuarioSistema);
        adicionarBotaoAoMenu(painelMenu, btnCadastrarLeitor);
        adicionarBotaoAoMenu(painelMenu, btnGerenciarUsuarios);
        adicionarBotaoAoMenu(painelMenu, btnEmprestarObra);
        adicionarBotaoAoMenu(painelMenu, btnDevolverObra);
        adicionarBotaoAoMenu(painelMenu, btnListarObras);
        adicionarBotaoAoMenu(painelMenu, btnRegistrarPagamento);
        adicionarBotaoAoMenu(painelMenu, btnRelatorios);
        painelMenu.add(Box.createVerticalGlue());
        adicionarBotaoAoMenu(painelMenu, btnSair);

        add(painelMenu, BorderLayout.WEST);

        painelConteudo = new JPanel();
        painelConteudo.setLayout(new BorderLayout());
        painelConteudo.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        add(painelConteudo, BorderLayout.CENTER);

        controlarAcessos();

        btnSair.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja sair?", "Confirmar Saída", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        btnCadastrarObra.addActionListener(e -> {
            mostrarPainel(new CadastroObraPanel());
        });

        btnCadastrarUsuarioSistema.addActionListener(e -> {
            mostrarPainel(new CadastroUsuarioPanel(false));
        });

        btnCadastrarLeitor.addActionListener(e -> {
            mostrarPainel(new CadastrarLeitorPanel());
        });

        btnGerenciarUsuarios.addActionListener(e -> {
            mostrarPainel(new GerenciarUsuariosPanel());
        });

        btnEmprestarObra.addActionListener(e -> {
            mostrarPainel(new EmprestarObraPanel());
        });

        btnDevolverObra.addActionListener(e -> {
            mostrarPainel(new DevolverObraPanel());
        });

        btnListarObras.addActionListener(e -> {
            mostrarPainel(new ListarObrasPanel());
        });

        setVisible(true);
    }

    private void adicionarBotaoAoMenu(JPanel menuPanel, JButton button) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(200, 40));
        button.setMinimumSize(new Dimension(200, 40));
        button.setPreferredSize(new Dimension(200, 40));
        menuPanel.add(button);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    private void controlarAcessos() {
        String perfil = usuarioLogado.getPerfilAcesso();

        btnCadastrarObra.setEnabled(false);
        btnCadastrarUsuarioSistema.setEnabled(false);
        btnCadastrarLeitor.setEnabled(false);
        btnGerenciarUsuarios.setEnabled(false);
        btnEmprestarObra.setEnabled(false);
        btnDevolverObra.setEnabled(false);
        btnListarObras.setEnabled(false);
        btnRegistrarPagamento.setEnabled(false);
        btnRelatorios.setEnabled(false);

        switch (perfil) {
            case "Administrador":
                btnCadastrarObra.setEnabled(true);
                btnCadastrarUsuarioSistema.setEnabled(true);
                btnCadastrarLeitor.setEnabled(true);
                btnGerenciarUsuarios.setEnabled(true);
                btnEmprestarObra.setEnabled(true);
                btnDevolverObra.setEnabled(true);
                btnListarObras.setEnabled(true);
                btnRegistrarPagamento.setEnabled(true);
                btnRelatorios.setEnabled(true);
                break;
            case "Bibliotecario":
                btnCadastrarLeitor.setEnabled(true);
                btnEmprestarObra.setEnabled(true);
                btnDevolverObra.setEnabled(true);
                btnListarObras.setEnabled(true);
                btnRegistrarPagamento.setEnabled(true);
                btnRelatorios.setEnabled(true);
                break;
            case "Estagiario":
                btnDevolverObra.setEnabled(true);
                btnListarObras.setEnabled(true);
                break;
            default:
                System.out.println("Tipo de usuário desconhecido: " + perfil + ". Acesso limitado.");
                break;
        }
        btnSair.setEnabled(true);
    }

    private void mostrarPainel(JPanel novoPainel) {
        painelConteudo.removeAll();
        painelConteudo.add(novoPainel, BorderLayout.CENTER);
        painelConteudo.revalidate();
        painelConteudo.repaint();
    }
}
