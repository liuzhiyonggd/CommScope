package sysu.CommScope.db;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import sysu.CommScope.ast.UnitCompiler;
import sysu.CommScope.bean.ClassMaxID;
import sysu.CommScope.bean.ClassMessage;
import sysu.CommScope.bean.CodeComment;
import sysu.CommScope.bean.CommentEntry;
import sysu.CommScope.bean.CommentMaxID;
import sysu.CommScope.bean.Token;
import sysu.CommScope.repository.ClassMaxIdRepository;
import sysu.CommScope.repository.ClassMessageRepository;
import sysu.CommScope.repository.CommentMaxIdRepository;
import sysu.CommScope.repository.RepositoryFactory;
import sysu.CommScope.token.Parser2;
import sysu.CommScope.tool.FileParser;

public class InsertClass {
	private static Logger log = Logger.getLogger(InsertClass.class);
	private static ClassMaxIdRepository classMaxIdRepo = RepositoryFactory.getClassMaxIdRepository();
	private static CommentMaxIdRepository commentMaxIdRepo = RepositoryFactory.getCommentMaxIdRepository();
	private static ClassMessageRepository classRepository = RepositoryFactory.getClassRepository();
	public static void insert(String fileName,String project) {
		
		List<String> codeList = new ArrayList<String>();
		try {
			codeList = FileUtils.readLines(new File(fileName),"UTF-8");
		} catch (IOException e) {
			log.error("read file error.");
		}
		
		List<CodeComment> commentList = FileParser.parseFile2CommentList(codeList);
		ClassMessage clazz = new ClassMessage();
		clazz.setProject(project);
		
		synchronized(classMaxIdRepo) {
	        ClassMaxID classMaxId = classMaxIdRepo.findAll().get(0);
	        classMaxId.setMaxID(classMaxId.getMaxID()+1);
	        classMaxIdRepo.save(classMaxId);
	        clazz.setClassID(classMaxId.getMaxID());
		}
		
		clazz.setClassName(fileName);
		clazz.setCodeList(codeList);
		
		UnitCompiler compiler = new UnitCompiler(codeList);
		List<Token> tokenList = Parser2.parseAST2Tokens(compiler.getUnit()).getTokenList();
		clazz.setTokenList(tokenList);
		classRepository.insert(clazz);
		
		for(CodeComment codeComment:commentList) {
			CommentEntry comment = new CommentEntry();
			comment.setClassID(clazz.getClassID());
			comment.setClassName(clazz.getClassName());
			
			synchronized(commentMaxIdRepo) {
				CommentMaxID commentMaxId = commentMaxIdRepo.findAll().get(0);
		        commentMaxId.setMaxID(commentMaxId.getMaxID()+1);
		        commentMaxIdRepo.save(commentMaxId);
		        clazz.setClassID(commentMaxId.getMaxID());
			}
			
			List<String> commentString = new ArrayList<String>();
			for(int i=codeComment.getStartLine()-1;i<codeComment.getEndLine();i++) {
				commentString.add(codeList.get(i));
			}
			comment.setComment(commentString);
			
			List<String> codeString = new ArrayList<String>();
			for(int i=codeComment.getScopeStartLine()-codeComment.getEndLine()+codeComment.getStartLine()-2;i<codeComment.getScopeEndLine();i++) {
				codeString.add(codeList.get(i));
			}
			comment.setCodeList(codeString);
			
			
		}
		
		
	}

}
