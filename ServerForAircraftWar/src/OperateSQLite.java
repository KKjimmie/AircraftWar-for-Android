import java.sql.*;

/**
 * 操作数据库
 */
public class OperateSQLite {
    private Connection conn = null;

    public OperateSQLite() {
        connect();
    }

    /**
     * 与数据库建立连接
     */
    public void connect(){
        if(conn == null) {
            try {
                Class.forName("org.sqlite.JDBC");
                String url = "jdbc:sqlite:account.db";
                // 连接到数据库
                conn = DriverManager.getConnection(url);

                System.out.println("Connection to SQLite has been established.");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 与数据库断开连接
     */
    public void disConnect(){
        if(conn == null){
            return;
        }
        try {
            conn.close();
            conn = null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 向数据库中插入新账户
     * @param account 账号
     * @param password 密码
     * @param credits 积分
     */
    public boolean insert(String account, String password, int credits){
        User user = select(account);
        if(user != null){
            System.out.println("注册失败，账号已存在");
            return false;
        }

        String sql = "INSERT INTO ACCOUNT(ACCOUNT, PASSWORD ,CREDITS) VALUES( ?,?,?)";

        try{
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.setString(1, account);
            pstm.setString(2, password);
            pstm.setInt(3, credits);
            pstm.executeUpdate();
            pstm.close();
            return true;
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public User select(String account){
        String sql = "SELECT * FROM ACCOUNT WHERE ACCOUNT='" + account + "'";
        User user =null;
        try{
            if(conn == null){
                connect();
            }
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                user = new User(rs.getString("ACCOUNT"), rs.getString("PASSWORD"), rs.getInt("CREDITS"));
                System.out.println("select：选中数据" + user.toString());
            }
            rs.close();
            stmt.close();

            if(user == null){
                System.out.println("账号不存在");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }

    /**
     * 修改账户密码
     * @param account 账号
     * @param oldPwd 旧密码
     * @param newPwd 新密码
     * @return 返回true表示修改成功，false表示修改失败
     */
    public boolean updatePwd(String account, String oldPwd, String newPwd){
        // 获取旧的账户信息
        User user = select(account);
        // 判断密码是否正确
        if(! oldPwd.equals(user.getPassword())){
            System.out.println("密码不正确");
            return false;
        }

        // 更新密码
        Statement stmt = null;
        try {
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            String sql = "UPDATE ACCOUNT set PASSWORD = '" + newPwd + "' where account = '" + account +"'";
            stmt.executeUpdate(sql);
            conn.commit();
            stmt.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 更新账户积分
     * @param account 账号
     * @param credits 新的积分
     * @return 更新成功返回true，失败返回false
     */
    public boolean updateCredits(String account, int credits){
        // 获取旧的账户信息
        User user = select(account);
        // 更新积分
        Statement stmt = null;
        try {
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            String sql = "UPDATE ACCOUNT set CREDITS = " + credits + " where account = '" + account +"'";
            stmt.executeUpdate(sql);
            conn.commit();
            stmt.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 删除一个账号
     * @param account 账号
     * @return 成功返回true，失败返回false
     */
    public boolean deleteAccount(String account){
        Statement stmt = null;

        try {
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            String sql = "DELETE from ACCOUNT where ACCOUNT = '" + account + "'";
            stmt.executeUpdate(sql);
            stmt.close();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        OperateSQLite operateSQLite = new OperateSQLite();
        operateSQLite.insert("kjm", "123456", 1000);
//        operateSQLite.select("kjm");
        operateSQLite.updatePwd("kjm","123456", "111111");
//        operateSQLite.updateCredits("kjm",10000);
        operateSQLite.insert("yhl", "111111", 10000);
        operateSQLite.updateCredits("yhl", 1000);

    }
}
