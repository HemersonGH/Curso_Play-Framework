package daos;

import java.util.Optional;

import com.avaje.ebean.Finder;

import models.Produto;

public class ProdutoDAO {

	private Finder<Long, Produto> produtos = new Finder<>(Produto.class);
	public Optional<Produto> comCodigo(String codigo) {
		Produto produto = produtos.query()
				.where()
				.eq("codigo", codigo)
				.findUnique();
		return Optional.ofNullable(produto);
	}
}
