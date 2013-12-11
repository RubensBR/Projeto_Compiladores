package compilador.semantico;

import java.util.ArrayList;
import java.util.Stack;

public class AnalisadorExpressao {

	private ArrayList<Tipo> expressao;
	private boolean ehTipoValor;
	public enum Tipo {
		ERRO, DESCONHECIDO, MARCADOR, PROCEDURE, FUNCTION, PROGRAM, //marcadores 
		LOGICO, RELACIONAL, ADITIVO, MULTIPLICATIVO, NOT, 		   //operadores 
		PARENTESE_ABERTO, PARENTESE_FECHADO, 			  		   //parenteses
		INTEGER, REAL, BOOLEANO 						  		   //tipos
	}
	private String mensagemErro = null;
	
	private Stack<Tipo> pilhaOperadores = new Stack<Tipo>();
	private Stack<Tipo> pilhaTipos = new Stack<Tipo>();
	private Tipo res;
	
	public AnalisadorExpressao(ArrayList<Tipo> expressao) {
		this.expressao = expressao;
	}
	
	public String getMensagemErro() {
		return mensagemErro;
	}
	
	public Tipo ehExpressaoValida() {
		
		for (int i = 0; i < expressao.size(); ++i) {
			ehTipoValor = false;
			if (!empilha(i)) {
				return Tipo.ERRO;
			}
				
			if (ehTipoValor) {
				if (temPrecedencia(i)) {
					if (!realizarOperacao())
						break;
				} else {
					continue;
				}
			}
		}
		
		while (!pilhaOperadores.isEmpty() && pilhaTipos.peek() != Tipo.ERRO) {
			realizarOperacao();
		}
		if (pilhaTipos.isEmpty() && mensagemErro == null) {
			mensagemErro = "expressão vazia";
			return Tipo.ERRO;
		}
		res = pilhaTipos.pop();
		return res;
	}
	
	private boolean empilha(int index) {
		if (expressao.get(index) == Tipo.INTEGER || expressao.get(index) == Tipo.REAL || expressao.get(index) == Tipo.BOOLEANO) { 
			ehTipoValor = true;
			pilhaTipos.push(expressao.get(index));
			//System.out.println("empilhou Tipo: " + expressao.get(index));
			return true;
		} 
		if (expressao.get(index).compareTo(Tipo.PARENTESE_FECHADO) == 0) {
			//System.out.println("Achou parentese fechado");
			desempilhaAteParentese();
			return true;
		}
		
		if (expressao.get(index).compareTo(Tipo.PROCEDURE) == 0 || expressao.get(index).compareTo(Tipo.PROGRAM) == 0) {
			//System.out.println("empilhou Operador: " + expressao.get(index));
			mensagemErro = expressao.get(index) + " não pode ser utilizado em expressão";
			return false;
		}
		
		pilhaOperadores.push(expressao.get(index));
		//System.out.println("empilhou Operador: " + expressao.get(index));
		return true;
	}
	
	private boolean realizarOperacao() {
		
		
		Tipo operador = pilhaOperadores.pop();
		if (operador == Tipo.NOT) {
			//System.out.println("Comparando operador not com tipo " + pilhaTipos.peek());
			if (pilhaTipos.peek() == Tipo.BOOLEANO) {
				return true;
			} 
			else {
				pilhaTipos.push(Tipo.ERRO);
				mensagemErro = "Operador not não é aplicável ao tipo " + pilhaTipos.peek();
				return false;
			}
		}
		
		Tipo tipo1 = pilhaTipos.pop();
		Tipo tipo2 = pilhaTipos.pop();
		
		
		Tipo res = getTipoResultante(tipo1, tipo2, operador);
		pilhaTipos.push(res);
		/*
		System.out.println("-- Compara os tipos da pilha (" + tipo1 + ", " + tipo2 + ")");
		System.out.println("Desempilha Tipo: " + tipo1);
		System.out.println("Desempilha Tipo: " + tipo2);		
		System.out.println("Desempilha Operador: " + operador);
		System.out.println("Empilha tipo resultante: " + res);
		System.out.println("-----------------------");*/
		if (res == Tipo.ERRO)
			return false;
		else 
			return true;
	}
	
	private void desempilhaAteParentese() {
		while (!pilhaOperadores.isEmpty() && pilhaOperadores.peek().compareTo(Tipo.PARENTESE_ABERTO) != 0) {
			realizarOperacao();
		}		
		pilhaOperadores.pop();
		//System.out.println("desempilha o parentese: " + "(");
	}
	
	private boolean temPrecedencia(int index) {
		if (pilhaOperadores.isEmpty())
			return false;
		if (pilhaOperadores.peek().compareTo(Tipo.PARENTESE_ABERTO) == 0)
			return false;
		if (index + 1 <= expressao.size() - 1) {
			if (pilhaOperadores.peek().compareTo(expressao.get(index+1)) > 0
					|| pilhaOperadores.peek().compareTo(expressao.get(index+1)) == 0) {
				//System.out.println("Operador " + pilhaOperadores.peek() + " tem precedencia sobre " + expressao.get(index+1));
				return true;
			} else {
				//System.out.println("Operador " + pilhaOperadores.peek() + " não tem precedencia sobre " + expressao.get(index+1));
				return false;
			}
		}
		return true;
	}
	
	private Tipo getTipoResultante(Tipo tipo1, Tipo tipo2, Tipo operador) {
		if (tipo1.compareTo(tipo2) == 0 && (tipo1 == Tipo.INTEGER || tipo1 == Tipo.REAL) 
				&& (operador == Tipo.ADITIVO || operador == Tipo.MULTIPLICATIVO)) {
			return tipo1;
		}
		else if ( ((tipo1 == Tipo.INTEGER && tipo2 == Tipo.REAL) || (tipo1 == Tipo.REAL && tipo2 == Tipo.INTEGER))  
				 && (operador == Tipo.ADITIVO || operador == Tipo.MULTIPLICATIVO)) {
			return Tipo.REAL;
		} 
		else if ((tipo1 == Tipo.INTEGER || tipo1 == Tipo.REAL) && (tipo2 == Tipo.INTEGER || tipo2 == Tipo.REAL)
				&& operador == Tipo.RELACIONAL) {
			return Tipo.BOOLEANO;
		}	
		else if (tipo1 == Tipo.BOOLEANO && tipo2 == Tipo.BOOLEANO && operador == Tipo.LOGICO) {
			return Tipo.BOOLEANO;
		} 
		else {
			mensagemErro = "O tipo " + tipo2 + " e o tipo " + tipo1 + " não sumportam o operador " + operador;
			return Tipo.ERRO;
		}
	}
	
	public boolean ehAtribuicaoValida(Tipo variavel) {
		if (variavel == Tipo.REAL && (res == Tipo.REAL || res == Tipo.INTEGER))
			return true;
		else if (variavel == Tipo.INTEGER && res == Tipo.INTEGER)
			return true;
		else if (variavel == Tipo.BOOLEANO && res == Tipo.BOOLEANO)
			return true;
		else 
			return false;
	}
	
}
