package sysu.CommScope.features;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Statement;

import sysu.CommScope.bean.Token;

public class SameMethodInvoked implements Feature{
	
	private int comparedIndex;
	
	public SameMethodInvoked(int comparedIndex) {
		this.comparedIndex = comparedIndex;
	}

	public int feature(String comment, int statementIndex, List<Statement> statementList, List<String> codeList,
			List<Token> tokenList, CompilationUnit unit) {
		if(comparedIndex>=statementList.size()) {
			return 0;
		}
		if(comparedIndex<0) {
			return 0;
		}
		Statement currentStatement = statementList.get(statementIndex);
		Statement compareStatement = statementList.get(comparedIndex);
		
		int c_startLine = unit.getLineNumber(currentStatement.getStartPosition());
		int c_endLine = unit.getLineNumber(currentStatement.getStartPosition()+currentStatement.getLength()-1);
		
		int p_startLine = unit.getLineNumber(compareStatement.getStartPosition());
		int p_endLine = unit.getLineNumber(compareStatement.getStartPosition()+compareStatement.getLength()-1);
		
		Set<String> c_methodInvokeList = new HashSet<String>();
		Set<String> p_methodInvokeList = new HashSet<String>();
		
		for(int i=0;i<tokenList.size();i++) {
			Token token = tokenList.get(i);
			if(token.getTokenName().equals("MethodInvocation")) {
			if(token.getStartLine()>=c_startLine&&token.getEndLine()<=c_endLine) {
			    c_methodInvokeList.add(tokenList.get(i+1).getKeyword());
			}
			if(token.getStartLine()>=p_startLine&&token.getEndLine()<=p_endLine) {
			    p_methodInvokeList.add(tokenList.get(i+1).getKeyword());
			}
			}
		}
		
		for(String invoke:c_methodInvokeList) {
			if(p_methodInvokeList.contains(invoke)) {
				return 1;
			}
		}
		return 0;
	}

}
