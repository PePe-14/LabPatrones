package cl.ucn.felix.biblioteca.servicio.impl.activador;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;


import cl.ucn.felix.biblioteca.servicio.api.ServicioInventarioLibro;
import cl.ucn.felix.biblioteca.servicio.api.ServicioInventarioLibroImpl;

public class ServicioImplActivador implements BundleActivator {

    private ServiceRegistration<ServicioInventarioLibro> reg = null;
  
    @Override
    public void start(BundleContext context) throws Exception {
        System.out.println("\nComenzando implementación del Servicio de Inventario de Libros");

        ServicioInventarioLibro servicioInventarioLibro = new ServicioInventarioLibroImpl(context);
        this.reg = context.registerService(
                ServicioInventarioLibro.class,
                servicioInventarioLibro,
                null
        );
    }


    @Override
    public void stop(BundleContext context) throws Exception {
        System.out.println("\nParando implementación del Servicio de Inventario de Libros");
        if (this.reg != null) {
            context.ungetService(reg.getReference());
            this.reg = null;
        }
    }
}
