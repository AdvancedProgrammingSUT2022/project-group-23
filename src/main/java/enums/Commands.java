package enums;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Commands {
    CREATE_USER("user create --(?<firstOption>\\w+) (?<firstValue>\\w+) " +
            "--(?<secondOption>\\w+) (?<secondValue>\\w+) " +
            "--(?<thirdOption>\\w+) (?<thirdValue>)\\w+");


    private String regex;

    Commands(String regex) {
        this.regex = regex;
    }

    public static Matcher getCommandMatcher(String input, Commands command) {
        Matcher matcher = Pattern.compile(command.regex).matcher(input);
        if (matcher.matches()) return matcher;
        return null;
    }
}
