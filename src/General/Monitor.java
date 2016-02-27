package General;

import java.util.ArrayList;


public class Monitor {
	
	private Semaforo mutex;
	private ArrayList<Semaforo> colasCondicion = new ArrayList<Semaforo>();
	private Petri redPetri;
	private Politica politica;
	
	public Monitor(Petri petri){
		redPetri = petri;
		int cantidadTransiciones = redPetri.getCantidadDeTransiciones();
		crearColasDeCondicion(cantidadTransiciones);
		mutex = new Semaforo(1);
	}

	private void crearColasDeCondicion(int cantidadTransiciones) {
		for (int i = 0; i < cantidadTransiciones; i++) {
			colasCondicion.add(new Semaforo(0));
		}
	}
	
	public void intentarDisparar(int numeroDeTransicion){
		mutex.WAIT();
		
		while(true){
			if(redPetri.intentarDisparo(numeroDeTransicion)){ //Si es posible disparar, intento despertar otro Hilo
				if(despertarHiloDeCola()){ //Intento despertar un hilo en una cola de Condicion
					break;
				}
				else{		//Si no hace falta despertar ningun hilo mas en una cola de condicion salgo
					mutex.SIGNAL(); //Libero el mutex porque no hay mas hilos en las colas
					break;
				}
			}
			else{
				if(despertarHiloDeCola()){
					colasCondicion.get(numeroDeTransicion).WAIT();
				}
				else{
					mutex.SIGNAL();		//No hay mas hilos q despertar, libero mutex y se mete en una cola de condicioon
					colasCondicion.get(numeroDeTransicion).WAIT();
				}
			}
		}
		
	}

	private boolean despertarHiloDeCola() {
		int[] hilosPosiblesADisparar = new int[redPetri.getCantidadDeTransiciones()];
		hilosPosiblesADisparar =  and();
		int cantidadDeHilosDisparables = analizarVector(hilosPosiblesADisparar);
		
		if(cantidadDeHilosDisparables == 0){
			return false;
		}
		else if(cantidadDeHilosDisparables == 1){
			int hiloDespertable = 0;
			for (int i = 0; i < hilosPosiblesADisparar.length; i++) {
				if(hilosPosiblesADisparar[i] != 0){
					hiloDespertable = i;
				}
			}
			colasCondicion.get(hiloDespertable).SIGNAL();
			return true;
		}
		else{
			int hiloParaDespertar = politica.masPrioridad(hilosPosiblesADisparar);
			colasCondicion.get(hiloParaDespertar).SIGNAL();
			return true;
		}
	}
	
	private int analizarVector(int[] hilosPosiblesADisparar) {
		int cantidadDeHilosDisparables = 0;
		for (int i = 0; i < hilosPosiblesADisparar.length; i++) {
			if(hilosPosiblesADisparar[i] != 0){
				cantidadDeHilosDisparables++;
			}
		}
		return cantidadDeHilosDisparables;
	}

	private int[] and(){
		int[] transiciones = redPetri.getTransicionesSensibilizadas();
		int[] threadsBloqueados = getNumeroColas();
		int[] disparos = new int[threadsBloqueados.length];
		
		for (int i = 0; i < threadsBloqueados.length; i++) {
			if((transiciones[i]==0) || (threadsBloqueados[i]==0)){
				disparos[i] = 0;
			}
			else{
				if(transiciones[i]<=threadsBloqueados[i]){
					disparos[i] = transiciones[i];
				}
				else{
					disparos[i] = threadsBloqueados[i];
				}
			}
		}
		
		return disparos;
		
	}

	private int[] getNumeroColas() {
		int[] numeroColas = new int[colasCondicion.size()];
		for (int i = 0; i < colasCondicion.size(); i++) {
			numeroColas[i] = colasCondicion.get(i).getNumBloqueados();
		}
		return numeroColas;
	}

}
