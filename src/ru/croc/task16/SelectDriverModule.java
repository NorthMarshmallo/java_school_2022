package ru.croc.task16;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.*;

public class SelectDriverModule {

    private static HashSet<Driver> driversDatabase = new HashSet<>();
    private static List<String> comfortClasses = Arrays.asList("Эконом", "Комфорт", "Ультра-Комфорт");
    private static List<String> facilities = Arrays.asList("Детское кресло", "Новый автомобиль", "Водитель со стажем",
            "Недорого для категории", "Подходит для людей с ограниченными возможностями");

    public static void main(String[] args) {
        getDriversBase("src\\\\ru\\\\croc\\\\task16\\\\DriversDatabase.txt");
        Scanner in = new Scanner(System.in);
        System.out.println("Модуль предоставит вам наиболее подходящего водителя.");
        System.out.println("На вход ожидается получить три строки: координаты double(широта от 41 до 77, долгота от 19 до 169), " +
                "желаемый класс комфорта и список особых пожеланий.");
        String line;
        while (!(line = in.nextLine()).equals("")){
            try {
                String driverId = selectDriver(new Coordinates(line.split(", ")),in.nextLine(),
                        new HashSet<>(Arrays.asList(in.nextLine().split(", "))));
                System.out.println("Вот идентификатор подходящего вам водителя: " + driverId);
            } catch (Exception e) {
                System.out.println("Условия ввода нарушены. Попробуйте еще раз");
                e.printStackTrace();
            }
        }
    }

    public static String selectDriver(Coordinates coordinates, String comfortClass, HashSet<String> requirements){
        List<Driver> driverSelectionList = new ArrayList<>(driversDatabase);
        driverSelectionList.sort((d1,d2) -> {
            if ((d1.getComfortClass().equals(comfortClass))==(d2.getComfortClass().equals(comfortClass))){
                int numberStsfdReqD1 = Util.numberContained(requirements,d1.getFacilities());
                int numberStsfdReqD2 = Util.numberContained(requirements,d2.getFacilities());
                if (numberStsfdReqD1 == numberStsfdReqD2) {
                    double d1Distance = Coordinates.compare(d1.getCoordinates(), coordinates);
                    double d2Distance = Coordinates.compare(d2.getCoordinates(), coordinates);
                    if (d1Distance == d2Distance) return 0;
                    else return (d1Distance>d2Distance) ? 1:-1;
                }
                else return (numberStsfdReqD1 < numberStsfdReqD2) ? 1:-1;
            } else return (d2.getComfortClass().equals(comfortClass)) ? 1:-1;
        });
        storeSortedDatabase("src\\\\ru\\\\croc\\\\task16\\\\SortedDatabase.txt",driverSelectionList);
        return driverSelectionList.get(0).getId();
    }

    private static void getDriversBase(String fileName){
        Random r = new Random();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            new FileWriter(fileName, false).close();
            for (int i=0; i<100; i++){
                //крайние координаты для России учтены
                Coordinates coordinates = new Coordinates(41 + r.nextDouble() * 36,19 + r.nextDouble() * 150);
                String comfortClass = comfortClasses.get((int)(Math.random() * 3));
                HashSet<String> facilityList = new HashSet<>(Util.pickNRandom(facilities, (int)(Math.random() * facilities.size())));
                //для удобства генерации id в виде id<num>
                String id = "id" + i;
                driversDatabase.add(new Driver(id, coordinates, comfortClass, facilityList));
                bw.write(id + ";" + coordinates + ";" + comfortClass + ";" + facilityList);
                bw.write("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private static void storeSortedDatabase(String fileName, List<Driver> driverList){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            new FileWriter(fileName, false).close();
            for (Driver driver : driverList){
                Coordinates coordinates = driver.getCoordinates();
                String comfortClass = driver.getComfortClass();
                HashSet<String> facilityList = driver.getFacilities();
                String id = driver.getId();
                bw.write(id + ";" + coordinates + ";" + comfortClass + ";" + facilityList);
                bw.write("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

}
