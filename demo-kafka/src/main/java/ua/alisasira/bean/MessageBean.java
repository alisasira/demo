package ua.alisasira.bean;

public class MessageBean extends BaseBean {

    private String message;

    public static MessageBean build(String message) {
        MessageBean bean = new MessageBean();
        bean.setMessage(message);
        return bean;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}