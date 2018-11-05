package controllers;

import static java.util.Calendar.YEAR;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import javax.inject.Inject;

import akka.util.Crypt;
import autenticadores.UsuarioAutenticado;
import daos.TokenDeCadastroDAO;
import daos.UsuarioDAO;
import models.EmailDeCadastro;
import models.TokenDaApi;
import models.TokenDeCadastro;
import models.Usuario;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.libs.mailer.MailerClient;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import validadores.ValidadorDeUsuario;
import views.html.formularioDeLogin;
import views.html.formularioDeNovoUsuario;
import views.html.painel;

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
				TokenDaApi tokenDaApi = new TokenDaApi(usuario);
				tokenDaApi.save();
				usuario.setToken(tokenDaApi);
				usuario.update();
				flash("success", "Cadastro confirmado com sucesso!");
				insereUsuarioNaSessao(usuario);
				return redirect(routes.UsuarioController.painel());
			}
		}
		flash("danger", "Ocorreu um erro ao tentar confirmar o cadastro!");
		return redirect(routes.UsuarioController.formularioDeLogin());
	}
	private void insereUsuarioNaSessao(Usuario usuario) {
		session(AUTH, usuario.getToken().getCodigo());
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
				insereUsuarioNaSessao(usuario);
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
		String codigo = session(AUTH);
		Usuario usuario = usuarioDAO.comToken(codigo).get();
		return ok(painel.render(usuario));
	}

	@Authenticated(UsuarioAutenticado.class)
	public Result fazLogout() {
		session().clear();
		flash("success", "Logout efetuado com sucesso!");
		return redirect(routes.UsuarioController.formularioDeLogin());
	}

}
