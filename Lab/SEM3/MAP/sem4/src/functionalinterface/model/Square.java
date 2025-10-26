package functionalinterface.model;

public class Square {
    private double length;

    public Square(double length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "Square{" +
                "length=" + length +
                '}';
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getLength() {
        return length;
    }
}
