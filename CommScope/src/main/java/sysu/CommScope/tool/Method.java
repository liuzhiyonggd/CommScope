package sysu.CommScope.tool;

import java.util.List;

import org.eclipse.jdt.core.dom.Statement;

import sysu.CommScope.bean.CodeComment;

public class Method {
	
	private int startLine;
	private int endLine;
	private List<CodeComment> codeCommentList;
	private List<Statement> statementList;
	
	public int getStartLine() {
		return startLine;
	}
	public void setStartLine(int startLine) {
		this.startLine = startLine;
	}
	public int getEndLine() {
		return endLine;
	}
	public void setEndLine(int endLine) {
		this.endLine = endLine;
	}
	public List<CodeComment> getCodeCommentList() {
		return codeCommentList;
	}
	public void setCodeCommentList(List<CodeComment> codeCommentList) {
		this.codeCommentList = codeCommentList;
	}
	public List<Statement> getStatementList() {
		return statementList;
	}
	public void setStatementList(List<Statement> statementList) {
		this.statementList = statementList;
	}
	
	

}
