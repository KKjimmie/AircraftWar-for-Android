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
    private int Item1 = 0;
    private int Item2 = 0;
    private int Item3 = 0;
    private int Item4 = 0;

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

    public void useCredits(){
        credits = credits-5;
        LgClient.responseWithType(LgClient.UPDATE_CREDIT);
    }

    public void addCredits(){
        credits = credits +1;
        LgClient.responseWithType(LgClient.UPDATE_CREDIT);
    }

    public void addItem1(){Item1=Item1 +1;}

    public void useItem1(){Item1=Item1 -1;}

    public int  getItem1(){ return Item1;}

    public void addItem2(){Item2=Item2 +1;}

    public void useItem2(){Item2=Item2 -1;}

    public int  getItem2(){ return Item2;}

    public void addItem3(){Item3=Item3 +1;}

    public void useItem3(){Item3=Item3 -1;}

    public int  getItem3(){ return Item3;}

    public void addItem4(){Item4=Item4 +1;}

    public void useItem4(){Item4=Item4 -1;}

    public int  getItem4(){ return Item4;}
    @Override
    public String toString() {
        return "User{" +
                "account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", credits=" + credits +
                '}';
    }
}
