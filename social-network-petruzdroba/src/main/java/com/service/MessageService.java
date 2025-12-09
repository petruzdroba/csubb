package com.service;

import com.domain.Message;
import com.repo.AbstractDatabaseRepository;

public class MessageService extends AbstractService<Long, Message> {
    public MessageService(AbstractDatabaseRepository<Long, Message> repository) {
        super(repository);
    }

    public void sendMessage(){

    }

    public void reply(){

    }

    public void replyAll(){

    }
}
