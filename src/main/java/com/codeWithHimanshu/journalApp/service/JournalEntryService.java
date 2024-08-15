package com.codeWithHimanshu.journalApp.service;

import com.codeWithHimanshu.journalApp.entity.JournalEntry;
import com.codeWithHimanshu.journalApp.entity.User;
import com.codeWithHimanshu.journalApp.repository.JournalEntryRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class JournalEntryService {


    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;


    @Transactional
    public void saveJournal(JournalEntry journalEntry, String userName){
        try{
            User user = userService.findByUserName(userName);
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry saved = journalEntryRepository.save(journalEntry);
            user.getJournalEntries().add(saved);
            userService.saveEntry(user);
        }
        catch (Exception e){
            log.error("Erron : ",e);
            throw new RuntimeException("An error occurred while execution : ",e);
        }
    }
    public void saveJournal(JournalEntry journalEntry){
        journalEntryRepository.save(journalEntry);
    }
    public List<JournalEntry> getJournals(){
        return journalEntryRepository.findAll();
    }
    public Optional<JournalEntry> getJournalsById(ObjectId myId){
        return journalEntryRepository.findById(myId);
    }
    @Transactional
    public boolean deleteJournalById(ObjectId myId, String userName){
        boolean removed=false;
        try {
            User user = userService.findByUserName(userName);
            removed = user.getJournalEntries().removeIf(x -> x.getId().equals(myId));
            if(removed){
                userService.saveEntry(user);
                journalEntryRepository.deleteById(myId);
            }
        }
        catch (Exception e){
            throw new RuntimeException("An error occurred while deleting the entry : ",e);
        }
        return removed;
    }
}
