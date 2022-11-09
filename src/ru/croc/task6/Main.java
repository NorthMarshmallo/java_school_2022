package ru.croc.task6;
import java.util.regex.*;

public class Main {
    public static void main(String[] args) {
        String source = "/*\n" +
                " * My // first ever program in Java!\n" +
                " */\n" +
                "class Hello { // class body starts here /*  */ \n" +
                "  \n" +
                "  /* main method */\n" +
                "  public static void main(String[] args/* we put command line arguments here*/) {\n" +
                "    // this line prints my first greeting to the screen\n" +
                "    System.out.println(\"Hi!\"); // :)\n" +
                "  }\n" +
                "} // the end\n" +
                "// to be continued...\n"; // test data
        String noComments = removeJavaComments(source);
        System.out.println(noComments);
    }
    public static String removeJavaComments(String source){
        Pattern pattern = Pattern.compile("\\/\\*((?!\\*\\/)[\\s\\S])+\\*\\/|\\/\\/(.*)"); //задали паттерн на многострочные
        // комментарии (начинается группировка с /*, далее включаются все символы пока не появится */ и этой последовательностью
        // символов группа завершается)
        // однострочные (начинается с // выполняется с помощью  quantifier и заканчивается группировка концом строки, т.к.
        // line terminators точка не считает
        Matcher matcher = pattern.matcher(source); //задали объект который паттерны в строке ищет
        return matcher.replaceAll(""); //заменили все найденные строки на пустые
    }
}



