import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class validationUtils {
    public static boolean validateEmail(String email) {
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)[\\w-]{2,63}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean validateUsername(String username) {
        String regex = "^[a-z\\d-]{2,10}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(username);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean validateQaPhoneNumber(String ph) {
        String regex = "^(?:00|\\+)974(\\d{8})$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(ph);

        String regex2 = "^(\\(974\\))(\\d{8})$";
        Pattern pattern2 = Pattern.compile(regex2);
        Matcher matcher2 = pattern2.matcher(ph);

        if (matcher.matches() || matcher2.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean validateDate(String date) {
        String regex = "^\\d{2}-\\d{2}-\\d{4}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(date);

        String regex2 = "^\\d{2}/\\d{2}/\\d{4}$";
        Pattern pattern2 = Pattern.compile(regex2);
        Matcher matcher2 = pattern2.matcher(date);

        String regex3 = "^\\d{2}/\\d{2}/\\d{2}$";
        Pattern pattern3 = Pattern.compile(regex3);
        Matcher matcher3 = pattern3.matcher(date);

        String regex4 = "^\\d{2}-\\d{2}-\\d{2}$";
        Pattern pattern4 = Pattern.compile(regex4);
        Matcher matcher4 = pattern4.matcher(date);

        if (matcher.matches() || matcher2.matches() || matcher3.matches() || matcher4.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean validateNoScript(String input) {
        String regex = "(?:<script>|</script>)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if (!matcher.find()) {
            return true;
        } else {
            return false;
        }
    }

}
