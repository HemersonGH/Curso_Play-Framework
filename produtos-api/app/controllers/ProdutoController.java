package controllers;

import javax.inject.Inject;

import models.Produto;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.*;
import views.html.*;

public class ProdutoController extends Controller {

	@Inject
	private FormFactory formularios;

	public Result salvaNovoProduto() {
		Form<Produto> formulario = formularios.form(Produto.class).bindFromRequest();
		Produto produto = formulario.get();
		produto.save();
		return redirect(routes.ProdutoController.formularioDeNovoProduto());
	}

	public Result formularioDeNovoProduto() {
		return ok(formularioDeNovoProduto.render("Cadastrar produto"));
	}
}
