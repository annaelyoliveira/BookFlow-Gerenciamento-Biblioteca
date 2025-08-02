package biblioteca.view;

import biblioteca.controller.EmprestimoController;
import biblioteca.model.Emprestimo;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
public class RegistrarPagamentoMultaPanel extends JPanel {

    private EmprestimoController emprestimoController;
    private JTextField campoCodigoObra;
    private JTextField campoMatriculaLeitor;
    private JLabel labelValorMulta;
    private JComboBox<String> comboMetodoPagamento;
    private JButton botaoConfirmarPagamento;
    private JLabel mensagemFeedback;

    private Emprestimo emprestimoComMultaEncontrado = null;

    public RegistrarPagamentoMultaPanel() {
        this.emprestimoController = new EmprestimoController();
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titulo = new JLabel("Registrar Pagamento de Multa", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titulo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        add(new JLabel("Código da Obra:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        campoCodigoObra = new JTextField(15);
        campoCodigoObra.setMaximumSize(new Dimension(200, campoCodigoObra.getPreferredSize().height));
        add(campoCodigoObra, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Matrícula do Leitor:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        campoMatriculaLeitor = new JTextField(15);
        campoMatriculaLeitor.setMaximumSize(new Dimension(200, campoMatriculaLeitor.getPreferredSize().height));
        add(campoMatriculaLeitor, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JButton botaoBuscarMulta = new JButton("Buscar Multa Pendente");
        add(botaoBuscarMulta, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        labelValorMulta = new JLabel("Valor da Multa: R$ ---");
        labelValorMulta.setHorizontalAlignment(SwingConstants.CENTER);
        add(labelValorMulta, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 1;
        add(new JLabel("Método de Pagamento:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5;
        String[] metodos = {"Dinheiro", "Pix", "Cartão"};
        comboMetodoPagamento = new JComboBox<>(metodos);
        add(comboMetodoPagamento, gbc);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        botaoConfirmarPagamento = new JButton("Confirmar Pagamento");
        botaoConfirmarPagamento.setPreferredSize(new Dimension(200, 40));
        botaoConfirmarPagamento.setMinimumSize(new Dimension(200, 40));
        botaoConfirmarPagamento.setMaximumSize(new Dimension(200, 40));
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        add(botaoConfirmarPagamento, gbc);

        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;
        mensagemFeedback = new JLabel("");
        mensagemFeedback.setHorizontalAlignment(SwingConstants.CENTER);
        add(mensagemFeedback, gbc);

        botaoBuscarMulta.addActionListener(e -> buscarMultaParaRegistro());
        botaoConfirmarPagamento.addActionListener(e -> registrarPagamentoMulta());
        botaoConfirmarPagamento.setEnabled(false);
    }

    private void buscarMultaParaRegistro() {
        labelValorMulta.setText("Valor da Multa: R$ ---");
        mensagemFeedback.setText("");
        botaoConfirmarPagamento.setEnabled(false);

        try {
            int codigoObra = Integer.parseInt(campoCodigoObra.getText());
            int matriculaLeitor = Integer.parseInt(campoMatriculaLeitor.getText());

            Emprestimo emprestimo = emprestimoController.buscarEmprestimoDevolvidoComMulta(codigoObra, matriculaLeitor);

            if (emprestimo != null) {
                emprestimoComMultaEncontrado = emprestimo;
                labelValorMulta.setText(String.format("Valor da Multa: R$ %.2f (Devolução em: %s)",
                        emprestimo.getMultaAplicada(), emprestimo.getDataDevolucaoReal().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
                labelValorMulta.setForeground(Color.BLUE);
                botaoConfirmarPagamento.setEnabled(true);
                mensagemFeedback.setText("Multa pendente encontrada. Selecione o método de pagamento.");
                mensagemFeedback.setForeground(new Color(0, 128, 0));
            } else {
                labelValorMulta.setText("Valor da Multa: R$ ---");
                mensagemFeedback.setText("Nenhuma multa pendente encontrada para esta obra e leitor.");
                mensagemFeedback.setForeground(Color.RED);
            }

        } catch (NumberFormatException ex) {
            mensagemFeedback.setText("Erro: Código da Obra e Matrícula do Leitor devem ser números válidos.");
            mensagemFeedback.setForeground(Color.RED);
        } catch (Exception ex) {
            mensagemFeedback.setText("Erro inesperado ao buscar multa: " + ex.getMessage());
            mensagemFeedback.setForeground(Color.RED);
            ex.printStackTrace();
        }
    }


    private void registrarPagamentoMulta() {
        if (emprestimoComMultaEncontrado == null) {
            mensagemFeedback.setText("Erro: Busque uma multa pendente antes de confirmar o pagamento.");
            mensagemFeedback.setForeground(Color.RED);
            return;
        }

        try {
            int codigoObra = Integer.parseInt(campoCodigoObra.getText());
            int matriculaLeitor = Integer.parseInt(campoMatriculaLeitor.getText());
            String metodoPagamento = (String) comboMetodoPagamento.getSelectedItem();

            String resultado = emprestimoController.registrarPagamentoMulta(codigoObra, matriculaLeitor, metodoPagamento);

            mensagemFeedback.setText(resultado);
            mensagemFeedback.setForeground(resultado.startsWith("Erro") ? Color.RED : new Color(0, 128, 0));

            if (!resultado.startsWith("Erro")) {
                campoCodigoObra.setText("");
                campoMatriculaLeitor.setText("");
                comboMetodoPagamento.setSelectedIndex(0);
                labelValorMulta.setText("Valor da Multa: R$ ---");
                botaoConfirmarPagamento.setEnabled(false);
            }

        } catch (NumberFormatException ex) {
            mensagemFeedback.setText("Erro: Código da Obra e Matrícula do Leitor devem ser números válidos.");
            mensagemFeedback.setForeground(Color.RED);
        } catch (Exception ex) {
            mensagemFeedback.setText("Erro inesperado ao registrar pagamento: " + ex.getMessage());
            mensagemFeedback.setForeground(Color.RED);
            ex.printStackTrace();
        }
    }
}