import java.io.Serializable;
import javax.persistence.*;
/**
 * 
 * @author Alberto Sánchez de la Nieta Pérez
 *	Clase Equipo tiene como atributos; id, nombre, estadio, ciudad,
 *  y el numero de socios.
 */
@NamedQuery(name = "queryEquipo", query = "SELECT equipo FROM Equipo e") 
@Entity
public class Equipo implements Serializable, Comparable<Equipo>{
	//Atributos de clase
	private static final long serialVersionUID = 671930986L;
	@Id @GeneratedValue
	private int codigo_Equipo; //Es autogenerado por el programa
	private String nombre;
	private String estadio;
	private String ciudad;
	private int num_socios;
	//Constructor con parametros, sin incluir el atributo ID
	public Equipo(String nombre, String estadio, String ciudad, int num_socios) {
		this.nombre = nombre;
		this.estadio = estadio;
		this.ciudad = ciudad;
		this.num_socios = num_socios;
	}
	//Metodos set y get de la clase
	public int getCodigo_Equipo() {
		return codigo_Equipo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEstadio() {
		return estadio;
	}

	public void setEstadio(String estadio) {
		this.estadio = estadio;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public int getNum_socios() {
		return num_socios;
	}

	public void setNum_socios(int num_socios) {
		this.num_socios = num_socios;
	}
	//Metodo sobreescrito que devuelve en forma de cadena, toda la informacion del Equipo
	@Override
	public String toString() {
		return "\n============== Equipo ==============\n\n"
				+ " Codido de Equipo: " + codigo_Equipo
				+ " Nombre: " + nombre 
				+ " Estadio: " + estadio
				+ " Ciudad: "+ ciudad 
				+ " Numero de socios: " + num_socios;
	}
	//Metodo sobreescrito para poder ordenar segun orden alfabetico
	@Override
	public int compareTo(Equipo o) {
		return nombre.compareTo(o.nombre);
	}
	
	
}
