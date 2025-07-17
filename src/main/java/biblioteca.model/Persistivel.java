package biblioteca.model;

import java.util.List;

public interface Persistivel <T> {

    void adicionar(T objeto);
    T buscarPorCodigo(int codigo);
    boolean remover(int codigo);
    List<T> listar();
    void atualizar(T objeto);
}
