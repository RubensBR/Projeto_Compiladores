package compilador.semantico;

import java.util.Stack;

import compilador.semantico.AnalisadorExpressao.Tipo;

public class PilhaEscopo {

	private Stack<Identificador> pilha = new Stack<Identificador>();
	private int nivelEscopo = 0;	
	
	public int getNivelEscopo() {
		return nivelEscopo;
	}

	public void setNivelEscopo(int nivelEscopo) {
		this.nivelEscopo = nivelEscopo;
	}

	public void iniciarEscopoPrograma(String token) {
		pilha.push(new Identificador("$", Tipo.MARCADOR));
		pilha.push(new Identificador(token, Tipo.PROGRAM));
		System.out.println("iniciarEscopo");
	}
	
	public void novoEscopo() {
		pilha.push(new Identificador("$", Tipo.MARCADOR));
		++nivelEscopo;
		System.out.println("novoEscopo");
		imprimirPilha();
	}
	
	public void fimDeEscopo() {
		while (pilha.pop().getTipo() != Tipo.MARCADOR);
		--nivelEscopo;
		System.out.println("fimEscopo");
		imprimirPilha();
	}
			
	public void push(Identificador identificador) {
		pilha.push(identificador);
		System.out.println("push");
		imprimirPilha();
	}
	
	public void atribuirTipo(Tipo tipo) {
		for (int i = pilha.size() - 1; i > 0; --i) {
			if (pilha.get(i).getTipo() == Tipo.DESCONHECIDO) {
				pilha.get(i).setTipo(tipo);
			} else {
				break;
			}
		}
		System.out.println("atribuirTipo");
		imprimirPilha();
	}
	
	public boolean foiDeclarada(String token) {
		System.out.print(token + " foi declarado: ");
		for (int i = pilha.size() - 1; i > 0; --i) {
			if (pilha.get(i).getToken().equals(token)) {
				System.out.println("SIM");
				return true;
			}
		}
		System.out.println("NÃO");
		return false;
	}
	
	private void imprimirPilha() {
		for (Identificador p: pilha) {
			System.out.print("[" + p.getToken() + " : " + p.getTipo() + "]");
		}
		System.out.println();
	}
}
