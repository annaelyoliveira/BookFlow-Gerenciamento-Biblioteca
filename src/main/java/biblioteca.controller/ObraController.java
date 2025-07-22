package biblioteca.controller;

import biblioteca.dao.ObraDao;
import biblioteca.model.Artigo;
import biblioteca.model.Livro;
import biblioteca.model.Obra;
import biblioteca.model.Revista;

import java.util.ArrayList;
import java.util.List;

public class ObraController {
    private ObraDao obraDao;

    public ObraController() {
        this.obraDao = new ObraDao();
    }

    public boolean cadastrarObra(int codigo, String titulo, String autor, int anoPublicacao, String tipoObra) {
        if (obraDao.buscarPorCodigo(codigo) != null) {
            System.out.println("Erro no Controller: Obra com código " + codigo + " já existe.");
            return false;
        }

        Obra novaObra;
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

        obraDao.adicionar(novaObra);
        return true;
    }

    public List<Obra> listarTodasObras() {
        return obraDao.listar();
    }

    public List<Obra> buscarObras(String termoPesquisa, String tipoPesquisa) {
        List<Obra> resultados = new ArrayList<>();
        List<Obra> todasObras = obraDao.listar();

        if (termoPesquisa == null || termoPesquisa.trim().isEmpty()) {
            return todasObras;
        }

        String termoLowerCase = termoPesquisa.toLowerCase();

        for (Obra obra : todasObras) {
            boolean adiciona = false;
            switch (tipoPesquisa) {
                case "titulo":
                    if (obra.getTitulo().toLowerCase().contains(termoLowerCase)) {
                        adiciona = true;
                    }
                    break;
                case "autor":
                    if (obra.getAutor().toLowerCase().contains(termoLowerCase)) {
                        adiciona = true;
                    }
                    break;
                case "tipo":
                    if (obra.getClass().getSimpleName().toLowerCase().contains(termoLowerCase)) {
                        adiciona = true;
                    }
                    break;
                default:
                    if (obra.getTitulo().toLowerCase().contains(termoLowerCase) ||
                            obra.getAutor().toLowerCase().contains(termoLowerCase)) {
                        adiciona = true;
                    }
                    break;
            }
            if (adiciona) {
                resultados.add(obra);
            }
        }
        return resultados;
    }
}