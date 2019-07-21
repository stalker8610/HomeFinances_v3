package model;

import javax.persistence.*;

@Entity
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq")
    @Column(name = "user_id")
    private long id;

    private final String login;
    private final String pass;

    public String getLogin() {
        return login;
    }

    public String getPass() {
        return pass;
    }

    public long getId() {
        return id;
    }

    public UserProfile(String login, String pass) {
        this.login = login;
        this.pass = pass;
    }

    public UserProfile(String login) {
        this.login = login;
        this.pass = login;
    }


}
