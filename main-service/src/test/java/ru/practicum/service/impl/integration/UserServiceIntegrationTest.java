package ru.practicum.service.impl.integration;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.UserDto;
import ru.practicum.model.User;
import ru.practicum.service.impl.UserServiceImpl;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
@AllArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceIntegrationTest {

    private final EntityManager em;
    private final UserServiceImpl service;

    @Test
    public void getAllCorrect() {
        List<UserDto> users = service.getUsers(0, 10, null);
        assertEquals(users.size(), 3);
    }

    @Test
    public void getAllByIdsCorrect() {
        List<UserDto> users = service.getUsers(0, 10, List.of(1, 2));
        assertEquals(users.size(), 2);
        assertEquals(users.get(0).getId(), 1);
        assertEquals(users.get(1).getId(), 2);
    }

    @Test
    public void deleteCorrect() {
        TypedQuery<User> query0 = em.createQuery("Select u from User u", User.class);
        List<User> users = query0.getResultList();
        assertEquals(users.size(), 3);

        service.deleteUser(3);

        users = query0.getResultList();
        assertEquals(users.size(), 2);
    }

    @Test
    public void createCorrect() {
        TypedQuery<User> query0 = em.createQuery("Select u from User u", User.class);
        List<User> users = query0.getResultList();
        assertEquals(users.size(), 3);

        UserDto userDto = new UserDto(null, "user4", "user4@mail.ru");
        service.createUser(userDto);
        TypedQuery<User> query1 = em.createQuery("Select u from User u WHERE u.id = 4", User.class);
        User user = query1.getSingleResult();
        assertEquals(user.getId(), 4);
        assertEquals(user.getName(), "user4");
        assertEquals(user.getEmail(), "user4@mail.ru");

        users = query0.getResultList();
        assertEquals(users.size(), 4);
    }

    @Test
    public void getUserByIdCorrect() {
        UserDto user = service.getUserByIdOrThrow(1);
        assertEquals(user.getId(), 1);
        assertEquals(user.getName(), "user1");
    }
}
