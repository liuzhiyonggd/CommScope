package sysu.CommScope.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import sysu.CommScope.bean.Method;


public interface MethodRepository extends MongoRepository<Method,ObjectId>{
	
	Method findASingleByMethodID(int methodID);
	List<Method> findByClassID(int classID);

}
