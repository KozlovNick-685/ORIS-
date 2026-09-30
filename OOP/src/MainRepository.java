import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class MainRepository {

    private static final String DB_USERNAME = "postgres";

    private static final String DB_PASSWORD = "hU73_bU80";

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/testdb_11_504";

    public static void main(String[] args) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

        UserRepository userRepository = new UserRepositoryJdbcImpl(connection);

        List<User> users = userRepository.findAll();

        users.forEach(user -> System.out.println(user.getName()));


        List<User> newUsers = List.of(new User(null, "Иван", "Иванов", 25),
                new User(null, "Пётр", "Петров", 30),
                new User(null, "Анна", "Сидорова", 25),
                new User(null, "Олег", "Олегов", 35),
                new User(null, "Мария", "Мариева", 22),
                new User(null, "Сергей", "Сергеев", 28)
        );
        userRepository.saveAll(newUsers);

        List<User> usersByAge = userRepository.findAllByAge(25);
        usersByAge.forEach(System.out::println);
    }
}
