package compilador.sintatico;

import java.util.ArrayList;

import compilador.lexico.Elemento;
import compilador.lexico.TipoToken;

public class AnalisadorSintatico {

	private ArrayList<Elemento> tabela;
	private int index = 0;
	private Elemento simboloLido = null;
	
	public AnalisadorSintatico(ArrayList<Elemento> tabela) {
		this.tabela = tabela;
	}
	
	public void analisar() {
		programa();
	}
	
	/*Métodos para auxiliar se um elemento da tabela é de um determinado tipo ou um determinado tipo e símbolo*/
	private boolean checarTipoElemento(Elemento elemento, TipoToken tipo, String simbolo) {
			return (elemento.getTipo() == tipo && elemento.getToken().equals(simbolo));
	}
	private boolean checarTipoElemento(Elemento elemento, TipoToken tipo) {
		return (elemento.getTipo() == tipo);
	}
	
	private void obterSimbolo() {
		if (tabela.size() == index) {
			System.out.println("Encerrou");
			System.exit(1);
		}
		
		simboloLido = tabela.get(index++);
		System.out.println("Leu: '" + simboloLido.getToken() + "' linha: " + simboloLido.getLinha());
	}	
	
	private void gerarErro(String msg) {
		System.out.println("Erro: " + msg + ", " + "linha: " + tabela.get(index - 1).getLinha());
		System.exit(1);
	}
	//programa → program id; declarações_variáveis declarações_de_subprogramas comando_composto . 
	private void programa() {
		obterSimbolo();
		if (checarTipoElemento(simboloLido, TipoToken.PALAVRA_CHAVE, "program")) {
			obterSimbolo();
			if (checarTipoElemento(simboloLido, TipoToken.IDENTIFICADOR)) {
				obterSimbolo();
				if (!checarTipoElemento(simboloLido, TipoToken.DELIMITADOR, ";")){
					gerarErro("';' esperado após identificador");
				}
			} else {
				gerarErro("Identificador esperado após palavra-chave program");	
			}
		} else {
			gerarErro("Palavra-chave program esperada");
		}
		
		obterSimbolo();
		declaracoesVariaveis();
//		declaracoesSubprogramas();
//		comandoComposto();
//		fimDePrograma();
		
		System.out.println("programa ok");
	}
	
	//declarações_variáveis → var lista_declarações_variáveis | ε
	private void declaracoesVariaveis() {
		if (checarTipoElemento(simboloLido, TipoToken.PALAVRA_CHAVE, "var")) {
			obterSimbolo();
			listaDeclaracoesVariaveis();
		}
	}
	
	//lista_declarações_variáveis_2 → lista_de_identificadores : tipo;  lista_declarações_variáveis_2 | ε
	private void listaDeclaracoesVariaveisAux() {
		if (checarTipoElemento(simboloLido, TipoToken.IDENTIFICADOR)){
			listaIdentificadores();
			if (checarTipoElemento(simboloLido, TipoToken.DELIMITADOR, ":")) {
				obterSimbolo();
				tipo();
				if (checarTipoElemento(simboloLido, TipoToken.DELIMITADOR, ";")) {
					obterSimbolo();
					listaDeclaracoesVariaveisAux();
				} else {
					gerarErro("';' esperado após identificador");
				}
			}
			else {
				gerarErro("Delimitador ':' esperado após identificador");
			}
		}
	}
	
	
	//lista_declarações_variáveis → lista_de_identificadores : tipo;  lista_declarações_variáveis_2
	private void listaDeclaracoesVariaveis() {
		listaIdentificadores();
		if (checarTipoElemento(simboloLido, TipoToken.DELIMITADOR, ":")) {
			obterSimbolo();
			tipo();
			if (checarTipoElemento(simboloLido, TipoToken.DELIMITADOR, ";")) {
				obterSimbolo();
				listaDeclaracoesVariaveisAux();
			} else {
				gerarErro("';' esperado após identificador");
			}
		}
		else {
			gerarErro("Delimitador ':' esperado após identificador");
		}
	}
	
	
	//lista_de_identificadores_2 → , id lista_de_identificadores_2 | ε
	private void listaIdentificadoresAux() {		
		if (checarTipoElemento(simboloLido, TipoToken.DELIMITADOR, ",")) {
			obterSimbolo();
			if (checarTipoElemento(simboloLido, TipoToken.IDENTIFICADOR)) {
				obterSimbolo();
				listaIdentificadoresAux();
			} else {
				gerarErro("Declaração de identificador esperado");
			}
		}
	}
	
	//lista_de_identificadores → id lista_de_identificadores_2
	private void listaIdentificadores() {
		if (checarTipoElemento(simboloLido, TipoToken.IDENTIFICADOR)) {
			obterSimbolo();
			listaIdentificadoresAux();
		} else {
			gerarErro("Declaração de identificador esperado");
		}
	}

	private void tipo() {
		if (checarTipoElemento(simboloLido, TipoToken.PALAVRA_CHAVE, "integer") 
				|| checarTipoElemento(simboloLido, TipoToken.PALAVRA_CHAVE, "real")
				|| checarTipoElemento(simboloLido, TipoToken.PALAVRA_CHAVE, "boolean")) {
			obterSimbolo();
		} else {
			gerarErro("Declaração de tipo esperado");
		}
	}	
}
