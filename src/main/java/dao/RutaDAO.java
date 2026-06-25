package dao;

import java.util.ArrayList;

import pojos.Ruta;

public interface RutaDAO {

	/* CRUD operations */
	/* Insercció de una Assistencies nova */
	public int addRuta(Ruta r);

	/* Buscar Assistencies */
	public Ruta getRutaById(Integer id, boolean fetchViatgers);

	/* Modificar Assistencies */
	public void updateRuta(Ruta r);

	/* Esborrar Assistencies */
	public Integer deleteRuta(Integer id);

	/* Llistar tots les Assistencies */
	public ArrayList<Ruta> listRuta();
}
