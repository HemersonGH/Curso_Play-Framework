package controllers;

import javax.inject.Inject;

import akka.util.Crypt;
import models.Usuario;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.*;
import validadores.ValidadorDeUsuario;
import views.html.*;

public class UsuarioController extends Controller {

	@Inject
	private FormFactory formularios;
	@Inject
	private ValidadorDeUsuario validadorDeUsuario;
	
	public Result formularioDeNovoUsuario() {
		Form<Usuario> formulario = formularios.form(Usuario.class);
		return ok(formularioDeNovoUsuario.render(formulario));
	}
	public Result salvaNovoUsuario() {
		Form<Usuario> formulario = formularios.form(Usuario.class).bindFromRequest();
		if (validadorDeUsuario.temErros(formulario)) {
			flash("danger", "Há erros no formulário de cadastro!");
			return badRequest(formularioDeNovoUsuario.render(formulario));
		}
		Usuario usuario = formulario.get();
		String senhaCrypto = Crypt.sha1(usuario.getSenha());
		usuario.setSenha(senhaCrypto);
		usuario.save();
		flash("success", "Usuário cadastrado com sucesso!");
		return redirect("/login"); //TODO
	}
}
