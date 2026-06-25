package main;  

import dao.RutaDAO;  
import dao.DAOManager;  
import dao.ClubDAO;  
import dao.ViatgerDAO;  
import pojos.Club;  
import pojos.Ruta;  
import pojos.Viatger;  

public class MainRuta {  

    public static void main(String[] args) {  

        ViatgerDAO viatgerDAO = DAOManager.getViatgerDAO();  
        ClubDAO clubDAO = DAOManager.getClubDAO();  
        RutaDAO rutaDAO = DAOManager.getRutaDAO();  

        // 1. Afegir una nova ruta sense viatgers  ---------------------------------------------------
        Ruta novaRuta = new Ruta();  
        novaRuta.setCodiRuta(1);  
        novaRuta.setNom("Ruta Sense Viatgers");  
        novaRuta.setOrigen("Origen A");  
        novaRuta.setDesti("Desti A");  
        int rutaId = rutaDAO.addRuta(novaRuta);  

        if (rutaId == -1) {  
            System.out.println("Error al afegir la ruta sense viatgers.");  
        } else {  
            System.out.println("Ruta afegida correctament sense viatgers.");  
        }  

        // 2. Afegir una nova ruta amb un nou viatger  ---------------------------------------------------
        Ruta novaRutaAmbViatger = new Ruta();  
        novaRutaAmbViatger.setCodiRuta(2);  
        novaRutaAmbViatger.setNom("Ruta amb viatger");  
        novaRutaAmbViatger.setOrigen("Origen B");  
        novaRutaAmbViatger.setDesti("Desti B");  
        int rutaIdAmbViatger = rutaDAO.addRuta(novaRutaAmbViatger);  

        if (rutaIdAmbViatger == -1) {  
            System.out.println("Error al afegir la ruta amb viatger.");  
        } else {  
            System.out.println("Ruta afegida correctament amb viatger.");  

            // Afegir un nou viatger a aquesta ruta  
            Viatger nouViatger = new Viatger();  
            nouViatger.setDni("10222");  
            nouViatger.setCognoms("Cognom Viatger");  
            nouViatger.setNom("Nom Viatger");  
            nouViatger.setTlf("123456789");  
            nouViatger.setRuta(novaRutaAmbViatger); 
            nouViatger.setClub(null); 

            int viatgerId = viatgerDAO.addViatger(nouViatger);  

            if (viatgerId == -1) {  
                System.out.println("Error al afegir el viatger.");  
            } else {  
                System.out.println("Viatger afegit correctament.");  
            }  
        }  

        // 3. Consultar una ruta per codi  ---------------------------------------------------
        Ruta rutaConsultada = rutaDAO.getRutaById(2, true); 
        if (rutaConsultada != null) {  
            System.out.println("Dades de la ruta:");  
            System.out.println("Codi Ruta: " + rutaConsultada.getCodiRuta());  
            System.out.println("Nom Ruta: " + rutaConsultada.getNom());  
            System.out.println("Origen: " + rutaConsultada.getOrigen());  
            System.out.println("Desti: " + rutaConsultada.getDesti());  
            System.out.println("Viatgers: " + rutaConsultada.getViatgers());  

        
            for (Viatger viatger : rutaConsultada.getViatgers()) {  
                System.out.println("Viatger: " + viatger.getNom() + " " + viatger.getCognoms());  
                if (viatger.getClub() != null) {  
                    System.out.println("Club: " + viatger.getClub().getNomClub());  
                }  
            }  
        } else {  
            System.out.println("Ruta no trobada.");  
        }  

        // 4. Consultar un club per id  ---------------------------------------------------
        Club clubConsultat = clubDAO.getClubById(1);
        if (clubConsultat != null) {  
            System.out.println("Dades del club:");  
            System.out.println("Codi Club: " + clubConsultat.getCodiClub());  
            System.out.println("Nom Club: " + clubConsultat.getNomClub());  
            System.out.println("Comentari: " + clubConsultat.getComentari());  
        } else {  
            System.out.println("Club no trobat.");  
        }  

        // 5. Afegir un nou club sense viatgers  ---------------------------------------------------
        Club nouClubSenseViatgers = new Club();  
        nouClubSenseViatgers.setCodiClub(2);  
        nouClubSenseViatgers.setNomClub("Club Sense Viatgers");  
        nouClubSenseViatgers.setComentari("Aquesta és una descripció del club.");  
        int clubIdSenseViatgers = clubDAO.addClub(nouClubSenseViatgers);  

        if (clubIdSenseViatgers == -1) {  
            System.out.println("Error al afegir el club sense viatgers.");  
        } else {  
            System.out.println("Club afegit correctament sense viatgers.");  
        }  

        // 6. Afegir un nou club amb un nou viatger  ---------------------------------------------------
        Club nouClubAmbViatger = new Club();  
        nouClubAmbViatger.setCodiClub(3);  
        nouClubAmbViatger.setNomClub("Club Amb Viatger");  
        nouClubAmbViatger.setComentari("Descripció del club amb viatger.");  
        int clubIdAmbViatger = clubDAO.addClub(nouClubAmbViatger);  

        if (clubIdAmbViatger == -1) {  
            System.out.println("Error al afegir el club amb viatger.");  
        } else {  
            System.out.println("Club afegit correctament amb viatger.");  

            // Afegir un nou viatger a aquest club  
            Viatger nouViatgerAmbClub = new Viatger();
            Ruta rutaExistente = rutaDAO.getRutaById(1, false);
            nouViatgerAmbClub.setDni("20222");  
            nouViatgerAmbClub.setCognoms("Cognom Viatger Amb Club");  
            nouViatgerAmbClub.setNom("Nom Viatger Amb Club");  
            nouViatgerAmbClub.setTlf("987654321");  
            nouViatgerAmbClub.setRuta(rutaExistente);
            nouViatgerAmbClub.setClub(nouClubAmbViatger); 

            int viatgerIdAmbClub = viatgerDAO.addViatger(nouViatgerAmbClub);  

            if (viatgerIdAmbClub == -1) {  
                System.out.println("Error al afegir el viatger al club.");  
            } else {  
                System.out.println("Viatger afegit correctament al club.");  
            }  
        }  

        // 7. Llistar les dades de tots els viatgers  ----------------------------------------------------
        System.out.println("Llistat de tots els viatgers:");  
        for (Viatger viatger : viatgerDAO.listViatger()) {  
            System.out.println("Nom: " + viatger.getNom() + " " + viatger.getCognoms());  
            if (viatger.getClub() != null) {  
                System.out.println(" - Club: " + viatger.getClub().getNomClub());  
            }  
            if (viatger.getRuta() != null) {  
                System.out.println(" - Ruta: " + viatger.getRuta().getNom());  
            }  
        } 
        
    	// • Consultar un club per id.--------------------------------------------------

		 Club clubEncontrada = DAOManager.getClubDAO().getClubById(1);
		 System.out.println(clubEncontrada.getCodiClub());
		 System.out.println(clubEncontrada.getNomClub());
		 System.out.println(clubEncontrada.getComentari());

		// • Afegir un nou club sense viatgers.---------------------------------------------------

		 Club nuevoClub = new Club();
		 nuevoClub.setCodiClub(9);
		 nuevoClub.setNomClub("Hola");
		 nuevoClub.setComentari("daddad");
		 int clubId = clubDAO.addClub(nuevoClub);

		 if (clubId == -1) {
			System.out.println("Error al añadir el club.");
		 } else {
			System.out.println("Club añadido correctamente");
		 }

    }  
}