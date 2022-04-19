package enums;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Commands {
    CREATE_USER("user create --(?<option1>\\w+) (?<value1>\\w+) " +
            "--(?<option2>\\w+) (?<value2>\\w+) " +
            "--(?<option3>\\w+) (?<value3>\\w+)"),
    LOGIN_USER("user login --(?<option1>\\w+) (?<value1>\\w+) " +
            "--(?<option2>\\w+) (?<value2>\\w+)"),
    ENTER_MENU("menu enter (?<menuName>.+)"),
    CHANGE_NICKNAME("profile change --nickname (?<nickname>\\w+)"),
    CHANGE_PASSWORD("profile change --password --(?<option1>\\w+) (?<value1>\\w+) " +
            "--(?<option2>\\w+) (?<value2>\\w+)");


    private String regex;

    Commands(String regex) {
        this.regex = regex;
    }

    public static Matcher getCommandMatcher(String input, Commands command) {
        Matcher matcher = Pattern.compile(command.regex).matcher(input);
        if (matcher.matches()) return matcher;
        return null;
    }
    public static HashMap<String, String> getOptions(Matcher matcher, int countOfOptions) {
        HashMap<String, String> options = new HashMap<>();
        for (int i = 1; i <= countOfOptions; i++) {
            options.put(matcher.group("option" + i), matcher.group("value" + i));

        }
        return options;
    }
}
