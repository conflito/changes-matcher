import java.util.ArrayDeque;
import java.util.Iterator;
/**
 * Arvore ternaria de Strings, representando um Map
 * @author Miguel Morais n�49478
 * @author Nuno Castanho n�49518
 *
 * @param <V> Tipo dos valores(value) associados a cada chave(key)
 */
public class PTTStringsMap<V>implements StringsMap<V>, Cloneable {

    private Node<V> root;
    private int size;

    private static final String SEM_NODE = "SEM NODE";

	/**
	 * Constroi uma PTTStringsMap
	 */
    public PTTStringsMap(){
		root = null;
		size = 0;
	}

    @Override
    public int size(){
    	return size;
    }

    @Override
    public boolean containsKey(String key) {
        boolean result;
        Node<V> encontrado = encontraNodeComChar(root, key.charAt(0));
        if(encontrado == null){
            result = false;
    	}
    	else{
            if(key.length() == 1){
                if(encontrado.value == null){
                    result = false;
    			}
                else{
                    result = true;
                }
    		}else{
    	        int indice = 1;
    	        result = percorreFraseParaAveriguarIgualdade(key, indice, encontrado.mid);
    		}
    	}
        return result;
    }

	/**
	 * Devolve o Node que possua o caracter dado
	 * @param node Node atual na procura
	 * @param c Caracter procurado
	 * @return O Node com o caracter dado, null caso nao exista
	 */
    private Node<V> encontraNodeComChar(Node<V> node, char c){
        if(node != null){
            if(node.character == c){
                return node;
			}
            else if(c < node.character){
                return encontraNodeComChar(node.left, c);
			}
            else{
                return encontraNodeComChar(node.right, c);
			}
		}
        return null;
	}

	/**
	 * Percorre o mapa apartir de um Node dado para averiguar se a String formada pelo
	 * proprio e os filhos eh a mesma que uma String dada
	 * @param key String a comparar igualdade
	 * @param indice Indice atual na String dade
	 * @param atual Node atual no decorrer da procura
	 * @return true caso a String dada esteja presente entre os Nodes, false caso contrario
	 * @requires key != null && key.length() > 0
	 */
    private boolean percorreFraseParaAveriguarIgualdade(String key, int indice, Node<V> atual){
        if(atual != null && indice < key.length()){
            if(atual.character == key.charAt(indice)){
                if(indice == key.length()-1 && atual.value != null){
                    return true;
				}
				else{
					return percorreFraseParaAveriguarIgualdade(key, ++indice, atual.mid);
				}
			}
            else if(key.charAt(indice) < atual.character){
                return percorreFraseParaAveriguarIgualdade(key, indice, atual.left);
			}
            else{
                return percorreFraseParaAveriguarIgualdade(key, indice, atual.right);
			}
		}
        return false;
	}

    @Override
    public V get(String key) {
        V result = null;
        if(containsKey(key)){
            Node<V> encontrado = encontraNodeComChar(root, key.charAt(0));
            if(encontrado != null){
                if(key.length() == 1){
                    result = encontrado.value;
    			}
                else{
                    int indice = 1;
                    result = valorProcurado(key, indice, encontrado.mid);
    			}
    		}
    	}
        return result;
    }

	/**
	 * Percorre o mapa a partir de um Node dado ah procura do valor de uma
	 * String dada
	 * @param key String dada
	 * @param indice Indice atual na key
	 * @param atual Node atual na procura
	 * @return O valor associado ah chave key, null caso nao tenha valor associado
	 * @requires key != null && key.length() > 0
	 */
    private V valorProcurado(String key, int indice, Node<V> atual){
        if(atual != null && indice < key.length()){
            if(atual.character == key.charAt(indice)){
                if(indice == key.length() -1){
                    return atual.value;
				}
                else{
                    return valorProcurado(key, ++indice, atual.mid);
				}
			}
            else if(key.charAt(indice) < atual.character){
                return valorProcurado(key, indice, atual.left);
			}
			else{
                return valorProcurado(key, indice, atual.right);
			}
		}
        return null;
	}

    @Override
    public void put(String key, V value) {
        int indice = 0;
        if(!containsKey(key)){
            size++;
    	}
        root = put(root, key, value, indice);
    }

	/**
	 * Coloca um par chave-valor no mapa, apartir de um Node dado
	 * @param atual Node atual na insercao
	 * @param key Chave inserida
	 * @param value Valor associado ah key
	 * @param indice Indice atual na key
	 * @return Node raiz que representa o mapa jah com o par inserido
	 * @requires key != null && key.length() > 0 && value != null
	 */
    private Node<V> put(Node<V> atual, String key, V value, int indice){
        char caracter = key.charAt(indice);
        if(atual == null){
            atual = new Node<V>(caracter, null);
		}
        if(caracter < atual.character){
            atual.left = put(atual.left, key, value, indice);
		}
        else if(caracter > atual.character){
            atual.right = put(atual.right, key, value, indice);
		}
        else if(indice < key.length()-1){
            atual.mid = put(atual.mid, key, value, ++indice);
		}
        else{
            atual.value = value;
		}
        return atual;
	}

