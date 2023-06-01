package ua.alisasira.spt.parse;

public class NumberParseCommand extends BaseParseCommand {

    int number = 0;
    private Command child;

    public NumberParseCommand(DataContext context) {
        super(context);
    }

    public NumberParseCommand withChild(Command child) {
        this.child = child;
        if (this.child != null) {
            this.child.setParent(this);
        }
        return this;
    }

    @Override
    public Command getNext() {
        if (number > 0) {
            number--;
            return child;
        } else if (next != null) {
            return next;
        } else {
            return parent.getNext();
        }
    }

    @Override
    public Command execute(String input) {
        number = Integer.parseInt(input);
        return getNext();
    }
}