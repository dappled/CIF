package spring;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Zhenghong Dong
 */
public class MainApp {

	static Logger log = Logger.getLogger(MainApp.class.getName());

	   public static void main(String[] args) {
	      ApplicationContext context = 
	             new ClassPathXmlApplicationContext("/spring/Beans.xml");

	      log.info("Going to create HelloWord Obj");

	      HelloWorldTS obj = (HelloWorldTS) context.getBean("helloWorld");

	      obj.getMessage();

	      log.info("Exiting the program");
	      
	   }

}
