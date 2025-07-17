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
        painelMenu.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); // Padding interno
        painelMenu.add(Box.createRigidArea(new Dimension(0, 10))); // Espaçamento

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
        painelMenu.add(Box.createVerticalGlue()); // Empurra os botões para cima
        adicionarBotaoAoMenu(painelMenu, btnSair); // Botão Sair no final

        add(painelMenu, BorderLayout.WEST);

        // --- Painel de Conteúdo Principal ---
        painelConteudo = new JPanel();
        painelConteudo.setLayout(new BorderLayout()); // Inicialmente vazio, pronto para receber outros JPanels
        painelConteudo.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY)); // Borda simples para visualização
        add(painelConteudo, BorderLayout.CENTER);

        // --- Lógica para controlar a visibilidade/habilitação dos botões ---
        controlarAcessos();

        // --- Listeners dos Botões (apenas para exemplo inicial) ---
        btnSair.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja sair?", "Confirmar Saída", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0); // Fecha a aplicação
            }
        });

        // TODO: Adicionar ActionListeners para os outros botões que carregarão diferentes JPanels no painelConteudo
        // Exemplo: btnCadastrarObra.addActionListener(e -> mostrarPainelCadastroObra());

        setVisible(true);
    }

    // Método auxiliar para adicionar botões ao menu com formatação
    private void adicionarBotaoAoMenu(JPanel menuPanel, JButton button) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT); // Centraliza o botão
        button.setMaximumSize(new Dimension(200, 40)); // Tamanho máximo para os botões do menu
        button.setMinimumSize(new Dimension(200, 40));
        button.setPreferredSize(new Dimension(200, 40));
        menuPanel.add(button);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Espaçamento entre botões
    }


    /**
     * Controla quais funcionalidades o usuário logado pode acessar
     * com base no seu tipo (Administrador, Bibliotecario, Estagiario).
     */
    private void controlarAcessos() {
        String tipo = usuarioLogado.getTipoUsuario(); //

        // Desabilita todos os botões por padrão, e habilita apenas os permitidos
        // Isso é mais seguro do que habilitar tudo e depois desabilitar
        btnCadastrarObra.setEnabled(false);
        btnCadastrarUsuario.setEnabled(false);
        btnEmprestarObra.setEnabled(false);
        btnDevolverObra.setEnabled(false);
        btnListarObras.setEnabled(false); // Geralmente, listar/pesquisar é permitido para todos. Decisão de design.
        btnRegistrarPagamento.setEnabled(false);
        btnRelatorios.setEnabled(false);

        switch (tipo) {
            case "Administrador":
                // Administrador: pode cadastrar obras, usuários e outros administradores.
                btnCadastrarObra.setEnabled(true);
                btnCadastrarUsuario.setEnabled(true);
                btnEmprestarObra.setEnabled(true); // Um admin também pode fazer isso
                btnDevolverObra.setEnabled(true); // E isso
                btnListarObras.setEnabled(true); // E isso
                btnRegistrarPagamento.setEnabled(true);
                btnRelatorios.setEnabled(true);
                break;
            case "Bibliotecario":
                // Bibliotecário: pode registrar empréstimos, devoluções e visualizar relatórios.
                btnEmprestarObra.setEnabled(true);
                btnDevolverObra.setEnabled(true);
                btnListarObras.setEnabled(true); // Bibliotecário precisa listar
                btnRegistrarPagamento.setEnabled(true); // Bibliotecário registra pagamento
                btnRelatorios.setEnabled(true);
                break;
            case "Estagiario":
                // Estagiário: apenas pode registrar devoluções.
                btnDevolverObra.setEnabled(true);
                break;
            default:
                System.out.println("Tipo de usuário desconhecido: " + tipo + ". Acesso limitado.");
                // Nenhuma permissão extra é concedida, permanecem desabilitados
                break;
        }
        // O botão Sair deve estar sempre habilitado
        btnSair.setEnabled(true);
    }

    // Método main temporário para testar a TelaPrincipal diretamente (remover no final)
    public static void main(String[] args) {
        // Crie um usuário de teste (ex: administrador) para testar a tela
        Usuario usuarioTeste = new Usuario("Admin Teste", 9999, "Administrador", "1234-5678", "teste@admin.com", "admin123");
        // Usuario usuarioTeste = new Usuario("Biblio Teste", 8888, "Bibliotecario", "1234-5678", "teste@biblio.com", "biblio123");
        // Usuario usuarioTeste = new Usuario("Estagi Teste", 7777, "Estagiario", "1234-5678", "teste@estag.com", "estag123");

        SwingUtilities.invokeLater(() -> new TelaPrincipalView(usuarioTeste));
    }
}