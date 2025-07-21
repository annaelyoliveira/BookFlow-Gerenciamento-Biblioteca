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
            return "Erro: Usuário com matrícula " + matriculaUsuario + " não encontrado.";
        }

        if (!obra.isStatus()) { // Verifica se o status da obra é 'emprestado' (false)
            return "Erro: Obra \"" + obra.getTitulo() + "\" já está emprestada.";
        }

        LocalDate dataEmprestimo = LocalDate.now();
        int tempoEmprestimoDias = obra.getTempoEmprestimo(); // Usa o método polimórfico da Obra
        LocalDate dataDevolucaoPrevista = dataEmprestimo.plusDays(tempoEmprestimoDias);


        Emprestimo novoEmprestimo = new Emprestimo(0, obra, usuario, dataEmprestimo, dataDevolucaoPrevista);
        emprestimoDao.adicionar(novoEmprestimo);

        obra.setStatus(false);
        obraDao.atualizar(obra);

        return "Empréstimo de \"" + obra.getTitulo() + "\" para \"" + usuario.getNome() + "\" realizado com sucesso. Devolução prevista: " + dataDevolucaoPrevista;
    }

    public String realizarDevolucao(int idEmprestimo) {
        Emprestimo emprestimo = emprestimoDao.buscarPorCodigo(idEmprestimo);
        if (emprestimo == null) {
            return "Erro: Empréstimo com ID " + idEmprestimo + " não encontrado.";
        }

        if (emprestimo.getDataDevolucaoReal() != null) {
            return "Erro: Empréstimo ID " + idEmprestimo + " já foi devolvido anteriormente.";
        }

        LocalDate dataDevolucaoReal = LocalDate.now();
        emprestimo.setDataDevolucaoReal(dataDevolucaoReal);

        double multa = calcularMultaPorAtraso(emprestimo.getDataDevolucaoPrevista(), dataDevolucaoReal);
        emprestimo.setMultaAplicada(multa);


        Obra obraDevolvida = emprestimo.getObra();
        obraDevolvida.setStatus(true);
        obraDao.atualizar(obraDevolvida);

        emprestimoDao.atualizar(emprestimo);

        String mensagem = "Devolução da obra \"" + obraDevolvida.getTitulo() + "\" (Empréstimo ID: " + idEmprestimo + ") realizada com sucesso.";
        if (multa > 0) {
            mensagem += " Multa aplicada: R$ " + String.format("%.2f", multa);
        } else {
            mensagem += " Devolução no prazo. Nenhuma multa aplicada.";
        }
        return mensagem;
    }

    private double calcularMultaPorAtraso(LocalDate dataPrevista, LocalDate dataReal) {
        if (dataReal.isAfter(dataPrevista)) {
            long diasAtraso = ChronoUnit.DAYS.between(dataPrevista, dataReal);
            // Defina sua regra de multa aqui, por exemplo, R$ 2,00 por dia de atraso
            return diasAtraso * 2.00; //
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

        emprestimo.setMultaPaga(true); //
        emprestimoDao.atualizar(emprestimo);
        return "Pagamento de multa para o empréstimo ID " + idEmprestimo + " registrado com sucesso.";
    }

    public List<Emprestimo> listarTodosEmprestimos() {
        return emprestimoDao.listar();
    }

}