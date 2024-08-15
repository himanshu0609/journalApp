package com.codeWithHimanshu.journalApp.repository;

import com.codeWithHimanshu.journalApp.entity.JournalEntry;
import com.codeWithHimanshu.journalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, ObjectId> {
    User findByUserName(String userName);
    User deleteByUserName(String userName);

}
