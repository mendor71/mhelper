package ru.pack.csps.service.dao;

import org.json.simple.JSONAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.pack.csps.entity.Roles;
import ru.pack.csps.entity.Users;
import ru.pack.csps.repository.RolesRepository;
import ru.pack.csps.repository.UsersRepository;

import static ru.pack.csps.util.JSONResponse.createNotFoundResponse;
import static ru.pack.csps.util.JSONResponse.createOKResponse;

@Service
public class RolesService {
    private UsersRepository usersRepository;
    private RolesRepository rolesRepository;

    public JSONAware addUserRole(Long userId, Integer roleId) {
        Users users = usersRepository.findOne(userId);
        if (users == null) {
            return createNotFoundResponse("Пользователь на найден по ID: " + userId);
        }
        Roles roles = rolesRepository.findOne(roleId);
        if (roles == null) {
            return createNotFoundResponse("Роль на найдена по ID: " + userId);
        }
        users.getRoleList().add(roles);
        usersRepository.save(users);
        return createOKResponse("Данные пользователя успешно обновлены");
    }

    public JSONAware removeUserRole(Long userId, Integer roleId) {
        Users users = usersRepository.findOne(userId);
        if (users == null) {
            return createNotFoundResponse("Пользователь на найден по ID: " + userId);
        }
        Roles roles = rolesRepository.findOne(roleId);
        if (roles == null) {
            return createNotFoundResponse("Роль на найдена по ID: " + userId);
        }
        users.getRoleList().remove(roles);
        usersRepository.save(users);
        return createOKResponse("Данные пользователя успешно обновлены");
    }

    public JSONAware removeUserRole(String userName, String roleName) {
        Users users = usersRepository.findUsersByUserName(userName);
        if (users == null) {
            return createNotFoundResponse("Пользователь на найден по userName: " + userName);
        }
        Roles roles = rolesRepository.findRolesByRoleName(roleName);
        if (roles == null) {
            return createNotFoundResponse("Роль на найдена по roleName: " + roleName);
        }
        users.getRoleList().remove(roles);
        usersRepository.save(users);
        return createOKResponse("Данные пользователя успешно обновлены");
    }

    public JSONAware removeUserRole(Users users, String roleName) {
        Roles roles = rolesRepository.findRolesByRoleName(roleName);
        if (roles == null) {
            return createNotFoundResponse("Роль на найдена по roleName: " + roleName);
        }
        users.getRoleList().remove(roles);
        usersRepository.save(users);
        return createOKResponse("Данные пользователя успешно обновлены");
    }
    @Autowired
    public void setUsersRepository(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }
    @Autowired
    public void setRolesRepository(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }
}
