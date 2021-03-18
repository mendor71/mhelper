package ru.pack.csps.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.pack.csps.entity.Roles;
import ru.pack.csps.entity.Users;

import javax.management.relation.Role;
import java.util.List;

public interface UsersRepository extends JpaRepository<Users, Long> {

    Users findUsersByUserName(String userName);

    @Query("select u from Users u join u.deviceList d where d.devUid = :devUid")
    Users findUsersByDeviceUid(@Param("devUid") String devUid);

    @Query("select distinct u from Users u join u.roleList r join u.userStateId s where s.stateName = :stateName and r.roleName not in ('ROLE_ADMIN')")
    List<Users> findUsersByUserStateName(@Param("stateName") String stateName);

    List<Users> findUserByUserStateIdStateId(@Param("stateId") Integer stateId);

    List<Users> findUserByUserStateIdStateIdAndRoleListNotContaining(@Param("stateId") Integer stateId, @Param("roleList") List<Roles> roles);

    Users findByUserCustomFieldsUcfMail(String mail);

    Users findByUserPwdRestoreUUID(String token);
}