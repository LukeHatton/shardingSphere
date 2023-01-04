package com.example.shardingsphere;

import com.example.shardingsphere.bean.User;
import com.example.shardingsphere.bean.UserOrder;
import com.example.shardingsphere.repository.OrderRepository;
import com.example.shardingsphere.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ShardingSphereApplicationTests {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private OrderRepository orderRepository;

  @Test
  void test01() {
    userRepository.findAll();
  }

  @Test
  void test02() {
    orderRepository.findAll();
  }

  @Test
  void test03() {
    // 为了避免主键自动生成策略对分库分表测试的影响，先不使用自增
    userRepository.save(new User(1L, "ming", "0"));
    userRepository.save(new User(2L, "ming", "0"));
    userRepository.save(new User(3L, "ming", "0"));
    userRepository.save(new User(4L, "ming", "0"));

    userRepository.save(new User(5L, "ming", "0"));
    userRepository.save(new User(6L, "ming", "0"));
    userRepository.save(new User(7L, "ming", "0"));
    userRepository.save(new User(8L, "ming", "0"));

    userRepository.save(new User(9L, "ming", "0"));
    userRepository.save(new User(10L, "hong", "0"));
    userRepository.save(new User(11L, "qiang", "0"));
    userRepository.save(new User(12L, "zheng", "0"));
  }

  @Test
  void test04() {
    orderRepository.save(new UserOrder(1L, 1L, "userId=1的订单"));
    orderRepository.save(new UserOrder(2L, 2L, "userId=2的订单"));
    orderRepository.save(new UserOrder(3L, 3L, "userId=3的订单"));
    orderRepository.save(new UserOrder(4L, 4L, "userId=4的订单"));

    orderRepository.save(new UserOrder(5L, 5L, "userId=4的订单"));
    orderRepository.save(new UserOrder(6L, 6L, "userId=4的订单"));
    orderRepository.save(new UserOrder(7L, 7L, "userId=4的订单"));
    orderRepository.save(new UserOrder(8L, 8L, "userId=4的订单"));

    orderRepository.save(new UserOrder(9L, 9L, "userId=4的订单"));
    orderRepository.save(new UserOrder(10L, 10L, "userId=4的订单"));
    orderRepository.save(new UserOrder(11L, 11L, "userId=4的订单"));
    orderRepository.save(new UserOrder(12L, 12L, "userId=4的订单"));
  }

  @Test
  void test05() {
    List<User> list1 = userRepository.findAll();
    System.out.println(list1.size());
    List<UserOrder> list2 = orderRepository.findAll();
    System.out.println(list2.size());
  }

}
