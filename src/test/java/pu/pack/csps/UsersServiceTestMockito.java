package pu.pack.csps;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import ru.pack.csps.entity.*;
import ru.pack.csps.exceptions.InvalidValueException;
import ru.pack.csps.repository.*;
import ru.pack.csps.service.EncryptorService;
import ru.pack.csps.service.FileCopyService;
import ru.pack.csps.util.IEncrypted;
import ru.pack.csps.service.SettingsService;
import ru.pack.csps.service.dao.UsersService;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UsersServiceTestMockito {
    @Mock private UsersRepository usersRepository;
    @Mock private StatesRepository statesRepository;
    @Mock private RolesRepository rolesRepository;
    @Mock private SettingsService settingsService;
    @Mock private EncryptorService encryptorService;
    @Mock private NotificationsRepository notificationsRepository;
    @Mock private UserCustomFieldsRepository ucfRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private HttpServletResponse servletResponse;
    @Mock private FileCopyService fileCopyService;
    @Mock private MultipartHttpServletRequest multipartHttpServletRequest;

    @Mock private UserCustomFields ucf;

    private UsersService usersService = new UsersService();
    private Users testUserWithRolesAndStateAndPassportPagesAndNotifications = new Users(42L);
    private Users testUserWithoutPassportPages = new Users(25L);

    private Users testUserForCreate = new Users(2L);

    @Before
    public void initTest() throws Exception {
        UserCustomFields ucf = new UserCustomFields(1L);
        ucf.setUcfP1FileFormat("jpg");
        ucf.setUcfP2FileFormat("jpg");

        Field f1 = UserCustomFields.class.getDeclaredField("ucfPassport1");
        f1.setAccessible(true);
        f1.set(ucf, new byte[]{0,1,2});

        Field f2 = UserCustomFields.class.getDeclaredField("ucfPassport2");
        f2.setAccessible(true);
        f2.set(ucf, new byte[]{0,1,2});

        testUserForCreate.setUserName("userName");
        testUserForCreate.setUserPassword("password");
        testUserForCreate.setUserCustomFields(ucf);

        testUserWithRolesAndStateAndPassportPagesAndNotifications.getRoleList().add(new Roles(1, "ROLE_USER"));
        testUserWithRolesAndStateAndPassportPagesAndNotifications.setUserStateId(new States(1, "active"));
        testUserWithRolesAndStateAndPassportPagesAndNotifications.setUserCustomFields(ucf);
        testUserWithoutPassportPages.setUserCustomFields(new UserCustomFields());

        doThrow(new NullPointerException()).when(encryptorService).decrypt((IEncrypted)null);
        doThrow(new NullPointerException()).when(encryptorService).encrypt((IEncrypted)null);
        doThrow(new NullPointerException()).when(passwordEncoder).encode(null);

        when(usersRepository.findUsersByUserName("exists_user")).thenReturn(new Users(1L));
        when(usersRepository.findUsersByUserName("exists_user_with_roles")).thenReturn(testUserWithRolesAndStateAndPassportPagesAndNotifications);
        when(usersRepository.findUsersByUserName("exists_user_with_state")).thenReturn(testUserWithRolesAndStateAndPassportPagesAndNotifications);
        when(usersRepository.findUsersByUserName("exists_user_with_passport")).thenReturn(testUserWithRolesAndStateAndPassportPagesAndNotifications);
        when(usersRepository.findUsersByUserName("exists_user_with_notifications")).thenReturn(testUserWithRolesAndStateAndPassportPagesAndNotifications);
        when(usersRepository.findUsersByUserName("exists_user_without_passport")).thenReturn(testUserWithoutPassportPages);
        when(usersRepository.findOne(42L)).thenReturn(testUserWithRolesAndStateAndPassportPagesAndNotifications);
        when(usersRepository.findOne(25L)).thenReturn(testUserWithoutPassportPages);
        when(usersRepository.save(testUserForCreate)).thenReturn(testUserForCreate);

        when(usersRepository.findUsersByUserName("new_user")).thenReturn(new Users(1L));

        when(usersRepository.findUserByUserStateIdStateId(3)).thenReturn(Collections.singletonList(testUserWithRolesAndStateAndPassportPagesAndNotifications));

        when(settingsService.getProperty("encrypt_enable")).thenReturn(true);

        doNothing().when(fileCopyService).writeUserImage(isA(byte[].class), isA(File.class), isA(String.class), isA(OutputStream.class));

        when(notificationsRepository.findNotificationsByNotifUserIdUserId(42L)).thenReturn(Collections.singletonList(new Notifications()));
        when(notificationsRepository.findNotificationsByNotifUserIdUserId(1L)).thenReturn(new ArrayList<>());

        when(ucfRepository.save(isA(UserCustomFields.class))).thenReturn(new UserCustomFields(1L));

        usersService.setEncryptorService(encryptorService);
        usersService.setNotificationsRepository(notificationsRepository);
        usersService.setRolesRepository(rolesRepository);
        usersService.setUsersRepository(usersRepository);
        usersService.setStatesRepository(statesRepository);
        usersService.setUcfRepository(ucfRepository);
        usersService.setPasswordEncoder(passwordEncoder);
        usersService.setSettingsService(settingsService);
        usersService.setFileCopyService(fileCopyService);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testCheckUserExists_userNameIsNull() throws Exception {
        usersService.userExists(null);
    }

    @Test
    public void testCheckUserExists_userExists() throws Exception {
        boolean exists = usersService.userExists("exists_user");
        assertTrue(exists);
    }

    @Test
    public void testCheckUserExists_userNotExists() throws Exception {
        boolean exists = usersService.userExists("not_exists_user");
        assertFalse(exists);
    }

    @Test
    public void testGetUserById_existsUser_byCurrentUserName() throws Exception {
        Users users = usersService.getUserById(-1L, "exists_user", false);
        assertNotNull(users);
    }

    @Test(expected = InvalidValueException.class)
    public void testGetUserById_notExistsUser_byCurrentUserName() throws Exception {
        Users users = usersService.getUserById(-1L, "not_exists_user", false);
        assertNotNull(users);
    }

    @Test(expected = AccessDeniedException.class)
    public void testGetUserById_existsUser_notAdminAccess() throws Exception {
        usersService.getUserById(42L, "exists_user", false);
    }

    @Test
    public void testGetUserById_existsUser_adminAccess() throws Exception {
        Users users = usersService.getUserById(42L, "exists_user", true);
        assertNotNull(users);
    }

    @Test(expected = InvalidValueException.class)
    public void testGetUserRolesByUserId_notExistsUser() throws Exception {
        usersService.getUserRolesByUserId(-1L, "not_exists_user");
    }

    @Test
    public void testGetUserRolesByUserId_existsUser() throws Exception {
        List<Roles> roles = usersService.getUserRolesByUserId(-1L, "exists_user_with_roles");
        assertEquals(roles.size(), 1);

        roles = usersService.getUserRolesByUserId(42L, "exists_user_with_roles");
        assertEquals(roles.size(), 1);
    }

    @Test
    public void testGetUsersByStateId_notAdmin() throws Exception {
        List users = usersService.getUsersByStateId(3, false);
        assertEquals(users.size(), 0);
    }

    @Test
    public void testGetUsersByStateId_admin() throws Exception {
        List users = usersService.getUsersByStateId(3, true);
        assertEquals(users.size(), 1);
    }

    @Test(expected = InvalidValueException.class)
    public void testGetUserState_notExistsUser() throws Exception {
        usersService.getUserState(-1L, "not_exists_user");
    }

    @Test
    public void testGetUserState_existsUser() throws Exception {
        States states = usersService.getUserState(-1L, "exists_user_with_state");
        assertNotNull(states);

        states = usersService.getUserState(42L, "exists_user_with_state");
        assertNotNull(states);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPassportUploaded_userIdIsNull() throws Exception {
        usersService.checkPassportUploaded(null, -1, "", false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPassportUploaded_currentUserNameIdIsNull() throws Exception {
        usersService.checkPassportUploaded(-1L, -1, null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPassportUploaded_illegalPageId() throws Exception {
        usersService.checkPassportUploaded(-1L, -1, "", false);
    }

    @Test(expected = InvalidValueException.class)
    public void testCheckPassportUploaded_notExistsUser() throws Exception {
        usersService.checkPassportUploaded(-1L, 1, "not_exists_user", false);
    }

    @Test
    public void testCheckPassportUploaded_existsUser_withPassport() throws Exception {
        boolean exists = usersService.checkPassportUploaded(-1L, 1, "exists_user_with_passport", false);
        assertTrue(exists);
    }

    @Test
    public void testCheckPassportUploaded_existsUser_withoutPassport() throws Exception {
        boolean exists = usersService.checkPassportUploaded(-1L, 1, "exists_user_without_passport", false);
        assertFalse(exists);

        exists = usersService.checkPassportUploaded(25L, 1, "exists_user_without_passport", false);
        assertFalse(exists);
    }

    @Test(expected = InvalidValueException.class)
    public void testCheckPassportUploaded_existsUser_withoutUserCustomFields() throws Exception {
        usersService.checkPassportUploaded(-1L, 1, "exists_user", false);
    }

    @Test(expected = AccessDeniedException.class)
    public void testCheckPassportUploaded_existsUserWithPassport_notAdminAccess() throws Exception {
        usersService.checkPassportUploaded(42L, 1, "exists_user_without_passport", false);
    }

    @Test
    public void testCheckPassportUploaded_existsUserWithPassport_adminAccess() throws Exception {
        boolean exists = usersService.checkPassportUploaded(42L, 1, "exists_user_without_passport", true);
        assertTrue(exists);
    }

    @Test(expected = InvalidValueException.class)
    public void testGetPassportPage_notExistUser() throws Exception {
        usersService.getPassportPage(-1L, 1, servletResponse,"not_exists_user", false);
    }

    @Test(expected = AccessDeniedException.class)
    public void testGetPassportPage_existsUserWithPassportPage_notAdminAccess() throws Exception {
        usersService.getPassportPage(42L, 1, servletResponse,"exists_user", false);
    }

    @Test
    public void testGetPassportPage_existsUserWithPassportPage_adminAccess() throws Exception {
        usersService.getPassportPage(42L, 1, servletResponse,"exists_user", true);
    }

    @Test
    public void testGetPassportPage_existsUserWithPassportPage_ower() throws Exception {
        usersService.getPassportPage(-1L, 1, servletResponse,"exists_user_with_passport", true);
    }

    @Test(expected = InvalidValueException.class)
    public void testGetPassportPage_existsUserWithoutPassportPage_ower() throws Exception {
        usersService.getPassportPage(-1L, 1, servletResponse,"exists_user", true);
    }

    @Test(expected = InvalidValueException.class)
    public void testGetAllUserNotifications_notExistsUser() throws Exception {
        usersService.getAllUserNotifications(-1L, "not_exists_user");
    }

    @Test(expected = AccessDeniedException.class)
    public void testGetAllUserNotifications_existsUser_notOwner() throws Exception {
        usersService.getAllUserNotifications(42L, "exists_user");
    }

    @Test
    public void testGetAllUserNotifications_existsUser_owner() throws Exception {
        List<Notifications> notifications = usersService.getAllUserNotifications(-1L, "exists_user");
        assertEquals(notifications.size(), 0);

        notifications = usersService.getAllUserNotifications(42L, "exists_user_with_notifications");
        assertEquals(notifications.size(), 1);
    }

    @Test(expected = InvalidValueException.class)
    public void testCreate_userNameIsNull() throws Exception {
        usersService.create(new Users());
    }

    @Test(expected = InvalidValueException.class)
    public void testCreate_userPasswordIsNull() throws Exception {
        usersService.create(new Users("userName", null, new States()));
    }

    @Test
    public void testCreate() throws Exception {
        Users users = usersService.create(testUserForCreate);
        assertEquals(users, testUserForCreate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpload_emptyFileRequest() throws Exception {
        usersService.uploadPassport(-1L, 1, new MockMultipartHttpServletRequest(),"exists_user");
    }

    @Test
    public void testUpload_fileRequest() throws Exception {
        /* TODO */
    }
}
