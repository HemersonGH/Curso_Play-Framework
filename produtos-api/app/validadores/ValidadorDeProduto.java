package validadores;

import javax.inject.Inject;

import daos.ProdutoDAO;
import models.Produto;
import play.data.Form;
import play.data.validation.ValidationError;

public class ValidadorDeProduto {

	@Inject 
	private ProdutoDAO produtoDAO;

	public boolean temErros(Form<Produto> formulario) {
		Produto produto = formulario.get();
		if (produto.getPreco() < 0.0) {
			formulario.reject(new ValidationError("preco", "O preço do produto tem que ser maior que 0"));
		}
		if (produtoDAO.comCodigo(produto.getCodigo()).isPresent()) {
			formulario.reject(new ValidationError("codigo", "Já existe um produto com este código!"));
		}
		return formulario.hasErrors();
	}
}
