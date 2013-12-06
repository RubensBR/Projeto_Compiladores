package analisador.teste;

import java.util.ArrayList;
import java.util.Stack;

public class AnalisadorExpressao {

	private ArrayList<Tipo> expressao;
	private boolean ehTipoValor;
	public enum Tipo {
		RELACIONAL, ADITIVO, MULTIPLICATIVO, PARENTESE_ABERTO, PARENTESE_FECHADO, INTEGER, REAL, BOOLEANO
	}
	
	private Stack<Tipo> pilhaOperadores = new Stack<Tipo>();
	private Stack<Tipo> pilhaTipos = new Stack<Tipo>();
	
	public AnalisadorExpressao(ArrayList<Tipo> expressao) {
		this.expressao = expressao;
	}
	
	public Tipo ehExpressaoValida() {
		
		for (int i = 0; i < expressao.size(); ++i) {
			ehTipoValor = false;
			empilha(i);
			if (ehTipoValor) {
				if (temPrecedencia(i)) {
					realizarOperacao();
				} else {
					continue;
				}
			}
		}
		
		while (!pilhaOperadores.isEmpty()) {
			realizarOperacao();
		}
		return Tipo.INTEGER;
	}
	
	private void empilha(int index) {
		if (expressao.get(index) == Tipo.INTEGER || expressao.get(index) == Tipo.REAL || expressao.get(index) == Tipo.BOOLEANO) { 
			ehTipoValor = true;
			pilhaTipos.push(expressao.get(index));
			System.out.println("empilhou Tipo: " + expressao.get(index));
		} else if (expressao.get(index).compareTo(Tipo.PARENTESE_FECHADO) == 0) {
			System.out.println("Achou parentese fechado");
			desempilhaAteParentese();
		} else {
			pilhaOperadores.push(expressao.get(index));
			System.out.println("empilhou Operador: " + expressao.get(index));
		}
	}
	
	private void realizarOperacao() {
		System.out.println("Compara os tipos da pilha");
		System.out.println("desempilha Operador: " + pilhaOperadores.pop());
		System.out.println("desempilha Tipo: " + pilhaTipos.pop());		
	}
	
	private void desempilhaAteParentese() {
		while (!pilhaOperadores.isEmpty() && pilhaOperadores.peek().compareTo(Tipo.PARENTESE_ABERTO) != 0) {
			realizarOperacao();
		}		
		System.out.println("desempilha o parentese: " + pilhaOperadores.pop());
	}
	
	private boolean temPrecedencia(int index) {
		if (pilhaOperadores.isEmpty())
			return false;
		if (pilhaOperadores.peek().compareTo(Tipo.PARENTESE_ABERTO) == 0)
			return false;
		if (index + 1 <= expressao.size() - 1) {
			if (pilhaOperadores.peek().compareTo(expressao.get(index+1)) > 0
					|| pilhaOperadores.peek().compareTo(expressao.get(index+1)) == 0) {
				System.out.println("Operador " + pilhaOperadores.peek() + " tem precedencia sobre " + expressao.get(index+1));
				return true;
			} else {
				System.out.println("Operador " + pilhaOperadores.peek() + " não tem precedencia sobre " + expressao.get(index+1));
				return false;
			}
		}
		return true;
	}
}
