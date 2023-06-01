package ua.alisasira.spt.parse;

public class ConnectionParseCommand extends BaseParseCommand {

    public ConnectionParseCommand(DataContext context) {
        super(context);
    }

    @Override
    public Command execute(String input) {
        String[] parts = input.split(" ");
        if (parts.length >= 2) {
            context.getCities().getLast().addConnection(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        }
        return getNext();
    }
}