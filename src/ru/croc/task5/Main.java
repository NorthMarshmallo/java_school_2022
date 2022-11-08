package ru.croc.task5;
import ru.croc.task4.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("There is what your image have:");
        AnnotatedImage image = imageCreation(); //внутри можно создать любую картинку с аннотиациями
        System.out.println(' ');
        Point checkPoint = new Point(80, 80);
        Annotation an1 = pointCheck(checkPoint,image); //для получения первой фигуры, в которой будет точка
        Annotation an2 = labelCheck("ee", image); //для получения первой фигуры, в подписи которой будет строка
        Annotation an = an2; //выберите любую аннотацию из найденных или любую присутствующую по индексу (ниже шаблон)
        //для самой картинки аннотации также изменятся, поскольку хранятся в ней по ссылке
        // Annotation an = image.getAnnotations()[index];
        an.moveFigure(-15,-15);
        System.out.println("Following figure was moved:");
        System.out.println(image.getAnnotations()[0]);
        System.out.println("Update for image:");
        pointCheck(checkPoint,image);
    }

    protected static AnnotatedImage imageCreation() {
        // Посмотрим на аннотацию картинки с деревом и машиной на ней
        Point c1 = new Point(100, 100);
        Figure circle = new Circle(c1, 10);
        Annotation an1 = new Annotation(circle, "Tree");
        Annotation an2 = new Annotation(new Rectangle(new Point(100, 100), new Point(150, 200)), "Car");

        Annotation[] annotations = {an1, an2};

        AnnotatedImage image = new AnnotatedImage("somePath", annotations);
        for (Annotation an : image.getAnnotations()) {
            System.out.println(an);
        }
        return image;
    }

    protected static Annotation pointCheck(Point checkPoint, AnnotatedImage image){
        Annotation pointPlace = image.findByPoint(checkPoint);
        if (pointPlace != null) {
            System.out.println("The point " + checkPoint.view() + " is contained in { " + pointPlace + " }");
        } else {
            System.out.println("The point " + checkPoint.view() + " is not contained in any figure");
        }
        return pointPlace;
    }
    protected static Annotation labelCheck(String label, AnnotatedImage image){
        Annotation labelPlace = image.findByLabel(label);
        if (labelPlace != null) {
            System.out.println("The label '" + label + "' is contained in { " + labelPlace + " }");
        } else {
            System.out.println("The label '" + label + "' is not contained in any label");
        }
        return labelPlace;
    }
}
