import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Calculator {

    public static void main(String[] args) throws Exception {
        StringCalculator calculator = new StringCalculator();
        Command command = calculator.readArgumentsAndValidate();
        System.out.println(calculator.performCommand(command));
    }

    private static class Command {

        private final String firstArgument;
        private final String operation;
        private final String secondArgument;

        public Command(String firstArgument, String operation, String secondArgument) throws Exception {
            this.firstArgument = firstArgument;
            this.operation = operation;
            this.secondArgument = secondArgument;
            isValid();
        }

        public boolean isFirstArgumentString() {
            return firstArgument.charAt(0) == '\"' && firstArgument.charAt(firstArgument.length() - 1) == '\"';
        }

        public boolean isSecondArgumentString() {
            return secondArgument.charAt(0) == '\"' && secondArgument.charAt(secondArgument.length() - 1) == '\"';
        }

        public boolean isSecondArgumentNumber() {
            try {
                int number = Integer.parseInt(secondArgument);
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        public boolean isFirstArgumentNumber() {
            try {
                int number = Integer.parseInt(firstArgument);
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        public void isValid() throws Exception {
            if (isFirstArgumentNumber()) {
                throw new Exception("First argument cannot be a number");
            } else if (!isFirstArgumentString()) {
                throw new Exception("Input correct first argument");
            } else if (!isSecondArgumentString() && !isSecondArgumentNumber()) {
                throw new Exception("Input correct second argument");
            } else if (isSecondArgumentNumber() && (Integer.parseInt(secondArgument) < 1 || Integer.parseInt(secondArgument) > 10)) {
                throw new Exception("Number should be from 1 to 10 inclusive");
            } else if (isFirstArgumentString() && firstArgument.length() - 2 > 10) {
                throw new Exception("String cannot be longer that 10 symbols");
            } else if (isSecondArgumentString() && secondArgument.length() - 2 > 10) {
                throw new Exception("String cannot be longer that 10 symbols");
            } else if (isSecondArgumentNumber() && Float.parseFloat(secondArgument) != (Integer.parseInt(secondArgument) * 1.0)) {
                throw new Exception("Number should be integer");
            }
        }

        public boolean isOperationAddition() {
            return operation.equals("+");
        }

        public boolean isOperationSubtraction() {
            return operation.equals("-");
        }

        public boolean isOperationMultiplication() {
            return operation.equals("*");
        }

        public boolean isOperationDivision() {
            return operation.equals("/");
        }

        public String getFirstArgument() {
            return firstArgument;
        }

        public String getSecondArgument() {
            return secondArgument;
        }
    }

    private static class StringCalculator {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        public StringCalculator() {
        }

        public Command readArgumentsAndValidate() throws Exception {
            Command command;
            String line = reader.readLine();
            String[] elements = new String[3];
            if (line.contains(" + ")) {
                int index = line.indexOf(" + ");
                elements[0] = line.substring(0, index);
                elements[1] = "+";
                elements[2] = line.substring(index + 3);
            } else if (line.contains(" - ")) {
                int index = line.indexOf(" - ");
                elements[0] = line.substring(0, index);
                elements[1] = "-";
                elements[2] = line.substring(index + 3);
            } else if (line.contains(" * ")) {
                int index = line.indexOf(" * ");
                elements[0] = line.substring(0, index);
                elements[1] = "*";
                elements[2] = line.substring(index + 3);
            } else if (line.contains(" / ")) {
                int index = line.indexOf(" / ");
                elements[0] = line.substring(0, index);
                elements[1] = "/";
                elements[2] = line.substring(index + 3);
            } else {
                throw new Exception("Invalid operation");
            }
            command = new Command(elements[0].trim(), elements[1].trim(), elements[2].trim());
            return command;
        }

        public String performCommand(Command command) throws Exception {

            String f = trimQuotes(command.getFirstArgument());
            String s = command.isSecondArgumentString() ? trimQuotes(command.getSecondArgument()) : command.getSecondArgument();

            if (command.isOperationAddition() && command.isFirstArgumentString() && command.isSecondArgumentString()) {
                return addQuotes(trim40(f + s));
            } else if (command.isOperationSubtraction() && command.isFirstArgumentString() && command.isSecondArgumentString()) {
                if (f.contains(s)) {
                    return addQuotes(f.replaceFirst(s, ""));
                } else {
                    return addQuotes(f);
                }
            } else if (command.isOperationMultiplication() && command.isFirstArgumentString() && command.isSecondArgumentNumber()) {
                String new_str = "";
                for (int i = 0; i < Integer.parseInt(s); ++i) {
                    new_str += f;
                }
                return addQuotes(trim40(new_str));
            } else if (command.isOperationDivision() && command.isFirstArgumentString() && command.isSecondArgumentNumber()) {
                int new_length = f.length() / Integer.parseInt(s);
                return addQuotes(f.substring(0, new_length));
            } else {
                throw new Exception("Calculator does not support this operation");
            }
        }

        public String trim40(String str) {
            if (str.length() > 40) {
                return str.substring(0, 40) + "...";
            }
            return str;
        }

        public String trimQuotes(String str) {
            return str.substring(1, str.length() - 1);
        }

        public String addQuotes(String str) {
            return "\"" + str + "\"";
        }

    }
}
