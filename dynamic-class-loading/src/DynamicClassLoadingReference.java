import java.lang.reflect.*;
import java.nio.file.*;
import java.io.*;


public class DynamicClassLoadingReference{
    public static void main(String[] args) 
            throws IllegalAccessException, InvocationTargetException,
            InterruptedException{
        String className = "MyDynamicClass";
        String methodName = "sayHello";
        for(;;){
            MyClassLoader myClassLoader = new MyClassLoader();
            Class klass = myClassLoader.getClass(className);
            if(klass!=null){
                try{
                    Method m = klass.getDeclaredMethod(methodName);
                    m.invoke(null);
                }catch(NoSuchMethodException e){
                    System.out.println("Waiting for impl of Method "
                            + methodName);
                }
            }
            Thread.sleep(2000);
        }
    }

}

class MyClassLoader extends ClassLoader{
    public Class getClass(String className){
        Path dynamicFile = null;
        try{
            dynamicFile = Paths.get(
                    System.getProperty("user.dir"), 
                    "build/" + className + ".class");
            byte[] data = Files.readAllBytes(dynamicFile);
            return defineClass(className, data, 0, data.length);
        }catch(IOException e){
            System.out.println("Failed to read data from file ["
                    + dynamicFile + "]");
            return null;
        }
    }
}

