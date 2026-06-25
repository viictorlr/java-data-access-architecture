package dao;

import java.util.ArrayList;

import pojos.Viatger;

public interface ViatgerDAO {

	
	/* CRUD operations */
	/* Insercció de una Assistencies nova */
	public int addViatger(Viatger v);

	/* Buscar Assistencies */
	public Viatger getViatgerById(Integer id, boolean fetchRelations);

	/* Modificar Assistencies */
	public void updateViatger(Viatger v);

	/* Esborrar Assistencies */
	public Integer deleteViatger(Integer id);

	/* Llistar tots les Assistencies */
	public ArrayList<Viatger> listViatger();
}
