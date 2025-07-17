package biblioteca.dao;

import biblioteca.model.Obra;
import biblioteca.model.Persistivel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class ObraDao implements Persistivel<Obra> {

    private List<Obra> obras;
    private final String arquivo = "obras.json";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public ObraDao() {
        this.obras = carregarDoArquivo();
    }

    @Override
    public void adicionar(Obra obra) {
        obras.add(obra);
        salvarNoArquivo();
        System.out.println("Obra adicionada com sucesso.");
    }

    @Override
    public Obra buscarPorCodigo(int codigo) {
        for (Obra obra : obras) {
            if (obra.getCodigo() == codigo) {
                System.out.println("Obra encontrada: " + obra.getTitulo());
                return obra;
            }
        }
        System.out.println("Nenhuma obra encontrada com o código: " + codigo);
        return null;
    }

    @Override
    public boolean remover(int codigo) {
        Obra obraParaRemover = buscarPorCodigo(codigo);
        if (obraParaRemover != null) {
            obras.remove(obraParaRemover);
            salvarNoArquivo();
            System.out.println("Obra removida com sucesso!");
            return true;
        }
        System.out.println("Erro ao remover: obra com código " + codigo + " não encontrada.");
        return false;
    }

    @Override
    public List<Obra> listar() {
        return new ArrayList<>(obras);
    }

    public void atualizar(Obra obraAtualizada) {
        for (int i = 0; i < obras.size(); i++) {
            if (obras.get(i).getCodigo() == obraAtualizada.getCodigo()) {
                obras.set(i, obraAtualizada);
                salvarNoArquivo();
                System.out.println("Obra atualizada com sucesso!");
                return;
            }
        }
        System.out.println("Obra não encontrada para atualização.");
    }

    private void salvarNoArquivo() {
        try (FileWriter writer = new FileWriter(arquivo)) {
            gson.toJson(obras, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Obra> carregarDoArquivo() {
        try (FileReader reader = new FileReader(arquivo)) {
            Type listType = new TypeToken<ArrayList<Obra>>() {}.getType();
            System.out.println("Obras carregadas do arquivo " + arquivo);
            return gson.fromJson(reader, listType);
        } catch (IOException e) {
            System.out.println("Arquivo não encontrado ou erro ao ler. Criando nova lista de obras.");
            return new ArrayList<>();
        }
    }
}

