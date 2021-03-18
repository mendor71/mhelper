package ru.pack.csps.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.pack.csps.entity.Users;
import ru.pack.csps.exceptions.InvalidValueException;
import ru.pack.csps.service.dao.UsersService;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Controller
public class PasswordResetController {
    private UsersService usersService;

    @RequestMapping(value = "/password_reset", method = RequestMethod.GET)
    public String showPage(@RequestParam(value = "token", required = false) String token, HttpServletRequest request, ModelMap modelMap, HttpServletResponse response) throws InvalidValueException, NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        if (token == null) {
            return "password_reset";
        } else {
            try {
                Users users = usersService.getUserByPasswordResetToken(token);

                Set<GrantedAuthority> roles = new HashSet<GrantedAuthority>();
                roles.add(new SimpleGrantedAuthority("ROLE_USER"));

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(users.getUserName(), "", roles);

                SecurityContext context = SecurityContextHolder.getContext();
                context.setAuthentication(authenticationToken);
                HttpSession session = request.getSession(true);

                session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, context);
                return "password_reset";
            } catch (InvalidValueException e) {
                response.setHeader("Content-Type", "text/plain; charset=UTF-8");
                modelMap.addAttribute("response", e.getMessage());
                return "response";
            }
        }
    }

    @Autowired
    public void setUsersService(UsersService usersService) {
        this.usersService = usersService;
    }
}
