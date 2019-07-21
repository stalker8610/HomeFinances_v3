package model;

import javax.persistence.*;


@Entity
public class Account {

    @Id
    @Column (name = "account_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_seq")
    @SequenceGenerator(name = "account_seq")
    private long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "user_id"))
    private UserProfile userProfile;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public Account(){}

    public Account(String name) { this.name = name; }

    @Override
    public String toString() {
        return "{"+this.id+"} "+this.name;
    }
}


