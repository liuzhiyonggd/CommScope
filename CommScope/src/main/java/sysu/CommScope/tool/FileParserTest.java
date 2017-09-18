package sysu.CommScope.tool;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import sysu.CommScope.bean.CodeComment;

public class FileParserTest {
	
	@Test
	public void test() {
		System.out.println("test begin.");
		List<String> codes;
		try {
			codes = FileUtils.readLines(new File("D:/work/9.13/AbstractEntityInsertAction.java"),"UTF-8");
			List<CodeComment> codeCommentList = FileParser.parseFile2CommentList(codes);
			System.out.println(codeCommentList.size()+"");
			for(CodeComment codeComment:codeCommentList) {
				System.out.println("----------------------");
				System.out.println("comment start line:"+codeComment.getStartLine());
				System.out.println("comment end line:"+codeComment.getEndLine());
				System.out.println("comment scope line:"+codeComment.getScopeEndLine());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
