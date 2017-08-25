package sysu.CommScope.features;

import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import sysu.CommScope.bean.Token;

public interface Feature {
	
	int feature(String comment,int statementIndex,List<Statement> statementList,List<String> codeList,List<Token> tokenList,CompilationUnit unit);

}
