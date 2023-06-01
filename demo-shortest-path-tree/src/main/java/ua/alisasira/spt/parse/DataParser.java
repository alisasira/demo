package ua.alisasira.spt.parse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class DataParser {

    public static DataContext parse(File file) {
        DataContext context = new DataContext();
        Command command = prepareCommandStructure(context);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (command != null) {
                command = command.execute(reader.readLine());
            }
        } catch (Exception e) {

        }
        return context;
    }

    private static Command prepareCommandStructure(DataContext context) {

        return new NumberParseCommand(context)
                .withChild(
                        new CityParseCommand(context)
                                .withNext(
                                        new NumberParseCommand(context)
                                                .withChild(
                                                        new ConnectionParseCommand(context)
                                                )
                                ))
                .withNext(
                        new NumberParseCommand(context)
                                .withChild(
                                        new PathParseCommand(context)
                                )
                );

    }
}