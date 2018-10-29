package models;

import java.util.List;

public class EnvelopeDeProdutos {

	private List<Produto> produtos;
	public EnvelopeDeProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}
	public List<Produto> getProdutos() {
		return produtos;
	}
}
