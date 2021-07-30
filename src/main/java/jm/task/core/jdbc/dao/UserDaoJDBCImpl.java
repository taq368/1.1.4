package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public class UserDaoJDBCImpl implements UserDao {
    private static final Logger logger = Logger.getLogger(UserDaoJDBCImpl.class.getName());

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        try (Connection connection = Util.getConnection()) {

                String sql = "CREATE TABLE if not exists users (" +
                        "`id` BIGINT AUTO_INCREMENT NOT NULL," +
                        "`name` VARCHAR(45) NULL," +
                        "`lastName` VARCHAR(45) NULL," +
                        "`age` TINYINT NOT NULL," +
                        "PRIMARY KEY (`ID`));";
                Statement statement = connection.createStatement();
                statement.execute(sql);
                logger.info("Create User's table - OK");
        } catch (SQLException ex) {
            throw new RuntimeException("Create User's table - FAIL", ex);
        }
    }

    public void dropUsersTable() {
        try (Connection connection = Util.getConnection()) {
                String sql = "DROP TABLE if exists users";
                Statement statement = connection.createStatement();
                statement.execute(sql);
        } catch (SQLException ex) {
            throw new RuntimeException("Drop User's table - FAIL", ex);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (Connection connection = Util.getConnection()) {
            String sql = "INSERT INTO users (`name`, `lastName`, `age`) VALUES (?, ?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.execute();
            logger.info("Insert user into User's table - OK");
        } catch (SQLException ex) {
            throw new RuntimeException("Insert user into User's table - FAIL", ex);
        }
    }

    public void removeUserById(long id) {
        try (Connection connection = Util.getConnection()) {
            String sql = "DELETE FROM users WHERE id = (?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            preparedStatement.execute();
            logger.log(Level.INFO, "Remove user by id={0} - OK", id);
        } catch (SQLException ex) {
            throw new RuntimeException("Remove user by id - FAIL", ex);
        }
    }

    public List<User> getAllUsers() {
        List<User> result = new ArrayList<>();
        try (Connection connection = Util.getConnection()) {
            String sql = "SELECT * FROM users;";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                Long id = rs.getLong("id");
                String name = rs.getString("name");
                String lastName = rs.getString("lastName");
                Byte age = rs.getByte("age");
                User user = new User(name, lastName, age);
                user.setId(id);
                result.add(user);
            }
            logger.info("Get all users - OK");
        } catch (SQLException ex) {
            throw new RuntimeException("Get all users - FAIL", ex);
        }
        return result;
    }

    public void cleanUsersTable() {
        try (Connection connection = Util.getConnection()) {
            String sql = "TRUNCATE TABLE users";
            Statement statement = connection.createStatement();
            statement.execute(sql);
            logger.info("Clean User's table - OK");
        } catch (SQLException ex) {
            throw new RuntimeException("Clean User's table - FAIL", ex);
        }
    }
}
