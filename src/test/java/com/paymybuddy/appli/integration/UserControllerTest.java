package com.paymybuddy.appli.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.paymybuddy.appli.model.DBUser;
import com.paymybuddy.appli.repository.UserRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {
	
	@Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private DBUser user;
	private final BCryptPasswordEncoder passwordEncoder= new BCryptPasswordEncoder();

    @BeforeEach
    public void setup() {
    	userRepository.deleteAll();
    	
        user = new DBUser();
        user.setEmail("test@example.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.setUsername("testUser");
        
        if (userRepository.findByEmail(user.getEmail()) == null) {
			userRepository.save(user);
		}
        
		DBUser anotherUser = new DBUser();
        anotherUser.setEmail("relation@example.com");
        anotherUser.setPassword(passwordEncoder.encode("password"));
        anotherUser.setUsername("relationUser");
        
        if (userRepository.findByEmail(anotherUser.getEmail()) == null) {
        	userRepository.save(anotherUser);
        }
    }

    @Test
    public void testGetRegister() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register.html"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    public void testPostRegister() throws Exception {    	
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", "newuser@example.com")
                .param("password", "newpassword")
                .param("username", "newUser"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        DBUser savedUser = userRepository.findByEmail("newuser@example.com");
        assert savedUser != null;
    }

    @Test
    public void testPostRegisterExistingUser() throws Exception {
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", "test@example.com")
                .param("password", "password")
                .param("username", "testUser"))
                .andExpect(status().isOk())
                .andExpect(view().name("register.html"));
    }
    
    @Test
    public void testGetLogin() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login.html"))
                .andExpect(model().attributeExists("user"));
    }
    
    @Test
    public void testPostLogin() throws Exception {    	
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", "test@example.com")
                .param("password", "newpassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil"));
    }
    
    @Test
    @WithMockUser(username = "test@example.com")
    public void testLogout() throws Exception {
        mockMvc.perform(get("/logout"))
		        .andExpect(status().is3xxRedirection())
		        .andExpect(redirectedUrl("/login"));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    public void testGetProfil() throws Exception {
        mockMvc.perform(get("/profil"))
                .andExpect(status().isOk())
                .andExpect(view().name("profil.html"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    public void testUpdateProfile() throws Exception {
        mockMvc.perform(post("/profil")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", "updatedUser")
                .param("email", "test@example.com")
                .param("password", "newpassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil"));

        DBUser updatedUser = userRepository.findByEmail("test@example.com");
        assert updatedUser != null;
        assert "updatedUser".equals(updatedUser.getUsername());
    }
    
    @Test
    public void testGetRelation() throws Exception {
        mockMvc.perform(get("/relation"))
                .andExpect(status().isOk())
                .andExpect(view().name("relation.html"))
                .andExpect(model().attributeExists("email"))
                .andExpect(model().attribute("email", ""));
    }

    @Test
    @Transactional
    @WithMockUser(username = "test@example.com")
    public void testCreateRelation() throws Exception {
        mockMvc.perform(post("/relation")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", "relation@example.com"))
                .andExpect(status().isOk());

        DBUser updatedUser = userRepository.findByEmail("test@example.com");
        assert updatedUser != null;
        assert updatedUser.getConnections().size() == 1;
        assert "relation@example.com".equals(updatedUser.getConnections().get(0).getEmail());
    }
}
