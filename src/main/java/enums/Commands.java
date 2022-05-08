package enums;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Commands {
    CREATE_USER("user create --?(?<option1>\\w+) (?<value1>\\w+) " +
            "--?(?<option2>\\w+) (?<value2>\\w+) " +
            "--?(?<option3>\\w+) (?<value3>\\w+)"),
    LOGIN_USER("user login --?(?<option1>\\w+) (?<value1>\\w+) " +
            "--?(?<option2>\\w+) (?<value2>\\w+)"),
    ENTER_MENU("menu enter (?<menuName>.+)"),
    CHANGE_NICKNAME("profile change --?nickname (?<nickname>\\w+)"),

    CHANGE_USERNAME("profile change --?username (?<username>\\w+)"),
    CHANGE_PASSWORD("profile change --?password --?(?<option1>\\w+) (?<value1>\\w+) " +
            "--?(?<option2>\\w+) (?<value2>\\w+)"),
    SELECT_COMBAT_UNIT("select combat unit (?<x>\\d+) (?<y>\\d+)"),
    SELECT_NONCOMBAT_UNIT("select noncombat unit (?<x>\\d+) (?<y>\\d+)"),
    MOVE_UNIT("unit move to (?<x>\\d+) (?<y>\\d+)"),
    SELECT_CITY("select city (?<x>\\d+) (?<y>\\d+)"),
    UNIT_BUILD("unit build (?<name>\\.+)"),
    UNIT_REMOVE("unit remove (?<name>\\.+)"),
    ADD_CITIZEN_TO_TILE("add citizen to tile (?<x>\\d+) (?<y>\\d+)"),
    REMOVE_CITIZEN_FROM_TILE("remove citizen from tile (?<x>\\d+) (?<y>\\d+)"),
    CHEAT_GOLD("increase –gold (?<amount>\\d+)"),
    CHEAT_TURN("increase –turn (?<amount>\\d+)"),
    CHEAT_HAPPINESS("increase –happiness (?<amount>\\d+)"),
    CHEAT_UNIT_FULL_HEALTH("increase units health"),
    CHEAT_CITY_FULL_HEALTH("increase cities health"),
    CHEAT_INCREASE_CITIZEN("-city (?<id>\\d+) increase -citizen (?<amount>\\d+)"),
    CHEAT_BUILD_ROAD("build road (?<x>\\d+) (?<y>\\d+)"),
    CHEAT_INCREASE_MOVEMENT("increase -movement (?<amount>\\d+)"),
    CHEAT_CREATE_CITY("create city (?<x>\\d+) (?<y>\\d+)"),
    CHEAT_FINISH_STUDY("finish current study"),
    CHEAT_BUILD_UNIT("-city (?<id>\\d+) build -unit (?<name>\\.+)");
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
            if(matcher.group("option" + i).equals("u"))
                options.put("username", matcher.group("value" + i));
            else if(matcher.group("option" + i).equals("n"))
                options.put("nickname", matcher.group("value" + i));
            else if (matcher.group("option" + i).equals("p"))
                options.put("password", matcher.group("value" + i));
            else options.put(matcher.group("option" + i), matcher.group("value" + i));
        }
        return options;
    }
}
