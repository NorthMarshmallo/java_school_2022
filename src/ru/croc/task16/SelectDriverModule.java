package ru.croc.task16;

import ru.croc.task16.enums.ComfortClass;
import ru.croc.task16.enums.Facility;
import ru.croc.task16.exceptions.NotInBaseException;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SelectDriverModule {

    private Set<Driver> driversDatabase = new HashSet<>();
    private String baseFile = "src\\ru\\croc\\task16\\output\\DriversDatabase.txt";

    //по дефолту возьмется созданная тестовая рандомная база
    public SelectDriverModule(){
        setRandomDriversBase();
        getDriversBase();
    }

    //можно передать заданную базу (формат как у рандомной предполагается для простоты)
    public SelectDriverModule(String fileName){
        this.baseFile = fileName;
        getDriversBase();

    }

    public String selectDriver(Coordinates coordinates, String comfortClassString, Set<String> requirementsStrings)
    throws NotInBaseException{

        try {
            //организованные strTo методы кидают RuntimeException если не входит в набор значений
            ComfortClass comfortClass = ComfortClass.strToComfortClass(comfortClassString);
            Set<Facility> requirements = requirementsStrings.stream().map(Facility::strToFacility).collect(Collectors.toSet());
            List<Driver> driverSelectionList = new ArrayList<>(driversDatabase);
            driverSelectionList.sort((d1, d2) -> {
                if ((d1.getComfortClass().equals(comfortClass)) == (d2.getComfortClass().equals(comfortClass))) {
                    long numberStsfdReqD1 = requirements.stream().filter(d1.getFacilities()::contains).count();
                    long numberStsfdReqD2 = requirements.stream().filter(d2.getFacilities()::contains).count();
                    if (numberStsfdReqD1 == numberStsfdReqD2) {
                        double d1Distance = Coordinates.compare(d1.getCoordinates(), coordinates);
                        double d2Distance = Coordinates.compare(d2.getCoordinates(), coordinates);
                        if (d1Distance == d2Distance) return 0;
                        else return (d1Distance > d2Distance) ? 1 : -1;
                    } else return (numberStsfdReqD1 < numberStsfdReqD2) ? 1 : -1;
                } else return (d2.getComfortClass().equals(comfortClass)) ? 1 : -1;
            });
            storeSortedDatabase("src\\ru\\croc\\task16\\output\\SortedDatabase.txt", driverSelectionList);
            return driverSelectionList.get(0).getId();
        } catch (RuntimeException e){
            System.out.println(e.getMessage());
            throw new NotInBaseException();
        }
    }

    private void setRandomDriversBase(){

        try {
            File f = new File(baseFile);
            f.getParentFile().mkdirs();
            f.createNewFile();
        } catch (Exception e){
            System.out.println("Файл для создания дефолтной базы не создается.");
            e.printStackTrace();
            System.exit(0);
        }

        Random r = new Random();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(baseFile))) {
            new FileWriter(baseFile, false).close();
            for (int i=0; i<20; i++){
                //крайние координаты для России учтены
                Coordinates coordinates = new Coordinates(41 + r.nextDouble() * 36,19 + r.nextDouble() * 150);
                ComfortClass comfortClass = ComfortClass.randomClass();
                HashSet<Facility> facilityList = new HashSet<>(Facility.pickRandomList());
                //для удобства генерации id в виде id<num>
                String id = "id" + i;
                bw.write(id + ";" + coordinates + ";" + comfortClass + ";" + facilityList);
                bw.write("\n");
            }
        } catch (Exception e) {
            System.out.println("Не получается задать дефолтную базу.");
            e.printStackTrace();
            System.exit(0);
        }
    }

    private void getDriversBase(){

        try (BufferedReader br = new BufferedReader(new FileReader(baseFile))) {

            String line;
            Set<Facility> facilityList;
            while ((line = br.readLine()) != null) {
                String[] pl = line.split(";"); //pl for parsedLine
                String id = pl[0];
                Coordinates coordinates = new Coordinates(pl[1].split(","));
                ComfortClass comfortClass = ComfortClass.valueOf(pl[2]);
                if (!pl[3].equals("[]")) {
                    facilityList = Stream.of(pl[3].replaceAll("[\\[\\]]", "").
                            split(", ")).map(Facility::valueOf).collect(Collectors.toSet());
                } else {
                    facilityList = new HashSet<>();
                }
                driversDatabase.add(new Driver(id, coordinates, comfortClass, facilityList));
            }

        } catch (Exception e) {

            System.out.println("Что-то не так с базой.");
            e.printStackTrace();
            System.exit(0);

        }

    }

    private void storeSortedDatabase(String fileName, List<Driver> driverList){
        try {
            File f = new File(fileName);
            f.getParentFile().mkdirs();
            f.createNewFile();
        } catch (Exception e){
            System.out.println("Не получается создать файл для сохранения отсортированной базы.");
            e.printStackTrace();
            System.exit(0);
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            new FileWriter(fileName, false).close();
            for (Driver driver : driverList){
                Coordinates coordinates = driver.getCoordinates();
                ComfortClass comfortClass = driver.getComfortClass();
                Set<Facility> facilityList = driver.getFacilities();
                String id = driver.getId();
                bw.write(id + ";" + coordinates + ";" + comfortClass + ";" + facilityList);
                bw.write("\n");
            }
        } catch (Exception e) {
            System.out.println("Не получается записать отсортированную базу.");
            e.printStackTrace();
            System.exit(0);
        }
    }

}
