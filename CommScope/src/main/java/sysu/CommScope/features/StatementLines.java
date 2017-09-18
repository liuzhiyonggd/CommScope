package sysu.CommScope.features;

import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Statement;

import sysu.CommScope.bean.Token;

public class StatementLines implements Feature{

	public int feature(String comment, int statementIndex, List<Statement> statementList, List<String> codeList,
			List<Token> tokenList, CompilationUnit unit) {
		Statement statement = statementList.get(statementIndex);
		int startLine = unit.getLineNumber(statement.getStartPosition());
		int endLine = unit.getLineNumber(statement.getStartPosition()+statement.getLength()-1);
		
		return endLine-startLine+1;
	}

}
