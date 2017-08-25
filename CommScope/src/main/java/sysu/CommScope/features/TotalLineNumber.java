package sysu.CommScope.features;

import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Statement;

import sysu.CommScope.bean.Token;

public class TotalLineNumber implements Feature{

	public int feature(String comment, int statementIndex, List<Statement> statementList, List<String> codeList,
			List<Token> tokenList, CompilationUnit unit) {
		int startLine = unit.getLineNumber(statementList.get(0).getStartPosition());
		int endLine = unit.getLineNumber(statementList.get(statementList.size()-1).getStartPosition()+statementList.get(statementList.size()-1).getLength()-1);
		
		return endLine - startLine + 1;
	}

}
