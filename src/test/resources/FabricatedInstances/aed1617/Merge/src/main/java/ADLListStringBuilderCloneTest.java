import static org.junit.Assert.assertEquals;

import datatypes.ADLList;

public class ADLListStringBuilderCloneTest {

	//@requires howMany>=0
	public static ADLList<StringBuilder> createList(int howMany) {
		ADLList<StringBuilder> list = new ADLList<>();
		for (int i = 0; i < howMany; i++) 
			list.add(new StringBuilder(i+""));
		return list;
	}
	
	
	public static boolean testChangeOriginal(ADLList<StringBuilder> listO) {
		ADLList<StringBuilder> listC = listO.clone();
	
		if (listO.size()==0)
			return false;


		//get element in index
		int index = listO.size()/2;
		String element = listC.get(index).toString();

		//change element in index at original
		listO.get(index).append("0");
		

		//System.out.println("lO = " + listO);
		//System.out.println("lc = "+ listC);		

		return (listC.get(index).toString().equals(element));
	}

	public static boolean testChangeClone(ADLList<StringBuilder> listO) {
		ADLList<StringBuilder> listC = listO.clone();
		
		if (listO.size()==0)
			return false;

		int index = 0;
		String element = listO.get(index).toString();
		
		//change element in index at clone
		listC.get(index).append("0");

		//System.out.println("lO = " + listO);
		//System.out.println("lc = "+ listC);		

		return (listO.get(index).toString().equals(element));
	}
	

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	/*public static void main(String[] args) throws InterruptedException {	
		testChangeOriginal(createList(12));
		System.out.println("###################");
		testChangeClone(createList(12));
	}*/
	
}
