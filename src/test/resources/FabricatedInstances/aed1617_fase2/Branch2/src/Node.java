/**
	 * Representa um noh da arvore tern�ria
	 * @author Miguel Morais n�XXXXX ,Nuno Castanho n�49518
	 *
	 * @param <V> Tipo de valores a serem associados 
	 */
public class Node<V>{
        char character;
        V value;

        Node<V> left;
        Node<V> mid;
        Node<V> right;

		/**
		 * Constroi e inicializa um Node com o caracter e valor pretentido
		 * @param character Caracter a atribuir ao Node
		 * @param value Valor atribuido ah String que termina neste Node
		 * @requires value != null
		 */
        Node(char character, V value){
            this.character = character;
            this.value = value;
            this.left = null;
            this.mid = null;
            this.right = null;
		}

		/**
		 * Constroi e inicializa um Node com os seus filhos, o caracter e valor dados
		 * @param character Caracter a atribuir ao Node
		 * @param value Valor atribuido ah String que termina neste Node
		 * @param left Filho esquerdo deste Node
		 * @param mid Filho do meio deste Node
		 * @param right Filho direito deste Node
		 * @requires value != null
		 */
        Node(char character, V value, Node<V> left, Node<V> mid, Node<V> right){
            this.character = character;
            this.value = value;
            this.left = left;
            this.mid = mid;
            this.right = right;
		}

		/**
		 * Constroi e devolve a representacao deste Node
		 * @return String com a representacao
		 */
        String nodeEmString(){
            return this.character + ":" + value;
		}
}