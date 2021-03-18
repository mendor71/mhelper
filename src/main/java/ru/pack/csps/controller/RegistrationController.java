package ru.pack.csps.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Mendor on 10.10.2017.
 */
@Controller
public class RegistrationController {
    @RequestMapping(value = "registration", method = RequestMethod.GET)
    public String registrationPage() {
        return "registration";
    }

//    @RequestMapping(value = "registration", method = RequestMethod.POST)
//    public String registrationPage(ModelMap modelMap, HttpServletResponse response, @RequestParam(value = "j_username", required = true) String login
//            , @RequestParam(value = "j_password", required = true) String password
//            , @RequestParam(value = "j_phone", required = true) String phone
//            , @RequestParam(value = "j_mail", required = true) String email
//    ) throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
//        Users dbUser = usersRepository.findUsersByUserName(login);
//        if (dbUser != null) {
//
//            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//            String hashedPassword = passwordEncoder.encode(password);
//            States state = statesRepository.findStatesByStateName("active"); //todo set default value in xml config
//            Roles role = rolesRepository.findRolesByRoleName("ROLE_USER");//todo set default value in xml config
//
//            Users user = new Users(login, hashedPassword, state);
//            user.setUserRate(100d);
//
//            if ((Boolean) settingsService.getProperty("encryptEnable")) {
//                encryptorService.encrypt(user);
//            }
//
//            user = usersRepository.save(user);
//
//            user.getRoleList().add(role);
//            role.getUserList().add(user);
//
//            usersRepository.save(user);
//            rolesRepository.save(role);
//
//            UserCustomFields ucf = new UserCustomFields(email, phone, user);
//
//            if ((Boolean) settingsService.getProperty("encryptEnable")) {
//                encryptorService.encrypt(ucf);
//            }
//
//            ucfRepository.save(ucf);
//            return "registration";
//        } else {
//            modelMap.addAttribute("response", JSONResponse.createNotUniqueResponse("User with user name " + login + " already exists"));
//            response.setStatus(406);
//            return "response";
//        }
//    }
//
//    @RequestMapping(value = "setUserDevice", method = RequestMethod.POST)
//    public String setUserDevice(ModelMap modelMap, HttpServletResponse response, @RequestParam(value = "j_device_uid", required = true) String devUID
//            , @RequestParam(value = "j_device_pin", required = true) String devPin) {
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//
//        if (auth.isAuthenticated() && !auth.getPrincipal().toString().equals("anonymousUser")) {
//            String userName = auth.getPrincipal().toString();
//
//            Users user = usersRepository.findUsersByUserName(userName);
//
//            Devices devices = new Devices(devUID, encoder.encode(devPin));
//            devices.getUserList().add(user);
//
//            devices = devicesRepository.save(devices);
//
//            user.getDeviceList().add(devices);
//            usersRepository.save(user);
//
//            modelMap.addAttribute("response", "device saved"); //TODO
//            return "response";
//        } else {
//            response.setStatus(403);
//            return "403";
//        }
//
//     }
}
