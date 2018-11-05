package acoes;

import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import daos.UsuarioDAO;
import models.RegistroDeAcesso;
import models.Usuario;
import play.Logger;
import play.mvc.Action;
import play.mvc.Http.Context;
import play.mvc.Result;

public class AcaoDeRegistroDeAcesso extends Action.Simple {

	@Inject
	private UsuarioDAO usuarioDAO;
	@Override
	public CompletionStage<Result> call(Context context) {
		String codigo = context.request().getHeader("API-Token");
		String uri = context.request().uri();
		Usuario usuario = usuarioDAO.comToken(codigo).get();
		RegistroDeAcesso acesso = new RegistroDeAcesso(usuario, uri);
		acesso.save();
		return delegate.call(context);
	}

	
}
