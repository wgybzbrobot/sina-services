package cc.pp.sina.web.server;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.engine.Engine;
import org.restlet.engine.converter.ConverterHelper;
import org.restlet.ext.jackson.JacksonConverter;

import cc.pp.sina.web.application.CommonInfoApplication;
import cc.pp.sina.web.conf.Config;

/**
 * 示例：
 * 1、博主用户基础信息：http://localhost:2222/sina/bozhu/uid/{uid}/baseinfo
 * 2、批量用户基础信息：http://localhost:2222/sina/bozhu/uids/{uids}/baseinfos
 * 3、粉丝uid列表信息：http://localhost:2222/sina/bozhu/uid/{uid}/fansids
 * 4、粉丝基础数据列表：http://localhost:2222/sina/bozhu/uid/{uid}/fansinfos
 * 5、博主标签数据：    http://localhost:2222/sina/bozhu/uid/{uid}/extend
 *
 *  待废弃
 * @author wgybzb
 */
public class CommonInfoServer {

	private static DateFormat sinaDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
	private final Component component;
	private final CommonInfoApplication application;

	public CommonInfoServer() {
		component = new Component();
		application = new CommonInfoApplication();
	}

	/**
	 * 主函数
	 */
	public static void main(String[] args) {

		CommonInfoServer server = new CommonInfoServer();
		server.start();
	}

	public void start() {
		component.getServers().add(Protocol.HTTP, Integer.parseInt(Config.getProperty("api.port")));
		try {
			component.getDefaultHost().attach("/sina", application);
			configureJacksonConverter();
			component.start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void stop() {
		try {
			component.stop();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void configureJacksonConverter() {
		JacksonConverter jacksonConverter = getRegisteredJacksonConverter();
		ObjectMapper objectMapper = jacksonConverter.getObjectMapper();
		objectMapper.setDateFormat(sinaDateFormat);
		objectMapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
	}

	private JacksonConverter getRegisteredJacksonConverter() {
		List<ConverterHelper> converters = Engine.getInstance().getRegisteredConverters();
		for (ConverterHelper converterHelper : converters) {
			if (converterHelper instanceof JacksonConverter) {
				return (JacksonConverter) converterHelper;
			}
		}
		throw new RuntimeException("Can not find JacksonConverter.");
	}

}
