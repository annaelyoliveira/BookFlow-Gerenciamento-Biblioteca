package biblioteca.controller;

import biblioteca.dao.UsuarioDao;
import biblioteca.model.Usuario;

public class UsuarioController {
    private UsuarioDao usuarioDao;

    public UsuarioController() {
        this.usuarioDao = new UsuarioDao();
    }

    public boolean cadastrarUsuario(String nome, int matricula, String tipoUsuario, String telefone, String email, String senha) {
        if (usuarioDao.buscarPorCodigo(matricula) != null) {
            System.out.println("Erro no Controller: Usuário com matrícula " + matricula + " já existe.");
            return false;
        }

        Usuario novoUsuario = new Usuario(nome, matricula, tipoUsuario, telefone, email, senha);
        usuarioDao.adicionar(novoUsuario);
        return true;
    }
}