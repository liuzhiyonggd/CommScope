package sysu.CommScope.features;

import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Statement;

import sysu.CommScope.bean.Token;
import sysu.CommScope.splitword.WordSpliter;

public class CommentStatementSimilarity implements Feature{

	public int feature(String comment, int statementIndex, List<Statement> statementList, List<String> codeList,
			List<Token> tokenList, CompilationUnit unit) {
		List<String> commentWordList = WordSpliter.split(comment, true);
		
		String statementString = statementList.get(statementIndex).toString();
		List<String> statementWordList = WordSpliter.split(statementString, false);
		
		int result = 0;
		for(String word:commentWordList) {
			if(statementWordList.contains(word)) {
				result++;
			}
		}
		return result;
	}

}
