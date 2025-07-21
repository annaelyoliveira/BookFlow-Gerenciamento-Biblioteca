// src/main/java/biblioteca/view/ListarObrasPanel.java
package biblioteca.view;

import biblioteca.controller.ObraController;
import biblioteca.model.Obra;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;


public class ListarObrasPanel extends JPanel {
    private ObraController obraController;
    private JTable tabelaObras;
    private DefaultTableModel modeloTabela;
    private JTextField campoPesquisa;
    private JComboBox<String> comboTipoPesquisa;
    private JButton botaoPesquisar;

    public ListarObrasPanel() {
        this.obraController = new ObraController();
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        JLabel tituloPagina = new JLabel("Listagem e Pesquisa de Obras", SwingConstants.CENTER);
        tituloPagina.setFont(new Font("Arial", Font.BOLD, 24));
        // Adiciona o título na parte superior (NORTH) do BorderLayout
        add(tituloPagina, BorderLayout.NORTH);

        JPanel painelPesquisa = new JPanel(new FlowLayout(FlowLayout.CENTER));

        campoPesquisa = new JTextField(20);
        painelPesquisa.add(campoPesquisa);

        String[] tiposPesquisa = {"Título", "Autor", "Tipo"};
        comboTipoPesquisa = new JComboBox<>(tiposPesquisa);
        painelPesquisa.add(comboTipoPesquisa);

        botaoPesquisar = new JButton("Pesquisar");
        painelPesquisa.add(botaoPesquisar);

        add(painelPesquisa, BorderLayout.NORTH);

        // --- Configuração da Tabela ---
        String[] colunas = {"Código", "Título", "Autor", "Ano", "Status", "Tipo", "Prazo Empréstimo"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaObras = new JTable(modeloTabela);
        tabelaObras.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(tabelaObras);

        add(scrollPane, BorderLayout.CENTER);

        botaoPesquisar.addActionListener(e -> carregarObrasNaTabela());


        carregarObrasNaTabela();
    }

    private void carregarObrasNaTabela() {
        modeloTabela.setRowCount(0);
        List<Obra> obras;

        String termo = campoPesquisa.getText();
        String tipoSelecionado = (String) comboTipoPesquisa.getSelectedItem();

        if (termo == null || termo.trim().isEmpty()) {
            obras = obraController.listarTodasObras();
        } else {
            obras = obraController.buscarObras(termo, tipoSelecionado.toLowerCase());
        }

        for (Obra obra : obras) {
            String status = obra.isStatus() ? "Disponível" : "Emprestado";
            Object[] linha = {
                    obra.getCodigo(),
                    obra.getTitulo(),
                    obra.getAutor(),
                    obra.getAnoPublicacao(),
                    status,

                    obra.getClass().getSimpleName(),
                    obra.getTempoEmprestimo() + " dias"
            };
            modeloTabela.addRow(linha);
        }
    }
}