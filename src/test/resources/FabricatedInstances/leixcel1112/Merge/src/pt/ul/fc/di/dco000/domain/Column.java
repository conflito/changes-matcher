package pt.ul.fc.di.dco000.domain;

import java.util.Arrays;

import pt.ul.fc.di.dco000.adts.SucAndPredessorable;

public class Column implements SucAndPredessorable<Column>{

	private static final char[] LETTERS = {'A', 'B', 'C','D','E','F'}; // TODO COMPLETE
	public static final Column FIRST = new Column("A");

	private final int[] id; // indices das letras no nome

	public static boolean validName(String name){
		boolean found = true;
		for (char c: name.toCharArray()){
			boolean foundC = false;
			for (int i = 0; i < LETTERS.length && !foundC; i++ ){
				if ( c == LETTERS[i] ){
					foundC = true;
				}
			}
			found = found && foundC;
		}
		return found; 
	}
	
	public static Column newColumn(int n){
		Column c = FIRST;
		for(int i=1; i<n; i++)
			c = c.suc();
		return c;
	}

	//@requires validName(name)
	public Column(String name) {
		this.id = new int[name.length()];
		for (int i = 0; i < name.length(); i++){
			for (int j = 0; j < LETTERS.length; j++){
				if( name.charAt(i) == LETTERS[j] )
					this.id[i] = j;
			}
		}
	}

	private Column(int[] v) {
		this.id = new int[v.length];
		for (int i = 0; i < v.length; i++){
			this.id[i] = v[i];
		}
	}
	
	

	@Override
	public boolean isFirst() {
		return this.equals(FIRST);
	}

	public String getColumnId(){
		char[] v = new char[id.length];
		for (int i = 0; i < v.length; i++){
			v[i] = LETTERS[id[i]];
		}
		return new String(v);
	}


	@Override
	public Column pred() {	
		boolean first = true;
		int[] idPred = null; 

		for (int i = 0; i < id.length && first; i++ )
			if ( id[i] != 0 )
				first = false;
		
		if (first){
			idPred = new int[id.length - 1];
		}
		else{
			idPred = new int[id.length];
		}
		int i = idPred.length - 1;
		int j = id.length - 1;
		boolean dec = true;
		do {
			idPred[i] = (id[j] > 0)? (id[j] - 1) :  (LETTERS.length - 1);
			dec = (idPred[i] == LETTERS.length-1);
			i--;
			j--;
		} while (i >= 0 &&  dec);
		return new Column(idPred);
	}

	@Override
	public Column suc() {
		boolean last = true;
		int[] idSuc = null; 

		for (int i = 0; i < id.length && last; i++ )
			if ( id[i] != LETTERS.length - 1 )
				last = false;
		
		if (last){
			idSuc = new int[id.length+1];
		}
		else{
			idSuc = new int[id.length];
		}
		int i = idSuc.length - 1;
		int j = id.length - 1;
		boolean inc = true;
		do {
			idSuc[i] = (id[j] + 1) % LETTERS.length;
			inc = (idSuc[i] == 0);
			i--;
			j--;
		} while (j >= 0 &&  inc);
		return new Column(idSuc);

	}

	@Override
	public int compareTo(Column other) {
		if (this.id.length != other.id.length)
			return this.id.length - other.id.length;
		else{
			for(int i = 0; i < this.id.length; i++){
				if(this.id[i] != other.id[i])
					return this.id[i] - other.id[i];
			}
		}
		return 0; 			
	}	

	public String toString(){
		return getColumnId();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(id);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Column other = (Column) obj;
		if (!Arrays.equals(id, other.id))
			return false;
		return true;
	}
}
