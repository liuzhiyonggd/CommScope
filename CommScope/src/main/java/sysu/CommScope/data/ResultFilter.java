package sysu.CommScope.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class ResultFilter {
	
	public static List<String> filter(String file) throws IOException{
		List<String> result = new ArrayList<String>();
		int commentCount = 0;
		List<String> fileLines = FileUtils.readLines(new File(file),"UTF-8");
		for(String str:fileLines) {
			String[] temps = str.split(";");
			if(temps.length<11) {
				continue;
			}
			String commentID = temps[0];
			List<String> cloneBlocks = new ArrayList<String>();
			for(int i=1;i<temps.length;i++) {
				String[] temps2 = temps[i].split(" ");
				if(temps2.length<2) {
					continue;
				}
				
				cloneBlocks.add(temps[i]);
				
			}
			if(cloneBlocks.size()>5) {
				commentCount ++;
				result.add(commentID+":");
				result.addAll(cloneBlocks);
				result.add("end");
			}
		}
		System.out.println(commentCount);
		return result;
	}
	
	public static void main(String[] args) throws IOException {
		List<String> result = filter("F:/cloneresults/71/comment71_7099_result.txt");
		
//		FileUtils.writeLines(new File("F:/cloneresults/71/comment71_7099_filter_result.txt"), result);
	}

}
