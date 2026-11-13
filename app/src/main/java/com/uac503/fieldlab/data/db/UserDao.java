
package com.uac503.fieldlab.data.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.uac503.fieldlab.model.User;

@Dao
public interface UserDao {

    @Insert
    long insertUser(User user);

    @Query("SELECT * FROM users WHERE email = :email COLLATE NOCASE LIMIT 1")
    User getUserByEmail(String email);

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    User getUserById(int id);

    @Query("SELECT COUNT(*) FROM users WHERE email = :email COLLATE NOCASE")
    int emailExists(String email);
}

