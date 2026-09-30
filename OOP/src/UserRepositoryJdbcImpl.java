import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryJdbcImpl implements UserRepository {

    private Connection connection;

    private static final String SQL_SELECT_FROM_DRIVERS = "select id, first_name, last_name, age from drivers";
    private static final String SQL_SELECT_BY_ID = "SELECT id, first_name, last_name, age FROM drivers WHERE id = ";
    private static final String SQL_SELECT_BY_AGE = "SELECT id, first_name, last_name, age FROM drivers WHERE age = ";
    private static final String SQL_INSERT_ONE = "INSERT INTO drivers (first_name, last_name, age) VALUES ";
    private static final String SQL_DELETE_BY_ID = "DELETE FROM drivers WHERE id = ";

    public UserRepositoryJdbcImpl(Connection connection) {
        this.connection = connection;
    }

    private User toUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getLong("id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getInt("age")
        );
    }

    @Override
    public List<User> findAll() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL_SELECT_FROM_DRIVERS);

        List<User> result = new ArrayList<>();

        while (resultSet.next()) {
            User user = new User(
                    resultSet.getLong(1),
                    resultSet.getString(2),
                    resultSet.getString("last_name"),
                    resultSet.getInt("age")
            );
            result.add(user);
        }
        return result;
    }

    @Override
    public Optional<User> findById(Long id) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(SQL_SELECT_BY_ID + id);
        if (rs.next()) {
            return Optional.of(toUser(rs));
        }
        return Optional.empty();
    }

    @Override
    public void save(User entity) throws SQLException {
        String sql = SQL_INSERT_ONE + "('" + entity.getName() + "', '" +
                entity.getSurname() + "', " + entity.getAge() + ")";
        Statement statement = connection.createStatement();
        statement.executeQuery(sql);
    }

    public void saveAll(List<User> users) throws SQLException {
        if (users == null || users.isEmpty()) {
            return;
        }

        StringBuilder sql = new StringBuilder(SQL_INSERT_ONE);
        for(int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            sql.append("('").append(user.getName()).append("', '")
                    .append(user.getSurname()).append("', ")
                    .append(user.getAge()).append(")");
            if (i < users.size() - 1) {
                sql.append(", ");
            }
        }
        sql.append(";");
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql.toString());
    }

    @Override
    public void update(User entity) throws SQLException {
        String sql = "UPDATE drivers SET first_name = '" + entity.getName()
                + "', last_name = '" + entity.getSurname()
                + "', age = " + entity.getAge()
                + " WHERE id = " + entity.getId();
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
    }

    @Override
    public void remove(User entity) throws SQLException {
        removeById(entity.getId());
    }

    @Override
    public void removeById(Long id) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate(SQL_DELETE_BY_ID + id);
    }

    @Override
    public List<User> findAllByAge(Integer age) throws SQLException {
        List<User> result = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(SQL_SELECT_BY_AGE + age);
        while (rs.next()) {
            result.add(toUser(rs));
        }
        return result;
    }
}
