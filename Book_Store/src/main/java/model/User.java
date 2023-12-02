package model;

import java.util.*;

// BEAN
public class User {
    private Long id;
    private String username;
    private String password;
    private List<Role> roles;
    private List<Book> booksIfEmployee;
    private String salt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Book> getBooksIfEmployee() {
        return booksIfEmployee;
    }

    public void setBooksIfEmployee(List<Book> booksIfEmployee) {
        this.booksIfEmployee = booksIfEmployee;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Override
    public String toString() {
        return String.format("User ID: %d | Username: %s | Password: %s | Roles: %s | Salt: %s | Books: %s", id, username, password, roles, salt, booksIfEmployee);
    }
}
