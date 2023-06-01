package ua.alisasira.spt.parse;

import ua.alisasira.spt.parse.model.CityModel;
import ua.alisasira.spt.parse.model.PathModel;

public class PathParseCommand extends BaseParseCommand {

    public PathParseCommand(DataContext context) {
        super(context);
    }

    @Override
    public Command execute(String input) {
        String[] parts = input.split(" ");
        if (parts.length >= 2) {
            CityModel from = context.findCityByName(parts[0]);
            CityModel to = context.findCityByName(parts[1]);
            context.getPaths().add(new PathModel(from, to));
        }
        return getNext();
    }
}