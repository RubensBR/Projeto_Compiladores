package compilador.lexico;

public class ExpressaoRegular {
	public static String DIGITO = "[0-9]";
	public static String LETRA = "[a-zA-Z]";
	public static String IDENTIFICADOR = "[a-zA-Z_0-9]";
	public static String DELIMITADOR = "^[;.:(),]$";
	public static String ATRIBUICAO = "^:=$";
	public static String OPERADOR_RELACIONAL = "^[=<>]$|^<=$|^>=$|^<>$";
	public static String OPERADOR_ADITIVO = "^[+-]$|^or$";
	public static String OPERADOR_MULTIPLICATIVO = "^[*/]$|^and$";
}
