package sysu.CommScope.features;

import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Statement;

import sysu.CommScope.bean.Token;

public class LineDistance implements Feature{

	public int feature(String comment, int statementIndex, List<Statement> statementList, List<String> codeList,
			List<Token> tokenList, CompilationUnit unit) {
		int commentLineNumber = unit.getLineNumber(statementList.get(0).getStartPosition())-1;
		int statementLineNumber = unit.getLineNumber(statementList.get(statementIndex).getStartPosition());
		return statementLineNumber - commentLineNumber;
	}

}
