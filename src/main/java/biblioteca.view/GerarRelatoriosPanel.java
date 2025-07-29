package biblioteca.view;

import biblioteca.controller.RelatorioController;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class GerarRelatoriosPanel extends JPanel {

    private RelatorioController relatorioController;
    private JLabel mensagemFeedback;

    private JComboBox<String> comboMes;
    private JTextField campoAno;
    private JButton btnGerarEmprestimosMes;

    private JButton btnGerarObrasMaisEmprestadas;

    private JButton btnGerarUsuariosMaisAtrasos;

    public GerarRelatoriosPanel() {
        this.relatorioController = new RelatorioController();
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel tituloPagina = new JLabel("Geração de Relatórios (PDF)", SwingConstants.CENTER);
        tituloPagina.setFont(new Font("Arial", Font.BOLD, 24));
        add(tituloPagina, BorderLayout.NORTH);

        JPanel painelControles = new JPanel();
        painelControles.setLayout(new BoxLayout(painelControles, BoxLayout.Y_AXIS));
        painelControles.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));

        // --- Relatório de Empréstimos no Mês ---
        JPanel panelEmprestimosMes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelEmprestimosMes.setBorder(BorderFactory.createTitledBorder("Empréstimos por Mês/Ano"));

        String[] meses = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};
        comboMes = new JComboBox<>(meses);
        campoAno = new JTextField(4);
        btnGerarEmprestimosMes = new JButton("Gerar PDF");

        panelEmprestimosMes.add(new JLabel("Mês:"));
        panelEmprestimosMes.add(comboMes);
        panelEmprestimosMes.add(new JLabel("Ano:"));
        panelEmprestimosMes.add(campoAno);
        panelEmprestimosMes.add(btnGerarEmprestimosMes);
        painelControles.add(panelEmprestimosMes);

        // --- Relatório de Obras Mais Emprestadas (Top 10) ---
        JPanel panelObrasEmprestadas = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelObrasEmprestadas.setBorder(BorderFactory.createTitledBorder("Obras Mais Emprestadas (Top 10)"));

        btnGerarObrasMaisEmprestadas = new JButton("Gerar PDF");
        panelObrasEmprestadas.add(btnGerarObrasMaisEmprestadas);
        painelControles.add(panelObrasEmprestadas);

        // --- Relatório de Usuários com Mais Atrasos (Top 10) ---
        JPanel panelUsuariosAtrasos = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelUsuariosAtrasos.setBorder(BorderFactory.createTitledBorder("Usuários com Mais Atrasos (Top 10)"));

        btnGerarUsuariosMaisAtrasos = new JButton("Gerar PDF");
        panelUsuariosAtrasos.add(btnGerarUsuariosMaisAtrasos);
        painelControles.add(panelUsuariosAtrasos);

        add(painelControles, BorderLayout.CENTER);

        mensagemFeedback = new JLabel("");
        mensagemFeedback.setHorizontalAlignment(SwingConstants.CENTER);
        add(mensagemFeedback, BorderLayout.SOUTH);

        btnGerarEmprestimosMes.addActionListener(e -> gerarRelatorioEmprestimosMes());
        btnGerarObrasMaisEmprestadas.addActionListener(e -> gerarRelatorioObrasMaisEmprestadas());
        btnGerarUsuariosMaisAtrasos.addActionListener(e -> gerarRelatorioUsuariosComMaisAtrasos());
    }

    private void gerarRelatorioEmprestimosMes() {
        try {
            int mes = comboMes.getSelectedIndex() + 1;
            int ano = Integer.parseInt(campoAno.getText());
            String nomeArquivo = "relatorio_emprestimos_" + mes + "_" + ano + ".pdf";

            String caminhoSalvar = escolherCaminhoSalvar(nomeArquivo);
            if (caminhoSalvar != null) {
                String resultado = relatorioController.gerarRelatorioEmprestimosMes(mes, ano, caminhoSalvar);
                mostrarFeedback(resultado);
            }
        } catch (NumberFormatException ex) {
            mostrarFeedback("Erro: Ano deve ser um número válido.", true);
        } catch (Exception ex) {
            mostrarFeedback("Erro inesperado: " + ex.getMessage(), true);
            ex.printStackTrace();
        }
    }

    private void gerarRelatorioObrasMaisEmprestadas() {
        try {
            String nomeArquivo = "relatorio_obras_mais_emprestadas_top10.pdf";

            String caminhoSalvar = escolherCaminhoSalvar(nomeArquivo);
            if (caminhoSalvar != null) {
                String resultado = relatorioController.gerarRelatorioObrasMaisEmprestadas(caminhoSalvar);
                mostrarFeedback(resultado);
            }
        } catch (Exception ex) {
            mostrarFeedback("Erro inesperado: " + ex.getMessage(), true);
            ex.printStackTrace();
        }
    }

    private void gerarRelatorioUsuariosComMaisAtrasos() {
        try {
            String nomeArquivo = "relatorio_usuarios_mais_atrasos_top10.pdf";

            String caminhoSalvar = escolherCaminhoSalvar(nomeArquivo);
            if (caminhoSalvar != null) {
                String resultado = relatorioController.gerarRelatorioUsuariosComMaisAtrasos(caminhoSalvar);
                mostrarFeedback(resultado);
            }
        } catch (Exception ex) {
            mostrarFeedback("Erro inesperado: " + ex.getMessage(), true);
            ex.printStackTrace();
        }
    }

    private void mostrarFeedback(String mensagem) {
        mostrarFeedback(mensagem, mensagem.startsWith("Erro"));
    }

    private void mostrarFeedback(String mensagem, boolean isError) {
        mensagemFeedback.setText(mensagem);
        mensagemFeedback.setForeground(isError ? Color.RED : new Color(0, 128, 0));
    }

    private String escolherCaminhoSalvar(String nomeSugerido) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvar Relatório PDF");
        fileChooser.setSelectedFile(new File(nomeSugerido));

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String path = fileToSave.getAbsolutePath();
            if (!path.toLowerCase().endsWith(".pdf")) {
                path += ".pdf";
            }
            return path;
        }
        return null;
    }
}