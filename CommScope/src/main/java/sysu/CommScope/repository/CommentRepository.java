package sysu.CommScope.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import sysu.CommScope.bean.CommentEntry;


public interface CommentRepository extends MongoRepository<CommentEntry,ObjectId>{
	CommentEntry findASingleByCommentID(int commentID);
	List<CommentEntry> findByProject(String project);
	
	
}
