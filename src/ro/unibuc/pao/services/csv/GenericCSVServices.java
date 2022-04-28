package ro.unibuc.pao.services.csv;

import java.io.IOException;
import java.util.Vector;

public interface GenericCSVServices<T> {

    void write(T object) throws IOException;

    Vector<T> read() throws IOException;
}