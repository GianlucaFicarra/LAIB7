package it.polito.tdp.poweroutages.model;

import java.util.HashMap;
import java.util.Map;

public class BlackIdMap {


	private Map<Integer, Blackout> map;
	
	public BlackIdMap() {
		map = new HashMap<>();
	}
	
	public Blackout get(Blackout black) { //se non esiste oggetto lo devo creare quindi passo tutto corso
		Blackout old = map.get(black.getId()); ///salvo o null o l'oggetto associato a quel codice e già creato
		if (old == null) {
			// nella mappa non c'è questo corso!
			map.put(black.getId(), black); //lo aggiungo
			return black;
		}
		return old;//se old non era null ritorno l'oggetto che avevo inserito
	}
	
	
	public Blackout get(int id) {  //metodo che riproduce il classico map della mappa
		return map.get(id);
	}
	
	public void put(int id, Blackout black) {
		map.put(id, black);
	}
}
