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
			System.out.println("Fim de programa inesperado");
			System.exit(1);
		}
		
		simboloLido = tabela.get(index++);
		//System.out.println("Leu: '" + simboloLido.getToken() + "' linha: " + simboloLido.getLinha());
	}	
	
	private void gerarErro(String msg) {
		System.out.println(">>> Erro: " + msg + ", " + "linha: " + tabela.get(index - 1).getLinha());
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
		declaracoesSubprogramas();
		comandoComposto();
		fimDePrograma();
		
		System.out.println("Programa analisado com sucesso.");
	}
	
	//declarações_variáveis → var lista_declarações_variáveis | ε
	private void declaracoesVariaveis() {
		if (checarTipoElemento(simboloLido, TipoToken.PALAVRA_CHAVE, "var")) {
			obterSimbolo();
			listaDeclaracoesVariaveis();
		}
	}
	
	//lista_declarações_variáveis_aux → lista_de_identificadores : tipo;  lista_declarações_variáveis_2 | ε
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
	
	
	//lista_declarações_variáveis → lista_de_identificadores : tipo;  lista_declarações_variáveis_aux
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
	
	
	//lista_de_identificadores_2 → , id lista_de_identificadores_aux | ε
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
	
	//lista_de_identificadores → id lista_de_identificadores_aux
	private void listaIdentificadores() {
		if (checarTipoElemento(simboloLido, TipoToken.IDENTIFICADOR)) {
			obterSimbolo();
			listaIdentificadoresAux();
		} else {
			gerarErro("Declaração de identificador esperado");
		}
	}
	
	//tipo → integer | real | boolean
	private void tipo() {
		if (checarTipoElemento(simboloLido, TipoToken.PALAVRA_CHAVE, "integer") 
				|| checarTipoElemento(simboloLido, TipoToken.PALAVRA_CHAVE, "real")
				|| checarTipoElemento(simboloLido, TipoToken.PALAVRA_CHAVE, "boolean")) {
			obterSimbolo();
		} else {
			gerarErro("O tipo '" + simboloLido.getToken() + "' não é válido");
		}
	}	
	
	//declarações_de_subprogramas → declaração_de_subprograma ; declaração_de_subprogramas| ε
	private void declaracoesSubprogramas() {
		if (!checarTipoElemento(simboloLido, TipoToken.PALAVRA_CHAVE, "procedure"))
			return;
		declaracaoSubprograma();
		if (checarTipoElemento(simboloLido, TipoToken.DELIMITADOR, ";")) {
			obterSimbolo();
			declaracoesSubprogramas();
		} else {
			gerarErro("Delimitador ';' esperado após declaração de subprograma");
		}
	}
	
	//declaração_de_subprograma → procedure id argumentos; declarações_variáveis declarações_de_subprogramas comando_composto
	private void declaracaoSubprograma() {
		if (checarTipoElemento(simboloLido, TipoToken.PALAVRA_CHAVE, "procedure")) {
			obterSimbolo();
			if (checarTipoElemento(simboloLido, TipoToken.IDENTIFICADOR)) {
				obterSimbolo();
				argumentos();
				if (checarTipoElemento(simboloLido, TipoToken.DELIMITADOR, ";")) {
					obterSimbolo();
					declaracoesVariaveis();
					declaracoesSubprogramas();
					comandoComposto();
				} else {
					gerarErro("Delimitador ';' esperado");
				}
			} else {
				gerarErro("Identificador esperado");
			}
		} else {
			gerarErro("Palavra-chave procedure esperada");
		}
	}
	
	//argumentos → (lista_de_parametros) | ε
	private void argumentos() {
		if (checarTipoElemento(simboloLido, TipoToken.DELIMITADOR, "(")) {
			obterSimbolo();
			listaParametros();
			if (checarTipoElemento(simboloLido, TipoToken.DELIMITADOR, ")")) {
				obterSimbolo();
			} else {
				gerarErro("Delimitador ')' esperado após a declaração dos parâmetros");
			}
		}
	}
	
	//lista_de_parametros_aux → ; lista_de_identificadores : tipo lista_de_parametros_aux | ε
	private void listaParametrosAux() {
		if (checarTipoElemento(simboloLido, TipoToken.DELIMITADOR, ";")) {
			obterSimbolo();
			listaIdentificadores();
			if (checarTipoElemento(simboloLido, TipoToken.DELIMITADOR, ":")) {
				obterSimbolo();
				tipo();
				listaParametrosAux();
			} else {
				gerarErro("Delimitador ':' esperado");
			}
		}
	}
	
	//lista_de_parametros →  lista_de_identificadores : tipo lista_de_parametros_aux
	private void listaParametros() {
		listaIdentificadores();
		if (checarTipoElemento(simboloLido, TipoToken.DELIMITADOR, ":")) {
			obterSimbolo();
			tipo();
			listaParametrosAux();
		} else {
			gerarErro("Delimitador ':' esperado");
		}
	}
	
	//comando_composto → begin comandos_opcionais end
	private void comandoComposto() {
		if (checarTipoElemento(simboloLido, TipoToken.PALAVRA_CHAVE, "begin")) {
			obterSimbolo();
			comandosOpcionais();
			if (checarTipoElemento(simboloLido, TipoToken.PALAVRA_CHAVE, "end")) {
				obterSimbolo();
			} else {
				gerarErro("Palavra-chave end esperada no lugar de " + simboloLido.getToken());
			}
		} else {
			gerarErro("Palavra-chave begin esperada no lugar de " + simboloLido.getToken());
		}
	}
	
	//comandos_opcionais → lista_de_comandos | ε
	private void comandosOpcionais() {
		listaComandos();
	}
	
	//lista_de_comandos_ aux → ; comando lista_de_comandos_ aux | ε
	private void listaComandosAux() {
		if (checarTipoElemento(simboloLido, TipoToken.DELIMITADOR, ";")) {
			obterSimbolo();
			comando();
			listaComandosAux();
		}
	}
	
	//lista_de_comandos → comando lista_de_comandos_aux
	private void listaComandos() {
		comando();
		listaComandosAux();
	}
	
	/* comando → variável := expressão | ativação_de_procedimento | comando_composto 
			| if expressão then comando parte_else | while expressão do comando */
	private boolean comando() {
		if (variavelAtribuicaoExpressao()) {
			return true;
		} else if (checarTipoElemento(simboloLido, TipoToken.PALAVRA_CHAVE, "begin")) {
			comandoComposto();
			return true;
		} else if (ifThen()) {
			return true;
		} else if (whileDo()) {
			return true;
		} else if(forTo()) {
			return true;
		} else {
			return false;
		}
	}
	
	/*comando -> for variavel := INT TO   INT listaComandos
	comando -> for variavel := INT UNTO INT listaComandos*/
	private boolean forTo() {
		if (checarTipoElemento(simboloLido, TipoToken.PALAVRA_CHAVE, "for")) {
			obterSimbolo();
			if (checarTipoElemento(simboloLido, TipoToken.IDENTIFICADOR)) {
				obterSimbolo();
				if (checarTipoElemento(simboloLido, TipoToken.COMANDO_ATRIBUICAO)) {
					obterSimbolo();
					if (checarTipoElemento(simboloLido, TipoToken.NUMERO_INTEIRO)) {
						obterSimbolo();
						if (checarTipoElemento(simboloLido, TipoToken.PALAVRA_CHAVE, "to") 
								|| checarTipoElemento(simboloLido, TipoToken.PALAVRA_CHAVE, "unto")) {
							obterSimbolo();
							if (checarTipoElemento(simboloLido, TipoToken.NUMERO_INTEIRO)) {
								obterSimbolo();
								listaComandos();
								return true;
							} else {
								gerarErro("esperado valor inteiro após 'to' ou 'unto'");
								return true;
							}
						} else {
							gerarErro("Esperado palavara-chave 'to' ou 'unto'");
							return true;
						}
					} else {
						gerarErro("esperado valor inteiro após comando de atribuição");
						return true;
					}
				} else {
					gerarErro("esperado comando de atribuição após variável");
					return true;
				}
				
			} else {
				gerarErro("variável esperada após 'for'");
				return true;
			}
		} else {
			return false;
		}		
	}
	
	//variável := expressão
	private boolean variavelAtribuicaoExpressao() {
		//variável
		if (checarTipoElemento(simboloLido, TipoToken.IDENTIFICADOR)) {
			// --Semantico--
			String aux = simboloLido.getToken();
			// -------------
			obterSimbolo();
			if (checarTipoElemento(simboloLido, TipoToken.COMANDO_ATRIBUICAO)) {
				// --Semantico--
				System.out.println(aux + " = ");
				// -------------
				obterSimbolo();
				expressao();
				// --Semantico--
				System.out.println(">>>Testar Expressao");
				// -------------
				return true;
			} else {
				/*Como variável é um id verifica se poder ser uma ativação de procedimento*/
				return ativacaoProcedimento(); 
			}
		} else {
			return false;
		}
	}
	
	//ativação_de_procedimento → id | id (lista_de_expressões)
	private boolean ativacaoProcedimento() {	
		if (checarTipoElemento(simboloLido, TipoToken.DELIMITADOR, "(")) {
			obterSimbolo();
			listaExpressoes();
			if (checarTipoElemento(simboloLido, TipoToken.DELIMITADOR, ")")) {
				obterSimbolo();
				return true;
			} else {
				gerarErro("delemitador ')' esperado");
				return true;
			}
		} else { //então é só o id
			return true;				
		}
	}
	
	//if expressão then comando parte_else 
	private boolean ifThen() {
		if (checarTipoElemento(simboloLido, TipoToken.PALAVRA_CHAVE, "if")) {
			obterSimbolo();
			expressao();
			if (checarTipoElemento(simboloLido, TipoToken.PALAVRA_CHAVE, "then")) {
				obterSimbolo();
				comando();
				parteElse();
				return true;
			} else {
				gerarErro("esperado palavra chave 'then' no lugar de " + simboloLido.getToken());
				return true;
			}
		} else {
			return false;
		}
	}
	
	//while expressão do comando
	private boolean whileDo() {
		if (checarTipoElemento(simboloLido, TipoToken.PALAVRA_CHAVE, "while")) {
			obterSimbolo();
			expressao();
			if (checarTipoElemento(simboloLido, TipoToken.PALAVRA_CHAVE, "do")) {
				obterSimbolo();
				comando();
				return true;
			} else {
				gerarErro("esperado palavra chave 'do' no lugar de " + simboloLido.getToken());
				return true;
			}
		} else {
			return false;
		}
	}
	
	//parte_else → else comando | ε
	private void parteElse() {
		
		if (checarTipoElemento(simboloLido, TipoToken.PALAVRA_CHAVE, "else")) {
			obterSimbolo();
			comando();
		}
	}
	
	//lista_de_expressões_aux → , expressão lista_de_expressões_aux | ε
	private void listaExpressoesAux() {
		if (checarTipoElemento(simboloLido, TipoToken.DELIMITADOR, ",")) {
			expressao();
			listaExpressoesAux();
		} 
	}
	
	//lista_de_expressões → expressão lista_de_expressões_ aux
	private void listaExpressoes() {
		expressao();
		listaExpressoesAux();
	}
	
	//expressão → expressão_simples | expressão_simples op_relacional expressão_simples
	private void expressao() {
		// -- Semantico --
		System.out.println("Expressão {");
		// ---------------
		if (expressaoSimples(true)) {
			if(opRelacioal()) {
				expressaoSimples(false);
				System.out.println("}");
			} else {
				System.out.println("}");
				return;
			}
		}
	}
	
	//expressão_simples_aux →  op_aditivo termo expressão_simples_ aux | ε
	private void expressaoSimplesAux() {
		if (opAditivo()) {
			if (termo(true)) {
				expressaoSimplesAux();
			} else {
				gerarErro("esperardo identificador ou identificador (lista_de_expressões) ou num_int ou num_real ou true ou false ou (expressão) ou not");
			}
		}
	}
	
	//expressão_simples →  termo expressão_simples_ aux | sinal termo expressão_simples_ aux 
	private boolean expressaoSimples(boolean teste) {
		if (termo(true)) {
			expressaoSimplesAux();
			return true;
		} else if (sinal()) {
			if (termo(true)) {
				expressaoSimplesAux();
				return true;
			}
		} else {
			if (!teste)
				gerarErro("expressão inválida");
			return false;
		}
		return false;
	}
	
	//termo_aux →  op_multiplicativo fator termo_ aux | ε
	private void termoAux() {
		if (opMultiplicativo()) {
			if (fator()) {
				termoAux();
			} else {
				gerarErro("esperardo identificador ou identificador (lista_de_expressões) ou num_int ou num_real ou true ou false ou (expressão) ou not");
			}
		} 
	}
	
	//termo → fator termo_aux
	private boolean termo(boolean teste) { 
		if (fator()) {
			termoAux();
			return true;
		} else {
			if (!teste)				
				gerarErro("esperardo identificador ou identificador (lista_de_expressões) ou num_int ou num_real ou true ou false ou (expressão) ou not");
			return false;
		}
	}
	
	
	//#fator → id | id (lista_de_expressões) | num_int | num_real | true | false | (expressão) | not fator
	private boolean fator() {
		if (checarTipoElemento(simboloLido, TipoToken.IDENTIFICADOR)) {
			// --Semantico--
			System.out.println("idenfificador: " + simboloLido.getToken());
			// -------------
			obterSimbolo();
			if (checarTipoElemento(simboloLido, TipoToken.DELIMITADOR, "(")) {
				// --Semantico--
				System.out.println("(");
				// -------------
				obterSimbolo();
				listaExpressoes();
				if (checarTipoElemento(simboloLido, TipoToken.DELIMITADOR, ")")) {
					// --Semantico--
					System.out.println(")");
					// -------------
					obterSimbolo();
					return true;
				} else {
					gerarErro("delimitadir ')' esperado");
					return true;
				}
			} else {
				return true;
			}
			
		} else if (checarTipoElemento(simboloLido, TipoToken.NUMERO_INTEIRO) 
				|| checarTipoElemento(simboloLido, TipoToken.NUMERO_REAL) 
				|| checarTipoElemento(simboloLido, TipoToken.OPERADOR_LOGICO, "true")
				|| checarTipoElemento(simboloLido, TipoToken.OPERADOR_LOGICO, "false")) {
			
			// --Semantico--
			System.out.println(simboloLido.getClassificao());
			// -------------
			obterSimbolo();
			return true;
			
		} else if (checarTipoElemento(simboloLido, TipoToken.DELIMITADOR, "(")) {
			// --Semantico--
			System.out.println("(");
			// -------------
			
			obterSimbolo();
			expressao();
			if (checarTipoElemento(simboloLido, TipoToken.DELIMITADOR, ")")) {
				// --Semantico--
				System.out.println(")");
				// -------------
				obterSimbolo();
				return true;
			} else {
				gerarErro("delimitador ')' esperado");
				return true;
			}
			
		} else if (checarTipoElemento(simboloLido, TipoToken.OPERADOR_LOGICO, "not")) {
			obterSimbolo();
			if (fator()) {
				return true;
			} else {
				gerarErro("simbolo inválido para operador 'not'");
				return true;
			}
		
		} else {
			return false;
		}			
	}
	
	//sinal → + | - 
	private boolean sinal() {
		if (checarTipoElemento(simboloLido, TipoToken.OPERADOR_ADITIVO, "+") 
				|| (checarTipoElemento(simboloLido, TipoToken.OPERADOR_ADITIVO, "-"))) {
			obterSimbolo();
			return true;
		}
		return false;
	}
	
	//op_relacional → = | < | > | <= | >= | <>
	private boolean opRelacioal() {
		if (checarTipoElemento(simboloLido, TipoToken.OPERADOR_RELACIONAL)) {
			// --Semantico--
			System.out.println("operador Relacional");
			// -------------
			obterSimbolo();
			return true;
		}
		return false;
	}
	
	//op_aditivo → + | - | or
	private boolean opAditivo() {
		if (checarTipoElemento(simboloLido, TipoToken.OPERADOR_ADITIVO)) {
			// --Semantico--
			System.out.println("Operador Aditivo");
			// -------------
			obterSimbolo();
			return true;
		}
		return false;
	}
	
	//op_multiplicativo → * | / | and
	private boolean opMultiplicativo() {
		if (checarTipoElemento(simboloLido, TipoToken.OPERADOR_MULTIPLICATIVO)) {
			// --Semantico--
			System.out.println("Operador Multiplicativo");
			// -------------
			obterSimbolo();
			return true;
		}
		return false;
	}
	
	private void fimDePrograma() {
		if (!checarTipoElemento(simboloLido, TipoToken.DELIMITADOR, ".")) {
			gerarErro("delimitador de fim de programa '.' esperado");
		}
	}
}
