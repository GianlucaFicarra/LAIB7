package it.polito.tdp.poweroutages.model;

import java.util.HashMap;
import java.util.Map;


public class NercIdMap {

	
		private Map<Integer, Nerc> map;
		
		public NercIdMap() {
			map = new HashMap<>();
		}
		
		public Nerc get(Nerc nerc) { //se non esiste oggetto lo devo creare quindi passo tutto corso
			Nerc old = map.get(nerc.getId()); ///salvo o null o l'oggetto associato a quel codice se già creato
			if (old == null) {
				// nella mappa non c'è questo corso!
				map.put(nerc.getId(), nerc); //lo aggiungo
				return nerc;
			}
			return old;//se old non era null ritorno l'oggetto che avevo inserito
		}
		
		
		public Nerc get(int id) {  //metodo che riproduce il classico map della mappa
			return map.get(id);
		}
		
		public void put(int id, Nerc nerc) {
			map.put(id, nerc);
		}
		
		
}
