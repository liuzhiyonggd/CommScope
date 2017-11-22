package sysu.CommScope.db;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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
import sysu.CommScope.repository.CommentRepository;
import sysu.CommScope.repository.RepositoryFactory;
import sysu.CommScope.token.Parser2;
import sysu.CommScope.tool.FileParser;

public class InsertClass {
	private Logger log = Logger.getLogger(InsertClass.class);
	private ClassMaxIdRepository classMaxIdRepo = RepositoryFactory.getClassMaxIdRepository();
	private CommentMaxIdRepository commentMaxIdRepo = RepositoryFactory.getCommentMaxIdRepository();
	private ClassMessageRepository classRepository = RepositoryFactory.getClassRepository();
	private CommentRepository commentRepository = RepositoryFactory.getCommentRepository();

	public void insert(String fileName, String project) {

		List<String> codeList = new ArrayList<String>();
		try {
			codeList = FileUtils.readLines(new File(fileName), "UTF-8");
		} catch (IOException e) {
			log.error("read file error.");
		}

		List<CodeComment> commentList = FileParser.parseFile2CommentList(codeList);
		ClassMessage clazz = new ClassMessage();
		clazz.setProject(project);

		synchronized (classMaxIdRepo) {
			ClassMaxID classMaxId = classMaxIdRepo.findAll().get(0);
			classMaxId.setMaxID(classMaxId.getMaxID() + 1);
			classMaxIdRepo.save(classMaxId);
			clazz.setClassID(classMaxId.getMaxID());
		}

		clazz.setClassName(fileName);
		clazz.setCodeList(codeList);

		UnitCompiler compiler = new UnitCompiler(codeList);
		List<Token> tokenList = Parser2.parseAST2Tokens(compiler.getUnit()).getTokenList();
		clazz.setTokenList(tokenList);
		classRepository.insert(clazz);

		for (CodeComment codeComment : commentList) {
			CommentEntry comment = new CommentEntry();
			comment.setClassID(clazz.getClassID());
			comment.setClassName(clazz.getClassName());

			synchronized (commentMaxIdRepo) {
				CommentMaxID commentMaxId = commentMaxIdRepo.findAll().get(0);
				commentMaxId.setMaxID(commentMaxId.getMaxID() + 1);
				commentMaxIdRepo.save(commentMaxId);
				comment.setCommentID(commentMaxId.getMaxID());
			}

			List<String> commentString = new ArrayList<String>();
			for (int i = codeComment.getStartLine() - 1; i < codeComment.getEndLine(); i++) {
				commentString.add(codeList.get(i));
			}
			comment.setComment(commentString);

			List<String> codeString = new ArrayList<String>();
			for (int i = codeComment.getScopeStartLine() + codeComment.getEndLine() - codeComment.getStartLine(); 
					i < codeComment.getScopeEndLine(); i++) {
				codeString.add(codeList.get(i));
			}
			comment.setCodeList(codeString);
			comment.setCommentStartLine(codeComment.getStartLine());
			comment.setCommentEndLine(codeComment.getEndLine());
			comment.setCommentStartPosition(codeComment.getStartPosition());
			comment.setScopeStartLine(codeComment.getScopeStartLine());
			comment.setScopeEndLine(codeComment.getScopeEndLine());
			List<Token> commentTokenList = new ArrayList<Token>();
			for (Token t : tokenList) {
				if (t.getStartLine() >= comment.getScopeStartLine() && t.getEndLine() <= comment.getScopeEndLine()) {
					commentTokenList.add(t);
				}
			}
			comment.setTokenList(commentTokenList);
			comment.setVerifyCount(0);
			comment.setVerifyScopeEndLine(new ArrayList<Integer>());
			commentRepository.insert(comment);
		}

	}

	public void insertProject(String project) {
		File file = new File("f:/javaproject/" + project);
		List<File> fileList = new ArrayList<File>();

		Queue<File> dirQueue = new LinkedList<File>();
		dirQueue.add(file);
		while (!dirQueue.isEmpty()) {
			File dir = dirQueue.poll();
			File[] files = dir.listFiles();
			if (files != null) {
				for (File f : files) {
					if (f.isDirectory()) {
						dirQueue.add(f);
					} else if (f.getName().endsWith(".java")) {
						fileList.add(f);
					}
				}
			}
		}

		for (File f : fileList) {
			insert(f.getAbsolutePath(), project);
		}
	}

	public static void main(String[] args) throws IOException {
		InsertClass insertClass = new InsertClass();
		

		List<String> projects = FileUtils.readLines(new File("F:/projects.txt"),"UTF-8");
		for (String project : projects) {
			insertClass.insertProject(project);
			System.out.println(project + " is done.");
		}
	}

}
