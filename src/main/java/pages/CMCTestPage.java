package pages;

import java.util.ArrayList;
import java.util.Comparator;

public class CMCTestPage {

    public static void main(String[] args) {
        ArrayList<Integer> numberArray = new ArrayList<>();
        numberArray.add(1);
        numberArray.add(5);
        numberArray.add(10);
        numberArray.add(5);
        numberArray.add(10);
        numberArray.add(9);
        numberArray.add(6);
        numberArray.add(1);
        numberArray.add(2);

        // Distinct the element
        int z = 1;
        for (int y = 0; y <  numberArray.size(); y++) {
            int firstNumber = numberArray.get(y);
            for (int x = z ; x <  numberArray.size(); x++) {
                if (firstNumber == numberArray.get(x)){
                    numberArray.remove(x);
                }
            }
            z++;
        }
        System.out.println("The distinct list: "+ numberArray);
        // Get the second largest number
        /*int z1 = 1;
        for (int y = 0; y <  numberArray.size(); y++) {
            int firstNumber = numberArray.get(y);
            for (int x = z1 ; x <  numberArray.size(); x++) {
                if (firstNumber > numberArray.get(x)){
                    //swap list
                    int swagNumber = firstNumber;
                    numberArray.set(y,numberArray.get(x));
                    numberArray.set(x,firstNumber);
                    break;
                }
            }

        }*/
        numberArray.sort(Comparator.naturalOrder());

        System.out.println("The list:" + numberArray);
        System.out.println("The second largest number:" + numberArray.get(numberArray.size()-2));

    }
}
