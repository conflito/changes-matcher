/**
 * @author antonialopes
 * Class responsible for starting up the system and run some tests.
 * @version $Revision: 1.0 $
 */

package pt.ul.fc.di.dco000.ui.console;

import java.io.File;

import pt.ul.fc.di.dco000.domain.Column;
import pt.ul.fc.di.dco000.domain.IController;
import pt.ul.fc.di.dco000.domain.IWorkbook;
import pt.ul.fc.di.dco000.domain.Leixcel;
import pt.ul.fc.di.dco000.domain.Line;
import pt.ul.fc.di.dco000.domain.charts.IChart;

public class TestLeixel {

    /**
     * Field controller.
     */
    private static IController controller;

    /**
     * Method main.
     * @param args String[]

     * @throws Exception   */
    public static void main (String [] args) {

        final Leixcel leixcel = new Leixcel();
        controller = leixcel.getController();

        System.out.println("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ ");		
        System.out.println("        New empty workbook                ");
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n ");

        IWorkbook wb = controller.getNewWorkbook();
        printState(wb);

        testSimpleEdits(wb);
        testInsertRemoveLinesAndColumns(wb);
        testEditsWithFormulas(wb);
        testInsertRemoveLinesAndColumns(wb);

        testCharts(wb);
        testInsertRemoveLinesAndColumns(wb);

        testInsertFunction(wb);

        testExport(wb);
    }


    /**
     * Method testCharts. 
     * @param wb IWorkbook
     */
    private static void testCharts(IWorkbook wb) {

        System.out.println("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ ");		
        System.out.println("           Fill Cells for Chart          ");
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n ");

        System.out.println("_______EDIT E5 -> lmat");
        wb.editCell(new Column("E"),new Line(5), "lmat");
        System.out.println("_______EDIT E6 -> lei");
        wb.editCell(new Column("E"),new Line(6), "lei");
        System.out.println("_______EDIT E7 -> ltic");
        wb.editCell(new Column("E"),new Line(7), "ltic");
        System.out.println("_______EDIT E8 -> others");
        wb.editCell(new Column("E"),new Line(8), "others");

        System.out.println("_______EDIT F5 -> 12");
        wb.editCell(new Column("F"),new Line(5), "12");
        System.out.println("_______EDIT F6 -> 150");
        wb.editCell(new Column("F"),new Line(6), "150");
        System.out.println("_______EDIT F7 -> 70");
        wb.editCell(new Column("F"),new Line(7), "70");
        System.out.println("_______EDIT F8 -> 15");
        wb.editCell(new Column("F"),new Line(8), "15");
        printState(wb);

        System.out.println("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ ");		
        System.out.println("           Insert Pie Chart            ");
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n ");

        Class<? extends IChart> cl = controller.getChartType("PIE");
        boolean result = wb.insertChart(cl, "Chart 1",new Column("E"), new Line(5), new Line(8),
                new Column("F"), new Line(5), new Line(8));
        if (!result)
            System.out.println("Not valid data for pie chart");
        printState(wb);

    }


    /**
     * Method testSimpleEdits.
     * @param wb IWorkbook
     */
    private static void testSimpleEdits(IWorkbook wb) {

        boolean result = true;

        System.out.println("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ ");		
        System.out.println("    Simple Cell Edits with values       ");
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n ");

        System.out.println("_______EDIT A1 -> 5");
        result = wb.editCell(new Column("A"),new Line(1), "5");
        printStateAfterEdit(wb, result);

        System.out.println("_______EDIT B2 -> Ola");
        result = wb.editCell(new Column("B"),new Line(2), "Ola");
        printStateAfterEdit(wb, result);

        System.out.println("_______EDIT C5 -> 20.2");
        result = wb.editCell(new Column("C"),new Line(5), "20.2");
        printStateAfterEdit(wb, result);
    }



