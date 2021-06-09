
import java.io.File;
import java.util.List;
import java.util.Scanner;
import com.db4o.*;
import com.db4o.Db4oEmbedded;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.ObjectContainer;
import com.db4o.query.Predicate;
import com.db4o.query.Query;
import com.db4o.query.QueryComparator;

public class Principal {

	private static final String BD_PLAYLIST = "playList.oo";
	private static final int OPCION_SALIR = 6;
	private static Scanner teclado = new Scanner(System.in);

	public static void main(String[] args) {

		int opc;

		cargarDatosIniciales();
		do {
			opc = solicitarOpcion();
			tratarOpcion(opc);
		} while (opc != OPCION_SALIR);

	}

	private static void cargarDatosIniciales() {

		File f = new File(BD_PLAYLIST); // SI NO EXISTE EL FICHERO CON LA BD LO CREA
		if (!f.exists()) {
			ObjectContainer db = abrirBd();
			guardarDatos(db);

			cerrarBd(db);
		}

	}

	private static ObjectContainer abrirBd() {
		EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
		ObjectContainer db = Db4oEmbedded.openFile(config, BD_PLAYLIST);
		return db;
	}

	private static ObjectContainer abrirBd(EmbeddedConfiguration configuracion) {

		ObjectContainer db = Db4oEmbedded.openFile(configuracion, BD_PLAYLIST);

		return db;
	}

	private static void guardarDatos(ObjectContainer db) {

		Estilo e1 = new Estilo("ROCK");
		Estilo e2 = new Estilo("POP");

		PlayList pl1 = new PlayList("MIA", e1);
		PlayList pl2 = new PlayList("TUYA", e2);
		PlayList pl3 = new PlayList("OTRA", e1);

		pl1.getListaCanciones().add(new Cancion(1, "I CAN'T GET NO (SATISFACTION)", "ROLLING STONES"));
		pl1.getListaCanciones().add(new Cancion(2, "SMELL LIKE TEAM SPIRITS", "NIRVANA"));

		pl2.getListaCanciones().add(new Cancion(3, "THRILLER", "MICHAEL JACKSON"));
		pl2.getListaCanciones().add(new Cancion(4, "LIKE A PRAYER", "MADONNA"));

		pl1.getListaValoraciones().add(new Valoracion(8, "Buenísima.."));
		pl1.getListaValoraciones().add(new Valoracion(7, "Esta bien.."));
		pl1.getListaValoraciones().add(new Valoracion(1, "Malísima.."));

		pl2.getListaValoraciones().add(new Valoracion(9, "La mejor"));
		pl2.getListaValoraciones().add(new Valoracion(7, "Esta bien.."));
		pl2.getListaValoraciones().add(new Valoracion(1, "Mala Malísima"));

		db.store(pl1);
		db.store(pl2);
		db.store(pl3);

	}

	private static void cerrarBd(ObjectContainer db) {
		db.close();
	}

	private static void tratarOpcion(int opc) {

		switch (opc) {

		case 1:
			nuevaCancion();
			break;

		case 2:
			String estilo = solicitarCadena("Introduce el estilo:");
			cancionesDeUnEstiloOrdenadas(estilo);
			break;

		case 3:
			String nombrePlayListBorrar = solicitarCadena("Introduce el nombre del playList a borrar:");
			borrarPlayList(nombrePlayListBorrar);
			break;

		case 4:
			int codigoCancion = solicitarEntero("Introduce el código de la canción");
			String nombrePlayList = solicitarCadena("Introduce el nombre del playList a borrar:");
			incluirCancionEnPlayList(codigoCancion, nombrePlayList);
			break;

		case 5:
			System.out.println(consultarTodo());
			break;
		}

	}

	private static void nuevaCancion() {
		String nombre, cantante;
		int codigo;

		codigo = solicitarEntero("Introduce el codigo de la cancion: ");
		Cancion codigoEncontrado = buscarCodigoCancion(codigo);

		ObjectContainer db = abrirBd();

		if (codigoEncontrado != null) {
			System.out.println("Error, este codigo ya existe " + codigo);
		} else {
			nombre = solicitarCadena("Introduce el nombre de la cancion: ");
			cantante = solicitarCadena("Introduce el nombre del cantante: ");

			codigoEncontrado = new Cancion(codigo, nombre, cantante);
			db.store(codigoEncontrado);
		}
		cerrarBd(db);

	}

	private static void cancionesDeUnEstiloOrdenadas(String estilo) {
		ObjectContainer db = abrirBd();
		db.ext().configure().activationDepth(3);

		ObjectSet<PlayList> resultado = db.query(new Predicate<PlayList>() {

			@Override
			public boolean match(PlayList estiloConMasValoracion) {
				//
				return estiloConMasValoracion.getEstilo().equals(estilo);

				// && estiloConMasValoracion.getListaValoraciones().size() > 2;
			}
		}, new QueryComparator<PlayList>() {

			@Override
			public int compare(PlayList nombreMayor, PlayList nombreMenor) {
				int result = 0;
				if (nombreMayor.mediaValoraciones() > nombreMenor.mediaValoraciones()) {
					result = 1;
				} else if (nombreMayor.mediaValoraciones() < nombreMenor.mediaValoraciones()) {
					result = -1;
				}

				return result;
			}

		});

		if (resultado.isEmpty()) {
			System.out.println(" No hay playlist en " + estilo);
		} else {
			for (PlayList playList : resultado) {
				System.out.println(playList);
			}
		}

		cerrarBd(db);
	}

