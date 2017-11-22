package sysu.CommScope.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import sysu.CommScope.bean.ClassMessage;


public interface ClassMessageRepository extends MongoRepository<ClassMessage,ObjectId>{
	
	List<ClassMessage> findByProject(String project);
	ClassMessage findASingleByClassID(int classID);

}
