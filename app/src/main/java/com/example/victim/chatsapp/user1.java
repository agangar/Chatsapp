package com.example.victim.chatsapp;

/**
 * Created by Harsh on 03-05-2017.
 */

public class user1 {
    private String username,pic,password;

    public user1() {
    }

    public user1(String username, String pic, String password) {
        this.username = username;
        this.pic = pic;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPic() {
        return pic;
    }

    public String getPassword() {
        return password;
    }
}
