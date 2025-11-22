import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtils {
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
        String regex = "^\\d{4}-\\d{2}-\\d{2}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(date);

        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean validateTime(String time) {
        String regex = "^([01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(time);

        if (matcher.matches()) {
            return true;
        }else  {
            return false;
        }
    }

    public static boolean validateNoScript(String input) {
        //if there are scripts return false
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
