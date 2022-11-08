package ru.croc.task4;

public class AnnotatedImage {

    private final String imagePath;

    private final Annotation[] annotations;

    public AnnotatedImage(String imagePath, Annotation... annotations) {
        this.imagePath = imagePath;
        this.annotations = annotations;
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public Annotation[] getAnnotations() {
        return this.annotations;
    }

    public Annotation findByPoint(double x, double y){
        for (Annotation an : this.annotations){
            if (an.checkInFigure(x, y)){
                return an;
            }
        }
        return null;
    }

    //перегрузка метода, на случай, если удобнее подать точку, чем x и y
    public Annotation findByPoint(Point z) {
        return findByPoint(z.getX(), z.getY());
    }
    public Annotation findByLabel(String str) {
        for (Annotation an : this.annotations){
            if (an.checkInLabel(str)){
                return an;
            }
        }
        return null;
    }
}
