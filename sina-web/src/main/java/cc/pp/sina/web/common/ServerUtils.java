package cc.pp.sina.web.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.restlet.engine.Engine;
import org.restlet.engine.converter.ConverterHelper;
import org.restlet.ext.jackson.JacksonConverter;

/**
 * 服务相关工具类
 * @author wgybzb
 *
 */
public class ServerUtils {

	private static DateFormat sinaDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);

	public static void configureJacksonConverter() {
		JacksonConverter jacksonConverter = getRegisteredJacksonConverter();
		ObjectMapper objectMapper = jacksonConverter.getObjectMapper();
		objectMapper.setDateFormat(sinaDateFormat);
		objectMapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
	}

	private static JacksonConverter getRegisteredJacksonConverter() {
		List<ConverterHelper> converters = Engine.getInstance().getRegisteredConverters();
		for (ConverterHelper converterHelper : converters) {
			if (converterHelper instanceof JacksonConverter) {
				return (JacksonConverter) converterHelper;
			}
		}
		throw new RuntimeException("Can not find JacksonCOnverter.");
	}

}
