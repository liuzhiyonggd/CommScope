package sysu.CommScope.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import sysu.CommScope.bean.ClassMessage;
import sysu.CommScope.bean.CommentEntry;
import sysu.CommScope.bean.EndLineVerify;
import sysu.CommScope.bean.Line;
import sysu.CommScope.bean.Token;
import sysu.CommScope.features.*;
import sysu.CommScope.repository.ClassMessageRepository;
import sysu.CommScope.repository.CommentRepository;
import sysu.CommScope.repository.EndLineVerifyRepository;
import sysu.CommScope.repository.RepositoryFactory;

public class DataGenerator {
	public static void main(String[] args) throws IOException {
		EndLineVerifyRepository endLineVerifyRepo = RepositoryFactory.getEndLineVerifyRepository();
		ClassMessageRepository classRepo = RepositoryFactory.getClassRepository();
		CommentRepository commentRepo = RepositoryFactory.getCommentRepository();

		List<List<Integer>> vectorList = new ArrayList<List<Integer>>();

		List<EndLineVerify> endLineVerifyList = endLineVerifyRepo.findAll();
		int count = 0;
		System.out.println(endLineVerifyList.size());
		endLineVerifyList = sameVerifyFilter(endLineVerifyList);
		System.out.println(endLineVerifyList.size());
		//去除重复的comment，以方便后期的处理
		Set<Integer> commentIDSet = new HashSet<Integer>();
		
		for (EndLineVerify endLineVerify : endLineVerifyList) {
			count++;
			int commentID = endLineVerify.getCommentID();
			if(commentIDSet.contains(commentID)) {
				continue;
			}else {
				commentIDSet.add(commentID);
			}
			CommentEntry comment = commentRepo.findASingleByCommentID(commentID);
//			if(comment.getProject().equals("jgit")) {
//				continue;
//			}
			if (endLineVerify.getEndLine() <= comment.getCommentEndLine()) {
				continue;
			}
			ClassMessage clazz = classRepo.findASingleByClassID(comment.getClassID());

			List<Token> tokenList = clazz.getTokenList();

			List<String> codeList = new ArrayList<String>();
			List<String> codes = clazz.getCodeList();
			String source = "";
			for (String code : codes) {
				codeList.add(code);
				source += code + "\n";
			}

			// 获取整个类的方法列表
			ASTParser parser = ASTParser.newParser(AST.JLS8);
			parser.setSource(source.toCharArray());
			CompilationUnit unit = (CompilationUnit) parser.createAST(null);

			TypeDeclaration type = (TypeDeclaration) unit.types().get(0);

			MethodDeclaration[] methods = type.getMethods();

			List<Statement> statementList = new ArrayList<Statement>();
			for (MethodDeclaration method : methods) {
				int methodStartLine = unit.getLineNumber(method.getStartPosition());
				int methodEndLine = unit.getLineNumber(method.getStartPosition() + method.getLength() - 1);

				if (methodStartLine <= comment.getCommentStartLine()
						&& methodEndLine >= comment.getCommentEndLine()) {
					List<Statement> statements = (List<Statement>) method.getBody().statements();
					for (Statement statement : statements) {
						int statementStartLine = unit.getLineNumber(statement.getStartPosition());
						int statementEndLine = unit
								.getLineNumber(statement.getStartPosition() + statement.getLength() - 1);
						if (statementStartLine >= comment.getScopeStartLine()
								&& statementEndLine <= comment.getScopeEndLine()) {
							statementList.add(statement);
							System.out.println(statement.toString());
							System.out.println("------------------------");
						}
					}
				}
			}

			String commentString = "";
			for (String str : comment.getComment()) {
				commentString += str + " ";
			}

			for (int i = 0; i < statementList.size(); i++) {

				List<Integer> vector = new ArrayList<Integer>();
				vector.add(commentID);
				vector.add(i);
				List<Feature> featureList = new ArrayList<Feature>();
				featureList.add(new PreviousLineIsBlank());
				featureList.add(new NextLineIsBlank());
				featureList.add(new ContainBlankLine());
				featureList.add(new SameMethodInvoked(0));
				featureList.add(new SameMethodInvoked(1));
				featureList.add(new SameMethodInvoked(2));
				featureList.add(new SameVariableUsed(0));
				featureList.add(new SameVariableUsed(1));
				featureList.add(new SameVariableUsed(2));
				featureList.add(new SameMethodInvoked(i-1));
				featureList.add(new SameMethodInvoked(i-2));
				featureList.add(new SameMethodInvoked(i-3));
				featureList.add(new SameMethodInvoked(i-4));
				featureList.add(new SameMethodInvoked(i-5));
				featureList.add(new SameMethodInvoked(i+1));
				featureList.add(new SameMethodInvoked(i+2));
				featureList.add(new SameMethodInvoked(i+3));
				featureList.add(new SameMethodInvoked(i+4));
				featureList.add(new SameMethodInvoked(i+5));
				featureList.add(new SameVariableUsed(i-1));
				featureList.add(new SameVariableUsed(i-2));
				featureList.add(new SameVariableUsed(i-3));
				featureList.add(new SameVariableUsed(i-4));
				featureList.add(new SameVariableUsed(i-5));
				featureList.add(new SameVariableUsed(i+1));
				featureList.add(new SameVariableUsed(i+2));
				featureList.add(new SameVariableUsed(i+3));
				featureList.add(new SameVariableUsed(i+4));
				featureList.add(new SameVariableUsed(i+5));
				featureList.add(new IsControlStatement(Statement.IF_STATEMENT));
				featureList.add(new IsControlStatement(Statement.WHILE_STATEMENT));
				featureList.add(new IsControlStatement(Statement.FOR_STATEMENT));
				featureList.add(new IsReturnStatement());
				featureList.add(new IsAssertStatement());
				featureList.add(new StatementLines());
				featureList.add(new LineDistance());
				featureList.add(new StatementDistance());
				featureList.add(new TotalLineNumber());
				featureList.add(new CommentWordCount());
				featureList.add(new CommentStatementSimilarity());

				for (Feature feature : featureList) {
					vector.add(feature.feature(commentString, i, statementList, codeList, tokenList, unit));
				}
				if (unit.getLineNumber(statementList.get(i).getStartPosition()) <= endLineVerify.getEndLine()) {
					vector.add(1);
				} else {
					vector.add(0);
				}
				vectorList.add(vector);
			}
		}

		List<String> trainList = new ArrayList<String>();
		List<String> testList = new ArrayList<String>();

		List<String> head = new ArrayList<String>();
		head.add("@relation commentscope");
		head.add("@attribute 'commentID' numeric");
		head.add("@attribute 'StatementIndex' numeric");
		head.add("@attribute 'PreviousLineIsBlank' numeric");
		head.add("@attribute 'NextLineIsBlank' numeric");
		head.add("@attribute 'ContainBlankLine' numeric");
		head.add("@attribute 'SameMethodInvoked_0' numeric");
		head.add("@attribute 'SameMethodInvoked_1' numeric");
		head.add("@attribute 'SameMethodInvoked_2' numeric");
		head.add("@attribute 'SameVariableUsed_0' numeric");
		head.add("@attribute 'SameVariableUsed_1' numeric");
		head.add("@attribute 'SameVariableUsed_2' numeric");
		head.add("@attribute 'SameMethodInvoked_-1' numeric");
		head.add("@attribute 'SameMethodInvoked_-2' numeric");
		head.add("@attribute 'SameMethodInvoked_-3' numeric");
		head.add("@attribute 'SameMethodInvoked_-4' numeric");
		head.add("@attribute 'SameMethodInvoked_-5' numeric");
		head.add("@attribute 'SameMethodInvoked_+1' numeric");
		head.add("@attribute 'SameMethodInvoked_+2' numeric");
		head.add("@attribute 'SameMethodInvoked_+3' numeric");
		head.add("@attribute 'SameMethodInvoked_+4' numeric");
		head.add("@attribute 'SameMethodInvoked_+5' numeric");
		head.add("@attribute 'SameVariableUsed_-1' numeric");
		head.add("@attribute 'SameVariableUsed_-2' numeric");
		head.add("@attribute 'SameVariableUsed_-3' numeric");
		head.add("@attribute 'SameVariableUsed_-4' numeric");
		head.add("@attribute 'SameVariableUsed_-5' numeric");
		head.add("@attribute 'SameVariableUsed_+1' numeric");
		head.add("@attribute 'SameVariableUsed_+2' numeric");
		head.add("@attribute 'SameVariableUsed_+3' numeric");
		head.add("@attribute 'SameVariableUsed_+4' numeric");
		head.add("@attribute 'SameVariableUsed_+5' numeric");
		head.add("@attribute 'IfStatement' numeric");
		head.add("@attribute 'WhileStatement' numeric");
		head.add("@attribute 'ForStatement' numeric");
		head.add("@attribute 'ReturnStatement' numeric");
		head.add("@attribute 'AssertStatement' numeric");
		head.add("@attribute 'StatementLines' numeric");
		head.add("@attribute 'LineDistance' numeric");
		head.add("@attribute 'StatementDistance' numeric");
		head.add("@attribute 'TotalLineNumber' numeric");
		head.add("@attribute 'CommentWordCount' numeric");
		head.add("@attribute 'CommentStatementSimilarity' numeric");
		head.add("@attribute 'class' {0,1}");
		head.add("@data");
		trainList.addAll(head);
		testList.addAll(head);

		Map<Integer, List<List<Integer>>> vectorMap = new HashMap<Integer, List<List<Integer>>>();
		for (List<Integer> v : vectorList) {
			if (vectorMap.containsKey(v.get(0))) {
				vectorMap.get(v.get(0)).add(v);
			} else {
				List<List<Integer>> vList = new ArrayList<List<Integer>>();
				vList.add(v);
				vectorMap.put(v.get(0), vList);
			}
		}

		int size = vectorMap.keySet().size();
		int num = 0;
		for (Integer key : vectorMap.keySet()) {

			List<List<Integer>> commentVectors = vectorMap.get(key);

			for (List<Integer> v : commentVectors) {
				String vString = "";
				for (int i = 0; i < v.size() - 1; i++) {
					vString += v.get(i) + ",";
				}
				vString += v.get(v.size() - 1);

				if (num < size * 2 / 3) {
					trainList.add(vString);
				} else {
					testList.add(vString);
				}
			}
			num++;
		}

		FileUtils.writeLines(new File("D:/work/9.11/train.arff"), trainList);
		FileUtils.writeLines(new File("D:/work/9.11/test.arff"), testList);

	}

