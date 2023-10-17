package org.conflito.matcher.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class MapUtilities {
	
	private MapUtilities() {
		
	}

	public static void mergeMaps(Map<Integer, List<String>> first, 
			Map<Integer, List<String>> second) {
		for(Entry<Integer, List<String>> e: second.entrySet()) {
			if(first.containsKey(e.getKey())) {
				List<String> firsts = first.get(e.getKey());
				List<String> seconds = e.getValue();
//				List<String> intersect = firsts.stream().filter(seconds::contains)
//														.collect(Collectors.toList());
				List<String> intersect = new ArrayList<>(firsts);
				seconds.forEach(s -> {
					if(!intersect.contains(s)) {
						intersect.add(s);
					}
				});
				first.put(e.getKey(), intersect);
			}
			else {
				first.put(e.getKey(), e.getValue());
			}
		}
		first.forEach((key, val) -> first.put(key, val.stream().distinct().collect(Collectors.toList())));
	}
	
	public static Map<Integer,List<String>> combine(List<Integer> vars, List<String> names){
		Map<Integer, List<String>> result =  new HashMap<>();
		for(int i: vars) {
			result.put(i, names);
		}
		return result;
	}
}
