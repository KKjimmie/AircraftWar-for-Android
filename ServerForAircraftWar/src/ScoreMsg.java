import java.net.Socket;

public class ScoreMsg {
    private String account;
    private Socket targetSocket;
    private int score;

    public ScoreMsg(String account, int score) {
        this.account = account;
        this.score = score;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Socket getTargetSocket() {
        return targetSocket;
    }

    public void setTargetSocket(Socket targetSocket) {
        this.targetSocket = targetSocket;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
