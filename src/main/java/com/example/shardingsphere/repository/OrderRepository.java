package com.example.shardingsphere.repository;

import com.example.shardingsphere.bean.UserOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<UserOrder, String> {
}