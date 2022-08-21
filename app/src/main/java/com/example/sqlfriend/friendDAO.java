package com.example.sqlfriend;
import java.util.List;

public interface friendDAO {
    void insert(friend f);
    void update(friend f);
    void delete(int id);
    friend getFriendById(int id);
    List<friend> getAllFriends();
}
