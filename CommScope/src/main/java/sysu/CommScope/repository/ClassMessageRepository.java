package sysu.CommScope.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import sysu.CommScope.bean.ClassMessage;


public interface ClassMessageRepository extends MongoRepository<ClassMessage,ObjectId>{
	
	List<ClassMessage> findByProjectAndCommitID(String project,int commitID);
	List<ClassMessage> findByProjectAndType(String project,String type);
	List<ClassMessage> findByType(String type);
	List<ClassMessage> findByProject(String project);
	ClassMessage findASingleByProjectAndCommitIDAndClassName(String project,String commitID,String className);
	ClassMessage findASingleByClassID(int classID);

}
