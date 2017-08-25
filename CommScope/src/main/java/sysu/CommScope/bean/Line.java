package sysu.CommScope.bean;

import org.springframework.data.mongodb.core.mapping.Field;

public class Line {
	@Field("line_number")
	private int lineNumber;
	private String line;
	public int getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	public String getLine() {
		return line;
	}
	public void setLine(String line) {
		this.line = line;
	}
	
	
}
