package sysu.CommScope.features;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Statement;

import sysu.CommScope.bean.Token;

public class SameVariableUsed implements Feature{
	
	private int comparedIndex;
	
	public SameVariableUsed(int comparedIndex) {
		this.comparedIndex = comparedIndex;
	}

	public int feature(String comment, int statementIndex, List<Statement> statementList, List<String> codeList,
			List<Token> tokenList,CompilationUnit unit) {
		if(statementList.size()<=comparedIndex) {
			return 0;
		}
		Statement currentStatement = statementList.get(statementIndex);
		Statement compareStatement = statementList.get(comparedIndex);
		
		int c_startLine = unit.getLineNumber(currentStatement.getStartPosition());
		int c_endLine = unit.getLineNumber(currentStatement.getStartPosition()+currentStatement.getLength()-1);
		
		int f_startLine = unit.getLineNumber(compareStatement.getStartPosition());
		int f_endLine = unit.getLineNumber(compareStatement.getStartPosition()+compareStatement.getLength()-1);
		
		
		Set<String> variableList = new HashSet<String>();
		for(Token token:tokenList) {
			if(token.getTokenName().equals("VariableDeclarationStatement")) {
				String keyword = token.getKeyword();
				if(keyword.indexOf(",")>=0) {
					keyword = keyword.substring(keyword.indexOf(",")+1);
				}
				variableList.add(keyword);
			}
		}
		
		String splitToken = " .,;:/&|`~%+=-*<>$#@!^\\()[]{}''\"\r\n\t";
		
		Set<String> c_variableList = new HashSet<String>();
		for(int i=c_startLine-1;i<c_endLine;i++) {
		    String code = codeList.get(i);
			StringTokenizer st = new StringTokenizer(code,splitToken,false);
			while(st.hasMoreTokens()) {
				String variable = st.nextToken();
				if(variableList.contains(variable)) {
				    c_variableList.add(variable);
				}
			}
		}
		
		for(int i=f_startLine-1;i<f_endLine;i++) {
			String code = codeList.get(i);
			StringTokenizer st = new StringTokenizer(code,splitToken,false);
			while(st.hasMoreTokens()) {
			    String variable = st.nextToken();
			    if(c_variableList.contains(variable)) {
			    	return 1;
			    }
			}
		}
		
		return 0;
		
	}

}
