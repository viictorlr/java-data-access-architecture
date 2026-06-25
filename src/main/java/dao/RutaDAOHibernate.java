package dao;

import java.util.ArrayList;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pojos.Ruta;

/**
 * Implementación del patrón DAO para la entidad Ruta utilizando Hibernate ORM.
 * Gestiona el ciclo de vida de los objetos en el contexto de persistencia (Session),
 * controlando transacciones y optimizando la carga de relaciones perezosas (Lazy Loading).
 */
public class RutaDAOHibernate implements RutaDAO {

    @Override
    public int addRuta(Ruta r) {
		
        Session session = SessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx = null;
        Integer rutaID = null;

        try {
            tx = session.beginTransaction();
            // Guarda el objeto en la base de datos y recupera el ID autogenerado
            rutaID = (Integer) session.save(r);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            // Cierre obligatorio de la sesión para liberar el pool de conexiones
            session.close();
        }
        return rutaID;
    }

    @Override
    public Ruta getRutaById(Integer id, boolean fetchViatgers) {
        Session session = SessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx = null;
        Ruta ruta = null;

        try {
            tx = session.beginTransaction();
            // Recupera la entidad Ruta por su Clave Primaria
            ruta = session.get(Ruta.class, id);
            
            // Carga bajo demanda (Fetch/Lazy Initialization) de la colección de viajeros parametrizada
            if (ruta != null && fetchViatgers) {
                Hibernate.initialize(ruta.getViatgers());
            }
            
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return ruta;
    }

    @Override
    public void updateRuta(Ruta r) {
        Session session = SessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            // Pasa el objeto de estado 'Detached' a 'Persistent', actualizando sus campos en la BD
            session.update(r);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @Override
    public Integer deleteRuta(Integer id) {
        Session session = SessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx = null;
        Ruta departaments = null;

        try {
            tx = session.beginTransaction();
            // Es necesario recuperar la instancia persistente antes de eliminarla del contexto
            departaments = session.get(Ruta.class, id);
            if (departaments != null) {
                session.delete(departaments);
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return id;
    }

    @Override
    public ArrayList<Ruta> listRuta() {
        Session session = SessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx = null;
        ArrayList<Ruta> rutaList = null;

        try {
            tx = session.beginTransaction();
            // Consulta HQL (Hibernate Query Language) para recuperar todas las instancias de la entidad Ruta
            rutaList = (ArrayList<Ruta>) session.createQuery("FROM Ruta").list();

            // Fuerza la inicialización de las colecciones Lazy para evitar excepciones 'LazyInitializationException' fuera de la sesión
            for (Ruta a : rutaList) {
                Hibernate.initialize(a.getViatgers());
            }

            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return rutaList;
    }
}