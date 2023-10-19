package ua.alisasira.wrapper;

import ua.alisasira.bean.MessageBean;

import java.util.List;

public class MessageBeanWrapper {

    private List<MessageBean> messages;

    public MessageBeanWrapper(List<MessageBean> messages) {
        this.messages = messages;
    }

    public List<MessageBean> getMessages() {
        return messages;
    }
}
