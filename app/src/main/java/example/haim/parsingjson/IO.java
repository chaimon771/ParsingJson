package example.haim.parsingjson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by DELL e7440 on 01/06/2017.
 */

public class IO {
    public static String getString(InputStream in) throws IOException {

        StringBuilder builder = new StringBuilder();
        String line = null;

        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        try {
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        }
        finally {
            reader.close();
        }

        return builder.toString();
    }
}
