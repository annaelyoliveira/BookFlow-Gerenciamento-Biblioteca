package biblioteca.controller;

import biblioteca.dao.EmprestimoDao;
import biblioteca.dao.ObraDao;
import biblioteca.dao.UsuarioDao;
import biblioteca.model.Emprestimo;
import biblioteca.model.Obra;
import biblioteca.model.Usuario;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class EmprestimoController {

    private EmprestimoDao emprestimoDao;
    private ObraDao obraDao;
    private UsuarioDao usuarioDao;

    public EmprestimoController() {
        this.emprestimoDao = new EmprestimoDao();
        this.obraDao = new ObraDao();
        this.usuarioDao = new UsuarioDao();
    }

    public String realizarEmprestimo(int codigoObra, int matriculaUsuario) {
        Obra obra = obraDao.buscarPorCodigo(codigoObra);
        if (obra == null) {
            return "Erro: Obra com código " + codigoObra + " não encontrada.";
        }

        Usuario usuario = usuarioDao.buscarPorCodigo(matriculaUsuario);
        if (usuario == null) {
            return "Erro: Usuário com matrícula " + matriculaUsuario + " não encontrada.";
        }

        if (!obra.isStatus()) {
            return "Erro: Obra \"" + obra.getTitulo() + "\" já está emprestada.";
        }

        LocalDate dataEmprestimo = LocalDate.now();
        int tempoEmprestimoDias = obra.getTempoEmprestimo();
        LocalDate dataDevolucaoPrevista = dataEmprestimo.plusDays(tempoEmprestimoDias);

        Emprestimo novoEmprestimo = new Emprestimo(0, obra, usuario, dataEmprestimo, dataDevolucaoPrevista);
        emprestimoDao.adicionar(novoEmprestimo);

        obra.setStatus(false);
        obraDao.atualizar(obra);

        return "Empréstimo de \"" + obra.getTitulo() + "\" para o leitor \"" + usuario.getNome() + "\" (Matrícula: " + usuario.getMatricula() + ") realizado com sucesso. Devolução prevista: " + dataDevolucaoPrevista + ". ID do Empréstimo: " + novoEmprestimo.getId();
    }

    public String realizarDevolucao(int codigoObra, int matriculaLeitor) {
        Obra obra = obraDao.buscarPorCodigo(codigoObra);
        if (obra == null) {
            return "Erro: Obra com código " + codigoObra + " não encontrada para devolução.";
        }
        Usuario leitor = usuarioDao.buscarPorCodigo(matriculaLeitor);
        if (leitor == null) {
            return "Erro: Leitor com matrícula " + matriculaLeitor + " não encontrado para devolução.";
        }

        Emprestimo emprestimo = buscarEmprestimoAtivoPorObraELeitor(codigoObra, matriculaLeitor); // NOVO MÉTODO AUXILIAR

        if (emprestimo == null) {
            return "Erro: Obra \"" + obra.getTitulo() + "\" (Código: " + codigoObra + ") não está emprestada para o leitor " + leitor.getNome() + " (Matrícula: " + matriculaLeitor + ") ou já foi devolvida.";
        }

        LocalDate dataDevolucaoReal = LocalDate.now();
        emprestimo.setDataDevolucaoReal(dataDevolucaoReal);

        double multa = calcularMultaPorAtraso(emprestimo.getDataDevolucaoPrevista(), dataDevolucaoReal);
        emprestimo.setMultaAplicada(multa);

        Obra obraDevolvida = emprestimo.getObra();
        obraDevolvida.setStatus(true);
        obraDao.atualizar(obraDevolvida);

        emprestimoDao.atualizar(emprestimo);

        String mensagem = "Devolução da obra \"" + obraDevolvida.getTitulo() + "\" (Empréstimo ID: " + emprestimo.getId() + ") realizada com sucesso.";
        if (multa > 0) {
            mensagem += " Multa aplicada: R$ " + String.format("%.2f", multa);
        } else {
            mensagem += " Devolução no prazo. Nenhuma multa aplicada.";
        }
        return mensagem;
    }

    private Emprestimo buscarEmprestimoAtivoPorObraELeitor(int codigoObra, int matriculaLeitor) {
        List<Emprestimo> todosEmprestimos = emprestimoDao.listar();
        for (Emprestimo emp : todosEmprestimos) {
            if (emp.getDataDevolucaoReal() == null &&
                    emp.getObra().getCodigo() == codigoObra &&
                    emp.getUsuario().getMatricula() == matriculaLeitor) {
                return emp;
            }
        }
        return null;
    }

    private double calcularMultaPorAtraso(LocalDate dataPrevista, LocalDate dataReal) {
        if (dataReal.isAfter(dataPrevista)) {
            long diasAtraso = ChronoUnit.DAYS.between(dataPrevista, dataReal);
            return diasAtraso * 2.00;
        }
        return 0.0;
    }

    public String registrarPagamentoMulta(int idEmprestimo) {
        Emprestimo emprestimo = emprestimoDao.buscarPorCodigo(idEmprestimo);
        if (emprestimo == null) {
            return "Erro: Empréstimo com ID " + idEmprestimo + " não encontrado.";
        }
        if (emprestimo.getMultaAplicada() == 0.0) {
            return "Erro: Não há multa aplicada para o empréstimo ID " + idEmprestimo + ".";
        }
        if (emprestimo.isMultaPaga()) {
            return "Erro: Multa do empréstimo ID " + idEmprestimo + " já foi paga.";
        }

        emprestimo.setMultaPaga(true);
        emprestimoDao.atualizar(emprestimo);
        return "Pagamento de multa para o empréstimo ID " + idEmprestimo + " registrado com sucesso.";
    }

    public List<Emprestimo> listarTodosEmprestimos() {
        return emprestimoDao.listar();
    }
}