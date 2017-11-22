package sysu.CommScope.db;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import sysu.CommScope.bean.CommentEntry;
import sysu.CommScope.repository.ClassMessageRepository;
import sysu.CommScope.repository.CommentRepository;
import sysu.CommScope.repository.RepositoryFactory;

public class UpdateCommentType {
	
	public static void main(String[] args) throws IOException {
		
		List<String> lineList = FileUtils.readLines(new File("D:/work/data/output.csv"),"UTF-8");
		Map<String,String> map = new HashMap<String,String>();
		for(String str:lineList) {
			String[] temps = str.split(",");
			String className = temps[0];
			String startLine = temps[1];
			String type = temps[2];
			
			map.put(className+","+startLine,type);
		}
		
		CommentRepository commRepo = RepositoryFactory.getCommentRepository();
		ClassMessageRepository classRepo = RepositoryFactory.getClassRepository();
		List<CommentEntry> commentList = commRepo.findAll();
		System.out.println("comment list:"+commentList.size());
		int unknownSize = 0;
		for(CommentEntry comment:commentList) {
			
			String className = classRepo.findASingleByClassID(comment.getClassID()).getClassName();
			int startLine = comment.getCommentStartLine();
			String key = className+","+startLine;
			if(map.containsKey(key)) {
				comment.setType(map.get(key));
				System.out.println(key+":"+map.get(key));
			}else {
				comment.setType("unknown");
			    unknownSize++;	
			}
			commRepo.save(comment);
			
		}
		System.out.println("unknown:"+unknownSize);
	}

}
