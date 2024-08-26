package ru.t1academy.java.hw3.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import ru.t1academy.java.hw3.config.WebSecurityConfig;
import ru.t1academy.java.hw3.dto.user.ReturnUserDto;
import ru.t1academy.java.hw3.dto.user.UserDto;
import ru.t1academy.java.hw3.filter.JwtTokenFilter;
import ru.t1academy.java.hw3.model.UserDetailsImpl;
import ru.t1academy.java.hw3.service.authorization.JwtProvider;
import ru.t1academy.java.hw3.service.user.UserDetailsServiceImpl;
import ru.t1academy.java.hw3.service.user.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import({WebSecurityConfig.class, JwtTokenFilter.class})
class UserControllerTest {

    @MockBean
    private UserService userService;

    @SuppressWarnings("unused")
    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @SuppressWarnings("unused")
    @MockBean
    private JwtProvider jwtProvider;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void successfullyUpdateUser200() throws Exception {

        UserDetailsImpl admin = new UserDetailsImpl(
                1L,
                "admin",
                "admin@email.ru",
                "----",
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );

        UserDto updateUserDto = UserDto.builder()
                .email("user@email.ru")
                .roles(List.of("ADMIN_ROLE"))
                .build();

        ReturnUserDto returnUserDto = ReturnUserDto.builder()
                .email("user@email.ru")
                .roles(List.of("ADMIN_ROLE"))
                .build();

        when(userService.updateUser(anyLong(), any()))
                .thenReturn(returnUserDto);

        mvc.perform(put("/api/v1/users/2")
                        .content(mapper.writeValueAsString(updateUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(user(admin)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(returnUserDto)));

        verify(userService, times(1)).updateUser(2L, updateUserDto);
    }

    @Test
    void shouldReturn403WhenInsufficientRights() throws Exception {

        UserDetailsImpl user = new UserDetailsImpl(
                1L,
                "user",
                "user@email.ru",
                "----",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        UserDto updateUserDto = UserDto.builder()
                .email("user@email.ru")
                .roles(List.of("ADMIN_ROLE"))
                .build();

        ReturnUserDto returnUserDto = ReturnUserDto.builder()
                .email("user@email.ru")
                .roles(List.of("ADMIN_ROLE"))
                .build();

        when(userService.updateUser(anyLong(), any()))
                .thenReturn(returnUserDto);

        mvc.perform(put("/api/v1/users/1")
                        .content(mapper.writeValueAsString(updateUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(user(user)))
                .andExpect(status().isForbidden())
                .andExpect(content().string(""));

        verify(userService, times(0)).updateUser(anyLong(), any());
    }

    @Test
    void shouldReturn403() throws Exception {
        mvc.perform(get("/api/v1/users"))
                .andExpect(status().isForbidden());

        verify(userService, times(0)).getAll();
    }
}