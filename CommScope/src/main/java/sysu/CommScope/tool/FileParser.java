package sysu.CommScope.tool;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Statement;

import sysu.CommScope.ast.UnitCompiler;
import sysu.CommScope.bean.CodeComment;

public class FileParser {
	
	public static List<CodeComment> parseFile2CommentList(List<String> codes){
		UnitCompiler compiler = new UnitCompiler(codes);
		CompilationUnit unit = compiler.getUnit();
		List<Method> methodList = compiler.getMethodList();
		List<CodeComment> codeCommentList = new ArrayList<CodeComment>();
		for(Method method:methodList) {
			List<CodeComment> methodCommentList = method.getCodeCommentList();
			List<Statement> methodStatementList = method.getStatementList();
			System.out.println("method statement list size:"+methodStatementList.size()+" method start line:"+method.getStartLine());
			for(int i=0,n=methodCommentList.size();i<n;i++) {
				CodeComment codeComment = methodCommentList.get(i);
				int scopeEndLine = CommentScopeTool.computeCommentScope(codeComment, i, methodCommentList, method.getStatementList(), unit, method.getEndLine());
			    codeComment.setScopeEndLine(scopeEndLine);
			    codeCommentList.add(codeComment);
			}
		}
		
		return codeCommentList;
	}

}
