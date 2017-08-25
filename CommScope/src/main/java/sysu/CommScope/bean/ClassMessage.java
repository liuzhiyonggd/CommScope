package sysu.CommScope.bean;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection="class_message2")
public class ClassMessage {

	@Field("class_id")
	private int classID;
	
	private String project;
	
	@Field("commit_id")
	private String commitID;
	
	@Field("class_name")
	private String className;
	
	private String type;
	
	@Field("old_code")
	private List<Line> oldCode;  
	
	@Field("new_code")
	private List<Line> newCode;
	
	@Field("new_tokens")
	private List<Token> newTokenList;
	
	@Field("old_tokens")
	private List<Token> oldTokenList;
	
	@Field("diffs")
	private List<DiffType> diffList;
	
	@Field("new_comments")
	private List<CodeComment> newComment;
	
	@Field("old_comments")
	private List<CodeComment> oldComment;
	
	@Field("iscore_probability")
	private double isCoreProbability;
	
	
	
	public int getClassID() {
		return classID;
	}
	public void setClassID(int classID) {
		this.classID = classID;
	}
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public List<Token> getNewTokenList() {
		return newTokenList;
	}
	public void setNewTokenList(List<Token> newTokenList) {
		this.newTokenList = newTokenList;
	}
	public List<Token> getOldTokenList() {
		return oldTokenList;
	}
	public void setOldTokenList(List<Token> oldTokenList) {
		this.oldTokenList = oldTokenList;
	}
	public List<DiffType> getDiffList() {
		return diffList;
	}
	public void setDiffList(List<DiffType> diffList) {
		this.diffList = diffList;
	}
	public List<CodeComment> getNewComment() {
		return newComment;
	}
	public void setNewComment(List<CodeComment> newComment) {
		this.newComment = newComment;
	}
	public List<CodeComment> getOldComment() {
		return oldComment;
	}
	public void setOldComment(List<CodeComment> oldComment) {
		this.oldComment = oldComment;
	}
	public List<Line> getNewCode() {
		return newCode;
	}
	public void setNewCode(List<Line> newCode) {
		this.newCode = newCode;
	}
	public List<Line> getOldCode() {
		return oldCode;
	}
	public void setOldCode(List<Line> oldCode) {
		this.oldCode = oldCode;
	}
	public double getIsCoreProbability() {
		return isCoreProbability;
	}
	public void setIsCoreProbability(double isCoreProbability) {
		this.isCoreProbability = isCoreProbability;
	}
	
}
