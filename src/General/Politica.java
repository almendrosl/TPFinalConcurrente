package General;

public class Politica {

	private int[][] matrizDePrioridades;
	
	public Politica(int[] vectorPrioridades){
		matrizDePrioridades = new int[vectorPrioridades.length][vectorPrioridades.length];
		hacerMatrizDePrioridades(vectorPrioridades);
	}

	private void hacerMatrizDePrioridades(int[] vectorPrioridades) {
		for (int i = 0; i < matrizDePrioridades.length; i++) {
			for (int j = 0; j < matrizDePrioridades.length; j++) {
				if(vectorPrioridades[i] == j){
					matrizDePrioridades[i][j] = 1;
				}
				else{
					matrizDePrioridades[i][j] = 0;
				}
			}
		}
	}
	
	public int masPrioridad(int[] hilosPosiblesADisparar) {
		int[] vectorPrioritario = new int[hilosPosiblesADisparar.length];
		int transicionADisparar = 0;
		int sum = 0;
		for (int i = 0; i < matrizDePrioridades.length; i++) {
			for (int j = 0; j < matrizDePrioridades.length; j++) {
				sum = sum + matrizDePrioridades[i][j]*hilosPosiblesADisparar[j];
			}
			
			vectorPrioritario[i] = sum;
			sum = 0;
		}
		
		int num = 0;
		for (int i = vectorPrioritario.length; i >= 0; i--) {
			if(vectorPrioritario[i] > 0){
				num = i;
			}
			vectorPrioritario[i] = 0;
		}
		vectorPrioritario[num] = 1;
		
		for (int i = 0; i < matrizDePrioridades.length; i++) {
			for (int j = 0; j < matrizDePrioridades.length; j++) {
				sum = sum + matrizDePrioridades[j][i]*vectorPrioritario[j];
			}
			if(sum != 0){
				transicionADisparar = i;
				break;
			}
		}

		
		return transicionADisparar;
	}
	
	public void setPolitica(int[] vectorPrioridades){
		hacerMatrizDePrioridades(vectorPrioridades);
	}
}
