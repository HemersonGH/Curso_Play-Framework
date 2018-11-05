package daos;

import java.util.Optional;

import com.avaje.ebean.Model.Finder;

import models.Usuario;

public class UsuarioDAO {

	private Finder<Long, Usuario> usuarios = new Finder<>(Usuario.class);

	public Optional<Usuario> comEmail(String email) {
		Usuario usuario = usuarios.where().eq("email", email).findUnique();
		return Optional.ofNullable(usuario);
	}

	public Optional<Usuario> comEmailESenha(String email, String senha) {
		Usuario usuario = usuarios.where().eq("email", email).eq("senha", senha).findUnique();
		return Optional.ofNullable(usuario);
	}

	public Optional<Usuario> comToken(String codigo) {
		Usuario usuario = usuarios.where().eq("token.codigo", codigo).findUnique();
		return Optional.ofNullable(usuario);
	}

}
