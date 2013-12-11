package analisador.teste;

import java.util.ArrayList;

import compilador.lexico.AnalisadorLexico;
import compilador.lexico.Elemento;
import compilador.semantico.AnalisadorExpressao;
import compilador.semantico.AnalisadorExpressao.Tipo;
import compilador.sintatico.AnalisadorSintatico;


public class Teste {

	public static void main(String[] args) {
		String caminho = "C:\\Users\\Rubens\\Documents\\GitHub\\Projeto_Compiladores\\programa.txt";

		AnalisadorLexico lexico = new AnalisadorLexico(caminho);
		ArrayList<Elemento> tabela = lexico.analisar();	
		
//		System.out.println("+--------------------+-------------------------+----------+");
//		System.out.format("|%20s|%25s|%10s|\n", "Token        ", "Classificação      ", "Linha  ");
//		System.out.println("+--------------------+-------------------------+----------+");
//		
//		for (Elemento linha : tabela) {
//			String token = linha.getToken();
//			String operacao = linha.getClassificao();
//			String line = ""+ linha.getLinha();
//			System.out.format("|%20s|%25s|%10s|\n", token, operacao, line);
//		}
//		System.out.println("+--------------------+-------------------------+----------+");
		
		AnalisadorSintatico sintatico = new AnalisadorSintatico(tabela);
		sintatico.analisar();

		
	}
}
