package biblioteca.model;

import java.time.LocalDate;

public class PagamentoMulta {

    private int id;
    private int emprestimoId;
    private int usuarioMatricula;
    private double valorPago;
    private LocalDate dataPagamento;
    private String metodoPagamento;

    public PagamentoMulta(int id, int emprestimoId, int usuarioMatricula, double valorPago, LocalDate dataPagamento, String metodoPagamento) {
        this.id = id;
        this.emprestimoId = emprestimoId;
        this.usuarioMatricula = usuarioMatricula;
        this.valorPago = valorPago;
        this.dataPagamento = dataPagamento;
        this.metodoPagamento = metodoPagamento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmprestimoId() {

        return emprestimoId;
    }

    public void setEmprestimoId(int emprestimoId) {

        this.emprestimoId = emprestimoId;
    }

    public int getUsuarioMatricula() {

        return usuarioMatricula;
    }

    public void setUsuarioMatricula(int usuarioMatricula) {

        this.usuarioMatricula = usuarioMatricula;
    }

    public double getValorPago() {

        return valorPago;
    }

    public void setValorPago(double valorPago) {

        this.valorPago = valorPago;
    }

    public LocalDate getDataPagamento() {

        return dataPagamento;
    }

    public void setDataPagamento(LocalDate dataPagamento) {

        this.dataPagamento = dataPagamento;
    }

    public String getMetodoPagamento() {

        return metodoPagamento;
    }

    public void setMetodoPagamento(String metodoPagamento) {

        this.metodoPagamento = metodoPagamento;
    }
}
