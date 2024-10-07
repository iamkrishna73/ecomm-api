package io.github.iamkrishna73.Ecomm.Repositories;

import io.github.iamkrishna73.Ecomm.entity.AppRole;
import io.github.iamkrishna73.Ecomm.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
     Optional<Role> findByRoleName(AppRole appRole);
}
