package ua.alisasira.consumer;

import ua.alisasira.bean.MessageBean;

public interface MessageListener {

    void onMessage(MessageBean message);
}