    @Override
    public Iterable<String> keys() {
        ArrayDeque<String> fila = new ArrayDeque<String>();
        StringBuilder frase = new StringBuilder();
        StringBuilder anterior = new StringBuilder();
        if(size > 0){
            frase.append(root.character);
            if(root.value != null){
                fila.push(frase.toString());
                frase = new StringBuilder();
    		}
            anterior.append(root.character);
            encheFila(fila, frase, anterior, root.mid);
            encheFila(fila, new StringBuilder(), new StringBuilder(), root.left);
            encheFila(fila, new StringBuilder(), new StringBuilder(), root.right);
    	}
        return fila;
    }

	/**
	 * Percorre o mapa a partir de um Node dado e vai colocando as keys nesse "sub mapa" num
	 * ArrayDeque
	 * @param fila ArrayDeque com as keys deste "sub mapa"
	 * @param frase StringBuilder que representa a key em construcao
	 * @param anterior StringBuilder com a String de caracteres anteriores para serem considerados
	 * nos filhos da esquerda e da direita
	 * @param atual Node atual a insercao de valors na fila
	 * @requires fila != null && frase != null && anterior != null
	 */
    private void encheFila(ArrayDeque<String> fila, StringBuilder frase, StringBuilder anterior, Node<V> atual){
        if(atual != null){
            if(atual.value != null){
                frase.append(atual.character);
                fila.push(frase.toString());
                StringBuilder ladoEsquerdo = new StringBuilder(anterior);
                StringBuilder ladoDireito = new StringBuilder(anterior);
                anterior.append(atual.character);
                frase = new StringBuilder(anterior);
                encheFila(fila, frase, anterior, atual.mid);
                if(atual.left != null){
                    frase = new StringBuilder(ladoEsquerdo);
                    encheFila(fila, frase, ladoEsquerdo, atual.left);
				}
                if(atual.right != null){
                    frase = new StringBuilder(ladoDireito);
                    encheFila(fila, frase, ladoDireito, atual.right);
				}
			}
            else{
                frase.append(atual.character);
                StringBuilder ladoEsquerdo = new StringBuilder(anterior);
                StringBuilder ladoDireito = new StringBuilder(anterior);
                anterior.append(atual.character);

                encheFila(fila, frase, anterior, atual.mid);
                if(atual.left != null){
                    frase = new StringBuilder(ladoEsquerdo);
                    encheFila(fila, frase, ladoEsquerdo, atual.left);
				}
                if(atual.right != null){
                    frase = new StringBuilder(ladoDireito);
                    encheFila(fila, frase, ladoDireito, atual.right);
				}
			}
		}
	}

	/**
	 * Verifica se este mapa e outro objeto sao iguais
	 */
    @SuppressWarnings("unchecked")
    public boolean equals(Object other){
        return this == other || other instanceof PTTStringsMap && equalsPTTStringsMap((PTTStringsMap<V>)other);
    }

	/**
	 * Verifica se este mapa eh igual a outro dado
	 * @param other Mapa a comparar
	 * @return true caso sejam iguais, false caso contrario
	 * @requires other != null
	 */
    private boolean equalsPTTStringsMap(PTTStringsMap<V> other) {
        boolean result = true;
        if(size != other.size){
            result = false;
		}
        else{
            Iterable<String> estaArvore = keys();
            Iterator<String> iterator = estaArvore.iterator();
            boolean achouDiferente = false;
            while(iterator.hasNext() && !achouDiferente){
                String alvo = iterator.next();
                V valueDesta = get(alvo);
                if(!other.containsKey(alvo)){
                    achouDiferente = true;
				}
                else if(!valueDesta.equals(other.get(alvo.toString()))){
                    achouDiferente = true;
				}

			}
            if(achouDiferente){
                result = false;
			}
		}
        return result;
	}

	/**
	 * Devolve um clone deste mapa
	 */
    @SuppressWarnings("unchecked")
    public PTTStringsMap<V> clone(){
        try{
            PTTStringsMap<V> result = (PTTStringsMap<V>)super.clone();
            result.root = copiaNodes(this.root, result.root);
            return result;
    	}catch(CloneNotSupportedException e){
            throw new InternalError(e.toString());
    	}
    }

