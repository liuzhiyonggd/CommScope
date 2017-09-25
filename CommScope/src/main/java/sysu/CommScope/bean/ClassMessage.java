package sysu.CommScope.bean;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection="class_message")
public class ClassMessage {

	@Field("class_id")
	private int classID;
	
	private String project;
	
	@Field("class_name")
	private String className;
	
	private String type;
	
	@Field("codes")
	private List<String> codeList;  
	
	
	@Field("tokens")
	private List<Token> tokenList;
	
	@Field("comment")
	private List<CodeComment> comment;

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

	public List<CodeComment> getComment() {
		return comment;
	}

	public void setComment(List<CodeComment> comment) {
		this.comment = comment;
	}
	
}
