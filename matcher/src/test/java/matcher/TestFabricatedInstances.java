package matcher;

import java.io.File;

import matcher.exceptions.ApplicationException;

public class TestFabricatedInstances {

	private static final String SRC_FOLDER = ".." + File.separator + "Examples" + 
			File.separator + "SemanticConflicts3" + File.separator;
	
	private static final String AED_FOLDER = "aed1617" + File.separator;
	
	private static final String AED_FOLDER_2 = "aed1617_fase2" + File.separator;
	
	private static final String IP_FOLDER = "ip2021" + File.separator;
	
	private static final String LEIXEL_FOLDER = "leixcel1112" + File.separator;
	
	private static final String CSS_FIELD_HIDING = "css2021_projeto1_047" + File.separator;
	
	public static void aed(String[] args) throws ApplicationException {
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
	
	public static void aed_2(String[] args) throws ApplicationException {
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
				matcher.matchingAssignments(baseFiles, variants1Files, variants2Files, 
						"Dependency Based (new class)"));
	}
	
	public static void ip(String[] args) throws ApplicationException {
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
		
		System.out.println(
				matcher.matchingAssignments(baseFiles, variants1Files, variants2Files, 
						"Dependency Based (new class)"));
	}
	
	public static void leixel(String[] args) throws ApplicationException {
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
		
		System.out.println(
				matcher.matchingAssignments(baseFiles, variants1Files, variants2Files, 
						"Change Method 3"));
	}
	
	public static void css_field_hiding(String[] args) throws ApplicationException {
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
		
		System.out.println(
				matcher.matchingAssignments(baseFiles, variants1Files, variants2Files, 
						"Field Hiding"));
	}
}
