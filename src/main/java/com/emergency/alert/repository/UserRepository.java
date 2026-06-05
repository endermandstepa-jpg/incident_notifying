package com.emergency.alert.repository;

import com.emergency.alert.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}