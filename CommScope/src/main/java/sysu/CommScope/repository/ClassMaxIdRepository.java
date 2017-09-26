package sysu.CommScope.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import sysu.CommScope.bean.ClassMaxID;

public interface ClassMaxIdRepository extends MongoRepository<ClassMaxID,ObjectId>{
	
}
