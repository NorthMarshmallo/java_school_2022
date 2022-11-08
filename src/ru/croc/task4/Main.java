package ru.croc.task4;

public class Main {
    public static void main(String[] args) {

       // Посмотрим на аннотацию картинки с деревом и машиной на ней
       Point c1 = new Point(100,100);
       Figure circle = new Circle(c1,10);
       Annotation an1 = new Annotation(circle, "Tree");
       Annotation an2 = new Annotation(new Rectangle(new Point(100,100), new Point(150,200)), "Car");

       Annotation[] annotations = {an1, an2};
       AnnotatedImage image = new AnnotatedImage("somePath", annotations);
       for (Annotation an : image.getAnnotations()){
           System.out.println(an);
        }
    }

}



