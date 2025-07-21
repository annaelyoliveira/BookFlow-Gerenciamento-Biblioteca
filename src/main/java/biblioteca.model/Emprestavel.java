package biblioteca.model;

public interface Emprestavel {

    boolean verificarDisponibilidade();
    void emprestar();
    void devolver();
    int calcularPrazoDevolucaoDias();

}
