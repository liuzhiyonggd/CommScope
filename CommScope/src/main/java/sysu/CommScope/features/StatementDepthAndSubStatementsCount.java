package sysu.CommScope.features;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import sysu.CommScope.bean.Token;
import sysu.CommScope.tool.BlockComputer;

public class StatementDepthAndSubStatementsCount implements Feature {

	public int feature(String comment, int statementIndex, List<Statement> statementList, List<String> codeList,
			List<Token> tokenList, CompilationUnit unit) {
		Statement currStatement = statementList.get(statementIndex);
		Queue<Block> firstQueue = new LinkedList<Block>();
		Queue<Block> secondQueue = new LinkedList<Block>();
		List<Statement> staList = new ArrayList<Statement>();
		int depth = 0;
		List<Block> blockList = BlockComputer.getBlock(currStatement, staList);
		firstQueue.addAll(blockList);
		while (!firstQueue.isEmpty() || !secondQueue.isEmpty()) {
			depth++;
			if (!firstQueue.isEmpty()) {
				while (!firstQueue.isEmpty()) {
					Block block = firstQueue.poll();
					List<Statement> tStatementList = block.statements();
					staList.addAll(tStatementList);
					for (Statement sta : tStatementList) {
						secondQueue.addAll(BlockComputer.getBlock(sta, staList));
					}
				}
			} else {
				while (!secondQueue.isEmpty()) {
					Block block = secondQueue.poll();
					List<Statement> tStatementList = block.statements();
					staList.addAll(tStatementList);
					for (Statement sta : tStatementList) {
						firstQueue.addAll(BlockComputer.getBlock(sta, staList));
					}
				}
			}
		}
		return depth * 10000 + staList.size();
	}

	public static void main(String[] args) throws IOException {
		StatementDepthAndSubStatementsCount counter = new StatementDepthAndSubStatementsCount();

		ASTParser parser = ASTParser.newParser(AST.JLS8);
		List<String> javaLines = FileUtils.readLines(new File("file/Test.java"), "UTF-8");
		StringBuilder sb = new StringBuilder();
		for (String str : javaLines) {
			sb.append(str).append("\n");
		}
		String codeString = sb.toString();
		parser.setSource(codeString.toCharArray());
		CompilationUnit unit = (CompilationUnit) parser.createAST(null);

		TypeDeclaration type = (TypeDeclaration) unit.types().get(0);
		for (MethodDeclaration method : type.getMethods()) {
			List<Statement> statements = method.getBody().statements();
			for (int i = 0; i < statements.size(); i++) {
				int result = counter.feature(null, i, statements, null, null, null);
				System.out.println(i + ": depth " + result / 10000 + " substatements " + result % 10000);
			}
		}
	}

}
