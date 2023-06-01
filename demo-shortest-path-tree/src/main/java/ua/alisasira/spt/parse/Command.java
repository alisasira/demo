package ua.alisasira.spt.parse;

public interface Command {

    Command execute(String input);

    Command getNext();

    Command withNext(Command next);

    void setParent(Command parent);

}