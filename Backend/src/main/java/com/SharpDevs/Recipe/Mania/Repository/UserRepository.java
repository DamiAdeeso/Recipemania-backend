package com.SharpDevs.Recipe.Mania.Repository;


import com.SharpDevs.Recipe.Mania.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String Email);
}
