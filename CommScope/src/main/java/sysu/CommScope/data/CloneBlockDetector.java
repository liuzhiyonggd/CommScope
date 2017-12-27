package sysu.CommScope.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import sysu.CommScope.bean.CommentHashBean;
import sysu.CommScope.bean.MethodHashBean;
import sysu.CommScope.tool.SimilarComputer;

public class CloneBlockDetector {

	public static List<String> detect(String commentPath, String methodPath) throws IOException {
		List<String> result = new ArrayList<String>();
		List<String> commentLines = FileUtils.readLines(new File(commentPath), "UTF-8");

		// 读取comment文件
		System.out.println("load comment file...");
		List<CommentHashBean> commentList = new ArrayList<CommentHashBean>();
		for (String str : commentLines) {
			CommentHashBean commentHashBean = new CommentHashBean();
			String[] temps = str.split(";");

			int classID = Integer.parseInt(temps[0]);
			int commentID = Integer.parseInt(temps[1]);

			commentHashBean.setClassID(classID);
			commentHashBean.setCommentID(commentID);

			List<CommentHashBean.CommentLine> lineList = new ArrayList<CommentHashBean.CommentLine>();
			for (int i = 2; i < temps.length; i++) {
				String[] temps2 = temps[i].split(" ");

				CommentHashBean.CommentLine commentLine = commentHashBean.new CommentLine();
				int line = Integer.parseInt(temps2[0]);
				List<Integer> hashList = new ArrayList<Integer>();
				for (int j = 1; j < temps2.length; j++) {
					hashList.add(Integer.parseInt(temps2[j]));
				}
				commentLine.setLineNumber(line);
				commentLine.setHashList(hashList);
				lineList.add(commentLine);
			}
			commentHashBean.setCommentLines(lineList);
			commentList.add(commentHashBean);
		}
		System.out.println("load comment file done...");

		// 读取methods文件
		System.out.println("load method files... ");
		List<MethodHashBean> methodList = new ArrayList<MethodHashBean>();
		File methodFile = new File(methodPath);

		List<String> methodLines = FileUtils.readLines(methodFile, "UTF-8");
		for (String str : methodLines) {
			String[] temps = str.split(";");

			MethodHashBean methodHashBean = new MethodHashBean();
			int classID = Integer.parseInt(temps[0]);
			int methodID = Integer.parseInt(temps[1]);
			methodHashBean.setClassID(classID);
			methodHashBean.setMethodID(methodID);

			List<MethodHashBean.MethodLine> methodLineList = new ArrayList<MethodHashBean.MethodLine>();
			for (int i = 2; i < temps.length; i++) {
				String[] temps2 = temps[i].split(" ");
				MethodHashBean.MethodLine methodLine = methodHashBean.new MethodLine();
				int lineNumber = Integer.parseInt(temps2[0]);
				methodLine.setLineNumber(lineNumber);

				List<Integer> hashList = new ArrayList<Integer>();
				for (int j = 1; j < temps2.length; j++) {
					int hashNumber = Integer.parseInt(temps2[j]);
					hashList.add(hashNumber);
				}
				methodLine.setHashList(hashList);
				methodLineList.add(methodLine);
			}
			methodHashBean.setLines(methodLineList);
			methodList.add(methodHashBean);
		}

		System.out.println("load method file done... ");

		int commentCount = 0;
		for (CommentHashBean comment : commentList) {
			if (comment.getCommentLines().size() <= 1) {
				continue;
			}
			commentCount++;
			if (commentCount % 100 == 0) {
				System.out.println(commentCount + " is done.");
			}
			StringBuilder sb = new StringBuilder();
			sb.append(comment.getCommentID() + ";");
			List<MethodHashBean> candidateMethodList = methodList;
			for (int i = 0; i < comment.getCommentLines().size(); i++) {
				candidateMethodList = computeSimilar(comment, candidateMethodList, i);
			}

			for (MethodHashBean method : candidateMethodList) {
				sb.append(method.getMethodID() + " ").append(method.getStartLine() + " ");
				List<MethodHashBean.LineSimilar> similarList = method.getSimilarList();
				if (similarList != null) {
					for (MethodHashBean.LineSimilar lineSimilar : similarList) {
						sb.append(lineSimilar.getSimilar() + " ");
					}
				}
				sb.append(";");

			}
			result.add(sb.toString());
		}

		return result;
	}

