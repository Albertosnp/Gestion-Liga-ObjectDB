import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

/**
 * 
 * @author Alberto Sánchez de la Nieta Pérez
 *	Clase Jugador tiene como atributos; id, nombre, apellidos, fecha de nacimiento,
 *  y el equipo donde juega.
 */
@NamedQuery(name = "queryJugador", query = "SELECT j FROM Jugador j") 
@Entity
public class Jugador implements Serializable{
	//Atributos de clase
	private static final long serialVersionUID = 28121L;
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id_codigo_jugador; //Es autogenerado por el programa
	private String nombre;
	private String apellidos;
	private String fecha_nac;
	private Equipo equipo;
	//Constructor sin parametros
	public Jugador(){}
	//Constructor con parametros, sin incluir el atributo ID
	public Jugador(String nombre, String apellidos, String fecha_nac, Equipo equipo) {
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.fecha_nac = fecha_nac;
		this.equipo = equipo;
	}
	//Metodos set y get de la clase
	public int getId_codigo_jugador() {
		return id_codigo_jugador;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getFecha_nac() {
		return fecha_nac;
	}

	public void setFecha_nac(String fecha_nac) {
		this.fecha_nac = fecha_nac;
	}

	public Equipo getEquipo() {
		return equipo;
	}

	public void setEquipo(Equipo equipo) {
		this.equipo = equipo;
	}
	//Metodo sobreescrito que devuelve en forma de cadena, toda la informacion del Jugador
	@Override
	public String toString() {
		return "========== Jugador ============"
				+ "\nId jugador: " + id_codigo_jugador 
				+ "\nNombre y apellidos: " + nombre + " " + apellidos
				+ "\nFecha de nacimiento: " + fecha_nac 
				+ "\nEquipo: " + equipo;
	}

}
