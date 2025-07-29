package biblioteca.dao;

import biblioteca.model.Artigo;
import biblioteca.model.Livro;
import biblioteca.model.Obra;
import biblioteca.model.Revista;
import biblioteca.model.Persistivel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import biblioteca.util.RuntimeTypeAdapterFactory;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ObraDao implements Persistivel<Obra> {

    private List<Obra> obras;
    private final String NOME_ARQUIVO = "data/obras.json";

    private static final RuntimeTypeAdapterFactory<Obra> obraAdapterFactory =
            RuntimeTypeAdapterFactory
                    .of(Obra.class, "type")
                    .registerSubtype(Livro.class, "livro")
                    .registerSubtype(Revista.class, "revista")
                    .registerSubtype(Artigo.class, "artigo");

    private final Gson gsonEscrita = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapterFactory(obraAdapterFactory)
            .create();

    private final Gson gsonLeitura = new GsonBuilder()
            .registerTypeAdapterFactory(obraAdapterFactory)
            .create();

    public ObraDao() {
        this.obras = carregarDoArquivo();
    }


    private List<Obra> carregarDoArquivo() {
        try (FileReader reader = new FileReader(NOME_ARQUIVO)) {
            Type tipoListaObra = new TypeToken<ArrayList<Obra>>() {}.getType();
            List<Obra> obrasCarregadas = gsonLeitura.fromJson(reader, tipoListaObra);
            System.out.println("Obras carregadas do arquivo " + NOME_ARQUIVO);
            return obrasCarregadas != null ? obrasCarregadas : new ArrayList<>();
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo " + NOME_ARQUIVO + " não encontrado. Criando uma nova lista de obras.");
            return new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Erro ao carregar obras do arquivo " + NOME_ARQUIVO + ": " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void salvarNoArquivo() {
        try (FileWriter writer = new FileWriter(NOME_ARQUIVO)) {
            gsonEscrita.toJson(obras, writer);
        } catch (IOException e) {
            System.err.println("Erro ao salvar obras no arquivo " + NOME_ARQUIVO + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void adicionar(Obra objeto) {
        if (buscarPorCodigo(objeto.getCodigo()) != null) {
            System.out.println("Erro: Já existe uma obra com o código " + objeto.getCodigo() + ". Não foi possível adicionar.");
            return;
        }
        this.obras.add(objeto);
        salvarNoArquivo();
        System.out.println("Obra \"" + objeto.getTitulo() + "\" (Código: " + objeto.getCodigo() + ") adicionada com sucesso!");
    }

    @Override
    public Obra buscarPorCodigo(int codigo) {
        for (Obra obra : this.obras) {
            if (obra.getCodigo() == codigo) {
                System.out.println("Obra encontrada: " + obra.getTitulo());
                return obra;
            }
        }
        System.out.println("Nenhuma obra encontrada com o código: " + codigo);
        return null;
    }

    @Override
    public boolean atualizar(Obra objeto) {
        for (int i = 0; i < this.obras.size(); i++) {
            if (this.obras.get(i).getCodigo() == objeto.getCodigo()) {
                this.obras.set(i, objeto);
                salvarNoArquivo();
                System.out.println("Obra \"" + objeto.getTitulo() + "\" (Código: " + objeto.getCodigo() + ") atualizada com sucesso!");
            }
        }
        System.out.println("Obra com código " + objeto.getCodigo() + " não encontrada para atualização.");
        return false;
    }

    @Override
    public boolean remover(int codigo) {
        Iterator<Obra> iterator = this.obras.iterator();
        while (iterator.hasNext()) {
            Obra obra = iterator.next();
            if (obra.getCodigo() == codigo) {
                iterator.remove();
                salvarNoArquivo();
                System.out.println("Obra removida com sucesso!");
                return true;
            }
        }
        System.out.println("Erro: Obra com código " + codigo + " não encontrada para remoção.");
        return false;
    }

    @Override
    public List<Obra> listar() {
        System.out.println("Listando todas as obras (" + this.obras.size() + " encontradas):");
        return new ArrayList<>(this.obras);
    }
}