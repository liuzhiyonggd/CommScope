package sysu.CommScope.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

public class DataGenerator2 {
	
	public static void generate(String filePath,String savePath) throws IOException {
	    List<String> fileLines = FileUtils.readLines(new File(filePath),"UTF-8");
	    Map<String,List<Integer>> vectorMap = new HashMap<String,List<Integer>>();
	    Map<String,List<Integer>> classMap = new HashMap<String,List<Integer>>();
	    for(String line:fileLines) {
	    	String[] temps = line.split(",");
	    	String commentID = temps[0];
	    	int label = (int)Double.parseDouble(temps[2]);
	    	int predictLabel = (int)Double.parseDouble(temps[3]);
	    	
	    	if(vectorMap.containsKey(commentID)) {
	    		vectorMap.get(commentID).add(predictLabel);
	    	}else {
	    		List<Integer> vectorList = new ArrayList<Integer>();
	    		vectorList.add(predictLabel);
	    		vectorMap.put(commentID,vectorList);
	    	}
	    	
	    	if(classMap.containsKey(commentID)) {
	    		classMap.get(commentID).add(label);
	    	}else {
	    		List<Integer> labelList = new ArrayList<Integer>();
	    		labelList.add(label);
	    		classMap.put(commentID, labelList);
	    	}
	    }
	    
	    Map<String,Integer> labelMap = new HashMap<String,Integer>();
	    for(Map.Entry<String, List<Integer>> entry:classMap.entrySet()) {
	    	String key = entry.getKey();
	    	List<Integer> labelList = entry.getValue();
	    	int label = 0;
	    	for(int i=0;i<labelList.size();i++) {
	    		if(labelList.get(i)==0) {
	    			break;
	    		}else {
	    			label = i;
	    		}
	    	}
	    	labelMap.put(key, label);
	    }
	    
	    List<String> output = new ArrayList<String>();
	    for(Map.Entry<String, List<Integer>> entry:vectorMap.entrySet()) {
	    	StringBuilder sb = new StringBuilder();
	    	sb.append(entry.getKey());
	    	int i=0;
	    	for(;i<entry.getValue().size();i++) {
	    		sb.append(",").append(entry.getValue().get(i)+"");
	    	}
	    	for(;i<=20;i++) {
	    		sb.append(",").append("0");
	    	}
	    	sb.append(",").append(labelMap.get(entry.getKey())+"");
	    	output.add(sb.toString());
	    }
	    
	    FileUtils.writeLines(new File(savePath), output);
	    
	}
	
	public static void main(String[] args) throws IOException {
		generate("d:/work/9.7/result_test_5.csv", "d:/work/9.7/test_5_2.csv");
	}

}
