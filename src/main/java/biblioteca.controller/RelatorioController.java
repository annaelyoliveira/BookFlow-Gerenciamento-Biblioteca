package biblioteca.controller;

import biblioteca.dao.EmprestimoDao;
import biblioteca.dao.ObraDao;
import biblioteca.dao.UsuarioDao;
import biblioteca.model.Emprestimo;
import biblioteca.model.Obra;
import biblioteca.model.Usuario;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RelatorioController {

    private EmprestimoDao emprestimoDao;
    private ObraDao obraDao;
    private UsuarioDao usuarioDao;

    public RelatorioController() {
        this.emprestimoDao = new EmprestimoDao();
        this.obraDao = new ObraDao();
        this.usuarioDao = new UsuarioDao();
    }

    public String gerarRelatorioEmprestimosMes(int mes, int ano, String caminhoArquivo) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(caminhoArquivo));
            document.open();

            document.add(new Paragraph("Relatório de Empréstimos Realizados em " + mes + "/" + ano));
            document.add(new Paragraph("\n"));

            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            table.addCell(new PdfPCell(new Phrase("Obra")));
            table.addCell(new PdfPCell(new Phrase("Leitor")));
            table.addCell(new PdfPCell(new Phrase("Matrícula Leitor")));
            table.addCell(new PdfPCell(new Phrase("Data Emprestimo")));
            table.addCell(new PdfPCell(new Phrase("Devolução Prevista")));
            table.addCell(new PdfPCell(new Phrase("Data Devolução")));

            List<Emprestimo> emprestimosDoMes = emprestimoDao.listar().stream()
                    .filter(e -> e.getDataEmprestimo().getMonthValue() == mes && e.getDataEmprestimo().getYear() == ano)
                    .collect(Collectors.toList());

            if (emprestimosDoMes.isEmpty()) {
                document.add(new Paragraph("Nenhum empréstimo registrado para o mês e ano especificados."));
            } else {
                for (Emprestimo e : emprestimosDoMes) {
                    table.addCell(e.getObra().getTitulo());
                    table.addCell(e.getUsuario().getNome());
                    table.addCell(String.valueOf(e.getUsuario().getMatricula()));
                    table.addCell(e.getDataEmprestimo().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    table.addCell(e.getDataDevolucaoPrevista().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    table.addCell(e.getDataDevolucaoReal() != null ? e.getDataDevolucaoReal().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "Empr. Ativo");
                }
                document.add(table);
            }

            document.close();
            return "Relatório de empréstimos gerado com sucesso em: " + caminhoArquivo;
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            return "Erro ao gerar relatório de empréstimos: " + e.getMessage();
        }
    }

    public String gerarRelatorioObrasMaisEmprestadas(String caminhoArquivo) {
        int topN = 5;
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(caminhoArquivo));
            document.open();

            document.add(new Paragraph("Relatório de Obras Mais Emprestadas (Top " + topN + ")"));
            document.add(new Paragraph("\n"));

            Map<Integer, Long> contagemEmprestimos = emprestimoDao.listar().stream()
                    .collect(Collectors.groupingBy(e -> e.getObra().getCodigo(), Collectors.counting()));

            Map<Obra, Long> obrasOrdenadas = contagemEmprestimos.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .limit(topN)
                    .collect(Collectors.toMap(
                            entry -> obraDao.buscarPorCodigo(entry.getKey()),
                            Map.Entry::getValue,
                            (e1, e2) -> e1, LinkedHashMap::new
                    ));

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            table.addCell(new PdfPCell(new Phrase("Obra")));
            table.addCell(new PdfPCell(new Phrase("Autor")));
            table.addCell(new PdfPCell(new Phrase("Empréstimos")));

            if (obrasOrdenadas.isEmpty()) {
                document.add(new Paragraph("Nenhuma obra encontrada ou registrada."));
            } else {
                for (Map.Entry<Obra, Long> entry : obrasOrdenadas.entrySet()) {
                    Obra obra = entry.getKey();
                    if (obra != null) {
                        table.addCell(obra.getTitulo());
                        table.addCell(obra.getAutor());
                        table.addCell(String.valueOf(entry.getValue()));
                    }
                }
                document.add(table);
            }

            document.close();
            return "Relatório de obras mais emprestadas gerado com sucesso em: " + caminhoArquivo;
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            return "Erro ao gerar relatório de obras mais emprestadas: " + e.getMessage();
        }
    }

    public String gerarRelatorioUsuariosComMaisAtrasos(String caminhoArquivo) {
        Document document = new Document();
        int topN = 5;
        try {
            PdfWriter.getInstance(document, new FileOutputStream(caminhoArquivo));
            document.open();

            document.add(new Paragraph("Relatório de Usuários com Mais Atrasos (Top " + topN + ")"));
            document.add(new Paragraph("\n"));

            Map<Integer, Long> contagemAtrasos = emprestimoDao.listar().stream()
                    .filter(e -> e.getDataDevolucaoReal() != null && e.getMultaAplicada() > 0)
                    .collect(Collectors.groupingBy(e -> e.getUsuario().getMatricula(), Collectors.counting()));

            Map<Usuario, Long> usuariosOrdenados = contagemAtrasos.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .limit(topN)
                    .collect(Collectors.toMap(
                            entry -> usuarioDao.buscarPorCodigo(entry.getKey()), // Converte matrícula para Usuario
                            Map.Entry::getValue,
                            (e1, e2) -> e1, LinkedHashMap::new
                    ));

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            table.addCell(new PdfPCell(new Phrase("Leitor")));
            table.addCell(new PdfPCell(new Phrase("Matrícula")));
            table.addCell(new PdfPCell(new Phrase("Nº de Atrasos")));

            if (usuariosOrdenados.isEmpty()) {
                document.add(new Paragraph("Nenhum usuário com atrasos registrados."));
            } else {
                for (Map.Entry<Usuario, Long> entry : usuariosOrdenados.entrySet()) {
                    Usuario usuario = entry.getKey();
                    if (usuario != null) {
                        table.addCell(usuario.getNome());
                        table.addCell(String.valueOf(usuario.getMatricula()));
                        table.addCell(String.valueOf(entry.getValue()));
                    }
                }
                document.add(table);
            }

            document.close();
            return "Relatório de usuários com mais atrasos gerado com sucesso em: " + caminhoArquivo;
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            return "Erro ao gerar relatório de usuários com mais atrasos: " + e.getMessage();
        }
    }
}