	/**
	 * Devolve o Node raiz do mapa clonado
	 * @param nodeDesta Node deste mapa a considerar
	 * @param nodeClone Node do clone a ser alterado
	 * @return Node raiz do clone
	 */
    private Node<V> copiaNodes(Node<V> nodeDesta, Node<V> nodeClone){
        if(nodeDesta != null && nodeClone != null){
            nodeClone = new Node<V>(nodeDesta.character, nodeDesta.value, 
            		nodeClone.left, nodeClone.mid, nodeClone.right);
            nodeClone.left = copiaNodes(nodeDesta.left, nodeClone.left);
            nodeClone.mid = copiaNodes(nodeDesta.mid, nodeClone.mid);
            nodeClone.right = copiaNodes(nodeDesta.right, nodeClone.right);
            return nodeClone;
		}
        return null;
	}

	/**
	 * Devolve a representacao deste mapa, mostrando os pares key:value
	 */
    public String toString(){
        StringBuilder sb = new StringBuilder();
        StringBuilder anterior = new StringBuilder();
        if(size > 0){
            sb.append("{" + root.character);
            if(root.value != null){
                sb.append(":" + root.value + ", ");
                if(root.mid != null){
                    sb.append(root.character);
				}
			}
            anterior.append(root.character);
            frases(sb, anterior, root.mid);
            frases(sb, new StringBuilder(), root.left);
            frases(sb, new StringBuilder(), root.right);
            if(sb.length() >= 2){
                sb.deleteCharAt(sb.length()-1);
                sb.deleteCharAt(sb.length()-1);
			}
            sb.append("}");
	}
        return sb.toString();
	}

	/**
	 * Insere num StringBuilder principal que eh a representacao deste mapa os pares a partir
	 * de um Node dado
	 * @param principal StringBuilder com a representacao deste mapa
	 * @param anterior StringBuilder com os caracteres a serem considerados para os Nodes filhos do
	 * lado direito e esquerdo
	 * @param atual Node atual no percurso do mapa
	 * @requires principal != null && anterior != null
	 */
    private void frases(StringBuilder principal, StringBuilder anterior, Node<V> atual){
        if(atual != null){
            if(atual.value != null){
                principal.append(atual.character + ":" + atual.value + ", ");
                StringBuilder anteriorParaEsquerda = new StringBuilder(anterior);
                StringBuilder anteriorParaDireita = new StringBuilder(anterior);
                anterior.append(atual.character);
                if(atual.mid != null){
                    principal.append(anterior);
                    frases(principal, anterior, atual.mid);
				}
                if(atual.left != null){
                    principal.append(anteriorParaEsquerda);
                    frases(principal, anteriorParaEsquerda, atual.left);
				}
                if(atual.right != null){
                    principal.append(anteriorParaDireita);
                    frases(principal, anteriorParaDireita, atual.right);
				}
			}
            else{
                principal.append(atual.character);
                StringBuilder anteriorParaEsquerda = new StringBuilder(anterior);
                StringBuilder anteriorParaDireita = new StringBuilder(anterior);
                anterior.append(atual.character);

                frases(principal, anterior, atual.mid);
                if(atual.left != null){
                    principal.append(anteriorParaEsquerda);
                    frases(principal, anteriorParaEsquerda, atual.left);
				}
                if(atual.right != null){
                    principal.append(anteriorParaDireita);
                    frases(principal, anteriorParaDireita, atual.right);
				}
			}

		}
	}

	/**
	 * Devolve um Iterable<String> com as chaves deste mapa que comecam com um dado prefixo
	 * @param pref Prefixo a considerar
	 * @return Iterable com as chaves
	 * @requires pref != null
	 */
    public Iterable<String> keysStartingWith(String pref){
        ArrayDeque<String> result = new ArrayDeque<String>();
        Iterator<String> iterator = keys().iterator();
        while(iterator.hasNext()){
            String alvo = iterator.next();
            if(alvo.startsWith(pref)){
                result.push(alvo);
			}
		}
        return result;
	}


    public String toStringForDebugging(){
        StringBuilder result = new StringBuilder();
        result.append(" NODE | LEFT | MID | RIGHT \n");
        encheString(result, root);
        return result.toString();
	}

    private void encheString(StringBuilder sb, Node<V> atual){
        if(atual != null){
            sb.append(atual.nodeEmString() + " ");
            if(atual.left != null){
                sb.append("| " + atual.left.nodeEmString()+ " ");
			}
            else{
                sb.append("| " + SEM_NODE + " ");
			}
            if(atual.mid != null){
                sb.append("| " + atual.mid.nodeEmString() + " ");
			}
            else{
                sb.append("| " + SEM_NODE + " ");
			}
            if(atual.right != null){
                sb.append("| " + atual.right.nodeEmString());
			}
            else{
                sb.append("| " + SEM_NODE + " ");
			}
            sb.append("\n");
            encheString(sb, atual.mid);
            encheString(sb, atual.left);
            encheString(sb, atual.right);
		}
	}
}
