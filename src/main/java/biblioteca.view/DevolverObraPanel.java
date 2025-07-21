package biblioteca.view;

import biblioteca.controller.EmprestimoController;

import javax.swing.*;
import java.awt.*;

public class DevolverObraPanel extends JPanel {

    private EmprestimoController emprestimoController;
    private JTextField campoIdEmprestimo;
    private JButton botaoConfirmarDevolucao;
    private JLabel mensagemFeedback;

    public DevolverObraPanel() {
        this.emprestimoController = new EmprestimoController();
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titulo = new JLabel("Realizar Devolução", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titulo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        add(new JLabel("ID do Empréstimo:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        campoIdEmprestimo = new JTextField(15);
        add(campoIdEmprestimo, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        botaoConfirmarDevolucao = new JButton("Confirmar Devolução");
        add(botaoConfirmarDevolucao, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        mensagemFeedback = new JLabel("");
        mensagemFeedback.setHorizontalAlignment(SwingConstants.CENTER);
        add(mensagemFeedback, gbc);

        botaoConfirmarDevolucao.addActionListener(e -> realizarDevolucao());
    }

    private void realizarDevolucao() {
        try {
            int idEmprestimo = Integer.parseInt(campoIdEmprestimo.getText());

            String resultado = emprestimoController.realizarDevolucao(idEmprestimo);
            mensagemFeedback.setText(resultado);
            mensagemFeedback.setForeground(resultado.startsWith("Erro") ? Color.RED : new Color(0, 128, 0));

            // Limpar campo
            if (!resultado.startsWith("Erro")) {
                campoIdEmprestimo.setText("");
            }

        } catch (NumberFormatException ex) {
            mensagemFeedback.setText("Erro: ID do Empréstimo deve ser um número válido.");
            mensagemFeedback.setForeground(Color.RED);
        } catch (Exception ex) {
            mensagemFeedback.setText("Erro inesperado: " + ex.getMessage());
            mensagemFeedback.setForeground(Color.RED);
            ex.printStackTrace();
        }
    }
}