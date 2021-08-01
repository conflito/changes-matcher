package matcher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;

import matcher.utils.Pair;

public class TestFabricatedInstances extends AbstractTestMatcher {

	private final static Logger logger = Logger.getLogger(TestFabricatedInstances.class);

	private static final String FABRICATED_INSTANCES_DIR_PATH = buildPath(RELATIVE_TEST_RESOURCES_DIR_PATH, "FabricatedInstances");

	@Test
	public void aedDependencyBasedTest() throws Exception {
		Matcher matcher = new Matcher(buildPath(FABRICATED_INSTANCES_DIR_PATH, "aed1617", CONFIG_FILE_NAME));

		String adlList = buildPath(RELATIVE_SRC_DIR_PATH, "datatypes", "ADLList.java");
		String cloneBuilderTest = buildPath(RELATIVE_SRC_DIR_PATH, "ADLListStringBuilderCloneTest.java");
		String cloneIntTest = buildPath(RELATIVE_SRC_DIR_PATH, "ADLListIntegerCloneTest.java");

		String[] baseFiles = {
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "aed1617", BASE_BRANCH_NAME, adlList),
			null,
			null
		};
		assertTrue(checkIfFilesExist(baseFiles));

		String[] variants1Files = {
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "aed1617", BRANCH_1_NAME, adlList),
			null,
			null
		};
		assertTrue(checkIfFilesExist(variants1Files));

		String[] variants2Files = {
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "aed1617", BRANCH_2_NAME, adlList),
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "aed1617", BRANCH_2_NAME, cloneBuilderTest),
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "aed1617", BRANCH_2_NAME, cloneIntTest)
		};
		assertTrue(checkIfFilesExist(variants2Files));

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(baseFiles, variants1Files, 
						variants2Files, "Dependency Based (new class)");

		assertEquals(5, result.size(), "Not fives result for aed project?");

		List<Pair<Integer,String>> assignments = result.get(4);
		assertEquals(4, assignments.size(), "Not 4 assignments with only 4 variables?");

		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("datatypes.ADLList", assignments.get(0).getSecond(), 
				"Class with updated method is not datatypes.ADLList");

		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("ADLListStringBuilderCloneTest", assignments.get(1).getSecond(), 
				"New class is not ADLListStringBuilderCloneTest");

		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("copyNodes(datatypes.ADLList)", 
				assignments.get(2).getSecond(), 
				"Method updated is not copyNodes(datatypes.ADLList)?");

		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("testChangeOriginal(datatypes.ADLList)", assignments.get(3).getSecond(), 
				"Method depends is not testChangeOriginal(datatypes.ADLList)?");
	}

	@Test
	public void aedSecondPhaseDependencyBasedTest() throws Exception {	
		Matcher matcher = new Matcher(buildPath(FABRICATED_INSTANCES_DIR_PATH, "aed1617_fase2", CONFIG_FILE_NAME));

		String ptt = buildPath("src", "PTTStringsMap.java");
		String client = buildPath("src", "Client.java");

		String[] baseFiles = {
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "aed1617_fase2", BASE_BRANCH_NAME, ptt),
			null
		};
		assertTrue(checkIfFilesExist(baseFiles));

		String[] variants1Files = {
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "aed1617_fase2", BRANCH_1_NAME, ptt),
			null
		};
		assertTrue(checkIfFilesExist(variants1Files));

		String[] variants2Files = {
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "aed1617_fase2", BRANCH_2_NAME, ptt),
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "aed1617_fase2", BRANCH_2_NAME, client)
		};
		assertTrue(checkIfFilesExist(variants2Files));

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(baseFiles, variants1Files, 
						variants2Files, "Dependency Based (new class)");

		assertEquals(3, result.size(), "Not three result for second aed project?");

		List<Pair<Integer,String>> assignments = result.get(2);
		assertEquals(4, assignments.size(), "Not 4 assignments with only 4 variables?");

		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("PTTStringsMap", assignments.get(0).getSecond(), 
				"Class with updated method is not PTTStringsMap");

		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("Client", assignments.get(1).getSecond(), 
				"New class is not Client");

		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("copiaNodes(Node, Node)", 
				assignments.get(2).getSecond(), 
				"Method updated is not copiaNodes(Node, Node)?");

		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("testCloneChangeOriginal()", assignments.get(3).getSecond(), 
				"Method depends is not testCloneChangeOriginal()?");
	}

	@Test
	public void ipDependencyBasedTest() throws Exception {
		Matcher matcher = new Matcher(buildPath(FABRICATED_INSTANCES_DIR_PATH, "ip2021", CONFIG_FILE_NAME));

		String puzzle = "Puzzle.java";
		String test = "WordSearchTest.java";

		String[] baseFiles = {
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "ip2021", BASE_BRANCH_NAME, puzzle),
			null
		};
		assertTrue(checkIfFilesExist(baseFiles));

		String[] variants1Files = {
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "ip2021", BRANCH_1_NAME, puzzle),
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "ip2021", BRANCH_1_NAME, test)
		};
		assertTrue(checkIfFilesExist(variants1Files));

		String[] variants2Files = {
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "ip2021", BRANCH_2_NAME, puzzle),
			null
		};
		assertTrue(checkIfFilesExist(variants2Files));

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(baseFiles, variants1Files, 
						variants2Files, "Dependency Based (new class)");

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
	public void dcoLEIXCEL_11_12Test() throws Exception {
		Matcher matcher = new Matcher(buildPath(FABRICATED_INSTANCES_DIR_PATH, "leixcel1112", CONFIG_FILE_NAME));

		String leixcel = buildPath("src", "pt", "ul", "fc", "di", "dco000", "domain", "Leixcel.java");
		String catalog = buildPath("src", "pt", "ul", "fc", "di", "dco000", "domain", "formats", "FormatCatalog.java");

		String[] baseFiles = {
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "leixcel1112", BASE_BRANCH_NAME, leixcel),
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "leixcel1112", BASE_BRANCH_NAME, catalog)
		};
		assertTrue(checkIfFilesExist(baseFiles));

		String[] variants1Files = {
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "leixcel1112", BRANCH_1_NAME, leixcel),
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "leixcel1112", BRANCH_1_NAME, catalog)
		};
		assertTrue(checkIfFilesExist(variants1Files));

		String[] variants2Files = {
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "leixcel1112", BRANCH_2_NAME, leixcel),
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "leixcel1112", BRANCH_2_NAME, catalog)
		};
		assertTrue(checkIfFilesExist(variants2Files));

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(baseFiles, variants1Files, variants2Files, "Change Method 3");

		assertEquals(1, result.size(), "Not 1 result for LEIXCEL_11_12?");

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
	}

	@Test
	public void cssFieldHidingTest() throws Exception {
		Matcher matcher = new Matcher(buildPath(FABRICATED_INSTANCES_DIR_PATH, "css2021_projeto1_047", CONFIG_FILE_NAME));

		String instalacaoAssentos = buildPath(RELATIVE_SRC_DIR_PATH, "business", "InstalacaoAssentos.java");

		String[] baseFiles = {
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css2021_projeto1_047", BASE_BRANCH_NAME, instalacaoAssentos)
		};
		assertTrue(checkIfFilesExist(baseFiles));

		String[] variants1Files = {
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css2021_projeto1_047", BRANCH_1_NAME, instalacaoAssentos)
		};
		assertTrue(checkIfFilesExist(variants1Files));

		String[] variants2Files = {
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css2021_projeto1_047", BRANCH_2_NAME, instalacaoAssentos)
		};
		assertTrue(checkIfFilesExist(variants2Files));

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(baseFiles, variants1Files, 
						variants2Files, "Field Hiding");

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
	public void cssRemoveOverridingTest() throws Exception {
		Matcher matcher = new Matcher(buildPath(FABRICATED_INSTANCES_DIR_PATH, "css2021_projeto1_047_RO", CONFIG_FILE_NAME));

		String instalacaoAssentos = buildPath(RELATIVE_SRC_DIR_PATH, "business", "InstalacaoAssentos.java");

		String[] baseFiles = {
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css2021_projeto1_047_RO", BASE_BRANCH_NAME, instalacaoAssentos)
		};
		assertTrue(checkIfFilesExist(baseFiles));

		String[] variants1Files = {
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css2021_projeto1_047_RO", BRANCH_1_NAME, instalacaoAssentos)
		};
		assertTrue(checkIfFilesExist(variants1Files));

		String[] variants2Files = {
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css2021_projeto1_047_RO", BRANCH_2_NAME, instalacaoAssentos)
		};
		assertTrue(checkIfFilesExist(variants2Files));

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(baseFiles, variants1Files, 
						variants2Files, "Remove Overriding");

		assertEquals(1, result.size(), "Not one result for remove overriding?");

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
	public void cssAddOverridingTest() throws Exception {
		Matcher matcher = new Matcher(buildPath(FABRICATED_INSTANCES_DIR_PATH, "css2021_projeto1_009", CONFIG_FILE_NAME));

		String singleTicket = buildPath(RELATIVE_SRC_DIR_PATH, "business", "ticket", "SingleTicket.java");

		String[] baseFiles = {
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css2021_projeto1_009", BASE_BRANCH_NAME, singleTicket)
		};
		assertTrue(checkIfFilesExist(baseFiles));

		String[] variants1Files = {
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css2021_projeto1_009", BRANCH_1_NAME, singleTicket)
		};
		assertTrue(checkIfFilesExist(variants1Files));

		String[] variants2Files = {
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css2021_projeto1_009", BRANCH_2_NAME, singleTicket)
		};
		assertTrue(checkIfFilesExist(variants2Files));

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(baseFiles, variants1Files, 
						variants2Files, "Add Overriding (new method)");

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
	public void cssAccessChangeTest() throws Exception {
		Matcher matcher = new Matcher(buildPath(FABRICATED_INSTANCES_DIR_PATH, "css2021_projeto1_009_AC", CONFIG_FILE_NAME));

		String event = buildPath(RELATIVE_SRC_DIR_PATH, "business", "event", "Event.java");
		String producer = buildPath(RELATIVE_SRC_DIR_PATH, "business", "producer", "Producer.java");

		String[] baseFiles = {
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css2021_projeto1_009_AC", BASE_BRANCH_NAME, event),
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css2021_projeto1_009_AC", BASE_BRANCH_NAME, producer)
		};
		assertTrue(checkIfFilesExist(baseFiles));

		String[] variants1Files = {
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css2021_projeto1_009_AC", BRANCH_1_NAME, event),
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css2021_projeto1_009_AC", BRANCH_1_NAME, producer)
		};
		assertTrue(checkIfFilesExist(variants1Files));

		String[] variants2Files = {
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css2021_projeto1_009_AC", BRANCH_2_NAME, event),
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css2021_projeto1_009_AC", BRANCH_2_NAME, producer)
		};
		assertTrue(checkIfFilesExist(variants2Files));

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(baseFiles, variants1Files, 
						variants2Files, "Overload by Access Change 2 (new method)");

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
	public void cssOverloadByAdditionTest() throws Exception {
		Matcher matcher = new Matcher(buildPath(FABRICATED_INSTANCES_DIR_PATH, "css2021_projeto1_009_OA", CONFIG_FILE_NAME));

		String event = buildPath(RELATIVE_SRC_DIR_PATH, "business", "event", "Event.java");
		String producer = buildPath(RELATIVE_SRC_DIR_PATH, "business", "producer", "Producer.java");

		String[] baseFiles = {
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css2021_projeto1_009_OA", BASE_BRANCH_NAME, event),
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css2021_projeto1_009_OA", BASE_BRANCH_NAME, producer)
		};
		assertTrue(checkIfFilesExist(baseFiles));

		String[] variants1Files = {
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css2021_projeto1_009_OA", BRANCH_1_NAME, event),
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css2021_projeto1_009_OA", BRANCH_1_NAME, producer)
		};
		assertTrue(checkIfFilesExist(variants1Files));

		String[] variants2Files = {
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css2021_projeto1_009_OA", BRANCH_2_NAME, event),
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css2021_projeto1_009_OA", BRANCH_2_NAME, producer)
		};
		assertTrue(checkIfFilesExist(variants2Files));

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(baseFiles, variants1Files, 
						variants2Files, "Overload by Addition (new method)");

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
	public void cssUnexpectedOverridingTest() throws Exception {
		Matcher matcher = new Matcher(buildPath(FABRICATED_INSTANCES_DIR_PATH, "css2021_projeto1_009_UN", CONFIG_FILE_NAME));

		String ticket = buildPath(RELATIVE_SRC_DIR_PATH, "business", "ticket", "Ticket.java");
		String reservation = buildPath(RELATIVE_SRC_DIR_PATH, "business", "ticket", "Reservation.java");

		String[] baseFiles = {
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css2021_projeto1_009_UN", BASE_BRANCH_NAME, ticket),
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css2021_projeto1_009_UN", BASE_BRANCH_NAME, reservation)
		};
		assertTrue(checkIfFilesExist(baseFiles));

		String[] variants1Files = {
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css2021_projeto1_009_UN", BRANCH_1_NAME, ticket),
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css2021_projeto1_009_UN", BRANCH_1_NAME, reservation)
		};
		assertTrue(checkIfFilesExist(variants1Files));

		String[] variants2Files = {
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css2021_projeto1_009_UN", BRANCH_2_NAME, ticket),
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css2021_projeto1_009_UN", BRANCH_2_NAME, reservation)
		};
		assertTrue(checkIfFilesExist(variants2Files));

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(baseFiles, variants1Files, 
						variants2Files, "Unexpected Overriding (new method)");

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
	public void cssNoConflictTest() throws Exception {
		Matcher matcher = new Matcher(buildPath(FABRICATED_INSTANCES_DIR_PATH, "css2021_projeto1_009_no_conflict", CONFIG_FILE_NAME));

		String seat = buildPath(RELATIVE_SRC_DIR_PATH, "business", "event", "Seat.java");
		String handler = buildPath(RELATIVE_SRC_DIR_PATH, "business", "handlers", "CreateEventHandler.java");
		String producer = buildPath(RELATIVE_SRC_DIR_PATH, "business", "producer", "Producer.java");
		String coupleTicket = buildPath(RELATIVE_SRC_DIR_PATH, "business", "ticket", "CouplesTicket.java");
		String familyTicket = buildPath(RELATIVE_SRC_DIR_PATH, "business", "ticket", "FamilyTicket.java");

		String[] baseFiles = {
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css2021_projeto1_009_no_conflict", BASE_BRANCH_NAME, seat),
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css2021_projeto1_009_no_conflict", BASE_BRANCH_NAME, handler),
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css2021_projeto1_009_no_conflict", BASE_BRANCH_NAME, producer),
			null,
			null
		};
		assertTrue(checkIfFilesExist(baseFiles));

		String[] variants1Files = {
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css2021_projeto1_009_no_conflict", BRANCH_1_NAME, seat),
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css2021_projeto1_009_no_conflict", BRANCH_1_NAME, handler),
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css2021_projeto1_009_no_conflict", BRANCH_1_NAME, producer),
			null,
			null
		};
		assertTrue(checkIfFilesExist(variants1Files));

		String[] variants2Files = {
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css2021_projeto1_009_no_conflict", BRANCH_2_NAME, seat),
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css2021_projeto1_009_no_conflict", BRANCH_2_NAME, handler),
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css2021_projeto1_009_no_conflict", BRANCH_2_NAME, producer),
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css2021_projeto1_009_no_conflict", BRANCH_2_NAME, coupleTicket),
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css2021_projeto1_009_no_conflict", BRANCH_2_NAME, familyTicket)
		};
		assertTrue(checkIfFilesExist(variants2Files));

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(baseFiles, variants1Files, variants2Files);
		assertEquals(0, result.size(), "Found a conflict when there is none?");
	}

	@Test
	public void cssNoConflictTest2() throws Exception {
		Matcher matcher = new Matcher(buildPath(FABRICATED_INSTANCES_DIR_PATH, "css_047_no_conflict", CONFIG_FILE_NAME));

		String client = buildPath(RELATIVE_SRC_DIR_PATH, "client", "SimpleClient.java");
		String lugarDTO = buildPath(RELATIVE_SRC_DIR_PATH, "facade", "dto", "LugarDTO.java");

		String[] baseFiles = {
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css_047_no_conflict", BASE_BRANCH_NAME, "BilGes", client),
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css_047_no_conflict", BASE_BRANCH_NAME, "BilGes", lugarDTO),
		};
		assertTrue(checkIfFilesExist(baseFiles));

		String[] variants1Files = {
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css_047_no_conflict", BRANCH_1_NAME, "BilGes", client),
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css_047_no_conflict", BRANCH_1_NAME, "BilGes", lugarDTO),
		};
		assertTrue(checkIfFilesExist(variants1Files));

		String[] variants2Files = {
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css_047_no_conflict", BRANCH_2_NAME, "BilGes", client),
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css_047_no_conflict", BRANCH_2_NAME, "BilGes", lugarDTO),
		};
		assertTrue(checkIfFilesExist(variants2Files));

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(baseFiles, variants1Files, variants2Files);
		assertEquals(0, result.size(), "Found a conflict when there is none?");
	}

	@Test
	public void cssNoConflictTest3() throws Exception {
		Matcher matcher = new Matcher(buildPath(FABRICATED_INSTANCES_DIR_PATH, "css_009_no_conflict_2", CONFIG_FILE_NAME));

		String eventCatalog = buildPath(RELATIVE_SRC_DIR_PATH, "business", "catalogs", "EventCatalog.java");
		String ticketCatalog = buildPath(RELATIVE_SRC_DIR_PATH, "business", "catalogs", "TicketCatalog.java");
		String customer = buildPath(RELATIVE_SRC_DIR_PATH, "business", "entities", "Customer.java");
		String event = buildPath(RELATIVE_SRC_DIR_PATH, "business", "entities", "Event.java");
		String ticket = buildPath(RELATIVE_SRC_DIR_PATH, "business", "entities", "Ticket.java");

		String[] baseFiles = {
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css_009_no_conflict_2", BASE_BRANCH_NAME, eventCatalog),
			null,
			null,
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css_009_no_conflict_2", BASE_BRANCH_NAME, event),
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css_009_no_conflict_2", BASE_BRANCH_NAME, ticket)
		};
		assertTrue(checkIfFilesExist(baseFiles));

		String[] variants1Files = {
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css_009_no_conflict_2", BRANCH_1_NAME, eventCatalog),
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css_009_no_conflict_2", BRANCH_1_NAME, ticketCatalog),
			null,
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css_009_no_conflict_2", BRANCH_1_NAME, event),
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css_009_no_conflict_2", BRANCH_1_NAME, ticket)
		};
		assertTrue(checkIfFilesExist(variants1Files));

		String[] variants2Files = {
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css_009_no_conflict_2", BRANCH_2_NAME, eventCatalog),
			null,
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css_009_no_conflict_2", BRANCH_2_NAME, customer),
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css_009_no_conflict_2", BRANCH_2_NAME, event),
			buildPath(FABRICATED_INSTANCES_DIR_PATH, "css_009_no_conflict_2", BRANCH_2_NAME, ticket)
		};
		assertTrue(checkIfFilesExist(variants2Files));

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(baseFiles, variants1Files, variants2Files);
		assertEquals(0, result.size(), "Found a conflict when there is none?");
	}
}
