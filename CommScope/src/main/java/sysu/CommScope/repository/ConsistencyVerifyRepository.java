package sysu.CommScope.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import sysu.CommScope.tool.ConsistencyVerify;


public interface ConsistencyVerifyRepository extends MongoRepository<ConsistencyVerify,String>{
	
	

}
