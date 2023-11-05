package com.onk.repository;

import com.onk.model.Role;
import com.onk.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("SELECT r.users FROM Role r where r.name=:roleName")
    Set<User> findUsersByRoleName(@Param("roleName") String roleName);

    Role findRoleByNameEqualsIgnoreCase(String name);

    @Query("select r from Role r where r.name = :name")
    Role findByRoleName(String name);

}
