import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonFilter;
import org.codehaus.jackson.map.ser.FilterProvider;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;

import java.io.IOException;

public class JsonTest {
	public static void main(String[] args) throws IOException {
		Bean value = new Bean();
		value.name = "a";
		value.age = 18;
		ObjectMapper mapper = new ObjectMapper();
		// first, construct filter provider to exclude all properties but 'name', bind it as 'myFilter'
		FilterProvider filters = new SimpleFilterProvider().addFilter("myFilter",
				SimpleBeanPropertyFilter.filterOutAllExcept("name"));
		// and then serialize using that filter provider:
		String json = mapper.writer(filters).writeValueAsString(value);
		System.out.println(json);
	}
}

@JsonFilter("myFilter")
class Bean {
	public String name;
	public int age;
}