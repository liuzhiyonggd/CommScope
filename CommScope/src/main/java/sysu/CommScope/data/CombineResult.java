package sysu.CommScope.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;

public class CombineResult {

	public static void main(String[] args) throws IOException {

		// 9,17
		for (int i = 196; i <= 700; i++) {
			Map<Integer, List<String>> resultMap = new TreeMap<Integer, List<String>>();
			for (int k = 1; k <= 45; k++) {
				System.out.println("load comment " + i + " method result " + k);
				List<String> methodLines = FileUtils.readLines(
						new File("F:/cloneresults3/comment" + i + "_method_" + k + "_result.txt"), "UTF-8");
				for (String str : methodLines) {
					String[] temps = str.split(";");
					int commentID = Integer.parseInt(temps[0]);
					List<String> clones;
					if (resultMap.containsKey(commentID)) {
						clones = resultMap.get(commentID);
					} else {
						clones = new ArrayList<String>();
						resultMap.put(commentID, clones);
					}
					for (int j = 1; j < temps.length; j++) {
						clones.add(temps[j]);
					}
				}
			}

			List<String> output = new ArrayList<String>();
			for (Map.Entry<Integer, List<String>> entry : resultMap.entrySet()) {
				StringBuilder sb = new StringBuilder();
				sb.append(entry.getKey() + ";");
				for (String str : entry.getValue()) {
					sb.append(str + ";");
				}
				output.add(sb.toString());
			}
			
			FileUtils.writeLines(new File("F:/cloneresults3/combine/comment" + i + "_result.txt"), output);
			resultMap = null;
			output = null;
			System.gc();

		}
	}

}
