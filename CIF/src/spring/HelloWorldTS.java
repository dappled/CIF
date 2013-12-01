package spring;

/**
 * @author Zhenghong Dong
 */
public class HelloWorldTS {
	   private String message;

		   public void setMessage(String message){
		      this.message  = message;
		   }

		   public void getMessage(){
		      System.out.println("Your Message : " + message);
		   }
		
}
