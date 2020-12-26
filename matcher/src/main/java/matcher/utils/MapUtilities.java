package matcher.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class MapUtilities {

	public static void mergeMaps(Map<Integer, List<String>> first, 
			Map<Integer, List<String>> second) {
		for(Entry<Integer, List<String>> e: second.entrySet()) {
			if(first.containsKey(e.getKey())) {
				first.get(e.getKey()).addAll(e.getValue());
			}
			else {
				first.put(e.getKey(), e.getValue());
			}
		}
		first.forEach((key, val) -> val.stream().distinct().collect(Collectors.toList()));
	}
	
	public static Map<Integer,List<String>> combine(List<Integer> vars, List<String> names){
		Map<Integer, List<String>> result =  new HashMap<>();
		for(int i: vars) {
			result.put(i, names);
		}
		return result;
	}
}
