import com.miwang.entity.AutoEntityDemoConfig;
import com.miwang.service.AutoEntityDemoService;
import com.miwang.service.AutoEntityDemoServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author guozq
 * @date 2020-11-17-5:56 下午
 */
public class AutoBeanAssembleTest {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AutoEntityDemoConfig.class);
        AutoEntityDemoService autoEntityDemoService = (AutoEntityDemoService) context.getBean("autoEntityDemoService", AutoEntityDemoServiceImpl.class);
        autoEntityDemoService.printInfo();
    }
}
