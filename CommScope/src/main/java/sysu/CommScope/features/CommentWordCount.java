package sysu.CommScope.features;

import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Statement;

import sysu.CommScope.bean.Token;

public class CommentWordCount implements Feature{

	public int feature(String comment, int statementIndex, List<Statement> statementList, List<String> codeList,
			List<Token> tokenList, CompilationUnit unit) {
		
		String splitToken = " .,;:/&|`~%+=-*<>$#@!^\\()[]{}''\"\r\n\t";
		StringTokenizer st = new StringTokenizer(comment,splitToken,false);
		return st.countTokens()/5;
	}
	
	

}
