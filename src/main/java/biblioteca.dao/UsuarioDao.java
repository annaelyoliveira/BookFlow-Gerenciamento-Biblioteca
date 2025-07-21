package biblioteca.dao;

import biblioteca.model.Persistivel;
import biblioteca.model.Usuario;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDao implements Persistivel<Usuario> {

    private List<Usuario> usuarios;
    private final String NOME_ARQUIVO = "data/usuarios.json";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public UsuarioDao() {
        usuarios = carregarDoArquivo();
    }

    private List<Usuario> carregarDoArquivo() {
        try (FileReader reader = new FileReader(NOME_ARQUIVO)) {
            Type tipoLista = new TypeToken<ArrayList<Usuario>>() {}.getType();
            return gson.fromJson(reader, tipoLista);
        } catch (IOException e) {
            System.out.println("Erro ao carregar usuários do arquivo: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void salvarNoArquivo() {
        try (FileWriter writer = new FileWriter(NOME_ARQUIVO)) {
            gson.toJson(usuarios, writer);
        } catch (IOException e) {
            System.out.println("Erro ao salvar usuários no arquivo: " + e.getMessage());
        }
    }

    @Override
    public void adicionar(Usuario usuario) {
        if (buscarPorCodigo(usuario.getMatricula()) != null) {
            System.out.println("Já existe um usuário com a matrícula " + usuario.getMatricula());
            return;
        }
        this.usuarios.add(usuario);
        salvarNoArquivo();
        System.out.println("Usuário adicionado com sucesso.");
    }

    @Override
    public Usuario buscarPorCodigo(int codigo) {
        for (Usuario u : usuarios) {
            if (u.getMatricula() == codigo) {
                return u;
            }
        }
        System.out.println("Usuário com matrícula " + codigo + " não encontrado.");
        return null;
    }

    @Override
    public boolean remover(int codigo) {
        for (Usuario u : usuarios) {
            if (u.getMatricula() == codigo) {
                usuarios.remove(u);
                salvarNoArquivo();
                System.out.println("Usuário removido com sucesso.");
                return true;
            }
        }
        System.out.println("Usuário não encontrado para remoção.");
        return false;
    }

    @Override
    public List<Usuario> listar() {
        return usuarios;
    }

    @Override
    public void atualizar(Usuario usuarioAtualizado) {
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getMatricula() == usuarioAtualizado.getMatricula()) {
                usuarios.set(i, usuarioAtualizado);
                salvarNoArquivo();
                System.out.println("Usuário atualizado com sucesso.");
                return;
            }
        }
        System.out.println("Usuário não encontrado para atualização.");
    }
}
