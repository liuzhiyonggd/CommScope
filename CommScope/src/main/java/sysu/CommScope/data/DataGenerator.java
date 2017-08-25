package sysu.CommScope.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
		for(EndLineVerify endLineVerify:endLineVerifyList) {
			int commentID = endLineVerify.getCommentID();
			CommentEntry comment = commentRepo.findASingleByCommentID(commentID);
			ClassMessage clazz = classRepo.findASingleByProjectAndCommitIDAndClassName(comment.getProject(), comment.getCommitID(), comment.getClassName());
			
			List<Token> tokenList = clazz.getOldTokenList();
			
			List<String> codeList = new ArrayList<String>();
			List<Line> codes = clazz.getOldCode();
			String source = "";
			for(Line line:codes) {
				codeList.add(line.getLine());
				source += line.getLine()+"\n";
			}
			
			//获取整个类的方法列表
			ASTParser parser = ASTParser.newParser(AST.JLS3);
			parser.setSource(source.toCharArray());
			CompilationUnit unit = (CompilationUnit)parser.createAST(null);
			
			TypeDeclaration type = (TypeDeclaration)unit.types().get(0);
			
			MethodDeclaration[] methods = type.getMethods(); 
			
			List<Statement> statementList = new ArrayList<Statement>();
			for(MethodDeclaration method:methods) {
				int methodStartLine = unit.getLineNumber(method.getStartPosition());
				int methodEndLine = unit.getLineNumber(method.getStartPosition()+method.getLength()-1);
				
				if(methodStartLine<=comment.getOld_comment_startLine()&&methodEndLine>=comment.getOld_comment_endLine()) {
					List<Statement> statements = (List<Statement>)method.getBody().statements();
					for(Statement statement:statements) {
						int statementStartLine = unit.getLineNumber(statement.getStartPosition());
						int statementEndLine = unit.getLineNumber(statement.getStartPosition()+statement.getLength()-1);
						if(statementStartLine>=comment.getOld_scope_startLine()&&statementEndLine<=comment.getOld_scope_endLine()) {
							statementList.add(statement);
						}
					}
				}
			}
			
			String commentString = "";
			for(String str:comment.getOldComment()) {
				commentString += str+" ";
			}
			
			
			
			for(int i=0;i<statementList.size();i++) {
				
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
				featureList.add(new IsControlStatement(Statement.IF_STATEMENT));
				featureList.add(new IsControlStatement(Statement.WHILE_STATEMENT));
				featureList.add(new IsControlStatement(Statement.FOR_STATEMENT));
				featureList.add(new LineDistance());
				featureList.add(new StatementDistance());
				featureList.add(new TotalLineNumber());
				featureList.add(new CommentWordCount());
				featureList.add(new CommentStatementSimilarity());
				
				for(Feature feature:featureList) {
					vector.add(feature.feature(commentString, i, statementList, codeList, tokenList, unit));
				}
				if(unit.getLineNumber(statementList.get(i).getStartPosition())<=endLineVerify.getEndLine()) {
					vector.add(1);
				}
				else {
					vector.add(0);
				}
				vectorList.add(vector);
			}
		}
		
		List<String> output = new ArrayList<String>();
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
		head.add("@attribute 'IfStatement' numeric");
		head.add("@attribute 'WhileStatement' numeric");
		head.add("@attribute 'ForStatement' numeric");
		head.add("@attribute 'LineDistance' numeric");
		head.add("@attribute 'StatementDistance' numeric");
		head.add("@attribute 'TotalLineNumber' numeric");
		head.add("@attribute 'CommentWordCount' numeric");
		head.add("@attribute 'CommentStatementSimilarity' numeric");
		head.add("@attribute 'class' {0,1}");
		head.add("@data");
		output.addAll(head);
		for(List<Integer> v:vectorList) {
			String vString = "";
			for(int i=0;i<v.size()-1;i++) {
				vString+=v.get(i)+",";
			}
			vString += v.get(v.size()-1);
			output.add(vString);
		}
		
		FileUtils.writeLines(new File("D:/work/8.24/data.arff"), output);
		
	}
	

}
