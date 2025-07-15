package biblioteca.model;

public class Revista extends Obra {

    public Revista(int codigo, String titulo, String autor, int anoPublicacao) {
        super(codigo, titulo, autor, anoPublicacao);
    }

    public int getTempoEmprestimo(){
        return 3;
    }
}
