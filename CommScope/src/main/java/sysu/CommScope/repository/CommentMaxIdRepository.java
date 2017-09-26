package sysu.CommScope.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import sysu.CommScope.bean.ClassMaxID;
import sysu.CommScope.bean.CommentMaxID;

public interface CommentMaxIdRepository extends MongoRepository<CommentMaxID,ObjectId>{

}
