package biblioteca.controller;

import biblioteca.dao.UsuarioDao;
import biblioteca.model.Usuario;

import java.util.List;

public class UsuarioController {
    private UsuarioDao usuarioDao;

    private int proximaMatriculaSistema = -1;

    public UsuarioController() {
        this.usuarioDao = new UsuarioDao();
        List<Usuario> todosUsuarios = usuarioDao.listar();
        int menorMatriculaNegativa = -1;
        for (Usuario u : todosUsuarios) {
            if (u.getMatricula() < 0 && u.getMatricula() <= menorMatriculaNegativa) {
                menorMatriculaNegativa = u.getMatricula() - 1;
            }
        }
        this.proximaMatriculaSistema = menorMatriculaNegativa;
    }

    public String cadastrarUsuarioSistema(String nome, String login, String telefone, String email, String perfilAcesso, String senha) {
        if (usuarioDao.buscarPorLoginSemMensagem(login) != null) {
            return "Erro no Controller: Usuário com login '" + login + "' já existe.";
        }

        Usuario novoUsuario = new Usuario(nome, login,0, "Não se aplica", telefone, email, perfilAcesso, senha);
        usuarioDao.adicionar(novoUsuario);
        return "Usuário do sistema cadastrado com sucesso.";
    }

    public String cadastrarLeitor(String nome, int matricula, String categoriaLeitor, String telefone, String email) {
        if (usuarioDao.buscarPorCodigoSemMensagem(matricula) != null) {
            return "Erro: Leitor com matrícula " + matricula + " já existe.";
        }
        Usuario novoLeitor = new Usuario(nome, "", matricula, categoriaLeitor, telefone, email, "Leitor", "");
        usuarioDao.adicionar(novoLeitor);
        return "Leitor cadastrado com sucesso.";
    }

    public Usuario buscarLeitorPorMatricula(int matricula) {
        return usuarioDao.buscarPorCodigo(matricula);
    }

    public Usuario buscarUsuarioPorLogin(String login) {
        return usuarioDao.buscarPorLogin(login);
    }

    public boolean atualizarUsuario(Usuario usuarioAtualizado) {
        if (usuarioDao.buscarPorCodigoSemMensagem(usuarioAtualizado.getMatricula()) == null) {
            System.out.println("Erro no Controller: Usuário com matrícula " + usuarioAtualizado.getMatricula() + " não encontrado para atualização.");
            return false;
        }
        return usuarioDao.atualizar(usuarioAtualizado);
    }

    public boolean removerUsuario(int matricula) {
        return usuarioDao.remover(matricula);
    }

    public List<Usuario> listarTodosUsuarios() {
        return usuarioDao.listar();
    }



}