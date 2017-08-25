package sysu.CommScope.bean;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection="comment5")
public class CommentEntry {
	
	private String project;
	
	@Field("commit_id")
	private String commitID;
	
	@Field("class_name")
	private String className;
	
	@Field("comment_id")
	private int commentID;
	
	private String type;
	
	@Field("new_code")
	private List<String> newCode = new ArrayList<String>();
	
	@Field("old_code")
	private List<String> oldCode = new ArrayList<String>();
	
	@Field("new_comment")
	private List<String> newComment = new ArrayList<String>();
	
	@Field("old_comment")
	private List<String> oldComment = new ArrayList<String>();
	
	@Field("new_token")
	private List<Token> newToken = new ArrayList<Token>();
	
	@Field("old_token")
	private List<Token> oldToken = new ArrayList<Token>();
	
	@Field("ischange")
	private boolean isChange;
	
	@Field("ischange2")
	private boolean isChange2;
	
	@Field("diffs")
	private List<DiffType> diffList = new ArrayList<DiffType>();
	
	@Field("new_scope_startline")
	private int new_scope_startLine;
	
	@Field("new_scope_endline")
	private int new_scope_endLine;
	
	@Field("old_scope_startline")
	private int old_scope_startLine;
	
	@Field("old_scope_endline")
	private int old_scope_endLine;
	
	@Field("new_comment_startline")
	private int new_comment_startLine;
	
	@Field("new_comment_endline")
	private int new_comment_endLine;

	@Field("old_comment_startline")
	private int old_comment_startLine;
	
	@Field("old_comment_endline")
	private int old_comment_endLine;
	
	@Field("isverify")
	private boolean isVerify;
	
	@Field("ischange_probability")
	private double isChangeProbability;
	
	private boolean refactor1;
	private boolean refactor2;
	private boolean refactor3;
	private boolean refactor4;

	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public String getCommitID() {
		return commitID;
	}
	public void setCommitID(String commitID) {
		this.commitID = commitID;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<DiffType> getDiffList() {
		return diffList;
	}
	public void setDiffList(List<DiffType> diffList) {
		this.diffList = diffList;
	}
	public boolean isChange() {
		return isChange;
	}
	public void setChange(boolean isChange) {
		this.isChange = isChange;
	}
	
	
	public boolean isChange2() {
		return isChange2;
	}
	public void setChange2(boolean isChange2) {
		this.isChange2 = isChange2;
	}
	public List<String> getNewCode() {
		return newCode;
	}
	public void setNewCode(List<String> newCode) {
		this.newCode = newCode;
	}
	public List<String> getOldCode() {
		return oldCode;
	}
	public void setOldCode(List<String> oldCode) {
		this.oldCode = oldCode;
	}
	public List<String> getNewComment() {
		return newComment;
	}
	public void setNewComment(List<String> newComment) {
		this.newComment = newComment;
	}
	public List<String> getOldComment() {
		return oldComment;
	}
	public void setOldComment(List<String> oldComment) {
		this.oldComment = oldComment;
	}
	public int getNew_comment_startLine() {
		return new_comment_startLine;
	}
	public void setNew_comment_startLine(int new_comment_startLine) {
		this.new_comment_startLine = new_comment_startLine;
	}
	public int getOld_comment_startLine() {
		return old_comment_startLine;
	}
	public void setOld_comment_startLine(int old_comment_startLine) {
		this.old_comment_startLine = old_comment_startLine;
	}
	public int getNew_comment_endLine() {
		return new_comment_endLine;
	}
	public void setNew_comment_endLine(int new_comment_endLine) {
		this.new_comment_endLine = new_comment_endLine;
	}
	public int getOld_comment_endLine() {
		return old_comment_endLine;
	}
	public void setOld_comment_endLine(int old_comment_endLine) {
		this.old_comment_endLine = old_comment_endLine;
	}
	public int getNew_scope_startLine() {
		return new_scope_startLine;
	}
	public void setNew_scope_startLine(int new_scope_startLine) {
		this.new_scope_startLine = new_scope_startLine;
	}
	public int getNew_scope_endLine() {
		return new_scope_endLine;
	}
	public void setNew_scope_endLine(int new_scope_endLine) {
		this.new_scope_endLine = new_scope_endLine;
	}
	public int getOld_scope_startLine() {
		return old_scope_startLine;
	}
	public void setOld_scope_startLine(int old_scope_startLine) {
		this.old_scope_startLine = old_scope_startLine;
	}
	public int getOld_scope_endLine() {
		return old_scope_endLine;
	}
	public void setOld_scope_endLine(int old_scope_endLine) {
		this.old_scope_endLine = old_scope_endLine;
	}
	public boolean isVerify() {
		return isVerify;
	}
	public void setVerify(boolean isVerify) {
		this.isVerify = isVerify;
	}
	public double getIsChangeProbability() {
		return isChangeProbability;
	}
	public void setIsChangeProbability(double isChangeProbability) {
		this.isChangeProbability = isChangeProbability;
	}
	public List<Token> getNewToken() {
		return newToken;
	}
	public void setNewToken(List<Token> newToken) {
		this.newToken = newToken;
	}
	public List<Token> getOldToken() {
		return oldToken;
	}
	public void setOldToken(List<Token> oldToken) {
		this.oldToken = oldToken;
	}
	public boolean isRefactor1() {
		return refactor1;
	}
	public void setRefactor1(boolean refactor1) {
		this.refactor1 = refactor1;
	}
	public boolean isRefactor2() {
		return refactor2;
	}
	public void setRefactor2(boolean refactor2) {
		this.refactor2 = refactor2;
	}
	public boolean isRefactor3() {
		return refactor3;
	}
	public void setRefactor3(boolean refactor3) {
		this.refactor3 = refactor3;
	}
	public boolean isRefactor4() {
		return refactor4;
	}
	public void setRefactor4(boolean refactor4) {
		this.refactor4 = refactor4;
	}
	
	
	
}
