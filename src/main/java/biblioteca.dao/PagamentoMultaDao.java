package biblioteca.dao;

import biblioteca.model.PagamentoMulta;
import biblioteca.model.Persistivel;

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

public class PagamentoMultaDao implements Persistivel<PagamentoMulta> {

    private List<PagamentoMulta> pagamentos;
    private final String NOME_ARQUIVO = "data/pagamentos.json";
    private int proximoId = 1;

    private final Gson gsonEscrita = new GsonBuilder().setPrettyPrinting().create();
    private final Gson gsonLeitura = new Gson();

    public PagamentoMultaDao() {
        this.pagamentos = carregarDoArquivo();
        if (!pagamentos.isEmpty()) {
            this.proximoId = pagamentos.stream()
                    .mapToInt(PagamentoMulta::getId)
                    .max()
                    .orElse(0) + 1;
        }
    }

    private List<PagamentoMulta> carregarDoArquivo() {
        try (FileReader reader = new FileReader(NOME_ARQUIVO)) {
            Type tipoListaPagamento = new TypeToken<ArrayList<PagamentoMulta>>() {}.getType();
            List<PagamentoMulta> pagamentosCarregados = gsonLeitura.fromJson(reader, tipoListaPagamento);
            System.out.println("Pagamentos carregados do arquivo " + NOME_ARQUIVO);
            return pagamentosCarregados != null ? pagamentosCarregados : new ArrayList<>();
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo " + NOME_ARQUIVO + " não encontrado. Criando uma nova lista de pagamentos.");
            return new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Erro ao carregar pagamentos do arquivo " + NOME_ARQUIVO + ": " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void salvarNoArquivo() {
        try (FileWriter writer = new FileWriter(NOME_ARQUIVO)) {
            gsonEscrita.toJson(pagamentos, writer);
        } catch (IOException e) {
            System.err.println("Erro ao salvar pagamentos no arquivo " + NOME_ARQUIVO + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void adicionar(PagamentoMulta objeto) {
        objeto.setId(this.proximoId++);
        this.pagamentos.add(objeto);
        salvarNoArquivo();
        System.out.println("Pagamento de multa (ID: " + objeto.getId() + ") adicionado com sucesso.");
    }

    @Override
    public PagamentoMulta buscarPorCodigo(int id) {
        for (PagamentoMulta pagamento : this.pagamentos) {
            if (pagamento.getId() == id) {
                System.out.println("Pagamento ID " + id + " encontrado.");
                return pagamento;
            }
        }
        System.out.println("Nenhum pagamento encontrado com o ID: " + id);
        return null;
    }

    @Override
    public boolean atualizar(PagamentoMulta objeto) {
        for (int i = 0; i < this.pagamentos.size(); i++) {
            if (this.pagamentos.get(i).getId() == objeto.getId()) {
                this.pagamentos.set(i, objeto);
                salvarNoArquivo();
                System.out.println("Pagamento de multa (ID: " + objeto.getId() + ") atualizado com sucesso.");
                return true;
            }
        }
        System.out.println("Pagamento com ID " + objeto.getId() + " não encontrado para atualização.");
        return false;
    }

    @Override
    public boolean remover(int id) {
        Iterator<PagamentoMulta> iterator = this.pagamentos.iterator();
        while (iterator.hasNext()) {
            PagamentoMulta pagamento = iterator.next();
            if (pagamento.getId() == id) {
                iterator.remove();
                salvarNoArquivo();
                System.out.println("Pagamento de multa com ID " + id + " removido com sucesso.");
                return true;
            }
        }
        System.out.println("Erro ao remover: Pagamento de multa com ID " + id + " não encontrado.");
        return false;
    }

    @Override
    public List<PagamentoMulta> listar() {
        System.out.println("Listando todos os pagamentos (" + this.pagamentos.size() + " encontrados):");
        return new ArrayList<>(this.pagamentos);
    }
}