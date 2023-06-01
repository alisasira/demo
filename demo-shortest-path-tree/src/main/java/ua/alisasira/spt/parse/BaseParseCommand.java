package ua.alisasira.spt.parse;

public abstract class BaseParseCommand implements Command {

    protected DataContext context;

    protected Command next;

    protected Command parent;

    public BaseParseCommand(DataContext context) {
        this.context = context;
    }

    public Command withNext(Command next) {
        this.next = next;
        return this;
    }

    public Command getNext() {
        return next != null ? next : parent.getNext();
    }

    public void setParent(Command parent) {
        this.parent = parent;
        if (next != null) {
            next.setParent(parent);
        }
    }
}