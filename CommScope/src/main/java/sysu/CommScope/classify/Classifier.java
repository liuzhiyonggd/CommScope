package sysu.CommScope.classify;

import java.util.List;

public interface Classifier {
	
	List<String> getPrecision();
	List<String> getFalse1Instance();
	List<String> getFalse0Instance();
	List<String> getTrue1Instance();
	List<String> getTrue0Instance();
	
	List<String> getCommentScopePrecisioin();

}
