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

	public String getClassificao() {
		switch (tipo) {
			case PALAVRA_CHAVE:
				return "Palavra Reservada";				
			
			case IDENTIFICADOR:
				return "Identificador";				
				
			case NUMERO_INTEIRO:
				return "Número Inteiro";
			
			case NUMERO_REAL:
				return "Número Real";
			
			case DELIMITADOR: 
				return "Delimitador";
			
			case COMANDO_ATRIBUICAO: 
				return "Comando Atribuição";
			
			case OPERADOR_RELACIONAL:
				return "Operador Relacional";
				
			case OPERADOR_ADITIVO:
				return "Operador Aditivo";
			
			case OPERADOR_MULTIPLICATIVO:
				return "Operador Multiplicativo";	
			
			case OPERADOR_LOGICO:
				return "Operador Lógico";
		}
		return "erro";
	}
	
	
	public int getLinha() {
		return linha;
	}	
}
