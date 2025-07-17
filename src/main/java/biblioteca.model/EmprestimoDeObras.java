package biblioteca.model;

public interface EmprestimoDeObras {

    boolean verificarDisponibilidade();
    void emprestar();
    void devolver();
    int calcularPrazoDevolucaoDias();

}
