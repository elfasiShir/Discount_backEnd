package com.dev;

import com.dev.objects.UserObject;
import com.dev.utils.Utils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.*;


@Component
public class Persist {
    private final SessionFactory sessionFactory;
    private Connection connection;

    @Autowired
    public Persist (SessionFactory sf) {
        this.sessionFactory = sf;
    }

    @PostConstruct
    public void createConnectionToDatabase () {
        try {
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/ashCollege", "root", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //הרשמה
    public boolean signUp(String username, String password) {
        boolean success = false;
        if (doesUserExist(username)) {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            UserObject userObject = new UserObject(username, password, Utils.createHash(username, password));
            session.save(userObject);
            transaction.commit();
            session.close();
            if (userObject.getUserId() != 0) {
                success = true;

            }
        }
        return success;
    }

    //כניסה
    public String logIn(String username, String password) {
        Session session = sessionFactory.openSession();
        UserObject userObject = (UserObject) session.createQuery(
                "FROM UserObject u WHERE u.username =:username AND u.password =:password")
                .setParameter("username", username)
                .setParameter("password", password)
                .uniqueResult();
        session.close();
        if (userObject != null)
            return userObject.getToken();
        else
            return null;
    }

    public UserObject getUserByToken (String token){
        Session session = sessionFactory.openSession();
        UserObject userObject = (UserObject) session.createQuery(
                "FROM UserObject u WHERE u.token = :token")
                .setParameter("token",token)
                .uniqueResult();
        session.close();
        return userObject;
    }
    public UserObject getUserByUsername (String username){
        Session session = sessionFactory.openSession();
        UserObject userObject = (UserObject) session.createQuery(
                        "FROM UserObject u WHERE u.username = :username")
                .setParameter("username",username)
                .uniqueResult();
        session.close();
        return userObject;
    }


    public void countDownTries(String username){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        UserObject userObject = getUserByUsername (username);
        int loginTries = userObject.getLogin_tries()-1;
        userObject.setLogin_tries(loginTries);
        transaction.commit();
        session.close();
    }
    public int isBlocked(String username){
        //returns number of tries 0 is blocked
        try{
            PreparedStatement preparedStatement = this.connection.prepareStatement(
                    "SELECT login_tries FROM users WHERE username = ?");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("login_tries");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }
    public void updateLoginTries(String username){
        //updates user's login tries back to 5, activate this only when a user successfully logins
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(
                    "UPDATE users  SET login_tries = 5 WHERE username = ?");
            preparedStatement.setString(1, username);
            preparedStatement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean doesUsernameExists (String username) {
        boolean usernameExist = false;
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(
                    "SELECT username FROM users WHERE username = ?");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                usernameExist = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usernameExist;
    }

    public boolean doesUserExist(String username){
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(
                    "SELECT * FROM users WHERE username = ?");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public String getTokenByUsernameAndPassword(String username, String password) {
        String token = null;
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(
                    "SELECT token FROM users WHERE username = ? AND password = ?");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                token = resultSet.getString("token");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return token;
    }

    public boolean createAccount (UserObject userObject) {
        boolean success = false;
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(
                    "INSERT INTO users (username, password, token) VALUE (?, ?, ?)");
            preparedStatement.setString(1, userObject.getUsername());
            preparedStatement.setString(2, userObject.getPassword());
            preparedStatement.setString(3, userObject.getToken());
            if (!this.doesUserExist(userObject.getUsername())){
                preparedStatement.executeUpdate();
                success = true;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }

    public Integer getUserIdByToken (String token) {
        Integer id = null;
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(
                    "SELECT id FROM users WHERE token = ?");
            preparedStatement.setString(1, token);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }





    public String getUsernameById(int id) {
        String username = null;
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(
                    "SELECT username FROM users WHERE id=?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                username = resultSet.getString("username");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return username;
    }


    public boolean deleteMessageById(int messageId) {
        boolean deleteSuccess = false;
        try{
            PreparedStatement preparedStatement = this.connection.prepareStatement(
                    "DELETE FROM messages WHERE id = ? ");
            preparedStatement.setInt(1, messageId);
            preparedStatement.executeUpdate();
            deleteSuccess = true;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return deleteSuccess;
    }

    public boolean MessageWasRead(int messageId) {
        boolean MessageWasReadSuccess = false;
        // 1= readed message
        try{
            PreparedStatement preparedStatement = this.connection.prepareStatement(
                    "UPDATE messages SET `read` = '1' WHERE id = ?");
            preparedStatement.setInt(1, messageId);
            preparedStatement.executeUpdate();
            MessageWasReadSuccess = true;

        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return MessageWasReadSuccess;
    }

    public boolean addMessage (String token,String receiverPhone, String title ,String content) {
        boolean success = false;
        try {
            Integer senderId = getUserIdByToken(token);
            Integer receiverId = getUserIdByUsername(receiverPhone);
            if (senderId != null && receiverId != null) {
                PreparedStatement preparedStatement = this.connection.prepareStatement
                        ("INSERT INTO messages(senderId,receiverId,title,message,sendDate, read ) " +
                                "VALUE (?,?,?,?, CURRENT_DATE, 0 )");
                preparedStatement.setInt(1, senderId);
                preparedStatement.setInt(2, receiverId);
                preparedStatement.setString(3,title);
                preparedStatement.setString(4,content);
                preparedStatement.executeUpdate();
                success = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }

    private Integer getUserIdByUsername(String receiverPhone) {
        Integer receiverId = null;
        try{
            PreparedStatement preparedStatement = this.connection.prepareStatement(
                    "SELECT id FROM users WHERE username = ? "
            );
            preparedStatement.setString(1,receiverPhone);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
                receiverId = resultSet.getInt("id");
        }catch (SQLException e) { e.printStackTrace(); }
        return receiverId;
    }




}