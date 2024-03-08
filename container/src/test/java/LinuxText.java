import com.cloud.config.K8sConfig;
import com.cloud.config.LinuxConfig;
import com.cloud.utils.TypeUtil;
import com.jcraft.jsch.*;
import io.kubernetes.client.custom.Quantity;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;


public class LinuxText {

    @Test
    public void test() {
        for (int i = 0; i < 100; i++){
            String podController=TypeUtil.generateLetterOnlyUUID();
            System.out.println(podController);
        }
    }

}
