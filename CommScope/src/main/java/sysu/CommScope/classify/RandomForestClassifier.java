package sysu.CommScope.classify;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import sysu.CommScope.bean.CommentClassifyResult;
import weka.attributeSelection.ASSearch;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.classifiers.CostMatrix;
import weka.classifiers.Evaluation;
import weka.classifiers.meta.CostSensitiveClassifier;
import weka.classifiers.trees.RandomForest;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Debug.Random;
import weka.core.converters.ArffLoader;
import weka.core.converters.ConverterUtils.DataSink;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.supervised.attribute.Discretize;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.Standardize;

public class RandomForestClassifier implements Serializable{

	private RandomForest classifier;
	private String trainPath;
	private String testPath;
	private Instances instancesTrain;
	private Instances instancesTest;

	List<String> output = new ArrayList<String>();

	public List<String> getOutput() {
		return output;
	}

	public RandomForestClassifier(String trainPath, String testPath) throws FileNotFoundException, Exception {

		this.testPath = testPath;
		this.trainPath = trainPath;

		File inputFile = new File(trainPath);
		ArffLoader atf = new ArffLoader();

		File testFile = new File(testPath);
		ArffLoader atf2 = new ArffLoader();

		try {
			atf.setFile(inputFile);
			instancesTrain = atf.getDataSet();
			instancesTrain.setClassIndex(instancesTrain.numAttributes() - 1);

			atf2.setFile(testFile);
			instancesTest = atf2.getDataSet();
			instancesTest.setClassIndex(instancesTest.numAttributes() - 1);

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("init error.\n" + e.getMessage());
		}

		classifier = new RandomForest();
	}

	public void run(int iterate, int p, int k, String testFile, String savename) {

		init(iterate, p, k);
		removeID();
		train();
		evaluate();
		getClassifyResult(testPath, savename);
	}

	private void init(int iterate, int percentage, int featuresNum) {
		 classifier.setBagSizePercent(percentage);
		 classifier.setNumFeatures(featuresNum);
		classifier.setNumIterations(iterate);
	}

