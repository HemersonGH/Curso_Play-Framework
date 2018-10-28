package controllers;

import javax.inject.Inject;

import models.Produto;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.*;
import validadores.ValidadorDeProduto;
import views.html.*;

public class ProdutoController extends Controller {

	@Inject
	private FormFactory formularios;
	@Inject
	private ValidadorDeProduto validadorDeProduto;
	
	public Result salvaNovoProduto() {
		Form<Produto> formulario = formularios.form(Produto.class).bindFromRequest();
		Produto produto = formulario.get();
		if (validadorDeProduto.temErros(formulario)) {
			flash("danger", "Existem erros no seu formulário!");
			return badRequest(formularioDeNovoProduto.render(formulario));
		}
		produto.save();
		flash("success", "Seu produto '"+produto.getTitulo()+"' foi cadastrado com sucesso!");
		return redirect(routes.ProdutoController.formularioDeNovoProduto());
	}
	
	public Result formularioDeNovoProduto() {
		Produto produto = new Produto();
		produto.setTipo("e-book");
		Form<Produto> formulario = formularios.form(Produto.class).fill(produto);
		return ok(formularioDeNovoProduto.render(formulario));
	}
}
