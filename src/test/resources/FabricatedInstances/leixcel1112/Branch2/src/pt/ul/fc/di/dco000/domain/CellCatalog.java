package pt.ul.fc.di.dco000.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * @author antonialopes
 *
 */
public class CellCatalog implements Iterable<Cell>{
	
	private List<Cell> cells;
	
	public CellCatalog(){
		this.cells = new ArrayList<Cell>();
	}

	public void add(Cell cell) {
		this.cells.add(cell);
	}

	public Cell getCell(CellAddress cellAddress) {
		for(Cell c: cells)
			if (c.hasAddress(cellAddress))
				return c;
		return null;
	}

	// puxa para baixo as linhas abaixo ou em line
	public void moveDown(Line line) {
		for(Cell c: cells){
			if (c.after(line) || c.in(line))
				c.moveDown();
		}
	}

	// puxa para cima as linhas abaixo de line
	public void moveUp(Line line) {
		for(Cell c: cells){
			//System.out.println(c.getAddress());
			if (c.after(line)){
				c.moveUp();	
			}
		}	
	}

	public void removeLine(Line line) {
		HashSet<Cell> s = new HashSet<Cell>();
		for(Cell c: cells){
			if (c.in(line))
				s.add(c);
		}	
		for (Cell c: s){
		    c.removed();
		}
		cells.removeAll(s);   
	}
	
	public void removeColumn(Column column) {
		HashSet<Cell> s = new HashSet<Cell>();
		for(Cell c: cells){
			if (c.in(column))
				s.add(c);
		}	
	    for (Cell c: s){
	          c.removed();
	    }
		cells.removeAll(s);	
	}

	// puxa para a direita as colunas ˆ direita de column
	public void moveRight(Column column) {
		for(Cell c: cells){
			if (c.after(column) || c.in(column))
				c.moveRight();
		}
	}

	public void moveLeft(Column column) {
		for(Cell c: cells){
			if (c.after(column))
				c.moveLeft();
		}
	}

	@Override
	public Iterator<Cell> iterator(){
		return cells.iterator();
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		for (Cell c: cells)
			s = s.append(c.getAddress()+" "+ c.getContent() +"\n");
		return s.toString();
	}

/*	public List<Cell> getCells(Line line) {
		List<Cell> l = new ArrayList<Cell>();
		// TODO Auto-generated method stub
		return null;
	}
*/
}
