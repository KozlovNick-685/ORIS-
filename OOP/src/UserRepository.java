import java.sql.SQLException;
import java.util.List;

public interface UserRepository extends CrudRepository<User>{
    List<User> findAllByAge(Integer age) throws SQLException;

    void saveAll(List<User> users) throws SQLException;
}
