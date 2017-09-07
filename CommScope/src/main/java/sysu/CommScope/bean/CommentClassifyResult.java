package sysu.CommScope.bean;

import java.util.ArrayList;
import java.util.List;

public class CommentClassifyResult {
	
	private int commentID;
	private List<Integer> realLabelList = new ArrayList<Integer>();
	private List<Integer> classifyLabelList = new ArrayList<Integer>();
	public int getCommentID() {
		return commentID;
	}
	public void setCommentID(int commentID) {
		this.commentID = commentID;
	}
	public List<Integer> getRealLabelList() {
		return realLabelList;
	}
	public void setRealLabelList(List<Integer> realLabelList) {
		this.realLabelList = realLabelList;
	}
	public List<Integer> getClassifyLabelList() {
		return classifyLabelList;
	}
	public void setClassifyLabelList(List<Integer> classifyLabelList) {
		this.classifyLabelList = classifyLabelList;
	}
}
