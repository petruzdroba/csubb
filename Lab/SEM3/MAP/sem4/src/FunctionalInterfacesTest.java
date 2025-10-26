import functionalinterface.Arie;
import functionalinterface.model.Circle;
import functionalinterface.model.Square;

import java.util.List;

public class FunctionalInterfacesTest{
    private static <E>void printArea(List<E> l, Arie<E> f){
        for(E e: l){
            System.out.println(f.calculate(e));
        }
    }

    public static void main(String[] args){
        List<Square> squares = List.of(new Square(1), new Square(2), new Square(3));
        List<Circle> cicles = List.of(new Circle(1), new Circle(2), new Circle(3));


        printArea(squares);
    }
}