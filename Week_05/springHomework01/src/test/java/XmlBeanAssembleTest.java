import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author guozq
 * @date 2020-11-17-1:51 下午
 */
public class XmlBeanAssembleTest {
    public static void main(String[] args) {
        // 定义配置文件路径
        String xmlPath = "spring-bean01.xml";
        // 加载配置文件
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(xmlPath);
        // 构造方式输出结果
        System.out.println(applicationContext.getBean("xmlEntityDemo1"));
        // 设值方式输出结果
        System.out.println(applicationContext.getBean("xmlEntityDemo2"));
    }
}
