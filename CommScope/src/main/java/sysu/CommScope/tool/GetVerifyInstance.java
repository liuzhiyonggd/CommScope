package sysu.CommScope.tool;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import sysu.CommScope.repository.ConsistencyVerifyRepository;
import sysu.CommScope.repository.RepositoryFactory;


public class GetVerifyInstance {
	
	public static void main(String[] args) throws IOException {
		
		ConsistencyVerifyRepository consistencyRepo = RepositoryFactory.getConsistencyRepository();
		List<String> output = new ArrayList<String>();
		List<ConsistencyVerify> verifyList = consistencyRepo.findAll();
		
		for(ConsistencyVerify verify:verifyList) {
			if(verify.getChangeReason().equals("scope_error")) {
				continue;
			}
			output.add(verify.getCommentID()+","+(verify.getChangeReason().equals("refactoring")?1:0)+","+verify.getChangeReason()+","+verify.isChange());
		}
		
		FileUtils.writeLines(new File("E:\\注释一致性实验\\数据\\new_selectID.txt"), output);
	}

}
