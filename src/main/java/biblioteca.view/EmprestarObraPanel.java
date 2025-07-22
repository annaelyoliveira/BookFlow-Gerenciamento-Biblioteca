package biblioteca.view;

import biblioteca.controller.EmprestimoController;

import javax.swing.*;
import java.awt.*;

public class EmprestarObraPanel extends JPanel {

    private EmprestimoController emprestimoController;
    private JTextField campoCodigoObra;
    private JTextField campoMatriculaUsuario;
    private JButton botaoConfirmarEmprestimo;
    private JLabel mensagemFeedback;

    public EmprestarObraPanel() {
        this.emprestimoController = new EmprestimoController();
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel titulo = new JLabel("Realizar Empréstimo", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(titulo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        add(new JLabel("Código da Obra:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        campoCodigoObra = new JTextField(15);
        campoCodigoObra.setMaximumSize(new Dimension(200, campoCodigoObra.getPreferredSize().height));
        add(campoCodigoObra, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        add(new JLabel("Matrícula do Usuário:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        campoMatriculaUsuario = new JTextField(15);
        campoMatriculaUsuario.setMaximumSize(new Dimension(200, campoMatriculaUsuario.getPreferredSize().height));
        add(campoMatriculaUsuario, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        botaoConfirmarEmprestimo = new JButton("Confirmar Empréstimo");
        botaoConfirmarEmprestimo.setPreferredSize(new Dimension(200, 40));
        botaoConfirmarEmprestimo.setMinimumSize(new Dimension(200, 40));
        botaoConfirmarEmprestimo.setMaximumSize(new Dimension(200, 40));
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;

        add(botaoConfirmarEmprestimo, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        mensagemFeedback = new JLabel("");
        mensagemFeedback.setHorizontalAlignment(SwingConstants.CENTER);
        add(mensagemFeedback, gbc);

        botaoConfirmarEmprestimo.addActionListener(e -> realizarEmprestimo());
    }

    private void realizarEmprestimo() {
        try {
            int codigoObra = Integer.parseInt(campoCodigoObra.getText());
            int matriculaUsuario = Integer.parseInt(campoMatriculaUsuario.getText());

            String resultado = emprestimoController.realizarEmprestimo(codigoObra, matriculaUsuario);
            mensagemFeedback.setText(resultado);
            mensagemFeedback.setForeground(resultado.startsWith("Erro") ? Color.RED : new Color(0, 128, 0));

            if (!resultado.startsWith("Erro")) {
                campoCodigoObra.setText("");
                campoMatriculaUsuario.setText("");
            }

        } catch (NumberFormatException ex) {
            mensagemFeedback.setText("Erro: Código da Obra e Matrícula devem ser números válidos.");
            mensagemFeedback.setForeground(Color.RED);
        } catch (Exception ex) {
            mensagemFeedback.setText("Erro inesperado: " + ex.getMessage());
            mensagemFeedback.setForeground(Color.RED);
            ex.printStackTrace();
        }
    }
}