package sysu.CommScope.repository;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class RepositoryFactory {
	
	private final static AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(sysu.CommScope.config.MongoConfig.class); 
	
	public static AnnotationConfigApplicationContext getContext(){
		return context;
	}
	
	public static ClassMessageRepository getClassRepository(){
		return context.getBean(ClassMessageRepository.class);
	}
	
	public static CommentRepository getCommentRepository(){
		return context.getBean(CommentRepository.class);
	}
	
	public static EndLineVerifyRepository getEndLineVerifyRepository(){
		return context.getBean(EndLineVerifyRepository.class);
	}
	

}
