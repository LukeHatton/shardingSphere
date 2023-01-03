package com.example.shardingsphere.repository;

import com.example.shardingsphere.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}