    private static List<EndLineVerify> sameVerifyFilter(List<EndLineVerify> verifyList) {
    	Map<Integer,List<Integer>> verifyMap = new HashMap<Integer,List<Integer>>();
    	for(int i=0;i<verifyList.size();i++) {
    		if(verifyMap.containsKey(verifyList.get(i).getCommentID())) {
    			verifyMap.get(verifyList.get(i).getCommentID()).add(i);
    		}else {
    			List<Integer> list = new ArrayList<Integer>();
    			list.add(i);
    			verifyMap.put(verifyList.get(i).getCommentID(), list);
    		}
    	}
    	System.out.println(verifyMap.keySet().size());
    	List<EndLineVerify> resultList = new ArrayList<EndLineVerify>();
    	for(Map.Entry<Integer, List<Integer>> entry:verifyMap.entrySet()) {
    		List<Integer> commentIdList = entry.getValue();
    		boolean flag = true;
    		for(int i=0;i<commentIdList.size()-1;i++) {
    			if(verifyList.get(commentIdList.get(i)).getEndLine()!=verifyList.get(commentIdList.get(i+1)).getEndLine()) {
    				flag = false;
    				break;
    			}
    		}
    		if(flag) {
    			resultList.add(verifyList.get(commentIdList.get(0)));
    		}
    	}
    	return resultList;
    }
    
}
