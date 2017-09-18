package sysu.CommScope.features;

import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Statement;

import sysu.CommScope.bean.Token;
import sysu.CommScope.splitword.WordSpliter;

public class IsAssertStatement implements Feature{
	
	public int feature(String comment, int statementIndex, List<Statement> statementList, List<String> codeList,
			List<Token> tokenList,CompilationUnit unit) {
		
		Statement currentStatement = statementList.get(statementIndex);
		String statementString = currentStatement.toString();
		List<String> wordList = WordSpliter.split(statementString, true);
		if(wordList.contains("assert")) {
			return 1;
		}
		return 0;
		
	}

}
