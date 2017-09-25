package sysu.CommScope.db;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import sysu.CommScope.bean.CodeComment;
import sysu.CommScope.tool.FileParser;

public class InsertClass {
	private static Logger log = Logger.getLogger(InsertClass.class);
	public static void insert(String fileName,String project) {
		
		List<String> codeList = new ArrayList<String>();
		try {
			codeList = FileUtils.readLines(new File(fileName),"UTF-8");
		} catch (IOException e) {
			log.error("read file error.");
		}
		
		List<CodeComment> commentList = FileParser.parseFile2CommentList(codeList);
		
	}

}
