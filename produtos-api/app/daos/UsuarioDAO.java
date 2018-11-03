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

}
