package matcher;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

import matcher.exceptions.ApplicationException;
import matcher.utils.Pair;

public class TestFabricatedInstances {

	private static final String SRC_FOLDER = ".." + File.separator + "Examples" + 
			File.separator + "SemanticConflicts3" + File.separator;
	
	private static final String AED_FOLDER = "aed1617" + File.separator;
	
	private static final String AED_FOLDER_2 = "aed1617_fase2" + File.separator;
	
	private static final String IP_FOLDER = "ip2021" + File.separator;
	
	private static final String LEIXEL_FOLDER = "leixcel1112" + File.separator;
	
	private static final String CSS_FIELD_HIDING = "css2021_projeto1_047" + File.separator;
	
	private static final String CSS_REMOVE_OVERRIDING = "css2021_projeto1_047_RO" + File.separator;
	
	private static final String CSS_ADD_OVERRIDING = "css2021_projeto1_009" + File.separator;
	
	private static final String CSS_ACCESS_CHANGE = "css2021_projeto1_009_AC" + File.separator;
	
	private static final String CSS_OVERLOAD_BY_ADDITION = "css2021_projeto1_009_OA" + File.separator;
	
	private static final String CSS_UNEXPECTED_OVERRIDING = "css2021_projeto1_009_UN" + File.separator;
	
	private static final String CSS_NO_CONFLICT = "css2021_projeto1_009_no_conflict" + File.separator;
	
