package DZ3;

import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {

    UserRepository repo;
    List<User> loggedUsersList;
    List<User> usersList;
    User normalUser = new User("Andrey", "123456q", false);
    ;
    User adminUser = new User("PetrAdm", "q789456w", true);

    @BeforeEach
    void setUp() {
        usersList = new ArrayList<>();
        usersList.add(normalUser);
        usersList.add(adminUser);
        loggedUsersList = new ArrayList<>();
        repo = new UserRepository(loggedUsersList, usersList);
    }

    @AfterEach
    void tearDown() {
        usersList = null;
        loggedUsersList = null;
        repo = null;
    }

    @Test
    @DisplayName("Добавление нового пользователя")
    void addNewUser() {
        User newUser = new User("Ivan", "qwerty12", false);
        repo.addNewUser(newUser);
        assertEquals(newUser, repo.findByName(newUser.getName(), repo.getUsersList()));
    }

    @Test
    @DisplayName("Аутентификация нового пользователя")
    void authenticateTrue() {
        repo.authenticate("PetrAdm", "q789456w");
        assertEquals(adminUser, repo.findByName("PetrAdm", repo.getLoggedUsersList()));
    }

    @Test
    @DisplayName("Отказ в аутентификации пользователя")
    void authenticateFalse() {
        repo.authenticate("PetrAdm", "0123456"); //указан неверный пароль
        assertNull(repo.findByName("PetrAdm", repo.getLoggedUsersList()));
    }

    @Test
    @DisplayName("Поиск пользователя по имени в списке пользователей")
    void findByNameInUsersList() {
        User actualUser = repo.findByName("Andrey", repo.getUsersList());
        assertEquals(normalUser, actualUser);
    }

    @Test
    @DisplayName("Поиск пользователя по имени в списке залогинившихся пользователей")
    void findByNameInLoggedUsersList() {
        repo.authenticate("Andrey", "123456q");
        User actualUser = repo.findByName("Andrey", repo.getLoggedUsersList());
        assertEquals(normalUser, actualUser);
    }

    @Test
    @DisplayName("Проверка разлогирования ВСЕХ пользователей, кроме Админов")
    void unloginAllUsersExceptAdmins() {
        repo.authenticate("PetrAdm", "q789456w"); // аутентификация админа
        repo.authenticate("Andrey", "123456q"); // аутентификация обычного пользователя
        repo.unloginAllUsersExceptAdmins();  // разлогинили всех, кроме админов

        User resultAdmin = repo.findByName("PetrAdm", repo.getLoggedUsersList());  // д.б. adminUser
        User resultNormalUser = repo.findByName("Andrey", repo.getLoggedUsersList()); // д.б. null

        if (resultNormalUser == null) {
            assertEquals(adminUser, resultAdmin);
        } else {
            fail();
        }
    }
}