package com.example.csgs.bean;

public class User {
    private long uid;
    private Object password;
    private String nickname;
    private String email;

    public User(long uid, Object password, String nickname, String email) {
        this.uid = uid;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "uid=" + uid +
                ", password=" + password +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public long getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public Object getPassword() {
        return password;
    }

    public void setPassword(Object password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
