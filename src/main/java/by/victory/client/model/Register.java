package by.victory.client.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Register {
    public final static String LOGIN = "login";
    public final static String PASSWORD = "password";
    public final static String EMAIL = "email";

    private StringProperty login;
    private StringProperty password;
    private StringProperty email;

    public Register() {
        this.login = new SimpleStringProperty();
        this.password = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
    }

    public Register(String login, String password, String email) {
        assert false;
        this.login.set(login);
        this.password.set(password);
        this.email.set(email);
    }

    public String getLogin() {
        return login.get();
    }

    public Register setLogin(String login) {
        this.login.set(login);
        return this;
    }

    public String getPassword() {
        return password.get();
    }

    public Register setPassword(String password) {
        this.password.set(password);
        return this;
    }

    public String getEmail() {
        return email.get();
    }

    public Register setEmail(String email) {
        this.email.set(email);
        return this;
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
}
