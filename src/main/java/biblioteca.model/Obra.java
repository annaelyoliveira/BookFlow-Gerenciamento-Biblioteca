package biblioteca.model;

public abstract class Obra implements Emprestavel {

    private int codigo;
    private String titulo;
    private String autor;
    private int anoPublicacao;
    private boolean status;


    public Obra(int codigo, String titulo, String autor, int anoPublicacao) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.autor = autor;
        this.anoPublicacao = anoPublicacao;
        this.status = true;
    }

    public int getCodigo(){
        return this.codigo;
    }

    public void setCodigo(int codigo){
        this.codigo = codigo;
    }

    public String getTitulo(){
        return this.titulo;
    }

    public void setTitulo(String titulo){
        this.titulo = titulo;
    }

     public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getAnoPublicacao() {
        return anoPublicacao;
    }

    public void setAnoPublicacao(int anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }

    public boolean isStatus() { 
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public abstract int calcularPrazoDevolucaoDias();

    @Override
    public boolean verificarDisponibilidade() {
        return isStatus();
    }

    @Override
    public void emprestar() {
        setStatus(false);
    }

    @Override
    public void devolver() {
        setStatus(true);
    }
}
