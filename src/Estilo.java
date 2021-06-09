
public class Estilo {

	private String nombreEstilo;

	public Estilo(String nombreEstilo) {
		super();
		this.nombreEstilo = nombreEstilo;
	}

	public String getNombreEstilo() {
		return nombreEstilo;
	}

	public void setNombreEstilo(String nombreEstilo) {
		this.nombreEstilo = nombreEstilo;
	}

	@Override
	public String toString() {
		return "Estilo [nombreEstilo=" + nombreEstilo + "]";
	}
	
	
}
