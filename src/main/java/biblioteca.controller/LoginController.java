package biblioteca.controller;

import biblioteca.dao.UsuarioDao;
import biblioteca.model.Usuario;

public class LoginController {

    private UsuarioDao usuarioDao;

    public LoginController (){
        this.usuarioDao = new UsuarioDao();
    }

    public Usuario autenticar(int matricula, String senha) {
        Usuario usuario = usuarioDao.buscarPorCodigo(matricula);
        if (usuario != null && usuario.getSenha() != null && usuario.getSenha().equals(senha)) {
            System.out.println("Login bem-sucedido para: " + usuario.getNome() + " (" + usuario.getTipoUsuario() + ")");
            return usuario;
        } else {
            System.out.println("Matrícula ou senha incorretos.");
            return null;
        }
    }
}