    /**
     * Method testInsertRemoveLinesAndColumns.
     * @param wb IWorkbook
     */
    private static void testInsertRemoveLinesAndColumns(IWorkbook wb) {

        System.out.println("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ ");		
        System.out.println("    Insert and Remove Lines/Columns     ");
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n ");

        System.out.println("_______INSERT LINE at LINE 2");
        wb.insertLine(new Line(2));
        printState(wb);

        System.out.println("_______INSERT COLUMN at COLUMN B");
        wb.insertColumn(new Column("B"));
        printState(wb);
        
        //TODO: uncomment for running this additional test
  /*      System.out.println("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ ");       
        System.out.println("    Edit cell that refers to cell that will be removed");
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n ");

        System.out.println("_______EDIT A1 -> =A2");
        boolean result = wb.editCell(new Column("A"),new Line(1), "=A2");
        printStateAfterEdit(wb, result);
*/
        
        System.out.println("_______REMOVE LINE at LINE 2");
        wb.removeLine(new Line(2));
        printState(wb);

        System.out.println("_______REMOVE COLUMN at COLUMN B");
        wb.removeColumn(new Column("B"));
        printState(wb);

    }


    /**
     * Method testEditsWithFormulas.
     * @param wb IWorkbook
     */
    private static void testEditsWithFormulas(IWorkbook wb) {
        boolean result = true;

        System.out.println("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ ");		
        System.out.println("   Cell Edits using formulas           ");
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n ");     

        System.out.println("_______EDIT C1 ->  =A1");
        result = wb.editCell(new Column("C"),new Line(1), "=A1");
        printStateAfterEdit(wb, result);

        System.out.println("_______EDIT D2 -> =B2");
        result = wb.editCell(new Column("D"),new Line(2), "=B2");
        printStateAfterEdit(wb, result);

        System.out.println("_______EDIT A3 ->  =A1+C1");
        result = wb.editCell(new Column("A"),new Line(3), "=SUM(A1;C1)");
        printStateAfterEdit(wb, result);

        System.out.println("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ ");		
        System.out.println("    Editing cells used in formulas      ");
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n ");		

        System.out.println("_______EDIT A1 ->  =A3");
        result = wb.editCell(new Column("A"),new Line(1), "=A3");
        printStateAfterEdit(wb, result);

        System.out.println("_______EDIT C1 ->  =A5");
        result = wb.editCell(new Column("C"),new Line(1), "=A5");
        printStateAfterEdit(wb, result);


        System.out.println("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ ");		
        System.out.println(" Cell Edits that cause automatic resize    ");
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n ");      


        System.out.println("_______EDIT C2  -> =F3");
        result = wb.editCell(new Column("C"),new Line(2), "=F3");
        printStateAfterEdit(wb, result);

        System.out.println("_______EDIT F3  -> =A8");
        result = wb.editCell(new Column("F"),new Line(3), "=A8");
        printStateAfterEdit(wb, result);

    }

    /**
     * Method testExport.
     * @param wb IWorkbook
     * */
    private static void testExport(IWorkbook wb) {

        System.out.println("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ ");		
        System.out.println("     Export Workbook to HTML      ");
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n ");      

        wb.export(controller.getFormat("HTML"),new File(wb.getName()+".html"));

        //TODO: uncomment for running this additional test
  /*      
        System.out.println("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ ");        
        System.out.println("     Export Workbook to ODF      ");
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n ");      

        wb.export(controller.getFormat("ODF"),new File(wb.getName()+".odf"));
*/
    }


    /**
     * Method testInsertFunction.
     * @param wb IWorkbook
     */
    private static void testInsertFunction(IWorkbook wb) {
        System.out.println("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ ");		
        System.out.println("   Insert Average in Catalog           ");
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n ");      

        boolean result = controller.loadFunction("AVERAGE","Average");

        System.out.println("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ ");		
        System.out.println("    Use the new function                  ");
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n ");      

        System.out.println("_______EDIT A4 -> Average C5 A1");
        result = wb.editCell(new Column("A"),new Line(4), "=AVERAGE(C5;A1)");
        printStateAfterEdit(wb, result);
    }


    /**
     * Method printStateAfterEdit.
     * @param wb IWorkbook
     * @param result boolean
     */
    private static void printStateAfterEdit(IWorkbook wb, boolean result) {
        if (!result){
            System.out.println("_______ERROR: Invalid content");
            System.out.println("circularity problem or syntax error");
        }
        printState(wb);
    }

    /**
     * Method printState.
     * @param wb IWorkbook
     */
    private static void printState(IWorkbook wb) {
        System.out.println(wb);
    }
}
