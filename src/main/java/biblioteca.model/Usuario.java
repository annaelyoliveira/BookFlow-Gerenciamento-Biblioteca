package biblioteca.model;

public class Usuario {
    
    private String nome;
    private String login;
    private int matricula;
    private String categoriaLeitor;
    private String telefone;
    private String email;
    private String perfilAcesso;
    private String senha;

    public Usuario(String nome, String login, int matricula, String categoriaLeitor, String telefone, String email, String perfilAcesso, String senha) {
        this.nome = nome;
        this.login = login;
        this.matricula = matricula;
        this.categoriaLeitor = categoriaLeitor;
        this.telefone = telefone;
        this.email = email;
        this.perfilAcesso = perfilAcesso;
        this.senha = senha;
    }

    public Usuario(String nome, String login, String telefone, String email, String perfilAcesso, String senha) {
        this(nome, login, 0, "Não se aplica", telefone, email, perfilAcesso, senha);
    }

    public Usuario(String nome, int matricula, String categoriaLeitor, String telefone, String email) {
        this(nome, "", matricula, categoriaLeitor, telefone, email, "Leitor", "");
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public String getCategoriaLeitor() {
        return categoriaLeitor;
    }

    public void setCategoriaLeitor(String categoriaLeitor) {
        this.categoriaLeitor = categoriaLeitor;
    }


    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPerfilAcesso() {
        return perfilAcesso;
    }

    public void setPerfilAcesso(String perfilAcesso) {
        this.perfilAcesso = perfilAcesso;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}

