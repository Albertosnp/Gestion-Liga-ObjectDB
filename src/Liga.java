import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.*;
import javax.persistence.*;
/**
 * 
 * @author Alberto Sánchez de la Nieta Pérez
 *	Clase Liga que contiene todos los metodos para dar de alta equipos y jugadores, 
 *  darlos de baja, asi como modificar sus atributos.
 */
public class Liga {
	EntityManager em = null;
	// Constructor con parametros para la clase Liga, se le pasa por parametro el manejador
	public Liga(EntityManager em) {
		this.em = em;
		em.getTransaction().begin();
	}
	// Metodo que hara efectivos los cambios realizados en la base de datos
	public void guardar() {
		em.getTransaction().commit(); // Instruccion para hacer efectivos los cambios
	}
	// Metodo para dar de alta un equipo
	public void altaEquipo() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Nombre del Equipo:");
		String nombre = sc.nextLine().toUpperCase();
		System.out.println("Nombre del Estadio:");
		String estadio = sc.nextLine().toUpperCase();
		System.out.println("Nombre de la Ciudad:");
		String ciudad = sc.nextLine().toUpperCase();
		System.out.println("Numero de socios:");
		int socios = 0;
		try {
			socios = sc.nextInt();
		} catch (InputMismatchException e) {
			System.err.println("Has introducido un valor no valido. ");
		}
		sc.nextLine();
		Equipo equipo = new Equipo(nombre, estadio, ciudad, socios);
		em.persist(equipo);// Se añade el equipo a la memoria
	}
	// metodo para dar de alta a un jugador ne la base de datos, Si el codigo de
	// equipo introducido no existiera pondria el atributo a null
	public void altaJugador() throws DateTimeException {
		Scanner sc = new Scanner(System.in);
		System.out.println("Nombre del Jugador:");
		String nombre = sc.nextLine().toUpperCase();
		System.out.println("Apellidos del Jugador:");
		String apellidos = sc.nextLine().toUpperCase();
		System.out.println("Año de nacimiento: [YYYY]");
		int year = Integer.parseInt(sc.nextLine());
		System.out.println("Mes de nacimiento: [MM]");
		int mes = Integer.parseInt(sc.nextLine());
		System.out.println("Dia de nacimiento: [DD]");
		int dia = Integer.parseInt(sc.nextLine());
		LocalDate fecha = LocalDate.of(year, mes, dia);
		Jugador jugador = null;
		if (mostraEquipos()) {
			System.out.println("Introduce codigo (ID) del equipo en el que juega.");
			int codigo = 0;
			try {
				codigo = sc.nextInt();
			} catch (InputMismatchException e) {
				System.err.println("Has introducido un valor no valido. ");
			}
			jugador = new Jugador(nombre, apellidos, fecha.toString(), comprobarEquipo(codigo));
		} else {
			jugador = new Jugador(nombre, apellidos, fecha.toString(), null);
			System.out.println("No se ha asignado ningun equipo.");
		}
		em.persist(jugador);// Se añade el jugador a la memoria
	}

	// Metodo para dar de baja a un equipo, El criterio sera segun nombre de equipo
	public void bajaEquipo() {
		Scanner sc = new Scanner(System.in);
		if (mostraEquipos()) {
			boolean existe = false; // Variable que controla si el equipo pasado existe
			System.out.println("\nIntroduce el ID del equipo que quieras dar de baja");
			int id = 0;
			try {
				id = sc.nextInt();
			} catch (InputMismatchException e) {
				System.err.println(e.getMessage());
			}

			comprobarEquipo(id);
			Query q1 = em.createQuery("DELETE FROM Equipo e WHERE e.codigo_equipo=?1");
			em.getTransaction().begin();
			q1.setParameter(1, id);
			q1.executeUpdate();
			System.out.println("Se ha dado de baja con exito.");
		}
	}

	// Metodo para dar de baja un jugador, segun el id del jugador. EL metodo
	// mostrara el id junto con el nombre de cada uno.
	public void bajaJugador() {
		Scanner sc = new Scanner(System.in);
		if (mostrarJugadores()) {// SAlida por pantalla de todos los jugadores
			System.out.println("\nIntroduce el ID del jugador a borrar.");
			int id = 0;
			try {
				id = sc.nextInt();
			} catch (InputMismatchException e) {
				System.err.println("Has introdido un valor no valido.");
			}
			boolean encontrado = false;
			em.getTransaction().begin();
			Query q1 = em.createQuery("DELETE FROM Jugador j WHERE j.id_codigo_jugador=?1");
			q1.setParameter(1, id);
			q1.executeUpdate();
		}
	}

	// Metodo que muestra la informacion del jugador que se quiera consultar
	public void consultarJugador() {
		Scanner sc = new Scanner(System.in);
		if (mostrarJugadores()) {
			System.out.println("\nIntroduce el ID del jugador a consultar.");
			int id = 0;
			try {
				id = sc.nextInt();
			} catch (InputMismatchException e) {
				System.err.println("Has introdido un valor no valido.");
			}
			Query q1 = em.createQuery("SELECT j FROM Jugador j WHERE id_codigo_jugador=?1");
			q1.setParameter(1, id);
			try {
				System.out.println(q1.getSingleResult());
			} catch (NoResultException e) {
				System.err.println("No existe ese ID.");
			}
		}
	}

	// Metodo que muestra la informacion del equipo que se quiera consultar
	public void consultarEquipo() {
		Scanner sc = new Scanner(System.in);
		if (mostraEquipos()) {
			System.out.println("Introduce el ID del equipo que quieras consultar.");
			int id = 0;
			try {
				id = sc.nextInt();
			} catch (InputMismatchException e) {
				e.getMessage();
			}
			Query q1 = em.createQuery("SELECT e FROM Equipo e WHERE e.codigo_Equipo=?1");
			q1.setParameter(1, id);
			try {
				System.out.println(q1.getSingleResult());
			} catch (NoResultException e) {
				System.err.println("El equipo introducido no existe");
			}
		}
	}
	// Metodo que permite hacer modificaciones de los atributos, tanto de Equipos
	// como de Jugadores
	public void modificarDatos() throws InputMismatchException, NoResultException {
		Scanner sc = new Scanner(System.in);
		int decision = 0;
		do {
			System.out.println("¿Que quieres modificar?\n1. Equipo.\n2. Jugador\n");
			try {
				decision = sc.nextInt();
			} catch (InputMismatchException e) {
				System.err.println("Has introducido un valor no valido. [1-2]");
			}
			sc.nextLine();
		} while (decision != 1 && decision != 2);

		if (decision == 1) { //Modificara algun equipo

			if (mostraEquipos()) {
				System.out.println("Introduce el ID del equipo que quieras modificar.");
				int id = 0;
				id = sc.nextInt();
				sc.nextLine();
				if (comprobarEquipo(id) != null) {// Metodo que comprobara si el id corresponde con algun equipo
					Query q1 = em.createQuery("SELECT e FROM Equipo e WHERE e.codigo_Equipo=?1");
					q1.setParameter(1, id);
					q1.getSingleResult();
					// Bloque para mostrar al usuario las posibles opciones de modificacion
					decision = 0;
					do {
						System.out.println("¿Qué atributo quieres modificar?\n1. Nombre.\n2. Estadio\n3. Ciudad.\n4. Nº de socios.\n");
						try {
							decision = sc.nextInt();
							sc.nextLine();
						} catch (InputMismatchException e) {
							System.err.println("Has introducido un valor no valido. [1-4]");
						}
					} while (decision < 0 || decision > 4);
					switch (decision) {
					case 1:// Permite modificar el nombre del equipo
						System.out.println("Introduce el nuevo nombre:");
						String nombre = sc.nextLine().toUpperCase();
						Query q2 = em.createQuery("UPDATE Equipo SET nombre=?1 WHERE codigo_Equipo=?2");
						q2.setParameter(1, nombre);
						q2.setParameter(2, id);
						try {
							q2.executeUpdate();
						} catch (NoResultException e) {
							System.err.println("No se ha podido hacer la modificacion.");
						}
						System.out.println("\nModificado con exito.");
						break;
					case 2:// Permite modificar el nombre del estadio
						System.out.println("Introduce el nuevo nombre del estadio:");
						String estadio = sc.nextLine().toUpperCase();
						Query q3 = em.createQuery("UPDATE Equipo SET estadio=?1 WHERE codigo_Equipo=?2");
						q3.setParameter(1, estadio);
						q3.setParameter(2, id);
						try {
							q3.executeUpdate();
						} catch (NoResultException e) {
							System.err.println("No se ha podido hacer la modificacion.");
						}
						System.out.println("\nModificado con exito.");
						break;
					case 3:// Permite modificar el nombre de la Ciudad
						System.out.println("Introduce la nueva ciudad:");
						String ciudad = sc.nextLine().toUpperCase();
						Query q4 = em.createQuery("UPDATE Equipo SET ciudad=?1 WHERE codigo_Equipo=?2");
						q4.setParameter(1, ciudad);
						q4.setParameter(2, id);
						try {
							q4.executeUpdate();
						} catch (NoResultException e) {
							System.err.println("No se ha podido hacer la modificacion.");
						}
						System.out.println("\nModificado con exito.");
						break;
					case 4:// Permite modificar el numero de socios
						System.out.println("Introduce el nuevo numeros de socios:");
						int socios = sc.nextInt();
						sc.nextLine();
						Query q5 = em.createQuery("UPDATE Equipo e SET num_socios=?1 WHERE e.codigo_Equipo=?2");
						q5.setParameter(1, socios);
						q5.setParameter(2, id);
						try {
							q5.executeUpdate();
						} catch (Exception e) {
							System.err.println("No se ha podido hacer la modificacion.");
						}
						System.out.println("\nModificado con exito.");
						break;
					}
				}
			}
		} else { //Modificara algun jugador
			if (mostrarJugadores()) {
				System.out.println("Introduce el ID del jugador que quieras modificar.");
				int id = 0;
				id = sc.nextInt();
				sc.nextLine();
				if (comprobarJugador(id) != null) {// Metodo que comprobara si el id corresponde con algun jugador
					Query q1 = em.createQuery("SELECT j FROM Jugador j WHERE id_codigo_jugador=?1");
					q1.setParameter(1, id);
					q1.getSingleResult();
					// Bloque para mostrar al usuario las posibles opciones de modificacion
					decision = 0;
					do {
						System.out.println("¿Qué atributo quieres modificar?\n1. Nombre.\n2. Apellidos.\n3. Fecha de nacimiento.\n4. Equipo.\n");
						try {
							decision = sc.nextInt();
							sc.nextLine();
						} catch (InputMismatchException e) {
							System.err.println("Has introducido un valor no valido. [1-4]");
						}
					} while (decision < 0 || decision > 4);
					switch (decision) {
					case 1:// Permite modificar el nombre del jugador
						System.out.println("Introduce el nuevo nombre:");
						String nombre = sc.nextLine().toUpperCase();
						Query q2 = em.createQuery("UPDATE Jugador SET nombre=?1 WHERE id_codigo_jugador=?2");
						q2.setParameter(1, nombre);
						q2.setParameter(2, id);
						try {
							q2.executeUpdate();
						} catch (NoResultException e) {
							System.err.println("No se ha podido hacer la modificacion.");
						}
						System.out.println("\nModificado con exito.");
						break;
					case 2:// Permite modificar el apellido
						System.out.println("Introduce el nuevo apellido:");
						String apellidos = sc.nextLine().toUpperCase();
						Query q3 = em.createQuery("UPDATE Jugador SET apellidos=?1 WHERE id_codigo_jugador=?2");
						q3.setParameter(1, apellidos);
						q3.setParameter(2, id);
						try {
							q3.executeUpdate();
						} catch (NoResultException e) {
							System.err.println("No se ha podido hacer la modificacion.");
						}
						System.out.println("\nModificado con exito.");
						break;
					case 3:// Permite modificar la fecha de nacimiento
						System.out.println("Introduce la nueva fecha:\n");
						System.out.println("Año de nacimiento: [YYYY]");
						int year = Integer.parseInt(sc.nextLine());
						System.out.println("Mes de nacimiento: [MM]");
						int mes = Integer.parseInt(sc.nextLine());
						System.out.println("Dia de nacimiento: [DD]");
						int dia = Integer.parseInt(sc.nextLine());
						LocalDate fecha = LocalDate.of(year, mes, dia);
						fecha.toString(); // Convierto la fecha a String
						Query q4 = em.createQuery("UPDATE Jugador SET fecha_nac=?1 WHERE id_codigo_jugador=?2");
						q4.setParameter(1, fecha);
						q4.setParameter(2, id);
						try {
							q4.executeUpdate();
						} catch (NoResultException e) {
							System.err.println("No se ha podido hacer la modificacion.");
						}
						System.out.println("\nModificado con exito.");
						break;
					default:// Permite modificar el equipo
						System.out.println("Introduce el ID del nuevo equipo:");
						Equipo equipo = null;
						try {
							id = sc.nextInt();
						} catch (InputMismatchException e) {
							System.err.println(e.getMessage());
						}
						equipo = comprobarEquipo(id);
						if (equipo != null) {
							Query q5 = em.createQuery("UPDATE Jugador SET equipo=?1 WHERE id_codigo_jugador=?2");
							q5.setParameter(1, equipo);// MODIFICAR
							q5.setParameter(2, id);
							try {
								q5.executeUpdate();
							} catch (Exception e) {
								System.err.println("No se ha podido hacer la modificacion.");
							}
							System.out.println("\nModificado con exito.");
						} else {
							System.err.println("El ID no corresponde a ningun equipo de la Base de Datos.");
						}
						break;
					}
				}
			}
		}

	}
	// Metodo que comprueba si existe el equipo, devolvera el equipo correspondiente
	// si existe, si no devuelve null
	public Equipo comprobarEquipo(int codigo) {
		Query q1 = em.createQuery("SELECT e FROM Equipo e");
		List<Equipo> listaEquipos = q1.getResultList();
		for (Object e : listaEquipos) {
			if (((Equipo) e).getCodigo_Equipo() == codigo) {
				return (Equipo) e;
			}
		}
		return null;
	}
	// Metodo que comprueba si existe el jugador, devolvera el jugador
	// correspondiente si existe, si no devuelve null
	public Jugador comprobarJugador(int codigo) {
		Query q1 = em.createQuery("SELECT j FROM Jugador j");
		List<Jugador> listaEquipos = q1.getResultList();
		for (Object e : listaEquipos) {
			if (((Jugador) e).getId_codigo_jugador() == codigo) {
				return (Jugador) e;
			}
		}
		return null;
	}

	// Metodo para mostrar al usuario los jugadores que hay en la BD
	private boolean mostrarJugadores() {
		Query q1 = em.createQuery("SELECT j FROM Jugador j");
		List<Jugador> listaJugadores = q1.getResultList();
		if (listaJugadores.isEmpty()) {
			System.out.println("======= No hay registros. =======");
			return false;
		} else {
			System.out.println("========== Estos son los jugadores de la base de datos: ==========\n");
			for (Jugador j : listaJugadores) {
				System.out.println("ID: " + j.getId_codigo_jugador() + ". " + j.getNombre() + " " + j.getApellidos());
			}
			return true;
		}
	}

	// Metodo para mostrar al usuario los equipos que hay en la BD
	private boolean mostraEquipos() {
		Query q1 = em.createQuery("SELECT e FROM Equipo e");
		List<Equipo> listaEquipos = q1.getResultList();
		if (listaEquipos.isEmpty()) {
			System.out.println("======= No hay registros. ========");
			return false;
		} else {
			System.out.println("========== Estos son los equipos de la base de datos: ==========\n");
			for (Equipo e : listaEquipos) {
				System.out.println("ID: " + e.getCodigo_Equipo() + ". " + e.getNombre());
			}
			return true;
		}
	}

}
