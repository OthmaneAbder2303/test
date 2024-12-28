package ma.ensa.lis.dao;

import ma.ensa.lis.models.Test;
import java.util.List;

public interface TestDAO {

    Test findById(String id);
    List<Test> findAll();
    void save(Test test);
    void update(Test test);
    void delete(String id);

}
