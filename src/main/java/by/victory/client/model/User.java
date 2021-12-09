package by.victory.client.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User extends Identity {
    public final static String ROLE_ID = "role_id";
    public final static String TYPE = "type";
    public final static String LOGIN = "login";
    public final static String PASSWORD = "password";
    public final static String EMAIL = "email";

    private StringProperty type;
    private StringProperty login;
    private StringProperty password;
    private StringProperty email;

    public User() {
        super();
        this.type = new SimpleStringProperty();
        this.login = new SimpleStringProperty();
        this.password = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
    }

    public User(int id, String type, String login, String password, String email) {
        super(id);
        this.type.set(type);
        this.login.set(login);
        this.password.set(password);
        this.email.set(email);
    }

    public String getLogin() {
        return login.get();
    }

    public User setLogin(String login) {
        this.login.set(login);
        return this;
    }

    public String getPassword() {
        return password.get();
    }

    public User setPassword(String password) {
        this.password.set(password);
        return this;
    }

    public String getEmail() {
        return email.get();
    }

    public User setEmail(String email) {
        this.email.set(email);
        return this;
    }

    public StringProperty getTypeProperty() {
        return type;
    }

    public StringProperty getLoginProperty() {
        return login;
    }

    public StringProperty getPasswordProperty() {
        return password;
    }

    public StringProperty getEmailProperty() {
        return email;
    }

    public User setId(int id) {
        this.id.set(id);
        return this;
    }

    public User setType(String type) {
        this.type.set(type);
        return this;
    }
}