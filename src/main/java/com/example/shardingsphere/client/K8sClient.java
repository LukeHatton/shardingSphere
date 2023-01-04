package com.example.shardingsphere.client;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * <p>Project: shardingSphere
 * <p>Description:
 * <p>
 * kubernetes的客户端工具
 *
 * @author lizhao 2023/1/5
 */
@Configuration
public class K8sClient {

  /**
   * 1. 初始化ApiClient
   * <p>
   * 这一步会使用和kubectl工具相同的配置文件去初始化java client客户端
   * <p>
   * 配置文件会从如下位置搜索：
   * <ul>
   *   <li>通过环境变量<i>KUBECONFIG</i>定义的配置文件位置
   *   <li>k8s用户配置文件：<i>$HOME/.kube/config</i>
   *   <li>service account token信息：<i>/var/run/secrets/kubernetes.io/serviceaccount</i>
   *   <li>直接访问<a href="http://localhost:8080">本地链接</a>，如果响应包含k8s账户信息，就可以直接使用
   * </ul>
   */
  @Bean
  public ApiClient apiClient() throws IOException {
    return Config.defaultClient();
  }

  /**
   * 2. 初始化Api Stub
   * <p>
   * 创建真正的API客户端。之所以需要这一步，是因为k8s开发和发布十分快速，api版本可能会在将来发生改变。在实际使用中，
   * 可以将这个对象交给容器管理，并在需要使用的位置注入
   * <p>
   * 除了带参数的{@link CoreV1Api#CoreV1Api(ApiClient)}方法，还有一个无参构造方法{@link CoreV1Api#CoreV1Api()}。
   * 一般不推荐使用无参构造方法，因为它要求要先调用{@link io.kubernetes.client.openapi.Configuration#setDefaultApiClient(ApiClient)}，
   * 可能会由于人类因素导致初始化错误（如忘记调用或多次调用该方法等）
   */
  @Bean
  public CoreV1Api setCoreApi(ApiClient apiClient) {
    return new CoreV1Api(apiClient);
  }


}
