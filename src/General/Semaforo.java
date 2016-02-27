package General;

public class Semaforo {
	
	private int contador = 0;
	private int cantidadBloqueados = 0;
	
	public Semaforo(int valorInicial){
		contador = valorInicial;
	}
	
	synchronized public void WAIT(){
		while(contador==0){
			cantidadBloqueados++;
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		contador--;
	}
	
	synchronized public void SIGNAL(){
		contador = 1;
		cantidadBloqueados--;
		notify();
	}
	
	public int getNumBloqueados(){
		return cantidadBloqueados;
	}
}
