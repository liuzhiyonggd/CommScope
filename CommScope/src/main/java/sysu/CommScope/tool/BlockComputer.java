package sysu.CommScope.tool;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

public class BlockComputer {
	
	public static List<Block> getBlock(Statement statement, List<Statement> statementList) {

		List<Block> blockList = new ArrayList<Block>();
		if (statement instanceof IfStatement) {
			IfStatement ifStatement = (IfStatement) statement;
			Statement thenStatement = ifStatement.getThenStatement();
			if (thenStatement instanceof Block) {
				Block block = (Block) thenStatement;
				blockList.add(block);
			} else if (thenStatement != null) {
				statementList.add(thenStatement);
			}
			Statement elseStatement = ifStatement.getElseStatement();

			if (elseStatement instanceof Block) {
				Block block = (Block) elseStatement;
				blockList.add(block);
			}
			if (elseStatement != null) {
				statementList.add(elseStatement);
			}

			return blockList;
		}

		if (statement instanceof WhileStatement) {
			WhileStatement whileStatement = (WhileStatement) statement;
			Statement whileBody = whileStatement.getBody();
			if (whileBody instanceof Block) {
				Block block = (Block) whileBody;
				blockList.add(block);
			}
			return blockList;
		}

		if (statement instanceof ForStatement) {
			ForStatement forStatement = (ForStatement) statement;
			Statement forBody = forStatement.getBody();
			if (forBody instanceof Block) {
				Block block = (Block) forBody;
				blockList.add(block);
			}
			return blockList;
		}

		if (statement instanceof EnhancedForStatement) {
			EnhancedForStatement forStatement = (EnhancedForStatement) statement;
			Statement forBody = forStatement.getBody();
			if (forBody instanceof Block) {
				Block block = (Block) forBody;
				blockList.add(block);
			}
			return blockList;
		}

		if (statement instanceof TryStatement) {
			TryStatement tryStatement = (TryStatement) statement;
			Statement tryBody = tryStatement.getBody();
			if (tryBody instanceof Block) {
				Block block = (Block) tryBody;
				blockList.add(block);
			}
			List<CatchClause> catchClauses = tryStatement.catchClauses();
			if (catchClauses != null && catchClauses.size() > 0) {
				for (CatchClause clause : catchClauses) {
					Statement sta = clause.getBody();
					if (sta instanceof Block) {
						Block block = (Block) sta;
						blockList.add(block);
					}
				}
			}

			Statement finallyBody = tryStatement.getFinally();
			if (finallyBody instanceof Block) {
				Block block = (Block) finallyBody;
				blockList.add(block);
			}
			if (finallyBody != null) {
				statementList.add(finallyBody);
			}
			return blockList;
		}

		if (statement instanceof SwitchStatement) {
			SwitchStatement switchStatement = (SwitchStatement) statement;
			List<Statement> switchStatementList = switchStatement.statements();
			statementList.addAll(switchStatementList);
			for (Statement sta : switchStatementList) {
				List<Block> bList = getBlock(sta, statementList);
				blockList.addAll(bList);
			}
			return blockList;
		}

		if (statement instanceof Block) {
			blockList.add((Block) statement);
			return blockList;
		}
		return blockList;
	}

}
