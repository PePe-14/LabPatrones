package cl.ucn.felix.biblioteca.servicio.api;

public class ExcepcionCredencialInvalida extends Exception{

	private static final long serialVersionUID = 1L;

	public ExcepcionCredencialInvalida(String username) {
		super("La Excepcion es inviladia par el usuario: " + username);
	}
	
	
}