	private void removeID() {
		Remove remove = new Remove();
		String[] options = new String[] { "-R", "1" };
		try {
			remove.setOptions(options);
			remove.setInputFormat(instancesTrain);
			instancesTrain = Filter.useFilter(instancesTrain, remove);
			instancesTest = Filter.useFilter(instancesTest, remove);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void train() {
		try {
			classifier.buildClassifier(instancesTrain);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("train error.\n" + e.getMessage());
		}
	}

	private void evaluate() {
		Evaluation eval;
		try {
			eval = new Evaluation(instancesTrain);
			eval.evaluateModel(classifier, instancesTest);
			System.out.println(eval.toSummaryString("\nResults\n======\n", false));
			output.add(eval.toSummaryString("\nResults\n======\n", false));
			System.out.println("0 recall:" + eval.recall(0));
			System.out.println("0 precision:" + eval.precision(0));
			System.out.println("1 recall:" + eval.recall(1));
			System.out.println("1 precision:" + eval.precision(1));
		} catch (Exception e) {
			System.out.println("evaluate error.\n" + e.getMessage());
		}

	}

	public List<Double> classify(String testPath) {

		List<Double> result = new ArrayList<Double>();
		File testFile = new File(testPath);

		ArffLoader atf = new ArffLoader();

		try {
			atf.setFile(testFile);
			Instances instancesTest = atf.getDataSet();

			int num = instancesTest.numInstances();
			for (int i = 0; i < num; i++) {
				Double d = classifier.classifyInstance(instancesTest.instance(i));
				result.add(d);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("C45Classifier 测试实例创建不成功.");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("C45Classifier 分类失败.");
		}
		return result;
	}

	public void getClassifyResult(String testPath, String result) {
		File testFile = new File(testPath);
		ArffLoader atf = new ArffLoader();

		List<Integer> commentIDList = new ArrayList<Integer>();
		try {
			atf.setFile(testFile);
			Instances instancesTest2 = atf.getDataSet();
			for (int i = 0, n = instancesTest2.numInstances(); i < n; i++) {
				commentIDList.add((int) instancesTest2.get(i).value(0));
			}
			instancesTest2.deleteAttributeAt(0);
			instancesTest2.setClassIndex(instancesTest2.numAttributes() - 1);
			int num = instancesTest2.numInstances();

			List<String> output = new ArrayList<String>();

			for (int i = 0; i < num; i++) {
				double classifyValue = classifier.classifyInstance(instancesTest2.instance(i));
				double classValue = instancesTest2.instance(i).classValue();
				int commentID = commentIDList.get(i);
				double statementIndex = instancesTest2.instance(i).value(0);
				output.add(commentID + "," + statementIndex + "," + classValue + "," + classifyValue);
			}
			try {
				FileUtils.writeLines(new File(result), output);
			} catch (IOException e) {
				Logger.getLogger(this.getClass()).error("write result error.");
			}

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("C45Classifier 测试实例创建不成功.");
		} catch (Exception e) {

			e.printStackTrace();
			System.out.println("C45Classifier 分类失败.");
		}

	}
	
	public void getCommentPrecision(String result) {
		try {
			List<String> fileList = FileUtils.readLines(new File(result),"UTF-8");
			Map<Integer,List<String>> commentMap = new HashMap<Integer,List<String>>();
			for(String str:fileList) {
				String[] temps = str.split(",");
				int commentID = Integer.parseInt(temps[0]);
				if(commentMap.containsKey(commentID)) {
					commentMap.get(commentID).add(str);
				}else {
					List<String> temp = new ArrayList<String>();
					temp.add(str);
					commentMap.put(commentID, temp);
				}
			}
			
			List<CommentClassifyResult> commentResultList = new ArrayList<CommentClassifyResult>();
			for(Map.Entry<Integer, List<String>> entry:commentMap.entrySet()) {
				CommentClassifyResult commentResult = new CommentClassifyResult();
				commentResult.setCommentID(entry.getKey());
				
				for(String str:entry.getValue()) {
					String[] temps = str.split(",");
					int classValue = (int)Double.parseDouble(temps[2]);
					int classifyValue = (int)Double.parseDouble(temps[3]);
					
					commentResult.getRealLabelList().add(classValue);
					commentResult.getClassifyLabelList().add(classifyValue);
				}
				commentResultList.add(commentResult);
			}
			
			int totalNum = commentResultList.size();
			int rightNum = 0;
			for(CommentClassifyResult commentResult:commentResultList) {
				if(compareCommentResult(commentResult)) {
					rightNum ++;
				}
			}
			System.out.println(rightNum*1.0d/totalNum);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private boolean compareCommentResult(CommentClassifyResult commentResult) {
		
		List<Integer> classifyLabelList = commentResult.getClassifyLabelList();
		List<Integer> realLabelList = commentResult.getRealLabelList();
		
		if(classifyLabelList.size()!=realLabelList.size()) {
			return false;
		}
		
		int statementNum = classifyLabelList.size();
		int classifyMinZeroLabelIndex = 0;
		for(int i=0;i<statementNum;i++) {
			if(classifyLabelList.get(i)==0) {
				classifyMinZeroLabelIndex = i;
				break;
			}
		}
		
		int realMinZeroLabelIndex = 0;
		for(int i=0;i<statementNum;i++) {
			if(realLabelList.get(i)==0) {
				realMinZeroLabelIndex = i;
				break;
			}
		}
		
		if(classifyMinZeroLabelIndex == realMinZeroLabelIndex) {
			return true;
		}else {
		    return false;
		}
	}
	
	public void save(String filePath) {
		FileOutputStream fos=null;
		try {
			fos = new FileOutputStream(new File(filePath));
			ObjectOutputStream oos = new ObjectOutputStream(fos);  
			oos.writeObject(this);  
			oos.flush();  
			oos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(fos!=null) {  
			    try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		    
	}

	public static void main(String[] args) throws Exception {
//		RandomForestClassifier rs = new RandomForestClassifier("d:/work/9.7/train_5.arff", "d:/work/9.7/train_5.arff");
//		rs.run(500, 80,5, "d:/work/9.7/train_5.arff", "d:/work/9.7/result_train_5.csv");
//		rs.getCommentPrecision("d:/work/9.7/result_5.csv");
		RandomForestClassifier rs2 = new RandomForestClassifier("d:/work/9.7/train_5.arff", "d:/work/9.7/test_5.arff");
		rs2.run(500, 80,5, "d:/work/9.7/test_5.arff", "d:/work/9.7/result_test_5.csv");
		rs2.getCommentPrecision("d:/work/9.7/result_test_5.csv");
	}

}
