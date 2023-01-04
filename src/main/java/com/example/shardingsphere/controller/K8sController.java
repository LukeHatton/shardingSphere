package com.example.shardingsphere.controller;

import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Node;
import io.kubernetes.client.openapi.models.V1NodeList;
import io.kubernetes.client.openapi.models.V1Service;
import io.kubernetes.client.openapi.models.V1ServiceList;
import io.kubernetes.client.openapi.models.V1ServiceSpec;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>Project: shardingSphere
 * <p>Description:
 * <p>
 *   TODO spec对象无法被正确地解析为json字符串，如果需要http访问controller来取得这些信息，就需要先先解决这个问题
 *
 * @author lizhao 2023/1/5
 */
@RestController
@RequestMapping("/k8s")
public class K8sController {

  private final CoreV1Api api;

  public K8sController(CoreV1Api coreApi) {
    this.api = coreApi;
  }

  @RequestMapping("/nodes")
  public List<V1Node> getAllNodes() throws ApiException {
    V1NodeList nodeList = api.listNode(null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      10,
      false);
    return nodeList.getItems();
  }

  @RequestMapping("/services")
  public List<V1ServiceSpec> getAllServices() throws ApiException {
    V1ServiceList serviceList = api.listNamespacedService("default",
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      10,
      false);

    return serviceList.getItems()
      .stream()
      .map(V1Service::getSpec)
      .collect(Collectors.toList());
  }
}
