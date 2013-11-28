package spring;

/**
 * Created by IntelliJ IDEA.
 * User: eran
 * Date: 12/8/11
 * Time: 4:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class HelloWorld implements IHelloWorld {

   private String myString;
   private IHelloWorld helloWorld;

   public void setMyString(String myString){
      this.myString = myString;
   }

   public void setReallyHelloWorld(IHelloWorld reallyHelloWorld){
      this.helloWorld = reallyHelloWorld;
   }

   public String getMyString(String myString){
      return myString;
   }


   public void printMe() {
      System.out.println(myString);
      if (helloWorld == null ){ return; }
      helloWorld.printMe();
   }

}
