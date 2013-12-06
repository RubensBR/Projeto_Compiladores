package compilador.semantico;

public class Arvore {
	
	private Arvore filhoEsq;
	private Arvore filhoDir;
	private String token;
	
	public Arvore getFilhoEsq() {
		return filhoEsq;
	}
	public void setFilhoEsq(Arvore filhoEsq) {
		this.filhoEsq = filhoEsq;
	}
	public Arvore getFilhoDir() {
		return filhoDir;
	}
	public void setFilhoDir(Arvore filhoDir) {
		this.filhoDir = filhoDir;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	
}
