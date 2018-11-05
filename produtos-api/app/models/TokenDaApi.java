package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.avaje.ebean.Model;

import akka.util.Crypt;

@Entity
public class TokenDaApi extends Model {

	@Id
	@GeneratedValue
	private Long id;
	@OneToOne
	private Usuario usuario;
	private String codigo;
	private Date expiracao;
	
	public TokenDaApi(Usuario usuario) {
		this.usuario = usuario;
		this.expiracao = new Date();
		this.codigo = Crypt.sha1(Crypt.generateSecureCookie()+expiracao.toString()+usuario.toString());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Date getExpiracao() {
		return expiracao;
	}

	public void setExpiracao(Date expiracao) {
		this.expiracao = expiracao;
	}
}
