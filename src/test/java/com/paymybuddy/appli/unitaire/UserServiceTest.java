package com.paymybuddy.appli.unitaire;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.paymybuddy.appli.model.DBUser;
import com.paymybuddy.appli.repository.UserRepository;
import com.paymybuddy.appli.service.UserService;

@SpringBootTest
public class UserServiceTest {
	
	@Autowired
	private UserService userService;
	@MockBean
	private UserRepository userRepository;
//	@MockBean
//    private PasswordEncoder passwordEncoder;
	//private List<DBUser> users;
	private DBUser user;
	
	@BeforeEach
	public void setUp() {
		user = new DBUser();
		user.setEmail("dbuser2@gmail.com");
		user.setUsername("dbuser2");
		user.setPassword("dbuser2");
	}
    
    @Test
    public void testLoadUserByUsername_UserFound() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        UserDetails userDetails = userService.loadUserByUsername(user.getEmail());

        assertNotNull(userDetails);
        assertEquals(user.getEmail(), userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().isEmpty());
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        String nonExistentEmail = "nonexistent@example.com";
        when(userRepository.findByEmail(nonExistentEmail)).thenReturn(null);

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
        	userService.loadUserByUsername(nonExistentEmail);
        });
        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(nonExistentEmail);
    }
	
	@Test
    public void testRegisterUser_Success() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(null);
        when(userRepository.save(user)).thenReturn(user);

        DBUser registeredUser = userService.register(user);

        assertNotNull(registeredUser);
        assertEquals(user.getEmail(), registeredUser.getEmail());
        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testRegisterUser_EmailAlreadyExists_ThrowsException() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.register(user);
        });
        assertEquals("Un utilisateur avec cet email existe déjà.", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(userRepository, never()).save(any(DBUser.class));
    }

    @Test
    void updateProfil_ShouldThrowException_WhenEmailIsAlreadyTaken() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("current@example.com");

        DBUser existingUser = new DBUser();
        existingUser.setEmail("current@example.com");
        existingUser.setUsername("currentUser");

        DBUser updateUser = new DBUser();
        updateUser.setEmail("taken@example.com");

        DBUser anotherUser = new DBUser();
        anotherUser.setEmail("taken@example.com");

        when(userRepository.findByEmail("current@example.com")).thenReturn(existingUser);
        when(userRepository.findByEmail("taken@example.com")).thenReturn(anotherUser);

        assertThrows(IllegalArgumentException.class, () -> {
            userService.updateProfil(userDetails, updateUser);
        });

        verify(userRepository, never()).save(existingUser);
    }
    
    @Test
    void updateProfil_ShouldUpdateEmail_WhenEmailIsValid() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("current@example.com");

        DBUser existingUser = new DBUser();
        existingUser.setEmail("current@example.com");
        existingUser.setUsername("currentUser");

        DBUser updateUser = new DBUser();
        updateUser.setEmail("new@example.com");
        updateUser.setPassword("");

        when(userRepository.findByEmail("current@example.com")).thenReturn(existingUser);
        when(userRepository.findByEmail("new@example.com")).thenReturn(null);
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        DBUser result = userService.updateProfil(userDetails, updateUser);

        assertEquals("new@example.com", result.getEmail());
        verify(userRepository).save(existingUser);
    }

