package daos;

import java.util.Optional;

import com.avaje.ebean.Model.Finder;

import models.TokenDeCadastro;

public class TokenDeCadastroDAO {

	private Finder<Long, TokenDeCadastro> tokens = new Finder<>(TokenDeCadastro.class);
	public Optional<TokenDeCadastro> comCodigo(String codigo) {
		TokenDeCadastro tokenDeCadastro = tokens.where().eq("codigo", codigo).findUnique();
		return Optional.ofNullable(tokenDeCadastro);
	}

}
