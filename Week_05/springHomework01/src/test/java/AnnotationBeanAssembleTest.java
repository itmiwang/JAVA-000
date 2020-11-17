import com.miwang.entity.AnnotationEntityDemo;
import com.miwang.entity.AnnotationEntityDemoConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author guozq
 * @date 2020-11-17-4:58 下午
 */
public class AnnotationBeanAssembleTest {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AnnotationEntityDemoConfig.class);
        AnnotationEntityDemo annotationEntityDemo = (AnnotationEntityDemo) context.getBean("annotationEntityDemo", AnnotationEntityDemo.class);
        System.out.println(annotationEntityDemo);
    }
}
