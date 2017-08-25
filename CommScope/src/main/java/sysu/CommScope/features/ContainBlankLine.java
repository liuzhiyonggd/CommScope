package sysu.CommScope.features;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Statement;

import sysu.CommScope.bean.Token;

public class ContainBlankLine implements Feature{

	public int feature(String comment, int statementIndex, List<Statement> statementList, List<String> codeList,
			List<Token> tokenList,CompilationUnit unit) {
		int commentStartLine = unit.getLineNumber(statementList.get(0).getStartPosition())-1;
		int startLine = unit.getLineNumber(statementList.get(statementIndex).getStartPosition());
		for(int i=commentStartLine;i<startLine-1;i++) {
			String code = codeList.get(i);
			if(StringUtils.isBlank(code)) {
				return 1;
			}
		}
		return 0;
	}

}
