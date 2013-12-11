package compilador.semantico;

import compilador.semantico.AnalisadorExpressao.Tipo;

public class Identificador {

	private String token;
	private Tipo tipo;
	
	public Identificador(){}
	
	public Identificador(String token, Tipo tipo) {
		this.token = token;
		this.tipo = tipo;
	}

	public Identificador(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}
}
