package sysu.CommScope.ast;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import sysu.CommScope.bean.CodeComment;
import sysu.CommScope.tool.Method;

public class UnitCompiler {
	private CompilationUnit unit;
	private List<String> codes;
	private List<Method> methodList;
	
	public UnitCompiler(List<String> codes) {
		this.codes = codes;
	}
	public CompilationUnit getUnit() {
		
		if(unit!=null) {
			return unit;
		}
		
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		
		StringBuilder sb = new StringBuilder();
		for(String str:codes) {
			sb.append(str).append("\n");
		}
		String codeString = sb.toString();
		parser.setSource(codeString.toCharArray());
		unit = (CompilationUnit)parser.createAST(null);
		
		return unit;
	}
	
	@SuppressWarnings("unchecked")
	public List<Method> getMethodList(){
		
		if(methodList!=null) {
			return methodList;
		}
		
		if(unit==null) {
			getUnit();
		}
		
		//获取整个文件的注释信息
		List<Comment> commentList = unit.getCommentList();
		List<CodeComment> codeCommentList = new ArrayList<CodeComment>();
		for(Comment comment:commentList) {
			CodeComment codeComment = new CodeComment();
			int startLine = unit.getLineNumber(comment.getStartPosition());
			int endLine = unit.getLineNumber(comment.getStartPosition()+comment.getLength()-1);
			String type;
			if(comment.getNodeType()==Comment.JAVADOC) {
				type = "Javadoc";
			}else if(comment.getNodeType()==Comment.BLOCK) {
				type = "Block";
			}else {
				type = "Line";
			}
			//作用域的endline在后面确认
			codeComment.setStartLine(startLine);
			codeComment.setScopeStartLine(startLine);
			codeComment.setEndLine(endLine);
			codeComment.setType(type);
			codeCommentList.add(codeComment);
		}
		
		methodList = new ArrayList<Method>();
		for(Object obj:unit.types()) {
			if(obj instanceof TypeDeclaration) {
				TypeDeclaration type = (TypeDeclaration) obj;
				MethodDeclaration[] methodDeclarations = type.getMethods();
				
				for(MethodDeclaration methodDeclaration:methodDeclarations) {
					if(methodDeclaration.getBody()==null) {
						continue;
					}
					
					Method method = new Method();
					method.setStartLine(unit.getLineNumber(methodDeclaration.getStartPosition()));
					method.setEndLine(unit.getLineNumber(methodDeclaration.getStartPosition()+methodDeclaration.getLength()-1));
					method.setStatementList(methodDeclaration.getBody().statements());
					
					List<CodeComment> methodCommentList = new ArrayList<CodeComment>();
					for(int i=1;i<codeCommentList.size();i++) {
						CodeComment preComment = codeCommentList.get(i-1);
						CodeComment currentComment = codeCommentList.get(i);
						if(preComment.getEndLine()+1==currentComment.getStartLine()) {
							currentComment.setStartLine(preComment.getStartLine());
							currentComment.setScopeStartLine(preComment.getScopeStartLine());
							codeCommentList.remove(i-1);
							i--;
						}
					}
					for(CodeComment comment:codeCommentList) {
						if(comment.getEndLine()<=method.getEndLine()) {
							if(comment.getStartLine()>method.getStartLine()) {
								//只获取方法内的注释
								methodCommentList.add(comment);
							}
						}else {
							//遍历到超过方法范围的注释时，则跳出
							break;
						}
					}
					method.setCodeCommentList(methodCommentList);
					methodList.add(method);
				}
				
			}
		}
		return methodList;
	}

}
