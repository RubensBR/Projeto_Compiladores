package compilador.semantico;

import java.util.ArrayList;

import compilador.semantico.AnalisadorExpressao.Tipo;

public class Procedure extends Identificador {

	ArrayList<Tipo> parametros;
	
	public Procedure() {}
	
	public Procedure(String token, Tipo tipo, ArrayList<Tipo> parametros) {
		super(token, tipo);
		this.parametros = parametros;
	}

	public ArrayList<Tipo> getParametros() {
		return parametros;
	}

	public void setParametros(ArrayList<Tipo> parametros) {
		this.parametros = parametros;
	}
	
}
