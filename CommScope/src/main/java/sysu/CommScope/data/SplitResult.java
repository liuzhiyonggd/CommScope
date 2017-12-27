package sysu.CommScope.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

public class SplitResult {
	
	public static void main(String[] args) throws IOException {
		for(int i=1;i<=45;i++) {
			List<String> resultLines = FileUtils.readLines(new File("F:/cloneresults/comment71_method_"+i+"_result.txt"),"UTF-8");
			Map<Integer,List<String>> splitMap = new HashMap<Integer,List<String>>();
			for(String str:resultLines) {
				int commentID = Integer.parseInt(str.split(";")[0]);
				int splitToken = commentID/100;
				if(splitMap.containsKey(splitToken)) {
					splitMap.get(splitToken).add(str);
				}else {
					List<String> resultList = new ArrayList<String>();
					resultList.add(str);
					splitMap.put(splitToken, resultList);
				}
			}
			for(Map.Entry<Integer, List<String>> entry:splitMap.entrySet()) {
				FileUtils.writeLines(new File("F:/cloneresults/71/comment71_"+entry.getKey()+"_method_"+i+"_result.txt"), entry.getValue());
			}
		}
	}

}
