package sysu.CommScope.tool;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.bson.Document;

import sysu.CommScope.bean.ClassMessage;
import sysu.CommScope.bean.CommentEntry;
import sysu.CommScope.repository.ClassMessageRepository;
import sysu.CommScope.repository.CommentRepository;
import sysu.CommScope.repository.RepositoryFactory;

public class Temp {
	
//	public static void main(String[] args) throws IOException {
//		File dir = new File("F:/projects");
//		for(File file:dir.listFiles()) {
//			List<String> lines = FileUtils.readLines(file,"UTF-8");
//			List<String> output = new ArrayList<String>();
//			for(String str:lines) {
//				if(str.contains("database-mana")) {
//					output.add(str);
//				}
//			}
//			FileUtils.writeLines(new File(file.getAbsolutePath()+"_.txt"), output);
//		}
//		
//		
//	}
	
//	public static void main(String[] args) throws IOException {
//		File file = new File("F:/githubRepos_hibernate");
//		List<String> projects = new ArrayList<String>();
//		for(String str:file.list()) {
//			String[] temps = str.split("\\.");
//			projects.add(temps[0]);
//			
//		}
//		FileUtils.writeLines(new File("F:/projects2.txt"), projects);
//	}
	
	public static void main(String[] args) throws IOException {
		CommentRepository commentRepo = RepositoryFactory.getCommentRepository();
		File dir = new File("F:/projects2");
		for(File file:dir.listFiles()) {
		List<String> fileLines = FileUtils.readLines(file,"UTF-8");
		List<String> output = new ArrayList<String>();
		for(String str:fileLines) {
			String[] temps = str.split("/");
			String className = "";
			
			String[] temps2 = temps[temps.length-1].split(":");
			className = temps2[0];
			String[] temps3 = temps2[1].split("-");
			int startLine = Integer.parseInt(temps3[0].trim());
			int endLine = Integer.parseInt(temps3[1].trim());
			
			List<CommentEntry> comments = commentRepo.findByClassNameLike(className);
			
			System.out.println(str);
			output.add(str);
			for(CommentEntry comment:comments) {
				if(startLine-comment.getScopeStartLine()<=3&&startLine-comment.getScopeStartLine()>=0) {
					
					String commentString = "";
					for(String str2:comment.getComment()) {
						commentString += str2+" ";
					}
					
					System.out.println("comment id:"+comment.getCommentID()+" "+commentString);
					output.add("comment id:"+comment.getCommentID()+" "+commentString);
					System.out.println("start line:"+comment.getScopeStartLine()+" end line:"+comment.getScopeEndLine());
					output.add("start line:"+comment.getScopeStartLine()+" end line:"+comment.getScopeEndLine());
				}
			}
			
			
		}
		FileUtils.writeLines(new File(file.getAbsolutePath()+"_result.txt"), output);
		}
	}

}