//    @Test
//    void updateProfil_ShouldUpdatePassword_WhenPasswordIsProvided() {
//        UserDetails userDetails = mock(UserDetails.class);
//        when(userDetails.getUsername()).thenReturn("user@example.com");
//
//        DBUser existingUser = new DBUser();
//        existingUser.setEmail("user@example.com");
//        existingUser.setPassword("oldPassword");
//
//        DBUser updateUser = new DBUser();
//        updateUser.setPassword("newPassword");
//
//        when(userRepository.findByEmail("user@example.com")).thenReturn(existingUser);
//        when(userRepository.save(existingUser)).thenReturn(existingUser);
//
//        DBUser result = userService.updateProfil(userDetails, updateUser);
//        
//        assertNotEquals(existingUser.getPassword(), result.getPassword());
//        verify(passwordEncoder).encode("newPassword");
//        verify(userRepository).save(existingUser);
//    }

    @Test
    void updateProfil_ShouldNotUpdatePassword_WhenPasswordIsEmpty() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("current@example.com");

        DBUser existingUser = new DBUser();
        existingUser.setEmail("current@example.com");
        existingUser.setPassword("oldPassword");

        DBUser updateUser = new DBUser();
        updateUser.setPassword("");

        when(userRepository.findByEmail("current@example.com")).thenReturn(existingUser);
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        DBUser result = userService.updateProfil(userDetails, updateUser);

        assertEquals("oldPassword", result.getPassword());
        //verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository).save(existingUser);
    }

    @Test
    void updateProfil_ShouldUpdateUsername_WhenUsernameIsProvided() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("current@example.com");

        DBUser existingUser = new DBUser();
        existingUser.setEmail("current@example.com");
        existingUser.setUsername("currentUser");

        DBUser updateUser = new DBUser();
        updateUser.setUsername("newUsername");
        updateUser.setPassword("");

        when(userRepository.findByEmail("current@example.com")).thenReturn(existingUser);
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        DBUser result = userService.updateProfil(userDetails, updateUser);

        assertEquals("newUsername", result.getUsername());
        verify(userRepository).save(existingUser);
    }

    @Test
    void getProfil_ShouldReturnUser_WhenEmailExists() {
        String email = "user@example.com";
        DBUser existingUser = new DBUser();
        existingUser.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(existingUser);

        DBUser result = userService.getProfil(email);

        assertEquals(existingUser, result);
    }

    @Test
    void getProfil_ShouldReturnNull_WhenEmailDoesNotExist() {
        String email = "nonexistent@example.com";

        when(userRepository.findByEmail(email)).thenReturn(null);

        DBUser result = userService.getProfil(email);

        assertNull(result);
    }
    
    @Test
    void createRelation_ShouldCreateRelation_WhenValid() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("user@example.com");

        DBUser user = new DBUser();
        user.setEmail("user@example.com");

        DBUser relation = new DBUser();
        relation.setEmail("relation@example.com");

        when(userRepository.findByEmail("user@example.com")).thenReturn(user);
        when(userRepository.findByEmail("relation@example.com")).thenReturn(relation);
        when(userRepository.save(user)).thenReturn(user);

        DBUser result = userService.createRelation(userDetails, "relation@example.com");

        assertTrue(result.getConnections().contains(relation));
        verify(userRepository).save(user);
    }

    @Test
    void createRelation_ShouldThrowException_WhenRelationNotFound() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("user@example.com");

        DBUser user = new DBUser();
        user.setEmail("user@example.com");

        when(userRepository.findByEmail("user@example.com")).thenReturn(user);
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.createRelation(userDetails, "nonexistent@example.com");
        });

        assertEquals("Relation not found.", exception.getMessage());
        verify(userRepository, never()).save(user);
    }

    @Test
    void createRelation_ShouldThrowException_WhenRelationWithSelf() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("user@example.com");

        DBUser user = new DBUser();
        user.setEmail("user@example.com");

        when(userRepository.findByEmail("user@example.com")).thenReturn(user);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.createRelation(userDetails, "user@example.com");
        });

        assertEquals("Relation with self.", exception.getMessage());
        verify(userRepository, never()).save(user);
    }

    @Test
    void createRelation_ShouldThrowException_WhenRelationAlreadyExists() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("user@example.com");

        DBUser user = new DBUser();
        user.setEmail("user@example.com");

        DBUser relation = new DBUser();
        relation.setEmail("relation@example.com");

        when(userRepository.findByEmail("user@example.com")).thenReturn(user);
        when(userRepository.findByEmail("relation@example.com")).thenReturn(relation);

        user.getConnections().add(relation);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.createRelation(userDetails, "relation@example.com");
        });

        assertEquals("This connection already exists.", exception.getMessage());
        verify(userRepository, never()).save(user);
    }

}
