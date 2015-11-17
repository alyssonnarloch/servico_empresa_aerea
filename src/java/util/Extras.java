package util;

public class Extras {

    public static String brDateToUs(String date) {
        String[] parts;

        parts = date.split("/");

        if (parts.length < 3) {
            return "";
        }

        return parts[2] + "-" + parts[1] + "-" + parts[0];
    }
}
