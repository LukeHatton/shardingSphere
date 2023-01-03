package com.example.shardingsphere.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <p>Project: shardingSphere
 * <p>Description:
 * <p>
 *
 * @author lizhao 2023/1/3
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "cm_user")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private String id;

  @Column(name = "name")
  private String name;

  @Column(name = "gender")
  private String gender;
}
