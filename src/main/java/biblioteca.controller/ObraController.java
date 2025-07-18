package biblioteca.controller;

import biblioteca.dao.ObraDao;
import biblioteca.model.Artigo;
import biblioteca.model.Livro;
import biblioteca.model.Obra;
import biblioteca.model.Revista;

public class ObraController {

    private ObraDao obraDao;

    public ObraController() {
        this.obraDao = new ObraDao();
    }


    public boolean cadastrarObra(int codigo, String titulo, String autor, int anoPublicacao, String tipoObra) {
        // Validação básica: verificar se já existe uma obra com o mesmo código
        if (obraDao.buscarPorCodigo(codigo) != null) {
            System.out.println("Erro no Controller: Obra com código " + codigo + " já existe.");
            return false; // Retorna false para indicar que o cadastro falhou (código duplicado)
        }

        Obra novaObra = null;
        switch (tipoObra) {
            case "Livro":
                novaObra = new Livro(codigo, titulo, autor, anoPublicacao);
                break;
            case "Revista":
                novaObra = new Revista(codigo, titulo, autor, anoPublicacao);
                break;
            case "Artigo":
                novaObra = new Artigo(codigo, titulo, autor, anoPublicacao);
                break;
            default:
                System.err.println("Tipo de obra inválido: " + tipoObra);
                return false;
        }

        if (novaObra != null) {
            obraDao.adicionar(novaObra);
            return true; // Retorna true para indicar sucesso
        }
        return false;
    }

    // Você pode adicionar outros métodos aqui, como listarObras, buscarObra, etc.
}