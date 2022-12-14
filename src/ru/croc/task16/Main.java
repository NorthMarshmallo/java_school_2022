package ru.croc.task16;

import ru.croc.task16.exceptions.NotInBaseException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

import static java.lang.System.exit;

public class Main {

    public static void main(String[] args) {

        //Autocloseable -> try-with-resources
        try(Scanner in = new Scanner(System.in);) {
            System.out.println("Модуль предоставит вам наиболее подходящего водителя.");
            System.out.println("Предоставляемые классы комфорта: Ультра-Эконом, Эконом, Комфорт, Ультра-Комфорт.");
            System.out.println("Предоставляемые удобства: Детское кресло, Новый автомобиль, " +
                    "Водитель со стажем, Недорого для категории, Подходит для людей с ограниченными возможностями.");
            System.out.println("На вход ожидается получить три строки: координаты double(широта от 41 до 77, долгота от 19 до 169), " +
                    "желаемый класс комфорта и список особых пожеланий.");
            String line;
            SelectDriverModule sdm = new SelectDriverModule();
            while (!(line = in.nextLine()).equals("")) {
                try {
                    String driverId = sdm.selectDriver(new Coordinates(line.split(", ")), in.nextLine(),
                            new HashSet<>(Arrays.asList(in.nextLine().split(", "))));
                    System.out.println("Вот идентификатор подходящего вам водителя: " + driverId);
                } catch (NotInBaseException ne){
                    System.out.println(ne.getMessage());
                }
                catch (Exception e) {
                    System.out.println("Условия ввода нарушены. При вводе координат помните, что они разделены" +
                            " запятой и пробелом. Попробуйте еще раз");
                    e.printStackTrace();
                    exit(0);
                }
            }
        }
    }

}
