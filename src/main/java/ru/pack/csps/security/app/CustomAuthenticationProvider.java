package ru.pack.csps.security.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.pack.csps.entity.Devices;
import ru.pack.csps.entity.Roles;
import ru.pack.csps.entity.Users;
import ru.pack.csps.repository.UsersRepository;

import java.util.HashSet;
import java.util.Set;

public class CustomAuthenticationProvider implements AuthenticationProvider {

    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();

        if (password.startsWith("DEV.PIN") && userName.startsWith("DEV.UID")) {
            return authByDeviceUidAndPin(userName.replace("DEV.UID", ""), password.replace("DEV.PIN", ""));
        } else {
            return authByUserNameAndPassword(userName, password);
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return  aClass.equals(UsernamePasswordAuthenticationToken.class);
    }

    private UsernamePasswordAuthenticationToken authByUserNameAndPassword(String userName, String password) {
        Users user = usersRepository.findUsersByUserName(userName);
        if (user == null) {
            throw new BadCredentialsException("1000");
        }
        if (!passwordEncoder.matches(password, user.getUserPassword())) {
            throw new BadCredentialsException("1000");
        }
        Set<GrantedAuthority> roles = new HashSet<GrantedAuthority>();
        for (Roles r: user.getRoleList()) {
            roles.add(new SimpleGrantedAuthority(r.getRoleName()));
        }
        return new UsernamePasswordAuthenticationToken(userName, password, roles);
    }

    private UsernamePasswordAuthenticationToken authByDeviceUID(String deviceUID) {
        Users user = usersRepository.findUsersByDeviceUid(deviceUID);
        if (user == null) {
            throw new BadCredentialsException("1000");
        }

        Set<GrantedAuthority> roles = new HashSet<GrantedAuthority>();
        roles.add(new SimpleGrantedAuthority("ROLE_PRE_AUTH"));

        return new UsernamePasswordAuthenticationToken(user.getUserName(), "", roles);
    }

    private UsernamePasswordAuthenticationToken authByDeviceUidAndPin(String deviceUID, String devicePIN) {

            Users user = usersRepository.findUsersByDeviceUid(deviceUID);
            Devices targetDevice = null;

            if (user == null) {
                throw new BadCredentialsException("1000");
            }

            for (Devices d: user.getDeviceList()) {
                if (d.getDevUid().equals(deviceUID)) {
                    targetDevice = d;
                }
            }

            if (targetDevice == null) {
                throw new BadCredentialsException("1000");
            } else {
                if (!passwordEncoder.matches(devicePIN, targetDevice.getDevPin())) {
                    throw new BadCredentialsException("1000");
                }

                Set<GrantedAuthority> roles = new HashSet<GrantedAuthority>();
                for (Roles r : user.getRoleList()) {
                    roles.add(new SimpleGrantedAuthority(r.getRoleName()));
                }

                return new UsernamePasswordAuthenticationToken(user.getUserName(), "", roles);
            }

    }

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

}
