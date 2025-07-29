package biblioteca.controller;

import biblioteca.dao.EmprestimoDao;
import biblioteca.dao.ObraDao;
import biblioteca.dao.PagamentoMultaDao;
import biblioteca.dao.UsuarioDao;
import biblioteca.model.Emprestimo;
import biblioteca.model.Obra;
import biblioteca.model.PagamentoMulta;
import biblioteca.model.Usuario;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class EmprestimoController {

    private EmprestimoDao emprestimoDao;
    private ObraDao obraDao;
    private UsuarioDao usuarioDao;
    private PagamentoMultaDao pagamentoMultaDao;

    public EmprestimoController() {
        this.emprestimoDao = new EmprestimoDao();
        this.obraDao = new ObraDao();
        this.usuarioDao = new UsuarioDao();
        this.pagamentoMultaDao = new PagamentoMultaDao();
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
        LocalDate dataDevolucaoPrevista = dataEmprestimo.plusDays(obra.getTempoEmprestimo());

        Emprestimo novoEmprestimo = new Emprestimo(0, obra, usuario, dataEmprestimo, dataDevolucaoPrevista);
        emprestimoDao.adicionar(novoEmprestimo);

        obra.setStatus(false);
        obraDao.atualizar(obra);

        return "Empréstimo de \"" + obra.getTitulo() + "\" para o leitor \"" + usuario.getNome() + "\" (Matrícula: " + usuario.getMatricula() + ") realizado com sucesso. Devolução prevista: " + dataDevolucaoPrevista;
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

        Emprestimo emprestimo = buscarEmprestimoAtivoPorObraELeitor(codigoObra, matriculaLeitor);

        if (emprestimo == null) {
            return "Erro: Obra \"" + obra.getTitulo() + "\" (Código: " + codigoObra + ") não está emprestada para o leitor " + leitor.getNome() + " (Matrícula: " + matriculaLeitor + ") ou já foi devolvida.";
        }

        emprestimo.setDataDevolucaoReal(LocalDate.now());

        double multa = calcularMultaPorAtraso(emprestimo.getDataDevolucaoPrevista(), emprestimo.getDataDevolucaoReal());
        emprestimo.setMultaAplicada(multa);

       obra.setStatus(true);
        obraDao.atualizar(obra);

        emprestimoDao.atualizar(emprestimo);

        String mensagem = "Devolução da obra \"" + obra.getTitulo() + "\" realizada com sucesso.";
        if (multa > 0) {
            mensagem += " Multa aplicada: R$ " + String.format("%.2f", multa);
        } else {
            mensagem += " Devolução no prazo. Nenhuma multa aplicada.";
        }
        return mensagem;
    }

    private Emprestimo buscarEmprestimoAtivoPorObraELeitor(int codigoObra, int matriculaLeitor) {
        for (Emprestimo emp : emprestimoDao.listar()) {
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

    public String registrarPagamentoMulta(int codigoObra, int matriculaLeitor, String metodoPagamento) {
        Obra obra = obraDao.buscarPorCodigo(codigoObra);
        if (obra == null) {
            return  "Erro: Obra com código " + codigoObra + " não encontrada para registrar pagamento de multa.";
        }

        Usuario leitor = usuarioDao.buscarPorCodigo(matriculaLeitor);
        if (leitor == null) {
            return "Erro: Leitor com matrícula " + matriculaLeitor + " não encontrado para registrar pagamento de multa.";
        }

        Emprestimo emprestimoComMulta = buscarEmprestimoDevolvidoComMulta(codigoObra, matriculaLeitor); // NOVO MÉTODO AUXILIAR

        if (emprestimoComMulta == null) {
            return "Erro: Nenhuma multa pendente encontrada para a obra \"" + obra.getTitulo() + "\" (Código: " + codigoObra + ") e leitor " + leitor.getNome() + " (Matrícula: " + matriculaLeitor + ").";
        }
        if (emprestimoComMulta.isMultaPaga()) {
            return "Erro: Multa para a obra \"" + obra.getTitulo() + "\" e leitor " + leitor.getNome() + " já foi paga.";
        }

        emprestimoComMulta.setMultaPaga(true);
        emprestimoDao.atualizar(emprestimoComMulta);

        PagamentoMulta novoPagamento = new PagamentoMulta(0, emprestimoComMulta.getId(), leitor.getMatricula(), emprestimoComMulta.getMultaAplicada(), LocalDate.now(), metodoPagamento);
        pagamentoMultaDao.adicionar(novoPagamento);

        return "Pagamento de multa (R$ " + String.format("%.2f", emprestimoComMulta.getMultaAplicada()) + ") para a obra \"" + obra.getTitulo() + "\" e leitor " + leitor.getNome() + " registrado com sucesso (" + metodoPagamento + ").";
    }

    public Emprestimo buscarEmprestimoDevolvidoComMulta(int codigoObra, int matriculaLeitor) {
        for (Emprestimo emp : emprestimoDao.listar()) {
            if (emp.getDataDevolucaoReal() != null &&
                    emp.getMultaAplicada() > 0 &&
                    !emp.isMultaPaga() &&
                    emp.getObra().getCodigo() == codigoObra &&
                    emp.getUsuario().getMatricula() == matriculaLeitor) {
                return emp;
            }
        }
        return null;
    }

    public List<Emprestimo> listarTodosEmprestimos() {

        return emprestimoDao.listar();
    }
}