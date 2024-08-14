import java.util.Scanner;

public class CRC {

    public static String xor(String a, String b) {
        String result = "";
        int n = b.length();
        for (int i = 1; i < n; i++) {
            result += (a.charAt(i) == b.charAt(i)) ? "0" : "1";
        }
        return result;
    }

    public static String mod2div(String dividend, String divisor) {
        int pick = divisor.length();
        String tmp = dividend.substring(0, pick);
        int n = dividend.length();
        while (pick < n) {
            if (tmp.charAt(0) == '1') {
                tmp = xor(divisor, tmp) + dividend.charAt(pick);
            } else {
                tmp = xor("0".repeat(pick), tmp) + dividend.charAt(pick);
            }
            pick += 1;
        }
        if (tmp.charAt(0) == '1') {
            tmp = xor(divisor, tmp);
        } else {
            tmp = xor("0".repeat(pick), tmp);
        }
        return tmp;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the data: ");
        String data = scanner.next();
        System.out.print("Enter the divisor: ");
        String divisor = scanner.next();
        int len = divisor.length();
        String appendedData = data + "0".repeat(len - 1);
        String remainder = mod2div(appendedData, divisor);
        String codeword = data + remainder;
        System.out.println("The transmitted codeword is: " + codeword);
        System.out.print("Enter the received codeword: ");
        String receivedData = scanner.next();
        String checkRemainder = mod2div(receivedData, divisor);
        if (checkRemainder.contains("1")) {
            System.out.println("Error detected in the received codeword.");
        } else {
            System.out.println("No error detected in the received codeword.");
        }
        scanner.close();
    }
}
