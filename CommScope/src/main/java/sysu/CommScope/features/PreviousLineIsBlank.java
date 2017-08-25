package sysu.CommScope.features;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Statement;

import sysu.CommScope.bean.Token;

public class PreviousLineIsBlank implements Feature{

	public int feature(String comment, int statementIndex, List<Statement> statementList, List<String> codeList,
			List<Token> tokenList,CompilationUnit unit) {
		Statement currentStatement = statementList.get(statementIndex);
		int startLine = unit.getLineNumber(currentStatement.getStartPosition());
	    String priviousLine = null;
		if(startLine>1) {
	    	priviousLine = codeList.get(startLine-2);
	    }
		else {
			return 0;
		}
		return StringUtils.isBlank(priviousLine)?1:0;
	}

}