	private static List<PlayList> consultarTodo() {
		List<PlayList> p = null;

		ObjectContainer db = abrirBd();

		db.ext().configure().activationDepth(4);

		PlayList patron = new PlayList();
		ObjectSet<PlayList> resultado = db.queryByExample(patron);
		if (resultado.size() == 0) {
			System.out.println("Bd vacia");
		} else {

			for (PlayList playList : resultado) {
				System.out.println(playList);
			}
		}

		cerrarBd(db);
		return p;
	}

	private static void borrarPlayList(String nombrePlayListBorrar) {

		PlayList buscarPlayList = buscarPorNombre(nombrePlayListBorrar);

		ObjectContainer db = abrirBd();
		// Para que consulte las colecciones
		db.ext().configure().activationDepth(3);

		// Para borrar solo las valoraciones
		db.ext().configure().objectClass(Valoracion.class).cascadeOnDelete(true);

		if (buscarPlayList != null) {
			System.out.println("¿Esta seguro que desea borrar la valoracion (S/N) ?");
			char respuesta = Character.toUpperCase(teclado.nextLine().charAt(0));

			if (respuesta == 'S') {
				// Borro valoracion
				for (Valoracion v : buscarPlayList.getListaValoraciones()) {
					db.delete(v);
				}
			}
		}
		cerrarBd(db);
	}

	private static void incluirCancionEnPlayList(int codigoCancion, String nombrePlayList) {
		Cancion c = buscarCodigoCancion(codigoCancion);
		PlayList p = buscarPorNombre(nombrePlayList);

		ObjectContainer db = abrirBd();
		// Para que consulte las colecciones
		db.ext().configure().activationDepth(3);

		if (c == null) {
			System.out.println("No existe la cancion con codigo" + codigoCancion);
		} else {
			if (p == null) {
				System.out.println("No existe la playlist con el nombre " + nombrePlayList);
			} else {
				if (p.buscarCancion(c)) {

					p.addCancion(c);
					db.store(p);
				} else {
					System.out.println("La cancion con codigo ya esta en playlist");
				}
			}
		}
		cerrarBd(db);
	}

	private static PlayList buscarPorNombre(String nombre) {
		PlayList p = null;

		ObjectContainer db = abrirBd();

		PlayList patron = new PlayList(nombre);
		ObjectSet<PlayList> resultado = db.queryByExample(patron);

		if (resultado.size() == 1) {
			p = resultado.next();
		}

		cerrarBd(db);
		return p;
	}

	private static Cancion buscarCodigoCancion(int codigo) {
		Cancion codigoEncontrado = null;

		ObjectContainer db = abrirBd();

		Cancion patron = new Cancion(codigo);

		ObjectSet<Cancion> resultado = db.queryByExample(patron);

		if (resultado.size() ==1) {
			codigoEncontrado = resultado.next();
		}

		cerrarBd(db);
		return codigoEncontrado;
	}

	private static List<Estilo> buscarEstilo(String estilo) {
		List<Estilo> estiloEncontrado = null;

		ObjectContainer db = abrirBd();

		Estilo patron = new Estilo(estilo);

		ObjectSet<Estilo> resultado = db.queryByExample(patron);

		if (resultado.size() >= 1) {
			for (Estilo estilo2 : resultado) {
				estilo2 = resultado.next();
			}
		}

		cerrarBd(db);
		return estiloEncontrado;
	}

	private static int solicitarOpcion() {
		int opc;

		System.out.println("1.Nueva canción");
		System.out.println("2.Consulta playList de un estilo");
		System.out.println("3.Borrar una playList");
		System.out.println("4.Añadir una canción a una playList");
		System.out.println("5.Salir");
		System.out.println("6.Consultar cancion");
		do {
			System.out.println("Introduce opcion");
			opc = Integer.parseInt(teclado.nextLine());
		} while (opc < 1 || opc > OPCION_SALIR);
		return opc;
	}

	private static double solicitarDouble(String msg) {
		double numero;

		System.out.println(msg);
		numero = Double.parseDouble(teclado.nextLine());

		return numero;
	}

	private static int solicitarEntero(String msg) {
		int numero;

		System.out.println(msg);
		numero = Integer.parseInt(teclado.nextLine());

		return numero;
	}

	private static String solicitarCadena(String msg) {
		String nombre;
		System.out.println(msg);
		nombre = teclado.nextLine();
		return nombre;
	}

}
