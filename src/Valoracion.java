
public class Valoracion {

	private int nota;
	private String comentario;
	public Valoracion(int nota, String comentario) {
		super();
		this.nota = nota;
		this.comentario = comentario;
	}
	public int getNota() {
		return nota;
	}
	public void setNota(int nota) {
		this.nota = nota;
	}
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	@Override
	public String toString() {
		return "Valoracion [nota=" + nota + ", comentario=" + comentario + "]";
	}
	
	
}
