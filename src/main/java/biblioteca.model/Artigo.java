package biblioteca.model;

public class Artigo extends Obra{
    
    public Artigo(int codigo, String titulo, String autor, int anoPublicacao) {
        super(codigo, titulo, autor, anoPublicacao);
    }

    public int calcularPrazoDevolucaoDias(){

        return 2;
    }
}
