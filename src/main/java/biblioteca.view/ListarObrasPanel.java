package biblioteca.view;

import biblioteca.controller.ObraController;
import biblioteca.controller.EmprestimoController;
import biblioteca.model.Obra;
import biblioteca.model.Emprestimo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;


public class ListarObrasPanel extends JPanel {
    private ObraController obraController;
    private EmprestimoController emprestimoController;
    private JTable tabelaObras;
    private DefaultTableModel modeloTabela;
    private JTextField campoPesquisa;
    private JComboBox<String> comboTipoPesquisa;
    private JButton botaoPesquisar;
    private JButton btnExcluirObra;

    public ListarObrasPanel() {
        this.obraController = new ObraController();
        this.emprestimoController = new EmprestimoController();
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        JLabel tituloPagina = new JLabel("Listagem e Pesquisa de Obras", SwingConstants.CENTER);
        tituloPagina.setFont(new Font("Arial", Font.BOLD, 24));
        add(tituloPagina, BorderLayout.NORTH);

        JPanel painelControles = new JPanel(new FlowLayout(FlowLayout.CENTER));

        campoPesquisa = new JTextField(20);
        painelControles.add(campoPesquisa);

        String[] tiposPesquisa = {"Título", "Autor", "Tipo"};
        comboTipoPesquisa = new JComboBox<>(tiposPesquisa);
        painelControles.add(comboTipoPesquisa);

        botaoPesquisar = new JButton("Pesquisar");
        painelControles.add(botaoPesquisar);

        btnExcluirObra = new JButton("Excluir Obra");
        btnExcluirObra.setEnabled(false);
        painelControles.add(btnExcluirObra);

        add(painelControles, BorderLayout.NORTH);

        String[] colunas = {"Código", "Título", "Autor", "Ano", "Status", "Tipo", "Prazo Empréstimo",  "Data Empréstimo"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {

                return false;
            }
        };
        tabelaObras = new JTable(modeloTabela);
        tabelaObras.setFillsViewportHeight(true);
        tabelaObras.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabelaObras.getSelectedRow() != -1) {
                btnExcluirObra.setEnabled(true);
            } else {
                btnExcluirObra.setEnabled(false);
            }
        });
        JScrollPane scrollPane = new JScrollPane(tabelaObras);
        add(scrollPane, BorderLayout.CENTER);

        botaoPesquisar.addActionListener(e -> carregarObrasNaTabela());
        btnExcluirObra.addActionListener(e -> excluirObra()); // Adiciona o listener ao botão

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

        List<Emprestimo> todosEmprestimos = emprestimoController.listarTodosEmprestimos();

        for (Obra obra : obras) {
            String status = obra.isStatus() ? "Disponível" : "Emprestado";

            String dataEmprestimoStr = "";

            for (Emprestimo emprestimo : todosEmprestimos) {
                if (emprestimo.getObra().getCodigo() == obra.getCodigo()) {
                    if (!obra.isStatus() && emprestimo.getDataDevolucaoReal() == null) {
                        dataEmprestimoStr = formatarData(emprestimo.getDataEmprestimo());
                        break;
                    }
                }
            }

            Object[] linha = {
                    obra.getCodigo(),
                    obra.getTitulo(),
                    obra.getAutor(),
                    obra.getAnoPublicacao(),
                    status,
                    obra.getClass().getSimpleName(),
                    obra.calcularPrazoDevolucaoDias() + " dias",
                    dataEmprestimoStr,
            };
            modeloTabela.addRow(linha);
        }
    }

    private String formatarData(LocalDate data) {
        if (data == null) {
            return "";
        }
        return data.toString();
    }
    private void excluirObra() {
        int linhaSelecionada = tabelaObras.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma obra para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir esta obra?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
        if (confirmacao == JOptionPane.YES_OPTION) {
            int codigoObra = (int) modeloTabela.getValueAt(linhaSelecionada, 0);

            boolean sucesso = obraController.removerObra(codigoObra);

            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Obra excluída com sucesso.");
                carregarObrasNaTabela();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao excluir obra.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}