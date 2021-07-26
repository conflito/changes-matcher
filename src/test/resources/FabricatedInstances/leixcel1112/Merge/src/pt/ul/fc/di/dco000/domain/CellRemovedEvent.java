package pt.ul.fc.di.dco000.domain;

public class CellRemovedEvent {

        private Cell cell;
        
        public CellRemovedEvent(Cell cell) {
            this.cell = cell;
        }

        public Cell getCell(){
            return this.cell;
        }

}
