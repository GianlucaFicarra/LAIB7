package it.polito.tdp.poweroutages.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.poweroutages.db.PowerOutageDAO;


public class Model {

	PowerOutageDAO podao;
	
	private NercIdMap nercmap;
	private BlackIdMap blackmap;
	
	private  List<Nerc> nerc;
	
	private  List<Blackout> blackList;
	private  List<Blackout> blackFiltrati;
	private  List<Blackout> soluzione;
	
	private int maxDisserviti;
	
	
	public Model() {
		podao = new PowerOutageDAO();
		
		nercmap = new NercIdMap();
		blackmap = new BlackIdMap();
		
		//PATTERN relax 1:N o 1:1 ORM, evito ripetizioni elementi passando alle liste le mappe
		
		//prima carico la tabella degli N(dato 1 nerc tanti black)
		nerc = podao.getNercList(nercmap);
		System.out.println(nerc);
		
		//dopo associo la tabella degli 1(dato un black solo 1 nerc) 
		blackList = podao.getAllBlack(blackmap, nercmap);
		System.out.println(blackList);
		
		/*NB: data la relazione 1:N ci conviene per evitare di scrivere 4 metodi, di passare entrambe le mappe
		 * alla tabella degli 1, e qui dentro fare i riferimenti incrociati*/
	
		
	}
	


	public List<Nerc> getNercList() {
		return this.nerc;
	}
	
	
	public List<Blackout>  trovaSoluzione(Nerc nerc, int anniMax, int oreMax) {
		
		//inizzializzo
		soluzione=new ArrayList<>();
		maxDisserviti=0;
		
		//creo nuova lista blackout filtrata in base al nerc passato
		blackFiltrati=new ArrayList<Blackout>();
		for (Blackout event : blackList) {
			if (event.getNerc().equals(nerc)) {
				blackFiltrati.add(event);
			}
		}
		
		//comparatore al volo per ordinarli in ordine di crescente (vecchio-->recente)
		blackFiltrati.sort(new Comparator<Blackout>() {
			@Override
			public int compare(Blackout o1, Blackout o2) {
				return o1.getBlackStart().compareTo(o2.getBlackStart());
			}

		});
		
		//stampa di debug
		System.out.println("Event list filtered: " + blackFiltrati);

		
		List<Blackout> parziale= new ArrayList<>();
		
		recursive(parziale, anniMax, oreMax);
		
		return soluzione;
				
	}

	private void recursive(List<Blackout> parziale, int anniMax, int oreMax) {
		
		//A cond di terminazione, vedo se la soluzione sia la migliore
		if(contaDisserviti(parziale) >= maxDisserviti) {
			maxDisserviti=contaDisserviti(parziale);
			soluzione= new ArrayList<Blackout>(parziale); //deep copy
		}
		
		
		for(Blackout b: blackFiltrati) {
			// aggiungo solo blackout non presenti nella parziale
			if(!parziale.contains(b)) {
					parziale.add(b);
					
					//verifico se la soluzione sia valida
					if(checkMaxYears(parziale, anniMax) && checkMaxHoursOfOutage(parziale, oreMax)) {
						recursive(parziale, anniMax, oreMax);
					   } 
					
					parziale.remove(b);
				}
			
				
		}
		
	}
	
	
	//public per farla accedere dal test model
	public  int contaDisserviti(List<Blackout> parziale) {
		int count=0;
		for(Blackout b: parziale) //per ogni blackout conta i non serviti
			count+=b.getCustomerAffercted();

		return count;
	}
	
	
	private boolean checkMaxYears(List<Blackout> parziale, int anniMax) {
		if (parziale.size() >=2 ) {//se nella parziale ho almeno due elementi
			int y1 = parziale.get(0).getYear(); //prendo anno del più vecchio
			int y2 = parziale.get(parziale.size() - 1).getYear(); //prendo anno del più recente
			if ((y2 - y1 + 1) > anniMax) { // se l'intervallo è maggiore di quello richiesto non passa il test
				return false;
			}
		} //se un solo elemento o intervallo passa il test ritorna true
		return true;
	}
	
	private boolean checkMaxHoursOfOutage(List<Blackout> parziale, int oreMax) {
		int sum = sumBlackoutHours(parziale);
		if (sum > oreMax) {//se tot ore supera quello voluto non passa il test
			return false;
		}
		return true; //passa test se non supera le ore volute
	}
	
	//public per farla accedere dal test model
	public int sumBlackoutHours(List<Blackout> parziale) {
		int sum = 0;
		for (Blackout event : parziale) {
			sum += event.getOutageDuration(); //prendo intervallo durata del blackout corrente
		}
		return sum;
	}
	
	public List<Integer> getYearList() {
		//salvo anni di tutti i blackout del dao in SET per non avere ripetizioni
		Set<Integer> yearSet = new HashSet<Integer>();
		for (Blackout event : blackList) {
			yearSet.add(event.getYear());
		}
		
		//converto in lista di anni
		List<Integer> yearList = new ArrayList<Integer>(yearSet);
		
		//ordino dal più vecchio al più recente
		yearList.sort(new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o2.compareTo(o1);
			}
			
		});
		return yearList;
	}
	
}
