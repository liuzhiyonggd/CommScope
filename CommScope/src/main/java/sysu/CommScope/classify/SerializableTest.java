package sysu.CommScope.classify;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializableTest {
	
	public static void saveClassify() throws FileNotFoundException, Exception {
		RandomForestClassifier rs6 = new RandomForestClassifier("d:/work/9.7/train_5.arff", "d:/work/9.7/test_5.arff");
		rs6.run(500, 6,6, "d:/work/9.7/test_5.arff", "d:/work/9.7/result_5.csv");
		
		FileOutputStream fos = new FileOutputStream(new File("D:/work/9.8/classify.obj"));  
		   ObjectOutputStream oos = new ObjectOutputStream(fos);  
		   oos.writeObject(rs6);  
		   oos.flush();  
		   oos.close();  
		   fos.close();  
	}
	
	public static void getClassify() throws IOException, ClassNotFoundException {
		FileInputStream fis = new FileInputStream(new File("D:/work/9.8/classify.obj"));  
		   ObjectInputStream ois = new ObjectInputStream(fis);  
		   RandomForestClassifier rs = (RandomForestClassifier) ois.readObject();  
		   rs.run(500, 6,6, "d:/work/9.7/test_5.arff", "d:/work/9.7/result_5.csv");
		   ois.close();  
		   fis.close(); 
	}
	
	public static void main(String[] args) throws FileNotFoundException, Exception {
		saveClassify();
		getClassify();
	}

}
