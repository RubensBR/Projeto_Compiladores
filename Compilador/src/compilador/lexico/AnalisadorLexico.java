package compilador.lexico;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnalisadorLexico {

	private String programa;
	private int linha = 1;
	private boolean ehComentario = false;
	private PushbackReader reader;
	
	public AnalisadorLexico(String programa) {
		this.programa = programa;
	}
	
	public ArrayList<Elemento> analisar() {
		ArrayList<Elemento> tabela = new ArrayList<Elemento>();
		try {
			BufferedReader buffer = new BufferedReader(new FileReader(programa));
			reader = new PushbackReader(buffer);
			
			//laço principal
			while (reader.ready()) {				
				char caractere = (char) reader.read();
				Elemento elemento = processaCaractere(caractere);
				if (elemento != null) {
					tabela.add(elemento);
				}				
			}				
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
		if (ehComentario) {
			System.out.println("Erro: Comentário aberto e não fechado.");
		}
		
		return tabela;
	}
	
	private Elemento processaCaractere(char caractere) throws IOException {
		
		if (ehComentario) {
			checarComentario(caractere);
			return null;
		}
		
		if (checarEspacosEChaves(caractere)) {
			return null;
		}
		
		if (checarTipo(caractere, ExpressaoRegular.LETRA)) {
			return processaLetra(caractere);
		}	
		
		if (checarTipo(caractere, ExpressaoRegular.DIGITO)) {
			return processaDigito(caractere);
		}
		
		if (checarTipo(caractere, ExpressaoRegular.DELIMITADOR)) {
			return processaDelimitador(caractere);
		}
		
		if (checarTipo(caractere, ExpressaoRegular.OPERADOR_RELACIONAL)) {
			return processaOperadorRelacional(caractere);
		}		
		
		if (checarTipo(caractere, ExpressaoRegular.OPERADOR_ADITIVO)) {
			return new Elemento(""+caractere, TipoToken.OPERADOR_ADITIVO, linha);
		}
		
		if (checarTipo(caractere, ExpressaoRegular.OPERADOR_MULTIPLICATIVO)) {
			return new Elemento(""+caractere, TipoToken.OPERADOR_MULTIPLICATIVO, linha);
		}		
		
		System.out.println("Erro: O símbolo " + caractere + " não pertence a linguagem. Linha: " + linha);
		return null;
	}
	
	private void checarComentario(char caractere) {
		if (caractere == '\n') {
			++linha;
		}
		
		if (caractere == '}') {
			ehComentario = false;
		}
	}
	
	private boolean checarEspacosEChaves(char caractere) {
		if (caractere == '{') {
			ehComentario = true;
			return true;
		}
		
		if (Character.isWhitespace(caractere)) {
			if (caractere == '\n')
				++linha;
			return true;
		}
		
		return false;
	}
	
	private Elemento processaLetra(char caractere) throws IOException {
		StringBuilder token = new StringBuilder();
		token.append(caractere);
		while (reader.ready()) {
			char proximoCaractere = (char) reader.read();
			if (checarTipo(proximoCaractere, ExpressaoRegular.IDENTIFICADOR)) {
				token.append(proximoCaractere);
			}
			else {
				reader.unread((int) proximoCaractere);
				break;
			}
		}
		
		if (PalavrasChave.checar(token.toString())) {
			if (checarTipo(token.toString(), ExpressaoRegular.OPERADOR_MULTIPLICATIVO))
				return new Elemento(token.toString(), TipoToken.OPERADOR_MULTIPLICATIVO, linha);
			else if (checarTipo(token.toString(), ExpressaoRegular.OPERADOR_ADITIVO))
				return new Elemento(token.toString(), TipoToken.OPERADOR_ADITIVO, linha);
			else
				return new Elemento(token.toString(), TipoToken.PALAVRA_CHAVE, linha);
		}
		else
			return new Elemento(token.toString(), TipoToken.IDENTIFICADOR, linha);
	}
	
	private Elemento processaDigito(char caractere) throws IOException {
		StringBuilder token = new StringBuilder();
		boolean ehReal = false;
		token.append(caractere); 
		while (reader.ready()) {
			char proximoCaractere = (char) reader.read();
			if (checarTipo(proximoCaractere, ExpressaoRegular.DIGITO)) {
				token.append(proximoCaractere);
			}
			else {
				if (proximoCaractere == '.') {
					ehReal = true;
					token.append(proximoCaractere);
					break;
				} 
				reader.unread((int) proximoCaractere);
				break;
			}
		}
		
		if (!ehReal)
			return new Elemento(token.toString(), TipoToken.NUMERO_INTEIRO, linha);		
		
		while (reader.ready()) {
			char proximoCaractere = (char) reader.read();
			if (checarTipo(proximoCaractere, ExpressaoRegular.DIGITO)) {
				token.append(proximoCaractere);
			}
			else {
				reader.unread((int) proximoCaractere);
				break;					
			}
		}
		return new Elemento(token.toString(), TipoToken.NUMERO_REAL, linha);		
	}
	
	private Elemento processaDelimitador(char caractere) throws IOException {
		if (caractere == ':') {
			if (reader.ready()) {
				char proximoCaractere = (char) reader.read();
				String s = "" + caractere + proximoCaractere;
				if (checarTipo(s, ExpressaoRegular.ATRIBUICAO)) {
					return new Elemento(s, TipoToken.COMANDO_ATRIBUICAO, linha);
				}
				else {
					reader.unread((int) proximoCaractere);
				}
			}
		}
		return new Elemento(""+caractere, TipoToken.DELIMITADOR, linha);
	}
	
	private Elemento processaOperadorRelacional(char caractere) throws IOException {
		if (caractere == '<' || caractere == '>') {
			if (reader.ready()) {
				char proximoCaractere = (char) reader.read();
				String s = "" + caractere + proximoCaractere;
				if (checarTipo(s, ExpressaoRegular.OPERADOR_RELACIONAL)) {
					return new Elemento(s, TipoToken.OPERADOR_RELACIONAL, linha);
				}
				else {
					reader.unread((int) proximoCaractere);
				}
			}
		}
		return new Elemento(""+caractere, TipoToken.OPERADOR_RELACIONAL, linha);
	}
	
	private boolean checarTipo(char c, String expressaoRegular) {
		Pattern expressao = Pattern.compile(expressaoRegular);
		Matcher matcher = expressao.matcher("" + c);
		if (matcher.find()) 
			return true;
		else
			return false;
	}
	
	private boolean checarTipo(String s, String expressaoRegular) {
		Pattern expressao = Pattern.compile(expressaoRegular);
		Matcher matcher = expressao.matcher(s);
		if (matcher.find()) 
			return true;
		else
			return false;
	}
}
