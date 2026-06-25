package dao;

import java.util.ArrayList;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pojos.Viatger;

/**
 * Implementación del patrón DAO para la entidad Viatger utilizando Hibernate ORM.
 * Gestiona el ciclo de vida de los viajeros en la base de datos, controlando de forma
 * segura las transacciones y la inicialización de relaciones Many-to-One (Club y Ruta).
 */
public class ViatgerDAOHibernate implements ViatgerDAO {

    @Override
    public int addViatger(Viatger v) {
        Session session = SessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx = null;
        Integer viatgerID = null;

        try {
            tx = session.beginTransaction();
            // Guarda el objeto Viatger persistiendo sus propiedades en la tabla correspondiente
            viatgerID = (Integer) session.save(v);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return viatgerID;
    }

    @Override
    public Viatger getViatgerById(Integer id, boolean fetchRelations) {
        Session session = SessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx = null;
        Viatger v = null;

        try {
            tx = session.beginTransaction();
            // Obtiene el viajero por su identificador único (PK)
            v = session.get(Viatger.class, id);
            
            // Inicialización controlada por parámetro de las relaciones externas (Lazy Loading)
            if (v != null && fetchRelations) {
                Hibernate.initialize(v.getRuta());
                Hibernate.initialize(v.getClub());
            }
            
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return v;
    }

    @Override
    public void updateViatger(Viatger v) {
        Session session = SessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            // Sincroniza los cambios del objeto modificado con el registro de la base de datos
            session.update(v);
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
    public Integer deleteViatger(Integer id) {
        Session session = SessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx = null;
        Viatger departaments = null;

        try {
            tx = session.beginTransaction();
            // Recupera primero la instancia dentro del contexto de persistencia antes de eliminarla
            departaments = session.get(Viatger.class, id);
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
    public ArrayList<Viatger> listViatger() {
        Session session = SessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx = null;
        ArrayList<Viatger> ViatgerList = null;

        try {
            tx = session.beginTransaction();
            // Consulta HQL para extraer la colección completa de la tabla 'viatger'
            ViatgerList = (ArrayList<Viatger>) session.createQuery("FROM Viatger").list();

            // Fuerza la carga de las relaciones para cada viajero evitando errores al cerrar la sesión
            for (Viatger a : ViatgerList) {
                Hibernate.initialize(a.getClub());
                Hibernate.initialize(a.getRuta());
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

        return ViatgerList;
    }
}