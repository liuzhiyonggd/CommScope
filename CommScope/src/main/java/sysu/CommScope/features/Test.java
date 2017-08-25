package sysu.CommScope.features;



import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import sysu.CommScope.bean.ClassMessage;
import sysu.CommScope.bean.CommentEntry;
import sysu.CommScope.bean.Line;
import sysu.CommScope.bean.Token;
import sysu.CommScope.repository.ClassMessageRepository;
import sysu.CommScope.repository.CommentRepository;
import sysu.CommScope.repository.RepositoryFactory;

public class Test {
	
	public static void main(String[] args) {
		ClassMessageRepository classRepo = RepositoryFactory.getClassRepository();
		CommentRepository commentRepo = RepositoryFactory.getCommentRepository();
		
		CommentEntry comment = commentRepo.findASingleByCommentID(111274);
		ClassMessage clazz = classRepo.findASingleByProjectAndCommitIDAndClassName(
				comment.getProject(), comment.getCommitID(), comment.getClassName());
		
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
		
		List<Feature> features = new ArrayList<Feature>();
		features.add(new SameVariableUsed(0));
		features.add(new IsControlStatement(Statement.IF_STATEMENT));
		features.add(new IsControlStatement(Statement.WHILE_STATEMENT));
		features.add(new IsControlStatement(Statement.FOR_STATEMENT));
		features.add(new SameMethodInvoked(0));
		for(int index = 0;index<statementList.size();index++) {
		for(Feature feature:features) {
			int isFeature = feature.feature("", index, statementList, codeList, tokenList, unit);
			System.out.println(feature.getClass().getSimpleName()+": index "+index+":"+isFeature);
		}
		}
		
		
	}

}
