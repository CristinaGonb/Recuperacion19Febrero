
public class Cancion {

	private int codigo;
	private String nombreCancion;
	private String cantante;
	public Cancion(int codigo, String nombreCancion, String cantante) {
		super();
		this.codigo = codigo;
		this.nombreCancion = nombreCancion;
		this.cantante = cantante;
	}
	
	public Cancion(int codigo) {
		super();
		this.codigo = codigo;
	}
	
	public int getCodigo() {
		return codigo;
	}


	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}


	public String getNombreCancion() {
		return nombreCancion;
	}


	public void setNombreCancion(String nombreCancion) {
		this.nombreCancion = nombreCancion;
	}


	public String getCantante() {
		return cantante;
	}


	public void setCantante(String cantante) {
		this.cantante = cantante;
	}


	@Override
	public String toString() {
		return "Cancion [codigo=" + codigo + ", nombreCancion=" + nombreCancion + ", cantante=" + cantante + "]";
	}
	
	
	
	
}
