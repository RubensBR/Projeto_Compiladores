package compilador.lexico;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PalavrasChave {
	private static String[] palavrasChave = {"program", "var", "integer", "real", "boolean", "procedure",
		"begin", "end", "if", "then", "else", "while", "do", "function"};
	
	private static String expressao;
	
	static {
		//monta a expressão regular ^program$|^var$|^integer$|^real$ ... |^not$
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < palavrasChave.length; ++i) {			
			if (i == palavrasChave.length - 1) 
				s.append("^" + palavrasChave[i] + "$");
			else 
				s.append("^" + palavrasChave[i] + "$" + "|");			
		}		
		expressao = s.toString();
	}
	
	//checa se o token é uma palavra chave
	public static boolean checar(String token) {		
		Pattern pattern = Pattern.compile(expressao);
		Matcher matcher = pattern.matcher(token);
		if (matcher.find()) 
			return true;
		else
			return false;		
	}
	
	
}
