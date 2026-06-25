package dao;

/**
 * Gestor centralizado (Factory/Manager) para la administración de los objetos DAO.
 * Implementa el patrón Singleton con inicialización perezosa (Lazy Initialization)
 * para asegurar que solo exista una única instancia de cada DAO en toda la aplicación.
 */
public class DAOManager {

    // Instancias únicas globales de los DAOs (atriburtos de clase estáticos)
    private static RutaDAO rutaDAO;
    private static ClubDAO clubDAO;
    private static ViatgerDAO viatgerDAO;

    /**
     * Obtiene la instancia única de RutaDAO mapeada con Hibernate.
     * Si no existe previa, la inicializa.
     */
    public static RutaDAO getRutaDAO() {
        if (rutaDAO == null) {
            rutaDAO = new RutaDAOHibernate();
        }
        return rutaDAO;
    }

    /**
     * Obtiene la instancia única de ClubDAO mapeada con JDBC nativo.
     * Si no existe previa, la inicializa.
     */
    public static ClubDAO getClubDAO() {
        if (clubDAO == null) {
            clubDAO = new ClubDAOJDBC();
        }
        return clubDAO;
    }

    /**
     * Obtiene la instancia única de ViatgerDAO mapeada con Hibernate.
     * Si no existe previa, la inicializa.
     */
    public static ViatgerDAO getViatgerDAO() {
        if (viatgerDAO == null) {
            viatgerDAO = new ViatgerDAOHibernate();
        }
        return viatgerDAO;
    }
}