package sysu.CommScope.tool;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import sysu.CommScope.bean.CommentEntry;
import sysu.CommScope.repository.CommentRepository;
import sysu.CommScope.repository.RepositoryFactory;

public class CommentFileUtil {

	public static void main(String[] args) throws IOException {
		CommentRepository commentRepo = RepositoryFactory.getCommentRepository();
		List<String> output = new ArrayList<String>();
		
		for(int commentID=1;commentID<=8941;commentID++) {
			CommentEntry comment = commentRepo.findASingleByCommentID(commentID);
			List<String> commStrList = comment.getComment();
			StringBuilder sb = new StringBuilder();
			sb.append(commentID+",");
			for(String str:commStrList) {
				sb.append(str).append(" ");
			}
			output.add(sb.toString());
		}
		FileUtils.writeLines(new File("D:/scopecomment.txt"), output);
		
	}
}
