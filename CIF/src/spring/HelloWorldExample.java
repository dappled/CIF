package spring;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;

/**
 * Created by IntelliJ IDEA.
 * User: eran
 * Date: 12/8/11
 * Time: 4:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class HelloWorldExample {

   public static void main(String[] args){

       /**
        HelloWorld h1 = new HelloWorld();
        h1.setMyString("really HW");
        HelloWorld h2 = new HelloWorld();
        h2.setMyString("Thank you");
        h2.setReallyHelloWorld(h1);
        HelloWorld h3 = new HelloWorld();
        h3.setMyString("Yoo");
        h3.setReallyHelloWorld(h2);
        h3.printMe();
        */
      XmlBeanFactory factory = new XmlBeanFactory(new FileSystemResource("helloworld.xml"));
      IHelloWorld helloWorld = (IHelloWorld) factory.getBean("helloWorldSample");
      helloWorld.printMe();


   }
}
