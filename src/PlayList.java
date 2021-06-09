import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PlayList {

	private String nombrePlayList;
	private Estilo estilo;
	private List<Cancion> listaCanciones;
	private List<Valoracion> listaValoraciones;

	public PlayList() {}
	public PlayList(String nombrePlayList, Estilo estilo) {
		super();
		this.nombrePlayList = nombrePlayList;
		this.estilo = estilo;
		listaCanciones = new LinkedList<Cancion>();
		listaValoraciones = new ArrayList<Valoracion>();
	}

	public PlayList(String nombrePlayList) {
		super();
		this.nombrePlayList = nombrePlayList;
	}

	public PlayList(Estilo estilo) {
		super();
		this.estilo = estilo;
	}

	public Estilo getEstilo() {
		return estilo;
	}

	public void setEstilo(Estilo estilo) {
		this.estilo = estilo;
	}

	public String getNombrePlayList() {
		return nombrePlayList;
	}

	public void setNombrePlayList(String nombrePlayList) {
		this.nombrePlayList = nombrePlayList;
	}

	public List<Cancion> getListaCanciones() {
		return listaCanciones;
	}

	public void addCancion(Cancion cancion) {
		listaCanciones.add(cancion);
	}

	public void addValoracion(Valoracion valoracion) {
		listaValoraciones.add(valoracion);
	}

	public boolean buscarCancion(Cancion c) {
		boolean encontrado;

		encontrado = listaCanciones.contains(c);

		return encontrado;
	}

	public List<Valoracion> getListaValoraciones() {
		return listaValoraciones;
	}

	public double mediaValoraciones() {

		double media = 0;
		for (Valoracion valoracion : listaValoraciones) {
			media += valoracion.getNota();
		}
		if (listaValoraciones.size() > 0) {
			media = media / listaValoraciones.size();
		}
		return media;
	}

	@Override
	public String toString() {
		return "PlayList :" + nombrePlayList + " Estilo: " + estilo + " , listaCanciones=" + listaCanciones + "]";
	}

}
