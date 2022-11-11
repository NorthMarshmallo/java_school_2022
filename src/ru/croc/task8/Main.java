package ru.croc.task8;

import java.text.FieldPosition;
import java.util.Scanner;
import java.util.Locale;
import java.text.NumberFormat;

public class Main {
    public static void main(String[] args) throws WrongInputException{
        Scanner in = new Scanner(System.in).useDelimiter("\n"); //для того, чтобы при вводе через пробел чего-то после
        //первого double выдавало ошибку. Однако если будет разом введена не одна строка через copy past, то посчитает первый
        //double и выведет. Как я понимаю тут либо обрабатывать это и дважды делать Enter после ввода в обычных случаях
        //(Scanner ек знает что в следующей строке, если ее сразу через past не ввести), либо оставлять так, поэтому
        // я выбрала второе.
        try {
            convertToCurrency(in, Locale.KOREA);
        }
        catch (Exception e){
            System.out.println(e);
        }

        in.close();
    }
    public static void convertToCurrency(Scanner in, Locale locale) throws WrongInputException{

        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        String output = "";
        StringBuffer forOutp = new StringBuffer("Result: ");
        FieldPosition pos = new FieldPosition(0);
        System.out.println("Enter a double: ");
        if (in.hasNextBigDecimal()) {   //для корретного вывода больших чисел
            output = String.valueOf(format.format(in.nextBigDecimal(), forOutp, pos));
            System.out.println(output);
        } else{
            throw new WrongInputException(in.nextLine());
        }
    }

}
