package cl.ucn.felix.biblioteca.servicio.api;

import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import cl.ucn.felix.biblioteca.api.ExcepcionLibroNoEncontrado;
import cl.ucn.felix.biblioteca.api.Inventario;
import cl.ucn.felix.biblioteca.api.Libro;

import cl.ucn.felix.biblioteca.api.LibroMutable;
import cl.ucn.felix.biblioteca.api.ExcepcionLibroInvalido;
import cl.ucn.felix.biblioteca.api.ExcepcionLibroNoEncontrado;
import cl.ucn.felix.biblioteca.api.Inventario.CriterioBusqueda; 
import cl.ucn.felix.biblioteca.api.ExcepcionLibroExistente;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;



public class ServicioInventarioLibroImpl implements ServicioInventarioLibro{

	private String sesion;
	private BundleContext contexto;
	
	
	public ServicioInventarioLibroImpl(BundleContext contexto) {
		
		this.contexto = contexto;
	}

		
	@Override
	public String login(String username, String password) throws ExcepcionCredencialInvalida {
		// TODO Auto-generated method stub
		if ("admin".equals(username) && "admin".equals(password)) {
			
			this.sesion = Long.toString(System.currentTimeMillis());
			return this.sesion;
		}
		throw new ExcepcionCredencialInvalida(username);
	}

	@Override
	public void logout(String sesion) throws ExcepcionSesionNoValidaTiempoEjecucion {
		// TODO Auto-generated method stub
		chequearSesion(sesion);
		this.sesion = null;
	}

	@Override
	public boolean sesionEsValida(String sesion) {
		// TODO Auto-generated method stub
		return this.sesion != null && this.sesion.equals(sesion);
	}
	
	protected void chequearSesion(String sesion) throws ExcepcionSesionNoValidaTiempoEjecucion {
		
		if (!sesionEsValida(sesion)) {
			throw new ExcepcionSesionNoValidaTiempoEjecucion(sesion);
		}
	}

	@Override
	public Set<String> obtenerGrupos(String sesion)  {
		// TODO Auto-generated method stub
		
			Inventario inventario;
			try {
				inventario = buscarLibroEnInventario();
				return inventario.getCategorias();
			} catch (ExcepcionSesionNoValidaTiempoEjecucion e) {
				
				e.printStackTrace();
				return null;
			}
			

		
	}
	
	@Override
	public void removerLibro(String sesion, String isbn) {
	    try {
	        Inventario inventario = buscarLibroEnInventario();
	        inventario.removerLibro(isbn);
	    } catch (ExcepcionLibroNoEncontrado e) {
	        e.printStackTrace();
	    } catch (ExcepcionSesionNoValidaTiempoEjecucion e) {
	        e.printStackTrace();
	    }
	}
	
	@Override
	public Libro obtenerLibro(String sesion, String isbn) throws ExcepcionLibroNoEncontrado, ExcepcionSesionNoValidaTiempoEjecucion {
	    try {
	        this.chequearSesion(sesion);
	        Inventario inventario = buscarLibroEnInventario();
	        return inventario.cargarLibro(isbn);
	    } catch (ExcepcionSesionNoValidaTiempoEjecucion | ExcepcionLibroNoEncontrado e) {
	        e.printStackTrace();
	        throw e; 
	    }
	}
	@Override
	public void modificarCategoriaLibro(String sesion, String isbn, String categoria) {
	    try {
	        Inventario inventario = buscarLibroEnInventario();
	        LibroMutable libro = inventario.cargarLibroParaEdicion(isbn);
	        libro.setCategoria(categoria);
	        inventario.guardarLibro(libro);
	    } catch (ExcepcionLibroNoEncontrado | ExcepcionLibroInvalido e) {
	        e.printStackTrace();
	    } catch (ExcepcionSesionNoValidaTiempoEjecucion e) {
	        e.printStackTrace();
	    }
	}


	@Override
	public void adicionarLibro(String sesion, String isbn, String titulo, String autor, String categoria) {
	    try {
	        Inventario inventario = buscarLibroEnInventario();
	        LibroMutable libro = inventario.crearLibro(isbn);
	        libro.setTitulo(titulo);
	        libro.setAutor(autor);
	        libro.setCategoria(categoria);
	        inventario.guardarLibro(libro);
	    } catch (ExcepcionLibroInvalido | ExcepcionLibroExistente e) {
	        e.printStackTrace();
	    } catch (ExcepcionSesionNoValidaTiempoEjecucion e) {
	        e.printStackTrace();
	    }
	}

	@Override
	public Set<String> buscarLibrosPorCategoria(String sesion, String categoriaLike) {
	    try {
	        Inventario inventario = buscarLibroEnInventario();
	        Map<CriterioBusqueda, String> criterio = new HashMap<>();
	        criterio.put(CriterioBusqueda.CATEGORIA_LIKE, categoriaLike);
	        return inventario.buscarLibros(criterio);
	    } catch (ExcepcionSesionNoValidaTiempoEjecucion e) {
	        e.printStackTrace();
	        return null;
	    }
	}

	@Override
	public Set<String> buscarLibrosPorAutor(String session, String autorLike) {
	    try {
	        Inventario inventario = buscarLibroEnInventario();
	        Map<CriterioBusqueda, String> criterio = new HashMap<>();
	        criterio.put(CriterioBusqueda.AUTOR_LIKE, autorLike);
	        return inventario.buscarLibros(criterio);
	    } catch (ExcepcionSesionNoValidaTiempoEjecucion e) {
	        e.printStackTrace();
	        return null; 
	    }
	}

	@Override
	public Set<String> buscarLibrosPorTitulo(String sesion, String tituloLike) {
	    try {
	        Inventario inventario = buscarLibroEnInventario();
	        Map<CriterioBusqueda, String> criterio = new HashMap<>();
	        criterio.put(CriterioBusqueda.TITULO_LIKE, tituloLike);
	        return inventario.buscarLibros(criterio);
	    } catch (ExcepcionSesionNoValidaTiempoEjecucion e) {
	        e.printStackTrace();
	        return null;
	    }
	}


	private Inventario buscarLibroEnInventario() throws ExcepcionSesionNoValidaTiempoEjecucion {
		String nombre = Inventario.class.getName();
		ServiceReference<?> ref = this.contexto.getServiceReference(nombre);
		if (ref == null) {
			throw new ExcepcionSesionNoValidaTiempoEjecucion(nombre);
		}
		return (Inventario) this.contexto.getService(ref);
 		
	}
	
}
