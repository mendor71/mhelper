package ru.pack.csps.controller.rest;

import org.json.simple.JSONAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.pack.csps.entity.States;
import ru.pack.csps.exceptions.PropertyFindException;
import ru.pack.csps.service.dao.StatesService;
import ru.pack.csps.util.IncludeAPI;

import java.io.IOException;

@RestController
@RequestMapping("/states")
public class StatesController {
    private StatesService statesService;

    @IncludeAPI
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Iterable<States> getAll() {
        return statesService.findAll();
    }

    @IncludeAPI
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER_ADMIN')")
    @RequestMapping(method = RequestMethod.POST, value = "/confirmModeration/{userId}")
    public JSONAware confirmModeration(@PathVariable(value = "userId") Long userId) throws IOException, PropertyFindException {
        return statesService.confirmUserModeration(userId);
    }

    @IncludeAPI
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER_ADMIN')")
    @RequestMapping(method = RequestMethod.POST, value = "/{stateId}/users/{userId}")
    public JSONAware setUserState(@PathVariable(value = "userId") Long userId, @PathVariable(value = "stateId") Integer stateId) {
        return statesService.setUserState(userId, stateId);
    }

    @Autowired
    public void setStatesService(StatesService statesService) {
        this.statesService = statesService;
    }
}
