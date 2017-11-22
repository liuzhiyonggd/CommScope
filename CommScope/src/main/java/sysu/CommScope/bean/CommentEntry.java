package sysu.CommScope.bean;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection="comment")
public class CommentEntry {
	
	@Id
	private String id;
	
	@Field("class_id")
	private int classID;
	
	@Field("class_name")
	private String className;
	
	@Field("comment_id")
	private int commentID;
	
	private String project;
	
	@Field("comment")
	private List<String> comment = new ArrayList<String>();
	
	@Field("codes")
	private List<String> codeList = new ArrayList<String>();
	
	@Field("tokens")
	private List<Token> tokenList = new ArrayList<Token>();
	
	private String type;
	
	@Field("scope_start_line")
	private int scopeStartLine;
	
	@Field("scope_end_line")
	private int scopeEndLine;
	
	@Field("comment_start_line")
	private int commentStartLine;
	
	@Field("comment_end_line")
	private int commentEndLine;
	
	@Field("comment_start_position")
	private int commentStartPosition;

	@Field("verify_scope_end_line")
	private List<Integer> verifyScopeEndLine;
	
	@Field("verify_count")
	private int verifyCount;

	public int getClassID() {
		return classID;
	}

	public void setClassID(int classID) {
		this.classID = classID;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public int getCommentID() {
		return commentID;
	}

	public void setCommentID(int commentID) {
		this.commentID = commentID;
	}

	public List<String> getComment() {
		return comment;
	}

	public void setComment(List<String> comment) {
		this.comment = comment;
	}

	public List<String> getCodeList() {
		return codeList;
	}

	public void setCodeList(List<String> codeList) {
		this.codeList = codeList;
	}

	public List<Token> getTokenList() {
		return tokenList;
	}

	public void setTokenList(List<Token> tokenList) {
		this.tokenList = tokenList;
	}

	public int getScopeStartLine() {
		return scopeStartLine;
	}

	public void setScopeStartLine(int scopeStartLine) {
		this.scopeStartLine = scopeStartLine;
	}

	public int getScopeEndLine() {
		return scopeEndLine;
	}

	public void setScopeEndLine(int scopeEndLine) {
		this.scopeEndLine = scopeEndLine;
	}

	public int getCommentStartLine() {
		return commentStartLine;
	}

	public void setCommentStartLine(int commentStartLine) {
		this.commentStartLine = commentStartLine;
	}

	public int getCommentEndLine() {
		return commentEndLine;
	}

	public void setCommentEndLine(int commentEndLine) {
		this.commentEndLine = commentEndLine;
	}

	public List<Integer> getVerifyScopeEndLine() {
		return verifyScopeEndLine;
	}

	public void setVerifyScopeEndLine(List<Integer> verifyScopeEndLine) {
		this.verifyScopeEndLine = verifyScopeEndLine;
	}
	
	public void addVerifyScopeEndLine(int verify) {
		verifyScopeEndLine.add(verify);
	}

	public int getVerifyCount() {
		return verifyCount;
	}

	public void setVerifyCount(int verifyCount) {
		this.verifyCount = verifyCount;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getCommentStartPosition() {
		return commentStartPosition;
	}

	public void setCommentStartPosition(int commentStartPosition) {
		this.commentStartPosition = commentStartPosition;
	}
	
	
}
