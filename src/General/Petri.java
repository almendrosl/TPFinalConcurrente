package General;


public class Petri {
	// Campos de la clase Petri
		private int[][] matrizDeIncidencia; 
		private int[] marcacionActual;
		private int[] marcacionFutura;
		private int[][] matrizDisparoPrueba;
		private int[] transicionesSensibilizadas;
		
		/**
		 * @brief Constructor predeterminado de la clase Petri
		 */
		public Petri(int[][] matrizDeIncidencia, int[] marcacionActual) {
			this.matrizDeIncidencia = matrizDeIncidencia;
			this.marcacionActual =marcacionActual;
			matrizDisparoPrueba = matrizPruebaDisparos();
			printMatriz(matrizDisparoPrueba);
			printMatriz(matrizDeIncidencia);
		}



		public void printMatriz(int[][] vector) {
			System.out.println("Dimensiones: [" + vector.length + "x"
					+ vector[0].length + "]");
			for (int i = 0; i < vector.length; i++) {
				for (int j = 0; j < vector[0].length; j++) {
					if (vector[i][j] == -1)
						System.out.print(" " + vector[i][j]);
					else
						System.out.print("  " + vector[i][j]);
				}
				System.out.print("\n");
			}

		}



		
		
		public boolean modifMarcadoActualDisparando(int[] vectorDisparo){
			int[] nuevoMarcado = new int[marcacionActual.length];
			nuevoMarcado = disparar(vectorDisparo);
			if(!buscarNegativos(nuevoMarcado)){
				marcacionActual = nuevoMarcado;
				return true;
			}
			return false;
		}

		/**
		 * @brief Metodo que busca la presencia de valores negativos en el vector
		 *        que se le pasa com parametro
		 * @param nuevaMarcacion
		 * @return true si hay negativos, false si no los hay
		 */
		private boolean buscarNegativos(int[] nuevaMarcacion) {
			boolean tieneNegativos = false;
			for (int i = 0; i < nuevaMarcacion.length; i++) {
				// System.out.println(nuevaMarcacion[i][0]);
				if (nuevaMarcacion[i] < 0) {
					tieneNegativos = true;
				}
			}
			return tieneNegativos;
		}

		
		//Hace una matriz NxN para probar los posibles transiciones que esten sensibilizadas
		private int[][] matrizPruebaDisparos(){
			int[][] matrizDisparos = new int[matrizDeIncidencia[0].length][matrizDeIncidencia[0].length];
			for (int i = 0; i < matrizDisparos.length; i++) {
				for (int j = 0; j < matrizDisparos[i].length; j++) {
					if(j == i){
						matrizDisparos[i][j] = 1;
					}
					else{
						matrizDisparos[i][j] = 0;
					}
				}
			}
			return matrizDisparos;
		}
		
		public int[] getTransicionesSensibilizadas(){
			int[] transicionesSensibilizadas = new int[matrizDisparoPrueba.length];
			int[] marcadoTemporal = new int[marcacionActual.length];
			
			for (int i = 0; i < matrizDisparoPrueba.length; i++) {
				marcadoTemporal = disparar(matrizDisparoPrueba[i]);
				if(!buscarNegativos(marcadoTemporal)){ //Si no hay negativos es posible disparar
					transicionesSensibilizadas[i] = 1;
				}
			}
			
			return transicionesSensibilizadas;
			
		}



		private int[] disparar(int[] vectorDisparo) {
			if (vectorDisparo.length != matrizDeIncidencia[0].length) {
				System.err
						.println("ERROR en Metodo efectuarDisparo(): Error en la Dimension de los Operandos");
				System.out.println("Vector: " + vectorDisparo.length
						+ "x1  Matriz: 9x" + matrizDeIncidencia[0].length);
			}
			
			
			int sum=0;                         // multiplicar matriz.
		    int multiplicacion[] = new int[matrizDeIncidencia.length];	
		    int[] nuevoMarcado = new int[marcacionActual.length];
		    
		    for(int i=0; i<matrizDeIncidencia.length; i++){        
		        for(int k=0; k<matrizDeIncidencia[i].length; k++){
		        sum=sum+matrizDeIncidencia[i][k] * vectorDisparo[k];
		        }
		        multiplicacion[i] = sum;

		        sum=0;
			}
		    
		    for (int i = 0; i < multiplicacion.length; i++) {
				nuevoMarcado[i] = multiplicacion[i] + marcacionActual[i];
			}
			return nuevoMarcado;
		}



		public int getCantidadDeTransiciones() {
			
			return matrizDeIncidencia[0].length;
		}
		
		public boolean intentarDisparo(int transicion){
			int[] disparo = new int[getCantidadDeTransiciones()];
			for (int i = 0; i < disparo.length; i++) {
				if(i == transicion){
					disparo[i] = 1;
				}
			}
			return modifMarcadoActualDisparando(disparo);
		}


}
