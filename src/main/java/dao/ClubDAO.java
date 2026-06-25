package dao;

import java.util.ArrayList;

import pojos.Club;

public interface ClubDAO {

	/* CRUD operations */
	/* Insercció de una Assistencies nova */
	public Integer addClub(Club c);

	/* Buscar Assistencies */
	public Club getClubById(Integer id);

	/* Modificar Assistencies */
	public void updateClub(Club c);

	/* Esborrar Assistencies */
	public Integer deleteClub(Integer id);

	/* Llistar tots les Assistencies */
	public ArrayList<Club> listClub();
}
