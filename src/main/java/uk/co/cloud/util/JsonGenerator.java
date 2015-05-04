package uk.co.cloud.util;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.List;

/**
 * <p>[Pattern] to provide the following to {@link Object}:</p>
 * <ul>
 * <li></li>
 * </ul>
 * <p>Example:</p>
 * <pre>
 * </pre>
 *
 * @since 0.0.1
 *  
 */
public class JsonGenerator {

  public static <T> String jsonGenerator(List<T> list){
    String jsonString = null;
    ObjectMapper om = new ObjectMapper();
    try {
      jsonString = om.writeValueAsString(list);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return jsonString;
  }

}
