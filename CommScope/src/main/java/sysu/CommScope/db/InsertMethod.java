package sysu.CommScope.db;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import sysu.CommScope.bean.ClassMessage;
import sysu.CommScope.bean.Method;
import sysu.CommScope.bean.MethodMaxID;
import sysu.CommScope.bean.Token;
import sysu.CommScope.repository.ClassMessageRepository;
import sysu.CommScope.repository.MethodMaxIdRepository;
import sysu.CommScope.repository.MethodRepository;
import sysu.CommScope.repository.RepositoryFactory;

public class InsertMethod {
	private static ClassMessageRepository classRepo = RepositoryFactory.getClassRepository();
	private static MethodMaxIdRepository methodMaxIdRepo = RepositoryFactory.getMethodMaxRepository();
	private static MethodRepository methodRepo = RepositoryFactory.getMethodRepository();
	public static void insert(int startClassID,int endClassID) {
		
		for(int classID=startClassID;classID<=endClassID;classID++) {
			ClassMessage clazz = classRepo.findASingleByClassID(classID);
			List<String> codes = clazz.getCodeList();
			List<Token> tokens = clazz.getTokenList();
			
			ASTParser parser = ASTParser.newParser(AST.JLS8);
			
			StringBuilder sb = new StringBuilder();
			for(String str:codes) {
				sb.append(str).append("\n");
			}
			String codeString = sb.toString();
			parser.setSource(codeString.toCharArray());
			CompilationUnit unit = (CompilationUnit)parser.createAST(null);
			
			for(Object obj:unit.types()) {
				TypeDeclaration type = (TypeDeclaration)obj;
				
				if(type.getMethods()==null||type.getMethods().length<=0) {
					continue;
				}
				
				for(MethodDeclaration method:type.getMethods()) {
					Method methodBean = new Method();
					methodBean.setClassID(clazz.getClassID());
					
					synchronized (methodMaxIdRepo) {
						MethodMaxID methodMaxId = methodMaxIdRepo.findAll().get(0);
						methodMaxId.setMaxID(methodMaxId.getMaxID() + 1);
						methodMaxIdRepo.save(methodMaxId);
						methodBean.setMethodID(methodMaxId.getMaxID());
					}
					
					methodBean.setStartLine(unit.getLineNumber(method.getStartPosition()));
					methodBean.setEndLine(unit.getLineNumber(method.getStartPosition()+method.getLength()-1));
					
					List<String> methodCodes = new ArrayList<String>();
					for(int i=methodBean.getStartLine()-1;i<methodBean.getEndLine();i++) {
						methodCodes.add(codes.get(i));
					}
					methodBean.setCodes(methodCodes);
					
					List<Token> methodTokens = new ArrayList<Token>();
					for(Token token:tokens) {
						if(token.getStartLine()>=methodBean.getStartLine()&&token.getEndLine()<=methodBean.getEndLine()) {
							methodTokens.add(token);
						}
					}
					methodBean.setTokens(methodTokens);
					methodRepo.insert(methodBean);
					
				}
				
			}
			
		System.out.println(classID + " is done.");	
		}
	}
	
	public static void main(String[] args) {
		insert(540683, 548373);
	}
	

}
