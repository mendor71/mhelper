package ru.pack.csps.repository;

import org.springframework.data.repository.CrudRepository;
import ru.pack.csps.entity.Roles;

public interface RolesRepository extends CrudRepository<Roles, Integer> {
    Roles findRolesByRoleName(String roleName);
}
