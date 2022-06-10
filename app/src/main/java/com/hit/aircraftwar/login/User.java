package com.hit.aircraftwar.login;

/**
 * @author 柯嘉铭
 * @date 2022/6/10.
 * description：用户类，保存用户信息
 */
public class User {
    private String account;
    private String password;
    private int credits;

    public User(String account, String password, int credits) {
        this.account = account;
        this.password = password;
        this.credits = credits;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    @Override
    public String toString() {
        return "User{" +
                "account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", credits=" + credits +
                '}';
    }
}