	private static List<MethodHashBean> computeSimilar(CommentHashBean commentHashBean, List<MethodHashBean> methodList,
			int lineIndex) {

		List<MethodHashBean> returnMethodList = null;

		// 以第一行的相似度大于0.8且不与comment在同一个类中的条件对methodList进行过滤
		if (lineIndex == 0) {
			returnMethodList = new ArrayList<MethodHashBean>();
			List<Integer> commentHashList = commentHashBean.getCommentLines().get(0).getHashList();
			for (MethodHashBean methodHashBean : methodList) {
				if (methodHashBean.getClassID() == commentHashBean.getClassID()) {
					continue;
				}
				for (int startLine = 0; startLine < methodHashBean.getLines().size(); startLine++) {
					List<Integer> methodHashList = methodHashBean.getLines().get(startLine).getHashList();
					double sim = SimilarComputer.jaccard(commentHashList, methodHashList);
					if (sim > 0.99d) {
						MethodHashBean candidateMethodHashBean = new MethodHashBean();
						candidateMethodHashBean.setClassID(methodHashBean.getClassID());
						candidateMethodHashBean.setMethodID(methodHashBean.getMethodID());
						candidateMethodHashBean.setLines(methodHashBean.getLines());
						candidateMethodHashBean.setStartLine(startLine);

						List<MethodHashBean.LineSimilar> similarList = new ArrayList<MethodHashBean.LineSimilar>();
						MethodHashBean.LineSimilar lineSimilar = candidateMethodHashBean.new LineSimilar();
						lineSimilar.setLineNumber(candidateMethodHashBean.getLines().get(startLine).getLineNumber());
						lineSimilar.setSimilar(sim);
						similarList.add(lineSimilar);
						candidateMethodHashBean.setSimilarList(similarList);
						returnMethodList.add(candidateMethodHashBean);
					}
				}
			}

		} else if (lineIndex == 1) {
			List<Integer> commentHashList = commentHashBean.getCommentLines().get(lineIndex).getHashList();
			returnMethodList = new ArrayList<MethodHashBean>();
			for (MethodHashBean methodHashBean : methodList) {
				int startLine = methodHashBean.getStartLine();
				if (startLine + lineIndex >= methodHashBean.getLines().size()) {
					continue;
				}
				List<Integer> methodHashList = methodHashBean.getLines().get(startLine + lineIndex).getHashList();
				double sim = SimilarComputer.jaccard(commentHashList, methodHashList);
				if (sim < 0.99d) {
					continue;
				}
				MethodHashBean.LineSimilar lineSimilar = methodHashBean.new LineSimilar();
				lineSimilar.setLineNumber(methodHashBean.getLines().get(startLine + lineIndex).getLineNumber());
				lineSimilar.setSimilar(sim);
				methodHashBean.getSimilarList().add(lineSimilar);
				returnMethodList.add(methodHashBean);
			}

		} else {
			List<Integer> commentHashList = commentHashBean.getCommentLines().get(lineIndex).getHashList();
			for (MethodHashBean methodHashBean : methodList) {
				int startLine = methodHashBean.getStartLine();
				if (startLine + lineIndex >= methodHashBean.getLines().size()) {
					continue;
				}
				List<Integer> methodHashList = methodHashBean.getLines().get(startLine + lineIndex).getHashList();
				double sim = SimilarComputer.jaccard(commentHashList, methodHashList);
				MethodHashBean.LineSimilar lineSimilar = methodHashBean.new LineSimilar();
				lineSimilar.setLineNumber(methodHashBean.getLines().get(startLine + lineIndex).getLineNumber());
				lineSimilar.setSimilar(sim);
				methodHashBean.getSimilarList().add(lineSimilar);
			}
			returnMethodList = methodList;
		}

		return returnMethodList;
	}

	public static void main(String[] args) throws IOException {
		for (int i = 1; i <= 7900; i++) {
			for (int j = 1; j <= 45; j++) {
				List<String> result = detect("f:/commentfiles3/comment" + i + ".txt",
						"f:/methodfiles2/method_" + j + ".txt");
				FileUtils.writeLines(new File("f:/cloneresults3/comment" + i + "_method_" + j + "_result.txt"), result);
				System.out.println("comment file " + i + " method file "+j+" done.");
			}
		}
	}

}
