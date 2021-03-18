package ru.pack.csps.controller.rest;

import org.json.simple.JSONAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.pack.csps.service.dao.RolesService;
import ru.pack.csps.util.IncludeAPI;


@RestController
@RequestMapping(value = "/roles")
public class RolesController {
    private RolesService rolesService;

    @IncludeAPI
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER_ADMIN')")
    @RequestMapping(method = RequestMethod.POST, value = "/{roleId}/users/{userId}")
    public JSONAware addUserRole(@PathVariable(value = "userId") Long userId, @PathVariable(value = "roleId") Integer roleId) {
        return rolesService.addUserRole(userId, roleId);
    }

    @IncludeAPI
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER_ADMIN')")
    @RequestMapping(method = RequestMethod.DELETE, value = "/{roleId}/users/{userId}")
    public JSONAware removeUserRole(@PathVariable(value = "userId") Long userId, @PathVariable(value = "roleId") Integer roleId) {
        return rolesService.removeUserRole(userId, roleId);
    }

    @Autowired
    public void setRolesService(RolesService rolesService) {
        this.rolesService = rolesService;
    }
}
