package com.plain.spring.ioc;

public class UserService {
    private User user;

    public void test() {
        System.out.println(user.getUsername() + "--------------------");
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserService{" +
                "user=" + user +
                '}';
    }
}
