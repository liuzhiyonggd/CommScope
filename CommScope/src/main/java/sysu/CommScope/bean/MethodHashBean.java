package sysu.CommScope.bean;

import java.util.List;

public class MethodHashBean {
	
	private int methodID;
	private int classID;
	private List<MethodLine> lines;
	private int startLine;
	private List<LineSimilar> similarList;
	public int getMethodID() {
		return methodID;
	}
	public void setMethodID(int methodID) {
		this.methodID = methodID;
	}
	public int getClassID() {
		return classID;
	}
	public void setClassID(int classID) {
		this.classID = classID;
	}
	public List<MethodLine> getLines() {
		return lines;
	}
	public void setLines(List<MethodLine> lines) {
		this.lines = lines;
	}
	public int getStartLine() {
		return startLine;
	}
	public void setStartLine(int startLine) {
		this.startLine = startLine;
	}
	public List<LineSimilar> getSimilarList() {
		return similarList;
	}
	public void setSimilarList(List<LineSimilar> similarList) {
		this.similarList = similarList;
	}
	
	public class MethodLine{
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

	public class LineSimilar{
		private int lineNumber;
		private double similar;
		public int getLineNumber() {
			return lineNumber;
		}
		public void setLineNumber(int lineNumber) {
			this.lineNumber = lineNumber;
		}
		public double getSimilar() {
			return similar;
		}
		public void setSimilar(double similar) {
			this.similar = similar;
		}
		
	}
}


