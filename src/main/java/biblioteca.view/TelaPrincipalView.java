package biblioteca.view;

import biblioteca.model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaPrincipalView extends JFrame {

    private Usuario usuarioLogado;
    private JPanel painelConteudo; // Onde as telas de funcionalidade serão exibidas

    // Declaração dos botões como campos da classe para poder controlá-los
    private JButton btnCadastrarObra;
    private JButton btnCadastrarUsuario;
    private JButton btnEmprestarObra;
    private JButton btnDevolverObra;
    private JButton btnListarObras;
    private JButton btnRegistrarPagamento;
    private JButton btnRelatorios;
    private JButton btnSair; // Botão para sair

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

        // Inicialização dos botões
        btnCadastrarObra = new JButton("Cadastrar Obra");
        btnCadastrarUsuario = new JButton("Cadastrar Usuário");
        btnEmprestarObra = new JButton("Empréstimo de Obra");
        btnDevolverObra = new JButton("Devolução de Obra");
        btnListarObras = new JButton("Listar / Pesquisar Obras");
        btnRegistrarPagamento = new JButton("Registrar Pagamento de Multa");
        btnRelatorios = new JButton("Gerar Relatórios");
        btnSair = new JButton("Sair");

        // Adiciona os botões ao painel de menu com alinhamento e espaçamento
        adicionarBotaoAoMenu(painelMenu, btnCadastrarObra);
        adicionarBotaoAoMenu(painelMenu, btnCadastrarUsuario);
        adicionarBotaoAoMenu(painelMenu, btnEmprestarObra);
        adicionarBotaoAoMenu(painelMenu, btnDevolverObra);
        adicionarBotaoAoMenu(painelMenu, btnListarObras);
        adicionarBotaoAoMenu(painelMenu, btnRegistrarPagamento);
        adicionarBotaoAoMenu(painelMenu, btnRelatorios);
        painelMenu.add(Box.createVerticalGlue());
        adicionarBotaoAoMenu(painelMenu, btnSair);

        add(painelMenu, BorderLayout.WEST);

        // --- Painel de Conteúdo Principal ---
        painelConteudo = new JPanel();
        painelConteudo.setLayout(new BorderLayout());
        painelConteudo.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        add(painelConteudo, BorderLayout.CENTER);

        // --- Lógica para controlar a visibilidade/habilitação dos botões ---
        controlarAcessos();

        // --- Listeners dos Botões ---
        btnSair.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja sair?", "Confirmar Saída", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        // ActionListener para o botão Cadastrar Obra
        btnCadastrarObra.addActionListener(e -> {
            mostrarPainel(new CadastroObraPanel()); // Cria e mostra o painel de cadastro de obra
        });

        setVisible(true);
    }

    // Método auxiliar para adicionar botões ao menu com formatação
    private void adicionarBotaoAoMenu(JPanel menuPanel, JButton button) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(200, 40));
        button.setMinimumSize(new Dimension(200, 40));
        button.setPreferredSize(new Dimension(200, 40));
        menuPanel.add(button);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    /**
     * Controla quais funcionalidades o usuário logado pode acessar
     * [cite_start]com base no seu tipo (Administrador, Bibliotecario, Estagiario). [cite: 35, 36, 37, 38]
     */
    private void controlarAcessos() {
        String tipo = usuarioLogado.getTipoUsuario();

        // Desabilita todos os botões por padrão, e habilita apenas os permitidos
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

    /**
     * Método auxiliar para trocar o conteúdo do painel central da tela principal.
     * @param novoPainel O JPanel que será exibido.
     */
    private void mostrarPainel(JPanel novoPainel) {
        painelConteudo.removeAll();
        painelConteudo.add(novoPainel, BorderLayout.CENTER);
        painelConteudo.revalidate();
        painelConteudo.repaint();
    }

    // Método main temporário para testar a TelaPrincipal diretamente (remover no final)
    public static void main(String[] args) {
        Usuario usuarioTeste = new Usuario("Admin Teste", 9999, "Administrador", "1234-5678", "teste@admin.com", "admin123");
        // Usuario usuarioTeste = new Usuario("Biblio Teste", 8888, "Bibliotecario", "1234-5678", "teste@biblio.com", "biblio123");
        // Usuario usuarioTeste = new Usuario("Estagi Teste", 7777, "Estagiario", "1234-5678", "teste@estag.com", "estag123");

        SwingUtilities.invokeLater(() -> new TelaPrincipalView(usuarioTeste));
    }
}