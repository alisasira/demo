package ua.alisasira.spt.parse;

import ua.alisasira.spt.parse.model.CityModel;

public class CityParseCommand extends BaseParseCommand {

    public CityParseCommand(DataContext context) {
        super(context);
    }

    @Override
    public Command execute(String input) {
        int id = context.getCities().size() + 1;
        context.getCities().add(new CityModel(id, input));
        return getNext();
    }
}