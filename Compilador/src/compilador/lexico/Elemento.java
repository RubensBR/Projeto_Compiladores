package compilador.lexico;

public class Elemento {

	private String token;
	private TipoToken tipo;
	private int linha;
	
	public Elemento(String token, TipoToken tipo, int linha) {
		this.token = token;
		this.tipo = tipo;
		this.linha = linha;
	}

	public String getToken() {
		return token;
	}

	public TipoToken getTipo() {
		return tipo;
	}

	public int getLinha() {
		return linha;
	}	
}
