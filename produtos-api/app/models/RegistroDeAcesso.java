package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Model;

@Entity
public class RegistroDeAcesso extends Model {

	@Id
	@GeneratedValue
	private Long id;
	@ManyToOne
	private Usuario usuario;
	private String uri;
	private Date data;
	
	public RegistroDeAcesso(Usuario usuario, String uri) {
		this.usuario = usuario;
		this.uri = uri;
		this.data = new Date();
	}

	public Long getId() {
		return id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public String getUri() {
		return uri;
	}

	public Date getData() {
		return data;
	}
	
}
