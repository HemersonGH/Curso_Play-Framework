package daos;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Model.Finder;

import models.Produto;

public class ProdutoDAO {

	private Finder<Long, Produto> produtos = new Finder<>(Produto.class);

	public Optional<Produto> comCodigo(String codigo) {
		Produto produto = produtos
				.where()
				.eq("codigo", codigo)
				.findUnique();
		return Optional.of(produto);
	}

	public List<Produto> todos() {
		return produtos.all();
	}

	public List<Produto> doTipo(String tipo) {
		return produtos.where().eq("tipo", tipo).findList();
	}

	public List<Produto> comFiltro(Map<String, String> parametros) {
		ExpressionList<Produto> consulta = produtos.where();
		parametros.entrySet().forEach(entrada -> {
			consulta.eq(entrada.getKey(), entrada.getValue());
		});
		return consulta.findList();
	}
}
