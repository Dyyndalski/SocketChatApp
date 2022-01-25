package client;

import java.util.Comparator;

public class Sorting implements Comparator<Holder> {

    @Override
    public int compare(Holder o1, Holder o2) {
        if (o1.getDateTime() != o2.getDateTime()) {
            return o1.getDateTime().compareTo(o2.getDateTime());
        }
        return 0;
    }
}