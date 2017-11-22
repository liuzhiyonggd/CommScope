package sysu.CommScope.tool;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SimilarComputer {
	
	public static double jaccard(List<Integer> list1,List<Integer> list2) {
		double result = 0;
		Set<Integer> set1 = new HashSet<Integer>(list1);
		Set<Integer> set2 = new HashSet<Integer>(list2);
		int union = set1.size()+set2.size();
		int inter = 0;
		for(int l:set1) {
			if(set2.contains(l)) {
				union--;
				inter++;
			}
		}
		
		if(union>0) {
			result = inter*1.0d/union;
		}
		return result;
		
	}
	
	public static void main(String[] args) {
		List<Integer> list1 = new ArrayList<Integer>();
		List<Integer> list2 = new ArrayList<Integer>();
		
		list1.add(1);
		list1.add(2);
		list1.add(3);
		
		list2.add(1);
		list2.add(3);
		list2.add(4);
		
		
		System.out.println(jaccard(list1,list2));
	}

}
