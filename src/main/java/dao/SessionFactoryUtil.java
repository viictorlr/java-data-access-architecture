package dao;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Clase de utilidad para centralizar la configuración e inicialización de Hibernate.
 * Implementa un inicializador estático para garantizar que el objeto SessionFactory 
 * (que consume muchos recursos del sistema) se cree una única vez al arrancar la aplicación.
 */
public class SessionFactoryUtil {

    // Instancia única y global de la factoría de sesiones de Hibernate
    private static final SessionFactory sessionFactory;

    // Bloque de inicialización estática que se ejecuta la primera vez que se carga la clase en memoria
    static {
        try {
            // Lee el archivo 'hibernate.cfg.xml', procesa los mapeos de las entidades y construye la factoría
            sessionFactory = new Configuration().configure().buildSessionFactory();

        } catch (Throwable ex) {
            // Control crítico de errores catastróficos en el arranque (ej. problemas de red o fallos en el XML)
            System.err.println("❌ Initial SessionFactory creation failed: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Proporciona acceso a la factoría global de sesiones de Hibernate.
     * Utilizada por las clases DAO para abrir nuevas conexiones individuales (Session).
     * @return El objeto SessionFactory único de la aplicación.
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}