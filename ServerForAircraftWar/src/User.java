public class User {
    //账号
    private String account;
    //密码
    private String password;
    //积分
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
