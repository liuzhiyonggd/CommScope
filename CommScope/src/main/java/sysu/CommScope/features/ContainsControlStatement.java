package sysu.CommScope.features;

import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Statement;

import sysu.CommScope.bean.Token;

public class ContainsControlStatement implements Feature {

	private String containsStatementType;
	private int comparedIndex;

	public ContainsControlStatement(int containsStatementType, int comparedStatementIndex) {
		if (containsStatementType == Statement.IF_STATEMENT) {
			this.containsStatementType = "IfStatement";
		} else if (containsStatementType == Statement.FOR_STATEMENT) {
			this.containsStatementType = "ForStatement";
		} else if (containsStatementType == Statement.WHILE_STATEMENT) {
			this.containsStatementType = "WhileStatement";
		} else {
			this.containsStatementType = "ERROR";
		}
	}

	public int feature(String comment, int statementIndex, List<Statement> statementList, List<String> codeList,
			List<Token> tokenList, CompilationUnit unit) {

		if (statementList.size() < comparedIndex) {
			return 0;
		}

		Statement currentStatement = statementList.get(statementIndex);
		Statement compareStatement = statementList.get(comparedIndex);

		int c_startLine = unit.getLineNumber(currentStatement.getStartPosition());
		int c_endLine = unit.getLineNumber(currentStatement.getStartPosition() + currentStatement.getLength() - 1);

		int p_startLine = unit.getLineNumber(compareStatement.getStartPosition());
		int p_endLine = unit.getLineNumber(compareStatement.getStartPosition() + compareStatement.getLength() - 1);

		for (Token token : tokenList) {
			if (c_startLine > p_startLine) {
				if (token.getStartLine() > p_endLine && token.getEndLine() < c_startLine) {
					if (token.getTokenName().equals(containsStatementType)) {
						return 1;
					}
				}
			} else {
				if (token.getStartLine() > c_endLine && token.getEndLine() < p_startLine) {
					if (token.getTokenName().equals(containsStatementType)) {
						return 1;
					}
				}
			}
		}
		return 0;
	}

}
