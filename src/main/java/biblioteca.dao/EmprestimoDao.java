package biblioteca.dao;

import biblioteca.model.Emprestimo;
import biblioteca.model.Persistivel;
import biblioteca.model.Obra;
import biblioteca.util.RuntimeTypeAdapterFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EmprestimoDao implements Persistivel<Emprestimo> {

    private List<Emprestimo> emprestimos;
    private final String NOME_ARQUIVO = "data/emprestimos.json";
    private int proximoId = 1;

    private static final RuntimeTypeAdapterFactory<Obra> obraAdapterFactory =
            RuntimeTypeAdapterFactory
                    .of(Obra.class, "type")
                    .registerSubtype(biblioteca.model.Livro.class, "livro") // Caminho completo para Livro
                    .registerSubtype(biblioteca.model.Revista.class, "revista") // Caminho completo para Revista
                    .registerSubtype(biblioteca.model.Artigo.class, "artigo"); // Caminho completo para Artigo

    private final Gson gsonEscrita = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapterFactory(obraAdapterFactory)
            .create();

    private final Gson gsonLeitura = new GsonBuilder()
            .registerTypeAdapterFactory(obraAdapterFactory)
            .create();

    public EmprestimoDao() {
        this.emprestimos = carregarDoArquivo();
        if (!emprestimos.isEmpty()) {
            this.proximoId = emprestimos.stream()
                    .mapToInt(Emprestimo::getId)
                    .max()
                    .orElse(0) + 1;
        }
    }

    private List<Emprestimo> carregarDoArquivo() {
        try (FileReader reader = new FileReader(NOME_ARQUIVO)) {
            Type tipoListaEmprestimo = new TypeToken<ArrayList<Emprestimo>>() {}.getType();
            List<Emprestimo> emprestimosCarregados = gsonLeitura.fromJson(reader, tipoListaEmprestimo);
            System.out.println("Empréstimos carregados do arquivo " + NOME_ARQUIVO);
            return emprestimosCarregados != null ? emprestimosCarregados : new ArrayList<>();
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo " + NOME_ARQUIVO + " não encontrado. Criando uma nova lista de empréstimos.");
            return new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Erro ao carregar empréstimos do arquivo " + NOME_ARQUIVO + ": " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void salvarNoArquivo() {
        try (FileWriter writer = new FileWriter(NOME_ARQUIVO)) {
            gsonEscrita.toJson(emprestimos, writer);
        } catch (IOException e) {
            System.err.println("Erro ao salvar empréstimos no arquivo " + NOME_ARQUIVO + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void adicionar(Emprestimo objeto) {
        objeto.setId(this.proximoId++);
        this.emprestimos.add(objeto);
        salvarNoArquivo();
        System.out.println("Empréstimo (ID: " + objeto.getId() + ") adicionado com sucesso.");
    }

    @Override
    public Emprestimo buscarPorCodigo(int id) {
        for (Emprestimo emprestimo : this.emprestimos) {
            if (emprestimo.getId() == id) {
                System.out.println("Empréstimo ID " + id + " encontrado.");
                return emprestimo;
            }
        }
        System.out.println("Nenhum empréstimo encontrado com o ID: " + id);
        return null;
    }

    @Override
    public void atualizar(Emprestimo objeto) {
        for (int i = 0; i < this.emprestimos.size(); i++) {
            if (this.emprestimos.get(i).getId() == objeto.getId()) {
                this.emprestimos.set(i, objeto);
                salvarNoArquivo();
                System.out.println("Empréstimo (ID: " + objeto.getId() + ") atualizado com sucesso.");
            }
        }
        System.out.println("Empréstimo com ID " + objeto.getId() + " não encontrado para atualização.");
    }

    @Override
    public boolean remover(int id) {
        Iterator<Emprestimo> iterator = this.emprestimos.iterator();
        while (iterator.hasNext()) {
            Emprestimo emprestimo = iterator.next();
            if (emprestimo.getId() == id) {
                iterator.remove();
                salvarNoArquivo();
                System.out.println("Empréstimo com ID " + id + " removido com sucesso.");
                return true;
            }
        }
        System.out.println("Erro ao remover: Empréstimo com ID " + id + " não encontrado.");
        return false;
    }

    @Override
    public List<Emprestimo> listar() {
        System.out.println("Listando todos os empréstimos (" + this.emprestimos.size() + " encontrados):");
        return new ArrayList<>(this.emprestimos);
    }
}