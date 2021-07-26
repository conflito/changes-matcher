import static org.junit.Assert.assertEquals;

import datatypes.ADLList;

public class ADLListIntegerCloneTest {

	//@requires howMany>=0
	public static ADLList<Integer> createList(int howMany) {
		ADLList<Integer> list = new ADLList<Integer>();
		for (int i = 0; i < howMany; i++) 
			list.add(i);
		return list;
	}

	public static boolean testOriginalEqualsToClone(ADLList<Integer> listO) {
		ADLList<Integer> listC = listO.clone();
		
		System.out.println("lO = " + listO);
		System.out.println("lc = "+ listC);		

		return listO.equals(listC);
	}
	
	
	public static boolean testChangeOriginal(ADLList<Integer> listO) {
		ADLList<Integer> listC = listO.clone();
		
		if (listO.size()==0)
			return false;
		
		int index = listO.size()/2;
		listO.set(index,100+index);
		listO.add(300);

		System.out.println("lO = " + listO);
		System.out.println("lc = "+ listC);		

		//assertEquals(listC.get(index), new Integer(index));
		return listC.get(index).equals(new Integer(index));
	}

	public static boolean testChangeClone(ADLList<Integer> listO) {
		ADLList<Integer> listC = listO.clone();
		
		if (listO.size()==0)
			return false;

		int index = 0;
		listC.set(index,index-100);
		listC.add(100);

		System.out.println("lO = " + listO);
		System.out.println("lc = "+ listC);		

		//assertEquals(listO.get(index), new Integer(index));
		return listO.get(index).equals(new Integer(index));
	}
	

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) {	
		testOriginalEqualsToClone(createList(12));
		System.out.println("####################");
		testChangeOriginal(createList(12));
		System.out.println("####################");
		testChangeClone(createList(12));
	}
	
}
