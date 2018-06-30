import com.google.common.collect.Lists;

import java.util.List;
import java.util.ArrayList;

public class Schwifty
{
    public static void main(String... args)
    {
        List<String> peoples = new ArrayList<>();
        peoples.add("Rick");
        peoples.add("Morty");

        Lists.reverse(peoples).stream().forEach((p)->System.out.println(p));
        System.out.println("Let's get schwifty!");
    }
}
