package biblioteca.controller;

import biblioteca.dao.UsuarioDao;
import biblioteca.model.Usuario;

public class LoginController {

    private UsuarioDao usuarioDao;

    public LoginController (){
        this.usuarioDao = new UsuarioDao();
    }

    public Usuario autenticar(String login, String senha) {
        Usuario usuario = usuarioDao.buscarPorLogin(login);
        if (usuario != null && usuario.getSenha() != null && usuario.getSenha().equals(senha)) {
            System.out.println("Login bem-sucedido para: " + usuario.getNome() + " (" + usuario.getPerfilAcesso() + ")");
            return usuario;
        } else {
            System.out.println("Login ou senha incorretos.");
            return null;
        }
    }
}
