package nl.hu.bep.shopping.model;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MyUser implements Principal {


    private String name;
    private String role;


    private String userName;
    private String password;

    public MyUser(String name, String role, String userName, String password) {
        this.name = name;
        this.role = role;
        this.userName = userName;
        this.password = password;

        if (!allUsers.contains(this)) {
            allUsers.add(this);
        }
    }

    private static List<MyUser> allUsers = new ArrayList<>();

    public static List<MyUser> getAllUsers() {
        return Collections.unmodifiableList(allUsers);
    }

    public static MyUser validateLogin(String userName, String password) {
        return allUsers.stream()
                .filter(myUser -> myUser.userName.equals(userName) && myUser.password.equals(password))
                .findFirst().orElse(null);
    }


    public static MyUser getUserByName(String name) {
        return allUsers.stream()
                .filter(myUser -> myUser.name.equals(name))
                .findFirst().orElse(null);
    }


    @Override
    public String getName() {
        return this.name;
    }

    public String getRole() {
        return this.role;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyUser myUser = (MyUser) o;
        return name.equals(myUser.name) && Objects.equals(role, myUser.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
