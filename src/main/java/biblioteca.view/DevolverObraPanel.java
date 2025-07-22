package biblioteca.view;

import biblioteca.controller.EmprestimoController;

import javax.swing.*;
import java.awt.*;

public class DevolverObraPanel extends JPanel {

    private EmprestimoController emprestimoController;
    private JTextField campoCodigoObra;
    private JTextField campoMatriculaLeitor;
    private JButton botaoConfirmarDevolucao;
    private JLabel mensagemFeedback;

    public DevolverObraPanel() {
        this.emprestimoController = new EmprestimoController();
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);


        JLabel titulo = new JLabel("Realizar Devolução", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        add(titulo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0;
        add(new JLabel("Código da Obra:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;

        campoCodigoObra = new JTextField(15);
        campoCodigoObra.setMaximumSize(new Dimension(200, campoCodigoObra.getPreferredSize().height));
        add(campoCodigoObra, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0;
        add(new JLabel("Matrícula do Leitor:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;

        campoMatriculaLeitor = new JTextField(15);
        campoMatriculaLeitor.setMaximumSize(new Dimension(200, campoMatriculaLeitor.getPreferredSize().height));
        add(campoMatriculaLeitor, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        botaoConfirmarDevolucao = new JButton("Confirmar Devolução");

        botaoConfirmarDevolucao.setPreferredSize(new Dimension(200, 40));
        botaoConfirmarDevolucao.setMinimumSize(new Dimension(200, 40));
        botaoConfirmarDevolucao.setMaximumSize(new Dimension(200, 40));
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;

        add(botaoConfirmarDevolucao, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;

        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;

        mensagemFeedback = new JLabel("");
        mensagemFeedback.setHorizontalAlignment(SwingConstants.CENTER);
        add(mensagemFeedback, gbc);

        botaoConfirmarDevolucao.addActionListener(e -> realizarDevolucao());
    }

    private void realizarDevolucao() {
        try {
            int codigoObra = Integer.parseInt(campoCodigoObra.getText());
            int matriculaLeitor = Integer.parseInt(campoMatriculaLeitor.getText());

            String resultado = emprestimoController.realizarDevolucao(codigoObra, matriculaLeitor);
            mensagemFeedback.setText(resultado);
            mensagemFeedback.setForeground(resultado.startsWith("Erro") ? Color.RED : new Color(0, 128, 0));

            if (!resultado.startsWith("Erro")) {
                campoCodigoObra.setText("");
                campoMatriculaLeitor.setText("");
            }

        } catch (NumberFormatException ex) {
            mensagemFeedback.setText("Erro: Código da Obra e Matrícula do Leitor devem ser números válidos.");
            mensagemFeedback.setForeground(Color.RED);
        } catch (Exception ex) {
            mensagemFeedback.setText("Erro inesperado: " + ex.getMessage());
            mensagemFeedback.setForeground(Color.RED);
            ex.printStackTrace();
        }
    }
}