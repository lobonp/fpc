import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Sum {
    
    static class Work implements Runnable {
        private String path;
        
        public Work(String path) {
            this.path = path;
        }
        
        private int sum(FileInputStream fis) throws IOException {
            
            int byteRead;
            int sum = 0;
            
            while ((byteRead = fis.read()) != -1) {
                sum += byteRead;
            }
        
            return sum;
        }

        @Override
        public void run() {
            Path filePath = Paths.get(path);
            try {
                if (Files.isRegularFile(filePath)) {
                       FileInputStream fis = new FileInputStream(filePath.toString());
                    System.out.println(path + " : " + sum(fis));
                } else {
                    throw new RuntimeException("Non-regular file: " + path);
                }
            } catch (FileNotFoundException e) {
                System.err.println(e.getMessage());
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }


    public static void main(String[] args) throws Exception {

        if (args.length < 1) {
            System.err.println("Usage: java Sum filepath1 filepath2 filepathN");
            System.exit(1);
        }

	//many exceptions could be thrown here. we don't care
        for (String path : args) {
            Work w = new Work(path);
            Thread thread = new Thread(w);
            thread.start();
        }
    }
}
