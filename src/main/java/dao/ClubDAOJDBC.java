package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import pojos.Club;
import pojos.Viatger;

/**
 * Implementación del patrón DAO para la entidad Club utilizando JDBC nativo.
 * Gestiona el CRUD completo y el control de transacciones (Commit/Rollback).
 */
public class ClubDAOJDBC implements ClubDAO {

    @Override
    public Integer addClub(Club c) {
        Connection conn = null;
        PreparedStatement psClub = null;
        PreparedStatement psViatger = null;

        // Sentencias SQL para la inserción de datos relacionales
        String sqlInsertClub = "INSERT INTO club (codiClub, nomClub, comentari) VALUES (?, ?, ?)";
        String sqlInsertViatger = "INSERT INTO viatger (dni, nom, cognoms, tlf, idClub, idRuta) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            // Obtención de la conexión y desactivación del AutoCommit para iniciar una Transacción
            conn = GestorConnexions.obtenirConnexio();
            conn.setAutoCommit(false); 

            // 1. Inserción de los datos principales del Club
            psClub = conn.prepareStatement(sqlInsertClub);
            psClub.setInt(1, c.getCodiClub());
            psClub.setString(2, c.getNomClub());
            psClub.setString(3, c.getComentari());
            psClub.executeUpdate();

            // 2. Inserción de la lista de viajeros asociados al Club (si existen)
            if (c.getViatgers() != null && !c.getViatgers().isEmpty()) {
                psViatger = conn.prepareStatement(sqlInsertViatger);
                for (Viatger v : c.getViatgers()) {
                    psViatger.setString(1, v.getDni());
                    psViatger.setString(2, v.getNom());
                    psViatger.setString(3, v.getCognoms());
                    psViatger.setString(4, v.getTlf());
                    psViatger.setInt(5, c.getCodiClub()); // Clave foránea (FK) enlazada al club creado
                    psViatger.setInt(6, v.getRuta() != null ? v.getRuta().getCodiRuta() : 0);
                    psViatger.executeUpdate();
                }
            }

            // Confirmación de la transacción si todo se ha ejecutado correctamente
            conn.commit();
            System.out.println("✅ Club afegit correctament amb transacció.");
            return c.getCodiClub();

        } catch (Exception e) {
            // Deshacer cambios en la base de datos si ocurre cualquier error durante la transacción
            try {
                if (conn != null) conn.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            System.err.println("❌ Error afegint el club: " + e.getMessage());
            return -1;

        } finally {
            // Cierre seguro de recursos (Statements y Conexión) para evitar fugas de memoria
            try {
                if (psViatger != null) psViatger.close();
                if (psClub != null) psClub.close();
                if (conn != null) {
                    conn.setAutoCommit(true); // Restauración del estado por defecto
                    GestorConnexions.tancarConnexio();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Club getClubById(Integer id) {
        Boolean isConnectionOpen = false;
        Club serveis = null;
        String sql = "SELECT * FROM club WHERE CODICLUB=?";

        try {
            // Comprobación del estado previo de la conexión para no cerrarla erróneamente en el finally
            isConnectionOpen = GestorConnexions.isConnected();
            Connection conexio = GestorConnexions.obtenirConnexio();
            
            PreparedStatement sentencia = conexio.prepareStatement(sql);
            sentencia.setInt(1, id);
            ResultSet resultat = sentencia.executeQuery();

            // Mapeo del ResultSet de la base de datos al objeto POJO Club
            if (resultat.next()) {
                serveis = new Club();
                serveis.setCodiClub(resultat.getInt("codiclub"));
                serveis.setNomClub(resultat.getString("nomclub"));
                serveis.setComentari(resultat.getString("comentari"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Se cierra la conexión únicamente si no estaba abierta antes de llamar al método
            if (!isConnectionOpen) {
                GestorConnexions.tancarConnexio();
            }
        }
        return serveis;
    }

    @Override
    public void updateClub(Club c) {
        Boolean isConnectionOpen = false;
        String sql = "UPDATE CLUB SET NOMCLUB=? WHERE CODICLUB=?";
        
        try {
            isConnectionOpen = GestorConnexions.isConnected();
            Connection conexio = GestorConnexions.obtenirConnexio();
            
            PreparedStatement sentenciaPreparada = conexio.prepareStatement(sql);
            sentenciaPreparada.setString(1, c.getNomClub());
            sentenciaPreparada.setInt(2, c.getCodiClub());

            sentenciaPreparada.executeUpdate();

        } catch (SQLException e3) {
            e3.printStackTrace();
        } finally {
            if (!isConnectionOpen) {
                GestorConnexions.tancarConnexio();
            }
        }
    }

    @Override
    public Integer deleteClub(Integer id) {
        Boolean isConnectionOpen = false;
        int rowEliminada = 0;
        String sql = "DELETE FROM CLUB WHERE CODICLUB=?";
        Connection conexio = GestorConnexions.obtenirConnexio();

        try {
            // Ejecución del borrado bajo un entorno controlado de transacción transaccional
            conexio.setAutoCommit(false);
            PreparedStatement sentenciaPreparada1 = conexio.prepareStatement(sql);
            sentenciaPreparada1.setInt(1, id);

            rowEliminada = sentenciaPreparada1.executeUpdate();
            conexio.commit();

        } catch (SQLException e) {
            try {
                conexio.rollback();
                // Control específico del código de error 1451 (Restricción de clave foránea / Foreign Key Constraint)
                if (e.getErrorCode() == 1451) {
                    return e.getErrorCode() * 1;
                } else {
                    e.printStackTrace();
                    return 1;
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
                return -1;
            }
        } finally {
            if (!isConnectionOpen) {
                GestorConnexions.tancarConnexio();
            }
        }
        return rowEliminada;
    }

    @Override
    public ArrayList<Club> listClub() {
        Club serveis = null;
        Boolean isConnectionOpen = false;
        String sql = "SELECT * FROM club";
        ArrayList<Club> arrayServeis = new ArrayList<Club>();

        try {
            isConnectionOpen = GestorConnexions.isConnected();
            Connection conexio = GestorConnexions.obtenirConnexio();
            
            Statement sentencia = conexio.createStatement();
            ResultSet resultat = sentencia.executeQuery(sql);

            // Iteración de todos los registros devueltos para agregarlos a la lista final
            while (resultat.next()) {
                serveis = new Club();
                serveis.setCodiClub(resultat.getInt("codi"));
                serveis.setNomClub(resultat.getString("nom"));
                serveis.setComentari(resultat.getString("comentari"));

                arrayServeis.add(serveis);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return arrayServeis;
    }
}