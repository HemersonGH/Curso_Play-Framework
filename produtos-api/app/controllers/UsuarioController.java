package controllers;

import java.util.Optional;

import javax.inject.Inject;

import akka.util.Crypt;
import autenticadores.UsuarioAutenticado;
import daos.TokenDeCadastroDAO;
import daos.UsuarioDAO;
import models.EmailDeCadastro;
import models.TokenDeCadastro;
import models.Usuario;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.libs.mailer.MailerClient;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import validadores.ValidadorDeUsuario;
import views.html.*;

public class UsuarioController extends Controller {

	public static final String AUTH = "AUTH";
	@Inject
	private FormFactory formularios;
	@Inject
	private ValidadorDeUsuario validadorDeUsuario;
	@Inject
	private MailerClient enviador;
	@Inject
	private UsuarioDAO usuarioDAO;
	@Inject
	private TokenDeCadastroDAO tokenDeCadastroDAO;
	
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
		TokenDeCadastro token = new TokenDeCadastro(usuario);
		token.save();
		enviador.send(new EmailDeCadastro(token));
		flash("success", "Um email foi enviado para confirmar seu cadastro!");
		return redirect(routes.UsuarioController.formularioDeLogin());
	}

	public Result confirmaUsuario(String email, String codigo) {
		Optional<Usuario> possivelUsuario = usuarioDAO.comEmail(email);
		Optional<TokenDeCadastro> possivelToken = tokenDeCadastroDAO.comCodigo(codigo);
		if (possivelToken.isPresent() && possivelUsuario.isPresent()) {
			TokenDeCadastro token = possivelToken.get();
			Usuario usuario = possivelUsuario.get();
			if (token.getUsuario().equals(usuario)) {
				token.delete();
				usuario.setVerificado(true);
				usuario.update();
				flash("success", "Cadastro confirmado com sucesso!");
				session(AUTH, usuario.getEmail());
				return redirect(routes.UsuarioController.painel());
			}
		}
		flash("danger", "Ocorreu um erro ao tentar confirmar o cadastro!");
		return redirect(routes.UsuarioController.formularioDeLogin());
	}
	
	public Result formularioDeLogin() {
		return ok(formularioDeLogin.render(formularios.form()));
	}
	
	public Result fazLogin() {
		DynamicForm formulario = formularios.form().bindFromRequest();
		String email = formulario.get("email");
		String senha = Crypt.sha1(formulario.get("senha"));
		Optional<Usuario> possivelUsuario = usuarioDAO.comEmailESenha(email, senha);
		if (possivelUsuario.isPresent()) {
			Usuario usuario = possivelUsuario.get();
			if (usuario.isVerificado()) {
				session(AUTH, usuario.getEmail());
				flash("success", "Login foi efetuado com sucesso!");
				return redirect(routes.UsuarioController.painel());
			}
			else {
				flash("warning", "Usuário ainda não confirmado! Confirma seu email!");
			}
		}
		else {
			flash("danger", "Credenciais inválidas!");
		}
		return redirect(routes.UsuarioController.formularioDeLogin());
	}
	
	@Authenticated(UsuarioAutenticado.class)
	public Result painel() {
		return ok(painel.render());
	}
	
	@Authenticated(UsuarioAutenticado.class)
	public Result fazLogout() {
		session().clear();
		flash("success", "Logout efetuado com sucesso!");
		return redirect(routes.UsuarioController.formularioDeLogin());
	}
	
	
	
	
	
}
