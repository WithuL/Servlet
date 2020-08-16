import animal.Duck;
import animal.Duck2;
import animal.Duck3;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new
                ClassPathXmlApplicationContext("applications.xml");
        String bit1 = (String) context.getBean("bit");
        Duck d1 = (Duck) context.getBean("d1");
        Duck2 d2 = (Duck2) context.getBean("d2");

        Duck3 k1 = (Duck3) context.getBean("k3");
        while(k1 != null) {
            System.out.println(k1);
            k1 = k1.getNext();
        }
//        System.out.println(bit1);
//        System.out.println(d1);
//        System.out.println(d2);
    }
}