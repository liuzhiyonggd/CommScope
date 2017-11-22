package sysu.CommScope.bean;

import java.util.List;

public class CommentHashBean {
	
	private int commentID;
	private int classID;
	private List<CommentLine> commentLines;
	public int getCommentID() {
		return commentID;
	}
	public void setCommentID(int commentID) {
		this.commentID = commentID;
	}
	public int getClassID() {
		return classID;
	}
	public void setClassID(int classID) {
		this.classID = classID;
	}
	public List<CommentLine> getCommentLines() {
		return commentLines;
	}
	public void setCommentLines(List<CommentLine> commentLines) {
		this.commentLines = commentLines;
	}
	
	public class CommentLine{
		private int lineNumber;
		private List<Integer> hashList;
		public int getLineNumber() {
			return lineNumber;
		}
		public void setLineNumber(int lineNumber) {
			this.lineNumber = lineNumber;
		}
		public List<Integer> getHashList() {
			return hashList;
		}
		public void setHashList(List<Integer> hashList) {
			this.hashList = hashList;
		}
	}
}


