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
    private JButton btnCadastrarUsuario;
    private JButton btnEmprestarObra;
    private JButton btnDevolverObra;
    private JButton btnListarObras; // Botão de listar obras
    private JButton btnRegistrarPagamento;
    private JButton btnRelatorios;
    private JButton btnSair;

    public TelaPrincipalView(Usuario usuario) {
        super("Sistema de Gerenciamento de Biblioteca - " + usuario.getNome() + " (" + usuario.getTipoUsuario() + ")");
        this.usuarioLogado = usuario;

        // --- Configurações da Janela Principal ---
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- Painel Superior (Boas-vindas) ---
        JPanel painelSuperior = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel boasVindasLabel = new JLabel("Bem-vindo(a), " + usuarioLogado.getNome() + "! (" + usuarioLogado.getTipoUsuario() + ")");
        boasVindasLabel.setFont(new Font("Arial", Font.BOLD, 18));
        painelSuperior.add(boasVindasLabel);
        add(painelSuperior, BorderLayout.NORTH);

        // --- Painel de Menu Lateral ---
        JPanel painelMenu = new JPanel();
        painelMenu.setLayout(new BoxLayout(painelMenu, BoxLayout.Y_AXIS));
        painelMenu.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        painelMenu.add(Box.createRigidArea(new Dimension(0, 10)));

        btnCadastrarObra = new JButton("Cadastrar Obra");
        btnCadastrarUsuario = new JButton("Cadastrar Usuário");
        btnEmprestarObra = new JButton("Empréstimo de Obra");
        btnDevolverObra = new JButton("Devolução de Obra");
        btnListarObras = new JButton("Listar / Pesquisar Obras"); // Inicialização do botão
        btnRegistrarPagamento = new JButton("Registrar Pagamento de Multa");
        btnRelatorios = new JButton("Gerar Relatórios");
        btnSair = new JButton("Sair");

        adicionarBotaoAoMenu(painelMenu, btnCadastrarObra);
        adicionarBotaoAoMenu(painelMenu, btnCadastrarUsuario);
        adicionarBotaoAoMenu(painelMenu, btnEmprestarObra);
        adicionarBotaoAoMenu(painelMenu, btnDevolverObra);
        adicionarBotaoAoMenu(painelMenu, btnListarObras); // Adição do botão ao menu
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

        btnCadastrarUsuario.addActionListener(e -> {
            mostrarPainel(new CadastroUsuarioPanel());
        });

        btnListarObras.addActionListener(e -> {
            mostrarPainel(new ListarObrasPanel());
        });

        btnEmprestarObra.addActionListener(e -> {
            mostrarPainel(new EmprestarObraPanel());
        });

        btnDevolverObra.addActionListener(e -> {
            mostrarPainel(new DevolverObraPanel());
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
        String tipo = usuarioLogado.getTipoUsuario();

        btnCadastrarObra.setEnabled(false);
        btnCadastrarUsuario.setEnabled(false);
        btnEmprestarObra.setEnabled(false);
        btnDevolverObra.setEnabled(false);
        btnListarObras.setEnabled(false);
        btnRegistrarPagamento.setEnabled(false);
        btnRelatorios.setEnabled(false);

        switch (tipo) {
            case "Administrador":
                btnCadastrarObra.setEnabled(true);
                btnCadastrarUsuario.setEnabled(true);
                btnEmprestarObra.setEnabled(true);
                btnDevolverObra.setEnabled(true);
                btnListarObras.setEnabled(true);
                btnRegistrarPagamento.setEnabled(true);
                btnRelatorios.setEnabled(true);
                break;
            case "Bibliotecario":
                btnEmprestarObra.setEnabled(true);
                btnDevolverObra.setEnabled(true);
                btnListarObras.setEnabled(true);
                btnRegistrarPagamento.setEnabled(true);
                btnRelatorios.setEnabled(true);
                break;
            case "Estagiario":
                btnDevolverObra.setEnabled(true);
                break;
            default:
                System.out.println("Tipo de usuário desconhecido: " + tipo + ". Acesso limitado.");
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

    public static void main(String[] args) {
        Usuario usuarioTeste = new Usuario("Admin Teste", 9999, "Administrador", "1234-5678", "teste@admin.com", "admin123");
        SwingUtilities.invokeLater(() -> new TelaPrincipalView(usuarioTeste));
    }
}
