package sysu.CommScope.features;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Statement;

import sysu.CommScope.bean.Token;

public class NextLineIsBlank implements Feature{

	public int feature(String comment, int statementIndex, List<Statement> statementList, List<String> codeList,
			List<Token> tokenList,CompilationUnit unit) {
		Statement currentStatement = statementList.get(statementIndex);
		int endLine = unit.getLineNumber(currentStatement.getStartPosition()+currentStatement.getLength()-1);
	    String nextLine = null;
		if(endLine<codeList.size()) {
	    	nextLine = codeList.get(endLine);
	    }
		else {
			return 0;
		}
		return StringUtils.isBlank(nextLine)?1:0;
	}
}
