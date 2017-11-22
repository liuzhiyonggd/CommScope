package sysu.CommScope.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;

import sysu.CommScope.bean.Method;
import sysu.CommScope.bean.Token;
import sysu.CommScope.repository.MethodRepository;
import sysu.CommScope.repository.RepositoryFactory;

public class MethodFileGenerator {
	
	private static MethodRepository methodRepo = RepositoryFactory.getMethodRepository();
	
	public static List<String> generate(int startClassID,int endClassID){
		List<String> result = new ArrayList<String>();
		for(int classID = startClassID;classID<=endClassID;classID++) {
		    List<Method> methodList = methodRepo.findByClassID(classID);
		    for(Method method:methodList) {
		        StringBuilder sb = new StringBuilder();
		        sb.append(method.getClassID()).append(";").append(method.getMethodID()).append(";");
		        
		        Map<Integer,List<Long>> tokenMap = new TreeMap<Integer,List<Long>>();
		        for(Token token:method.getTokens()) {
		        	int startLine = token.getStartLine();
		        	long hashNum = token.getTokenName().hashCode();
		        	if(tokenMap.containsKey(startLine)) {
		        		tokenMap.get(startLine).add(hashNum);
		        	}else {
		        		List<Long> hashList = new ArrayList<Long>();
		        		hashList.add(hashNum);
		        		tokenMap.put(startLine, hashList);
		        	}
		        }
		        
		        for(Map.Entry<Integer, List<Long>> entry:tokenMap.entrySet()) {
		        	sb.append(entry.getKey()+" ");
		        	List<Long> hashList = entry.getValue();
		        	for(long hashNum:hashList) {
		        		sb.append(hashNum+" ");
		        	}
		        	sb.append(";");
		        }
		        result.add(sb.toString());
		    }
		}
		return result;
	}
	
	public static void main(String[] args) throws IOException {
		for(int i=1;i<=450;i++) {
		    List<String> fileLines = generate((i-1)*1000+1, i*1000);
		    FileUtils.writeLines(new File("f:/methodfiles3/method_"+i+".txt"), fileLines);
		}
	}

}
