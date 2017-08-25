package sysu.CommScope.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import sysu.CommScope.bean.CommentEntry;


public interface CommentRepository extends MongoRepository<CommentEntry,ObjectId>{
	List<CommentEntry> findByProjectAndType(String project,String type);
	List<CommentEntry> findByIsChange(boolean isChange);
	List<CommentEntry> findByProjectAndIsChange(String project,boolean isChange);
	CommentEntry findASingleByCommentID(int commentID);
	List<CommentEntry> findByProject(String project);
	List<CommentEntry> findByProjectAndCommitIDAndClassName(String project,int commitID,String className);
	
	
}
