package sysu.CommScope.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import sysu.CommScope.bean.EndLineVerify;

public interface EndLineVerifyRepository extends MongoRepository<EndLineVerify,ObjectId>{
	
}