	@Test
	public void aedDependencyBasedTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER + AED_FOLDER + "config.properties");
		
		String adlList = "src" + File.separator + "main" + 
				File.separator + "java" + File.separator + "datatypes" + 
				File.separator + "ADLList.java";
		
		String cloneBuilderTest = "src" + File.separator + "main" + 
				File.separator + "java" + File.separator + 
				"ADLListStringBuilderCloneTest.java";
		
		String cloneIntTest = "src" + File.separator + "main" + 
				File.separator + "java" + File.separator + 
				"ADLListIntegerCloneTest.java";
		
		String[] baseFiles = {
				SRC_FOLDER + AED_FOLDER + File.separator + "Base" +
				File.separator + adlList,
				null,
				null
		};
		
		String[] variants1Files = {
				SRC_FOLDER + AED_FOLDER + File.separator + "Branch1" +
				File.separator + adlList,
				null,
				null
		};
		
		String[] variants2Files = {
				SRC_FOLDER + AED_FOLDER + File.separator + "Branch2" +
				File.separator + adlList,
				SRC_FOLDER + AED_FOLDER + File.separator + "Branch2" +
						File.separator + cloneBuilderTest,
				SRC_FOLDER + AED_FOLDER + File.separator + "Branch2" +
						File.separator + cloneIntTest
		};
		
		System.out.println(
				matcher.matchingAssignments(baseFiles, variants1Files, variants2Files));
		
	}
	
	@Test
	public void aedSecondPhaseDependencyBasedTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER + AED_FOLDER_2 + "config.properties");
		
		
		String ptt = "src" + File.separator + "PTTStringsMap.java";
		
		String client = "src" + File.separator + "Client.java";
		
		String[] baseFiles = {
				SRC_FOLDER + AED_FOLDER_2 + File.separator + "Base" +
						File.separator + ptt,
				null
		};
		
		String[] variants1Files = {
				SRC_FOLDER + AED_FOLDER_2 + File.separator + "Branch1" +
						File.separator + ptt,
				null
		};
		
		String[] variants2Files = {
				SRC_FOLDER + AED_FOLDER_2 + File.separator + "Branch2" +
						File.separator + ptt,
				SRC_FOLDER + AED_FOLDER_2 + File.separator + "Branch2" +
						File.separator + client
		};
		
		System.out.println(
				matcher.matchingAssignments(baseFiles, variants1Files, variants2Files));
	}
	
	@Test
	public void ipDependencyBasedTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER + IP_FOLDER + "config.properties");
		
		String puzzle = "Puzzle.java";
		
		String test = "WordSearchTest.java";
		
		String[] baseFiles = {
				SRC_FOLDER + IP_FOLDER + File.separator + "Base" +
						File.separator + puzzle,
				null
		};
		
		String[] variants1Files = {
				SRC_FOLDER + IP_FOLDER + File.separator + "Branch1" +
						File.separator + puzzle,
				SRC_FOLDER + IP_FOLDER + File.separator + "Branch1" +
								File.separator + test
		};
		
		String[] variants2Files = {
				SRC_FOLDER + IP_FOLDER + File.separator + "Branch2" +
						File.separator + puzzle,
				null
		};
		
		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(baseFiles, variants1Files, variants2Files);
		
		assertEquals(3, result.size(), "Not 3 results for dependency based?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(4, assignments.size(), "Not 4 assignments with only 4 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("Puzzle", assignments.get(0).getSecond(), 
					"First class is not Puzzle?");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("WordSearchTest", assignments.get(1).getSecond(), 
					"Second class is not WordSearchTest?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("getWord(Move)", 
				assignments.get(2).getSecond(), 
				"Updated method is not getWord(Move)?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("testPuzzle()", 
				assignments.get(3).getSecond(), 
				"Dependant is not testPuzzle()?");
		
		assignments = result.get(1);
		
		assertEquals(4, assignments.size(), "Not 4 assignments with only 4 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("Puzzle", assignments.get(0).getSecond(), 
					"First class is not Puzzle?");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("WordSearchTest", assignments.get(1).getSecond(), 
					"Second class is not WordSearchTest?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("getWord(Move)", 
				assignments.get(2).getSecond(), 
				"Updated method is not getWord(Move)?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("testWordSearch()", 
				assignments.get(3).getSecond(), 
				"Dependant is not testWordSearch()?");
		
		assignments = result.get(2);
		
		assertEquals(4, assignments.size(), "Not 4 assignments with only 4 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("Puzzle", assignments.get(0).getSecond(), 
					"First class is not Puzzle?");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("WordSearchTest", assignments.get(1).getSecond(), 
					"Second class is not WordSearchTest?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("isHidden(char[][], java.lang.String)", 
				assignments.get(2).getSecond(), 
				"Updated method is not isHidden(char[][], java.lang.String)?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("testPuzzle()", 
				assignments.get(3).getSecond(), 
				"Dependant is not testPuzzle()?");
	}
	
	@Test
	public void ipLeixelTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER + LEIXEL_FOLDER + "config.properties");
		
		String leixel = "src" + File.separator + "pt" + File.separator + "ul" + 
				File.separator + "fc" + File.separator + "di" + File.separator + 
				"dco000" + File.separator + "domain" + File.separator + 
				"Leixcel.java";
		
		String catalog = "src" + File.separator + "pt" + File.separator + "ul" + 
				File.separator + "fc" + File.separator + "di" + File.separator + 
				"dco000" + File.separator + "domain" + File.separator + "formats" + 
				File.separator + "FormatCatalog.java";
		
		String[] baseFiles = {
				SRC_FOLDER + LEIXEL_FOLDER + File.separator + "Base" +
						File.separator + leixel,
				SRC_FOLDER + LEIXEL_FOLDER + File.separator + "Base" +
						File.separator + catalog
		};
		
		String[] variants1Files = {
				SRC_FOLDER + LEIXEL_FOLDER + File.separator + "Branch1" +
						File.separator + leixel,
				SRC_FOLDER + LEIXEL_FOLDER + File.separator + "Branch1" +
						File.separator + catalog
		};
		
		String[] variants2Files = {
				SRC_FOLDER + LEIXEL_FOLDER + File.separator + "Branch2" +
						File.separator + leixel,
				SRC_FOLDER + LEIXEL_FOLDER + File.separator + "Branch2" +
						File.separator + catalog
		};
		
		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(baseFiles, variants1Files, variants2Files);

		assertEquals(2, result.size(), "Not 2 result for leixel?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(4, assignments.size(), "Not 4 assignments with only 4 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("pt.ul.fc.di.dco000.domain.Leixcel", assignments.get(0).getSecond(), 
					"First class is not pt.ul.fc.di.dco000.domain.Leixcel?");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("pt.ul.fc.di.dco000.domain.formats.FormatCatalog", assignments.get(1).getSecond(), 
					"Second class is not pt.ul.fc.di.dco000.domain.formats.FormatCatalog?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("loadFormats()", 
				assignments.get(2).getSecond(), 
				"Updated method is not loadFormats()?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("put(pt.ul.fc.di.dco000.domain.formats.Format)", 
				assignments.get(3).getSecond(), 
				"Updated dependency is not put(pt.ul.fc.di.dco000.domain.formats.Format)?");
		
		assignments = result.get(1);
		
		assertEquals(4, assignments.size(), "Not 4 assignments with only 4 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("pt.ul.fc.di.dco000.domain.formats.FormatCatalog", assignments.get(0).getSecond(), 
					"First class is not pt.ul.fc.di.dco000.domain.formats.FormatCatalog?");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("pt.ul.fc.di.dco000.domain.Leixcel", assignments.get(1).getSecond(), 
					"Second class is not pt.ul.fc.di.dco000.domain.Leixcel?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("put(pt.ul.fc.di.dco000.domain.formats.Format)", 
				assignments.get(2).getSecond(), 
				"Updated dependency is not put(pt.ul.fc.di.dco000.domain.formats.Format)?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("loadFormats()", 
				assignments.get(3).getSecond(), 
				"Updated method is not loadFormats()?");
		
		
	}
	
	@Test
	public void cssFieldHidingTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER + CSS_FIELD_HIDING + "config.properties");
		
		String instalacaoAssentos = "src" + File.separator + "main" + File.separator + "java" + 
				File.separator + "business" + File.separator + 
				"InstalacaoAssentos.java";
		
		String[] baseFiles = {
				SRC_FOLDER + CSS_FIELD_HIDING + File.separator + "base" +
						File.separator + instalacaoAssentos
		};
		
		String[] variants1Files = {
				SRC_FOLDER + CSS_FIELD_HIDING + File.separator + "branch01" +
						File.separator + instalacaoAssentos
		};
		
		String[] variants2Files = {
				SRC_FOLDER + CSS_FIELD_HIDING + File.separator + "branch02" +
						File.separator + instalacaoAssentos
		};
		
		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(baseFiles, variants1Files, variants2Files, 
						"Field Hiding");
		
		assertEquals(1, result.size(), "Not one result for field hiding?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(4, assignments.size(), "Not 4 assignments with only 4 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("business.Instalacao", assignments.get(0).getSecond(), 
					"First class is not business.Instalacao?");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("business.InstalacaoAssentos", assignments.get(1).getSecond(), 
					"Second class is not business.InstalacaoAssentos?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("setFirstAndLastName(java.lang.String, java.lang.String)", 
				assignments.get(2).getSecond(), 
				"Method that uses field is not "
				+ "setFirstAndLastName(java.lang.String, java.lang.String)?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("nome", assignments.get(3).getSecond(), 
				"Field is not nome?");
	}
	
	@Test
	public void cssRemoveOverridingTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER + CSS_REMOVE_OVERRIDING + "config.properties");
		
		String instalacaoAssentos = "src" + File.separator + "main" + File.separator + "java" + 
				File.separator + "business" + File.separator + 
				"InstalacaoAssentos.java";
		
		String instalacao = "src" + File.separator + "main" + File.separator + "java" + 
				File.separator + "business" + File.separator + 
				"Instalacao.java";
		
		String[] baseFiles = {
				SRC_FOLDER + CSS_REMOVE_OVERRIDING + File.separator + "base" +
						File.separator + instalacaoAssentos,
				SRC_FOLDER + CSS_REMOVE_OVERRIDING + File.separator + "base" +
						File.separator + instalacao
		};
		
		String[] variants1Files = {
				SRC_FOLDER + CSS_REMOVE_OVERRIDING + File.separator + "branch01" +
						File.separator + instalacaoAssentos,
				SRC_FOLDER + CSS_REMOVE_OVERRIDING + File.separator + "branch01" +
							File.separator + instalacao
		};
		
		String[] variants2Files = {
				SRC_FOLDER + CSS_REMOVE_OVERRIDING + File.separator + "branch02" +
						File.separator + instalacaoAssentos,
				SRC_FOLDER + CSS_REMOVE_OVERRIDING + File.separator + "branch02" +
							File.separator + instalacao
		};
		
		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(baseFiles, variants1Files, variants2Files);

		assertEquals(2, result.size(), "Not one result for remove overriding?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(4, assignments.size(), "Not 4 assignments with only 4 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("business.Instalacao", assignments.get(0).getSecond(), 
					"First class is not business.Instalacao?");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("business.InstalacaoAssentos", assignments.get(1).getSecond(), 
					"Second class is not business.InstalacaoAssentos?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("helpString()", 
				assignments.get(2).getSecond(), 
				"Method with dependency is not helpString()?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("hashString()", assignments.get(3).getSecond(), 
				"Method not overwritten method is not hashString()?");
		
	}
	
	@Test
	public void cssAddOverridingTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER + CSS_ADD_OVERRIDING + "config.properties");
		
		String singleTicket = "src" + File.separator + "main" + File.separator + "java" + 
				File.separator + "business" + File.separator + "ticket" + File.separator +
				"SingleTicket.java";
		
		String[] baseFiles = {
				SRC_FOLDER + CSS_ADD_OVERRIDING + File.separator + "base" +
						File.separator + singleTicket
		};
		
		String[] variants1Files = {
				SRC_FOLDER + CSS_ADD_OVERRIDING + File.separator + "branch01" +
						File.separator + singleTicket
		};
		
		String[] variants2Files = {
				SRC_FOLDER + CSS_ADD_OVERRIDING + File.separator + "branch02" +
						File.separator + singleTicket
		};
		
		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(baseFiles, variants1Files, variants2Files);

		assertEquals(1, result.size(), "Not one result for add overriding?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(5, assignments.size(), "Not 5 assignments with only 5 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("business.ticket.Ticket", assignments.get(0).getSecond(), 
					"First class is not business.ticket.Ticket?");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("business.ticket.SingleTicket", assignments.get(1).getSecond(), 
					"Second class is not business.ticket.SingleTicket?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("business.ticket.SingleTicket", 
				assignments.get(2).getSecond(), 
				"Class with the dependant method is not business.ticket.SingleTicket?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("getValue()", assignments.get(3).getSecond(), 
				"Overwritten method is not getValue()?");
		
		assertEquals(4, assignments.get(4).getFirst(), "Variable id is not 4?"); 
		assertEquals("doubleValue()", assignments.get(4).getSecond(), 
				"Dependant method is not doubleValue()?");
	}
	
	@Test
	public void cssAccessChangeTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER + CSS_ACCESS_CHANGE + "config.properties");
		
		String event = "src" + File.separator + "main" + File.separator + "java" + 
				File.separator + "business" + File.separator + "event" + File.separator +
				"Event.java";
		
		String producer = "src" + File.separator + "main" + File.separator + "java" + 
				File.separator + "business" + File.separator + "producer" + File.separator +
				"Producer.java";
		
		String[] baseFiles = {
				SRC_FOLDER + CSS_ACCESS_CHANGE + File.separator + "base" +
						File.separator + event,
				SRC_FOLDER + CSS_ACCESS_CHANGE + File.separator + "base" +
						File.separator + producer
		};
		
		String[] variants1Files = {
				SRC_FOLDER + CSS_ACCESS_CHANGE + File.separator + "branch01" +
						File.separator + event,
				SRC_FOLDER + CSS_ACCESS_CHANGE + File.separator + "branch01" +
						File.separator + producer
		};
		
		String[] variants2Files = {
				SRC_FOLDER + CSS_ACCESS_CHANGE + File.separator + "branch02" +
						File.separator + event,
				SRC_FOLDER + CSS_ACCESS_CHANGE + File.separator + "branch02" +
						File.separator + producer
		};
		
		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(baseFiles, variants1Files, variants2Files);

		assertEquals(1, result.size(), "Not one result for access change?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(5, assignments.size(), "Not 5 assignments with only 5 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("business.producer.Producer", assignments.get(0).getSecond(), 
					"First class is not business.producer.Producer?");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("business.event.Event", assignments.get(1).getSecond(), 
					"Second class is not business.event.Event?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("setResgistrationNumber(java.lang.Integer)", 
				assignments.get(2).getSecond(), 
				"Method with access changed is not setResgistrationNumber(java.lang.Integer)?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("setResgistrationNumber(int)", assignments.get(3).getSecond(), 
				"Overloading method is not setResgistrationNumber(int)?");
		
		assertEquals(4, assignments.get(4).getFirst(), "Variable id is not 4?"); 
		assertEquals("setProducerNumber(int)", assignments.get(4).getSecond(), 
				"Dependant method is not setProducerNumber(int)?");
	}
	
	@Test
	public void cssOverloadByAdditionTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER + CSS_OVERLOAD_BY_ADDITION + "config.properties");
		
		String event = "src" + File.separator + "main" + File.separator + "java" + 
				File.separator + "business" + File.separator + "event" + File.separator +
				"Event.java";
		
		String producer = "src" + File.separator + "main" + File.separator + "java" + 
				File.separator + "business" + File.separator + "producer" + File.separator +
				"Producer.java";
		
		String[] baseFiles = {
				SRC_FOLDER + CSS_OVERLOAD_BY_ADDITION + File.separator + "base" +
						File.separator + event,
				SRC_FOLDER + CSS_OVERLOAD_BY_ADDITION + File.separator + "base" +
						File.separator + producer
		};
		
		String[] variants1Files = {
				SRC_FOLDER + CSS_OVERLOAD_BY_ADDITION + File.separator + "branch01" +
						File.separator + event,
				SRC_FOLDER + CSS_OVERLOAD_BY_ADDITION + File.separator + "branch01" +
						File.separator + producer
		};
		
		String[] variants2Files = {
				SRC_FOLDER + CSS_OVERLOAD_BY_ADDITION + File.separator + "branch02" +
						File.separator + event,
				SRC_FOLDER + CSS_OVERLOAD_BY_ADDITION + File.separator + "branch02" +
						File.separator + producer
		};
		
		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(baseFiles, variants1Files, variants2Files);

		assertEquals(1, result.size(), "Not one result for overload by addition?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(5, assignments.size(), "Not 5 assignments with only 5 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("business.producer.Producer", assignments.get(0).getSecond(), 
					"First class is not business.producer.Producer?");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("business.event.Event", assignments.get(1).getSecond(), 
					"Second class is not business.event.Event?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("setResgistrationNumber(java.lang.Integer)", 
				assignments.get(2).getSecond(), 
				"Overloaded method is not setResgistrationNumber(java.lang.Integer)?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("setProducerNumber(int)", assignments.get(3).getSecond(), 
				"Overloading method is not setProducerNumber(int)?");
		
		assertEquals(4, assignments.get(4).getFirst(), "Variable id is not 4?"); 
		assertEquals("setResgistrationNumber(int)", assignments.get(4).getSecond(), 
				"Dependant method is not setResgistrationNumber(int)?");
	}
	
	@Test
	public void cssUnexpectedOverridingTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER + CSS_UNEXPECTED_OVERRIDING + "config.properties");
		
		String ticket = "src" + File.separator + "main" + File.separator + "java" + 
				File.separator + "business" + File.separator + "ticket" + File.separator +
				"Ticket.java";
		
		String reservation = "src" + File.separator + "main" + File.separator + "java" + 
				File.separator + "business" + File.separator + "ticket" + File.separator +
				"Reservation.java";
		
		String[] baseFiles = {
				SRC_FOLDER + CSS_UNEXPECTED_OVERRIDING + File.separator + "base" +
						File.separator + ticket,
				SRC_FOLDER + CSS_UNEXPECTED_OVERRIDING + File.separator + "base" +
						File.separator + reservation
		};
		
		String[] variants1Files = {
				SRC_FOLDER + CSS_UNEXPECTED_OVERRIDING + File.separator + "branch01" +
						File.separator + ticket,
				SRC_FOLDER + CSS_UNEXPECTED_OVERRIDING + File.separator + "branch01" +
						File.separator + reservation
		};
		
		String[] variants2Files = {
				SRC_FOLDER + CSS_UNEXPECTED_OVERRIDING + File.separator + "branch02" +
						File.separator + ticket,
				SRC_FOLDER + CSS_UNEXPECTED_OVERRIDING + File.separator + "branch02" +
						File.separator + reservation
		};
		
		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(baseFiles, variants1Files, variants2Files);

		assertEquals(1, result.size(), "Not one result for unexpected overriding?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(4, assignments.size(), "Not 4 assignments with only 4 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("business.ticket.Ticket", assignments.get(0).getSecond(), 
					"First class is not business.ticket.Ticket?");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("business.ticket.Reservation", assignments.get(1).getSecond(), 
					"Second class is not business.ticket.Reservation?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("resHashCode()", 
				assignments.get(2).getSecond(), 
				"Method with dependency is not resHashCode()?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("hashCode()", assignments.get(3).getSecond(), 
				"Overwritten method is not hashCode()?");
	}
	
	@Test
	public void cssNoConflictTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER + CSS_NO_CONFLICT + "config.properties");
		
		String seat = "src" + File.separator + "main" + File.separator + "java" + 
				File.separator + "business" + File.separator + "event" + File.separator +
				"Seat.java";
		
		String handler = "src" + File.separator + "main" + File.separator + "java" + 
				File.separator + "business" + File.separator + "handlers" + File.separator +
				"CreateEventHandler.java";
		
		String producer = "src" + File.separator + "main" + File.separator + "java" + 
				File.separator + "business" + File.separator + "Producer" + File.separator +
				"Producer.java";
		
		String coupleTicket = "src" + File.separator + "main" + File.separator + "java" + 
				File.separator + "business" + File.separator + "ticket" + File.separator +
				"CouplesTicket.java";
		
		String familyTicket = "src" + File.separator + "main" + File.separator + "java" + 
				File.separator + "business" + File.separator + "ticket" + File.separator +
				"FamilyTicket.java";
		
		String[] baseFiles = {
				SRC_FOLDER + CSS_NO_CONFLICT + File.separator + "base" +
						File.separator + seat,
				SRC_FOLDER + CSS_NO_CONFLICT + File.separator + "base" +
						File.separator + handler,
				SRC_FOLDER + CSS_NO_CONFLICT + File.separator + "base" +
						File.separator + producer,
				null,
				null
		};
		
		String[] variants1Files = {
				SRC_FOLDER + CSS_NO_CONFLICT + File.separator + "branch01" +
						File.separator + seat,
				SRC_FOLDER + CSS_NO_CONFLICT + File.separator + "branch01" +
						File.separator + handler,
				SRC_FOLDER + CSS_NO_CONFLICT + File.separator + "branch01" +
						File.separator + producer,
				null,
				null
		};
		
		String[] variants2Files = {
				SRC_FOLDER + CSS_NO_CONFLICT + File.separator + "branch02" +
						File.separator + seat,
				SRC_FOLDER + CSS_NO_CONFLICT + File.separator + "branch02" +
						File.separator + handler,
				SRC_FOLDER + CSS_NO_CONFLICT + File.separator + "branch02" +
						File.separator + producer,
				SRC_FOLDER + CSS_NO_CONFLICT + File.separator + "branch02" +
						File.separator + coupleTicket,
				SRC_FOLDER + CSS_NO_CONFLICT + File.separator + "branch02" +
						File.separator + familyTicket
		};
		
		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(baseFiles, variants1Files, variants2Files);
		
		assertEquals(0, result.size(), "Found a conflict when there is none?");
	}
}
