package compilador.semantico;

import java.util.ArrayList;
import java.util.Stack;

import compilador.semantico.AnalisadorExpressao.Tipo;

public class PilhaEscopo {

	private Stack<Identificador> pilha = new Stack<Identificador>();	
	private Tipo tipoUltimaBusca;
	private Identificador ultimaProcedureChamada;

	public void iniciarEscopoPrograma(String token) {
		pilha.push(new Identificador("$", Tipo.MARCADOR));
		pilha.push(new Identificador(token, Tipo.PROGRAM));
		//System.out.println("iniciarEscopo");
	}
	
	public void novoEscopo() {
		pilha.push(new Identificador("$", Tipo.MARCADOR));
		//System.out.println("novoEscopo");
		//imprimirPilha();
	}
	
	public void fimDeEscopo() {
		while (pilha.pop().getTipo() != Tipo.MARCADOR);
		//System.out.println("fimEscopo");
		//imprimirPilha();
	}
			
	public int push(Identificador identificador) {
		pilha.push(identificador);
		//System.out.println("push");
		//imprimirPilha();
		return pilha.size() - 1;
	}
	
	public void atribuirTipo(Tipo tipo) {
		for (int i = pilha.size() - 1; i > 0; --i) {
			if (pilha.get(i).getTipo() == Tipo.DESCONHECIDO) {
				pilha.get(i).setTipo(tipo);
			} else {
				break;
			}
		}
		//System.out.println("atribuirTipo");
		//imprimirPilha();
	}
	
	public boolean foiDeclarada(String token) {
		//System.out.print(token + " foi declarado: ");
		for (int i = pilha.size() - 1; i > 0; --i) {
			if (pilha.get(i).getToken().equals(token)) {
				//System.out.println("SIM");
				tipoUltimaBusca = pilha.get(i).getTipo();
				if (pilha.get(i).getTipo() == Tipo.PROCEDURE)
					ultimaProcedureChamada = pilha.get(i);
				return true;
			}
		}
		//System.out.println("NÃO");
		return false;
	}
	
	public boolean foiDeclaradaNoEscopoAtual(String token) {
		//System.out.print(token + " foi declarado no escopo atual: ");
		for (int i = pilha.size() - 1; i > 0; --i) {
			if (pilha.get(i).getTipo() == Tipo.MARCADOR)
				break;
			if (pilha.get(i).getToken().equals(token)) {
				//System.out.println("SIM");
				tipoUltimaBusca = pilha.get(i).getTipo();
				if (pilha.get(i).getTipo() == Tipo.PROCEDURE)
					ultimaProcedureChamada = pilha.get(i);
				return true;
			}
		}
		//System.out.println("NÃO");
		return false;
	}
	
	public Identificador getUltimaProcedureChamada() {
		return ultimaProcedureChamada;
	}

	public Tipo getTipoUltimaBusca() {
		return tipoUltimaBusca;
	}
		
	private void imprimirPilha() {
		for (Identificador p: pilha) {
			System.out.print("[" + p.getToken() + " : " + p.getTipo() + "]");
		}
		System.out.println();
	}
	
	public ArrayList<Tipo> getListaArgumentos() {
		ArrayList<Tipo> argumentos = new ArrayList<Tipo>();
		for (int i = pilha.size() - 1; i > 0; --i) {
			if (pilha.get(i).getTipo() == Tipo.MARCADOR)
				break;
			argumentos.add(0, pilha.get(i).getTipo());
		}
		return argumentos;
	}
	
	public void ajustarProcedure(int indice, Procedure procedure) {
		pilha.set(indice, procedure);
	}
}
