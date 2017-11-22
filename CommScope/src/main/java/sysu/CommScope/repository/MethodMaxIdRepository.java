package sysu.CommScope.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import sysu.CommScope.bean.MethodMaxID;

public interface MethodMaxIdRepository extends MongoRepository<MethodMaxID,ObjectId>{

}
