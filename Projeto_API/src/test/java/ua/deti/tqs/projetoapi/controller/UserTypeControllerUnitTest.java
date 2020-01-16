package ua.deti.tqs.projetoapi.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;

import com.fasterxml.jackson.databind.ObjectMapper;

import ua.deti.tqs.projetoapi.controllers.UserTypeController;
import ua.deti.tqs.projetoapi.entities.User;
import ua.deti.tqs.projetoapi.entities.UserType;
import ua.deti.tqs.projetoapi.repositories.UserRep;
import ua.deti.tqs.projetoapi.repositories.UserTypeRep;

@RunWith(SpringRunner.class)
@WebMvcTest(UserTypeController.class)
public class UserTypeControllerUnitTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserTypeRep userTypeRep;

    @MockBean
    private UserRep userRep;

    @Autowired
    ObjectMapper obj;

    @Test
    public void completeTest() throws Exception {
        UserType type1 = new UserType(10, 100);
        UserType type2 = new UserType(100, 1000);
        UserType type3 = new UserType(100, 1000);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("mayuser", new User("test@test.pt", "test"));
        given(userTypeRep.findAll()).willReturn(Arrays.asList(type1, type2, type3));

        mvc.perform(get("/usertype/complete?id=0").session(session)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(view().name("redirect:../login"));

    }

    @Test
    public void completeErrorTest() throws Exception {
        UserType type1 = new UserType(10, 100);
        UserType type2 = new UserType(100, 1000);
        UserType type3 = new UserType(100, 1000);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", new User("test@test.pt", "test"));
        given(userTypeRep.findAll()).willReturn(Arrays.asList(type1, type2, type3));

        mvc.perform(get("/usertype/complete?id=3").session(session)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(view().name("redirect:../error"));

    }

}
