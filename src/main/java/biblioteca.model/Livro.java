package biblioteca.model;

public class Livro extends Obra{
    
    public Livro(int codigo, String titulo, String autor, int anoPublicacao) {
        super(codigo, titulo, autor, anoPublicacao);
    }

    public int getTempoEmprestimo(){
        return 7;
    }
}
