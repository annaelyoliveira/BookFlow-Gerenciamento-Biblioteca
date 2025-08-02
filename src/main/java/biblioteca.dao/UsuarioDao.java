package biblioteca.dao;

import biblioteca.model.Persistivel;
import biblioteca.model.Usuario;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UsuarioDao implements Persistivel<Usuario> {

    private List<Usuario> usuarios;
    private final String NOME_ARQUIVO = "data/usuarios.json";
    private final Gson gsonEscrita = new GsonBuilder().setPrettyPrinting().create();
    private final Gson gsonLeitura = new Gson();

    public UsuarioDao() {
        File diretorio = new File("data");
        if (!diretorio.exists()) {
            if (diretorio.mkdirs()) {
                System.out.println("Diretório 'data' criado no local de execução.");
            } else {
                System.err.println("Falha ao criar o diretório 'data'.");
            }
        }

        this.usuarios = carregarDoArquivo();
    }

    private List<Usuario> carregarDoArquivo() {
        try (FileReader reader = new FileReader(NOME_ARQUIVO)) {
            Type tipoLista = new TypeToken<ArrayList<Usuario>>() {}.getType();
            List<Usuario> usuariosCarregados = gsonLeitura.fromJson(reader, tipoLista);
            System.out.println("Usuários carregados do arquivo " + NOME_ARQUIVO);
            return usuariosCarregados != null ? usuariosCarregados : new ArrayList<>();
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo " + NOME_ARQUIVO + " não encontrado. Iniciando com uma nova lista de usuários.");
            return new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Erro ao carregar usuários do arquivo " + NOME_ARQUIVO + ": " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void salvarNoArquivo() {
        try (FileWriter writer = new FileWriter(NOME_ARQUIVO)) {
            gsonEscrita.toJson(usuarios, writer);
        } catch (IOException e) {
            System.err.println("Erro ao salvar usuários no arquivo " + NOME_ARQUIVO + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void adicionar(Usuario usuario) {
        this.usuarios.add(usuario);
        salvarNoArquivo();
        System.out.println("Usuário \"" + usuario.getNome() + "\" (Matrícula: " + usuario.getMatricula() + ", Login: " + usuario.getLogin() + ") adicionado com sucesso.");
    }

    @Override
    public Usuario buscarPorCodigo(int codigo) {
        for (Usuario u : usuarios) {
            if (u.getMatricula() == codigo) {
                System.out.println("Usuário com matrícula " + codigo + " encontrado.");
                return u;
            }
        }
        System.out.println("Usuário com matrícula " + codigo + " não encontrado.");
        return null;
    }

    public Usuario buscarPorLogin(String login) {
        for (Usuario u : this.usuarios) {
            if (u.getLogin() != null && u.getLogin().equals(login)) {
                return u;
            }
        }
        return null;
    }

    @Override
    public boolean remover(int matricula) { // O código aqui é a matrícula
        Iterator<Usuario> iterator = this.usuarios.iterator();
        while (iterator.hasNext()) {
            Usuario u = iterator.next();
            if (u.getMatricula() == matricula) {
                iterator.remove();
                salvarNoArquivo();
                System.out.println("Usuário com matrícula " + matricula + " removido com sucesso.");
                return true;
            }
        }
        System.out.println("Erro ao remover: Usuário com matrícula " + matricula + " não encontrado.");
        return false;
    }

    @Override
    public List<Usuario> listar() {
        return new ArrayList<>(this.usuarios);
    }

    @Override
    public boolean atualizar(Usuario usuarioAtualizado) {
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getMatricula() == usuarioAtualizado.getMatricula()) {
                usuarios.set(i, usuarioAtualizado);
                salvarNoArquivo();
                System.out.println("Usuário atualizado com sucesso.");
                return true;
            }
        }
        System.out.println("Usuário não encontrado para atualização.");
        return false;
    }
    //Métodos Internos auxiliares
    public Usuario buscarPorCodigoSemMensagem(int matricula) {
        for (Usuario u : this.usuarios) {
            if (u.getMatricula() == matricula) {
                return u;
            }
        }
        return null;
    }

    public Usuario buscarPorLoginSemMensagem(String login) {
        for (Usuario u : this.usuarios) {
            if (u.getLogin() != null && u.getLogin().equals(login)) {
                return u;
            }
        }
        return null;
    }

}
