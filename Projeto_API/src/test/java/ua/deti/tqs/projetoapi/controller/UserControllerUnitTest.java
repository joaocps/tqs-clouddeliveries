package ua.deti.tqs.projetoapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;

import java.util.List;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.BDDMockito.given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import ua.deti.tqs.projetoapi.controllers.UserController;
import ua.deti.tqs.projetoapi.entities.User;
import ua.deti.tqs.projetoapi.repositories.UserRep;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserControllerUnitTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserRep userRep;

    @Autowired
    ObjectMapper obj;

    @Test
    public void getUsersTest() throws Exception {
        User alex = new User("Alex", "Silva", "123456");

        List<User> allUsers = Arrays.asList(alex);

        given(userRep.findAll()).willReturn(allUsers);

        mvc.perform(get("/user/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", is(alex.getFirstName())))
                .andExpect(jsonPath("$[0].lastName", is(alex.getLastName())));
    }

    @Test
    public void getUserTest() throws Exception {
        User lucas = new User("lucasfrb45@ua.pt", "passw0rd");
        User pedro = new User("pedro@ua.pt", "whitemonkey");

        given(userRep.findById(lucas.getId())).willReturn(Optional.of(lucas));
        given(userRep.findById(pedro.getId())).willReturn(Optional.of(pedro));

        mvc.perform(get("/user/" + lucas.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(lucas.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(lucas.getLastName())));

    }

    @Test
    public void processTest() throws JsonProcessingException, Exception {

        User test = new User("Diogo", "Costa", "diogocosta");
        test.setAddress("Tomar");
        test.setCountry("Portugal");
        test.setEmail("email@ua.pt");
        test.setPhoneNumber(929184723);

        RequestBuilder request = post("/user/")
                .param("email", test.getEmail())
                .param("password", test.getPassword())
                .param("firstName", test.getFirstName())
                .param("lastName", test.getLastName())
                .param("address", test.getAddress())
                .param("country", test.getCountry())
                .param("phoneNumber", Integer.toString(test.getPhoneNumber()));

        mvc
                .perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(view().name("redirect:../choosetype"));
    }

    @Test
    public void verifyTest() throws Exception {
        User test = new User("diogo@ua.pt", "diogocosta");
        given(userRep.findByEmail(test.getEmail())).willReturn(Optional.of(test));

        RequestBuilder request = post("/user/verify_login/")
                .param("email", test.getEmail())
                .param("password", "diogocosta");

        mvc
                .perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(view().name("redirect:../userhome"));

    }

    @Test
    public void verifyUpdateTest() throws Exception {
        User test = new User("Diogo", "Costa", "diogocosta");
        test.setAddress("");
        test.setCountry("P");
        test.setEmail("email@ua.pt");
        test.setPhoneNumber(929184723);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", test);

        RequestBuilder request = post("/user/update/").session(session)
                .param("email", test.getEmail())
                .param("password", test.getPassword())
                .param("firstName", test.getFirstName())
                .param("lastName", test.getLastName())
                .param("address", test.getAddress())
                .param("country", test.getCountry())
                .param("phoneNumber", Integer.toString(test.getPhoneNumber()));

        mvc
                .perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(view().name("redirect:../userhome"));

    }

    @Test
    public void verifyUpdateErrorTest() throws Exception {
        User test = new User("Diogo", "Costa", "diogocosta");
        test.setAddress("");
        test.setCountry("P");
        test.setEmail("email@ua.pt");

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", test);

        RequestBuilder request = post("/user/update/").session(session)
                .param("email", test.getEmail())
                .param("password", test.getPassword())
                .param("firstName", test.getFirstName())
                .param("lastName", test.getLastName())
                .param("address", test.getAddress())
                .param("country", test.getCountry())
                .param("phoneNumber", "ola7878");

        mvc
                .perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(view().name("redirect:../error"));

    }

    @Test
    public void verifyUpdateAndUpdateErrorTest() throws Exception {
        User test = new User("Joao", "Santos", "joaosantos");
        test.setAddress("");
        test.setCountry("Portugal");
        test.setEmail("xyz@ua.pt");
        test.setPhoneNumber(929184723);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", test);

        RequestBuilder request = post("/user/update/").session(session)
                .param("email", test.getEmail())
                .param("password", test.getPassword())
                .param("firstName", test.getFirstName())
                .param("lastName", test.getLastName())
                .param("address", test.getAddress())
                .param("country", test.getCountry())
                .param("phoneNumber", Integer.toString(test.getPhoneNumber()));

        mvc
                .perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(view().name("redirect:../userhome"));

        RequestBuilder request2 = post("/user/update/").session(session)
                .param("email", test.getEmail())
                .param("password", test.getPassword())
                .param("firstName", test.getFirstName())
                .param("lastName", test.getLastName())
                .param("address", test.getAddress())
                .param("country", test.getCountry())
                .param("phoneNumber", "999/99999");

        mvc
                .perform(request2)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(view().name("redirect:../error"));

    }

    @Test
    public void verifyUpdateErrorandUpdateTest() throws Exception {
        User test = new User("Joao", "Santos", "joaosantos");
        test.setAddress("");
        test.setCountry("Portugal");
        test.setEmail("xyz@ua.pt");
        test.setPhoneNumber(929184723);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", test);

        RequestBuilder request = post("/user/update/").session(session)
                .param("email", test.getEmail())
                .param("password", test.getPassword())
                .param("firstName", test.getFirstName())
                .param("lastName", test.getLastName())
                .param("address", test.getAddress())
                .param("country", test.getCountry())
                .param("phoneNumber", "66666666+");

        mvc
                .perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(view().name("redirect:../error"));

        RequestBuilder request2 = post("/user/update/").session(session)
                .param("email", test.getEmail())
                .param("password", test.getPassword())
                .param("firstName", test.getFirstName())
                .param("lastName", test.getLastName())
                .param("address", test.getAddress())
                .param("country", test.getCountry())
                .param("phoneNumber", Integer.toString(test.getPhoneNumber()));

        mvc
                .perform(request2)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(view().name("redirect:../userhome"));

    }

    public String asJsonString(final Object o) {
        try {
            return obj.writeValueAsString(o);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
