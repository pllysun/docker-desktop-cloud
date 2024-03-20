package com.cloud.config;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.apis.NetworkingV1Api;
import io.kubernetes.client.util.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;

@Configuration
@Slf4j
public class K8sConfig {

    /**
     * 连接k8s客户端
     * @throws IOException
     */
    public static void k8sclient() throws IOException {
        // 获取配置文件的资源
        ClassLoader classLoader = K8sConfig.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("config");
        StringBuilder str = new StringBuilder();
        if (inputStream != null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    str.append(line).append("\n");
                }
            } catch (IOException e) {
                log.error("读取k8s配置文件出错：" + e.getMessage());
            }
        } else {
            System.out.println("k8s配置类资源路径错误或不存在");
            return;
        }

        // 写入内容到临时文件
        File tempFile = File.createTempFile("temp_config", ".yaml");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write(str.toString());
        } catch (IOException e) {
            log.error("写入临时文件出错：" + e.getMessage());
            return;
        }

        // 获取临时文件的路径
        String path = tempFile.getPath();

        ApiClient apiClient = Config.fromConfig(path);
        io.kubernetes.client.openapi.Configuration.setDefaultApiClient(apiClient);
    }

    /**
     * 组件api
     * @return
     * @throws Exception
     */
        @Bean
        public CoreV1Api apiClient() throws Exception {
            return new CoreV1Api();
        }

    /**
     *  部署api
     * @return
     * @throws Exception
     */
        @Bean
        public AppsV1Api appsV1Api() throws Exception{
            return new AppsV1Api();
        }

    /**
     *  网络api
     * @return
     */
    @Bean
    public NetworkingV1Api networkingV1Api(){
            return new NetworkingV1Api();
        }

}
