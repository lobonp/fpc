import java.util.ArrayList;
import java.util.List;

class Buffer {
    private final List<Integer> data = new ArrayList<>();
    
    public void put(int value) {
        data.add(value);
        System.out.printf("Inserted: %02d | Buffer size: %d%n", value, data.size());
    }
    
    public int remove() {
        if (!data.isEmpty()) {
            int value = data.remove(0);
            System.out.printf("Removed:  %02d | Buffer size: %d%n", value, data.size());
            return value;
        }
        return -1;
    }
}
