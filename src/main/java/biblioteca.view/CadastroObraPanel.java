package biblioteca.view;

import biblioteca.controller.ObraController;

import javax.swing.*;
import java.awt.*;

public class CadastroObraPanel extends JPanel {

    private ObraController obraController;
    private JTextField campoCodigo;
    private JTextField campoTitulo;
    private JTextField campoAutor;
    private JTextField campoAnoPublicacao;
    private JComboBox<String> comboTipoObra;
    private JButton botaoSalvar;
    private JLabel mensagemFeedback;

    public CadastroObraPanel() {
        this.obraController = new ObraController();
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel tituloPagina = new JLabel("Cadastro de Obras Literárias", SwingConstants.CENTER);
        tituloPagina.setFont(new Font("Arial", Font.BOLD, 24));
        add(tituloPagina, BorderLayout.NORTH);

        JPanel painelFormulario = new JPanel();
        painelFormulario.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        painelFormulario.add(new JLabel("Código:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        campoCodigo = new JTextField(20);
        painelFormulario.add(campoCodigo, gbc);

        int currentGridY = 1;

        gbc.gridx = 0; gbc.gridy = currentGridY;
        painelFormulario.add(new JLabel("Título:"), gbc);
        gbc.gridx = 1; gbc.gridy = currentGridY++;
        campoTitulo = new JTextField(20); painelFormulario.add(campoTitulo, gbc);

        gbc.gridx = 0; gbc.gridy = currentGridY;
        painelFormulario.add(new JLabel("Autor:"), gbc);
        gbc.gridx = 1; gbc.gridy = currentGridY++;
        campoAutor = new JTextField(20); painelFormulario.add(campoAutor, gbc);

        gbc.gridx = 0; gbc.gridy = currentGridY;
        painelFormulario.add(new JLabel("Ano Publicação:"), gbc);
        gbc.gridx = 1; gbc.gridy = currentGridY++;
        campoAnoPublicacao = new JTextField(20); painelFormulario.add(campoAnoPublicacao, gbc);

        gbc.gridx = 0; gbc.gridy = currentGridY;
        painelFormulario.add(new JLabel("Tipo de Obra:"), gbc);
        gbc.gridx = 1; gbc.gridy = currentGridY++;
        String[] tipos = {"Livro", "Revista", "Artigo"};
        comboTipoObra = new JComboBox<>(tipos); painelFormulario.add(comboTipoObra, gbc);

        add(painelFormulario, BorderLayout.CENTER);

        JPanel painelRodape = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botaoSalvar = new JButton("Salvar Obra");
        painelRodape.add(botaoSalvar);
        mensagemFeedback = new JLabel("");
        painelRodape.add(mensagemFeedback);
        add(painelRodape, BorderLayout.SOUTH);

        botaoSalvar.addActionListener(e -> cadastrarObra());
    }

    private void cadastrarObra() {
        try {
            int codigo = Integer.parseInt(campoCodigo.getText());
            String titulo = campoTitulo.getText();
            String autor = campoAutor.getText();
            int anoPublicacao = Integer.parseInt(campoAnoPublicacao.getText());
            String tipoObra = (String) comboTipoObra.getSelectedItem();

            boolean sucesso = obraController.cadastrarObra(codigo, titulo, autor, anoPublicacao, tipoObra);

            if (sucesso) {
                mensagemFeedback.setText("Obra cadastrada com sucesso!");
                mensagemFeedback.setForeground(new Color(0, 128, 0));
                campoCodigo.setText("");
                campoTitulo.setText("");
                campoAutor.setText("");
                campoAnoPublicacao.setText("");
                comboTipoObra.setSelectedIndex(0);
            } else {
                mensagemFeedback.setText("Erro ao cadastrar obra. Verifique o código (já existente ou inválido).");
                mensagemFeedback.setForeground(Color.RED);
            }
        } catch (NumberFormatException ex) {
            mensagemFeedback.setText("Erro: Código e Ano de Publicação devem ser números válidos.");
            mensagemFeedback.setForeground(Color.RED);
        } catch (Exception ex) {
            mensagemFeedback.setText("Erro inesperado: " + ex.getMessage());
            mensagemFeedback.setForeground(Color.RED);
            ex.printStackTrace();
        }
    }
}