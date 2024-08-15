package com.codeWithHimanshu.journalApp.controller;

import com.codeWithHimanshu.journalApp.entity.JournalEntry;
import com.codeWithHimanshu.journalApp.entity.User;
import com.codeWithHimanshu.journalApp.service.JournalEntryService;
import com.codeWithHimanshu.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping()
    public ResponseEntity<?> getAllJournalEntriesOfUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> all = user.getJournalEntries();
        if(all!=null && !all.isEmpty()){
            return new ResponseEntity<>(all,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping()
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry myEntry){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            journalEntryService.saveJournal(myEntry, userName);
            return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("id/{myId}")
    public ResponseEntity<?> getJournalEntryById(@PathVariable ObjectId myId)
    {
       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       String userName = authentication.getName();
       User user = userService.findByUserName(userName);
       List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(myId)).collect(Collectors.toList());
       if(!collect.isEmpty()) {
           Optional<JournalEntry> journalEntry = journalEntryService.getJournalsById(myId);
           if (journalEntry.isPresent()) {
               return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
           }
       }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("id/{myId}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId myId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        System.out.println("Inside deleteJournalEntryById");
        boolean removed = journalEntryService.deleteJournalById(myId, userName);
        if(removed) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("id/{myId}")
    public ResponseEntity<?> getJournalEntryById(@PathVariable ObjectId myId, @RequestBody JournalEntry myEntry){
       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       String userName = authentication.getName();
       User user = userService.findByUserName(userName);
       List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(myId)).collect(Collectors.toList());
       if(!collect.isEmpty()) {
           Optional<JournalEntry> journalEntry = journalEntryService.getJournalsById(myId);
           if (journalEntry.isPresent()) {
               JournalEntry old = journalEntry.get();
               old.setContent(myEntry.getContent()!=null && !myEntry.getContent().equals("")?myEntry.getContent():old.getContent());
               old.setTitle(myEntry.getTitle()!=null && !myEntry.getTitle().equals("")?myEntry.getTitle():old.getTitle());
               journalEntryService.saveJournal(old);
               return new ResponseEntity<>(old,HttpStatus.OK);
           }
       }
       return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
