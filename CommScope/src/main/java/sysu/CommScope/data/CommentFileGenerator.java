package sysu.CommScope.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;

import sysu.CommScope.bean.CommentEntry;
import sysu.CommScope.bean.Token;
import sysu.CommScope.repository.CommentRepository;
import sysu.CommScope.repository.RepositoryFactory;

public class CommentFileGenerator {
	private static CommentRepository commentRepo = RepositoryFactory.getCommentRepository();
	
	public static List<String> generate(int startCommentID,int endCommentID){
		List<String> result = new ArrayList<String>();
		for(int commentID=startCommentID;commentID<=endCommentID;commentID++) {
			CommentEntry comment = commentRepo.findASingleByCommentID(commentID);
			StringBuilder sb = new StringBuilder();
			sb.append(comment.getClassID()+";").append(comment.getCommentID()+";");
			
			Map<Integer,List<Long>> lineMap = new TreeMap<Integer,List<Long>>();
			for(Token token:comment.getTokenList()) {
				int startLine = token.getStartLine();
				long hashNumber = token.getTokenName().hashCode();
				if(lineMap.containsKey(startLine)) {
					lineMap.get(startLine).add(hashNumber);
				}else {
					List<Long> hashList = new ArrayList<Long>();
					hashList.add(hashNumber);
					lineMap.put(startLine, hashList);
				}
			}
			
			for(Map.Entry<Integer, List<Long>> entry:lineMap.entrySet()) {
				sb.append(entry.getKey()+" ");
				for(long hashNumber:entry.getValue()) {
					sb.append(hashNumber+" ");
				}
				sb.append(";");
			}
			result.add(sb.toString());
		}
		return result;
	}
	
	public static void main(String[] args) throws IOException {
		for(int i=1;i<=79;i++) {
		    List<String> commentLines = generate((i-1)*10000+1, i*10000);
		    FileUtils.writeLines(new File("F:/commentfiles2/comment"+i+".txt"), commentLines);
		}
	}

}
