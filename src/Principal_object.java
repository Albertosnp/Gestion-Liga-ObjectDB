import java.time.DateTimeException;
import java.util.*;
import javax.persistence.*;
/**
 * @author Alberto Sánchez de la Nieta Pérez
 *	Clase principal desde donde se manejan las principales opciones del programa.
 *	Se maneja la Liga de futbol mediante una base de datos orientada a objetos (ObjectDB)
 *	La base de datos se llama "LFP".
 */
public class Principal_object {
	public static void main(String[] args) {
		int eleccion = 0;
		boolean salir =false;	
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("$objectdb/db/LFP.odb");
		EntityManager em = emf.createEntityManager();
		Liga lfp = new Liga(em);
		do {
			Scanner sc = new Scanner(System.in);
			System.out.println("\n======= Menu Principal =======\n\n"
					+ "1. Alta equipo.\n"
					+ "2. Baja equipo.\n"
					+ "3. Alta jugador.\n"
					+ "4. Baja jugador.\n"
					+ "5. Consultar equipo.\n"
					+ "6. Consultar jugador.\n"
					+ "7. Modificar datos \n"
					+ "8. Guardar y Salir.\n");
			try {
				eleccion = sc.nextInt();
			} catch (InputMismatchException e) {
				System.err.println("Has introducido letras.");
			}
			if (eleccion<=0 || eleccion > 8) {
				System.err.println("Valores permitidos [1-8]");
			}	
			switch (eleccion) {
			case 1:
				lfp.altaEquipo();
				break;
			case 2:
				try {
					lfp.bajaEquipo();
				} catch (TransactionRequiredException e) {
					System.err.println(e.getMessage());
				}
				break;
			case 3:
				try {
					lfp.altaJugador();
				} catch (DateTimeException e) {
					System.err.println("El formato de fecha introducido no es el correcto.");
				}
				break;
			case 4:
				try {
					lfp.bajaJugador();
				} catch (TransactionRequiredException e) {
					System.err.println(e.getMessage());
				}
				break;
			case 5:
				lfp.consultarEquipo();
				break;
			case 6:
				lfp.consultarJugador();
				break;
			case 7:
				try {
					lfp.modificarDatos();
				} catch (NoResultException e) {
					System.err.println("El equipo introducido no existe");
				}catch (InputMismatchException e) {
					System.err.println("Has introducido un valor no valido.");
				}
				break;
			case 8:
				lfp.guardar();
				System.out.println("\nSe ha guardado con exito.\n");
				salir=true;
				System.out.println("======= Fin del programa =======");
				break;
			}
		} while (!salir);
		emf.close();
	}
}