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

import cc.pp.sina.web.application.SinaUsersApplication;
import cc.pp.sina.web.conf.Config;

/**
 * 新浪用户信息服务器，包括基础数据、扩展数据和分析数据。
 *
 * 1、基础数据：http://114.112.65.13:8111/sina/users/{uid}/basic
 *                         http://60.169.74.26:8111/sina/users/{uid}/basic
 *                         http://60.169.74.26:8111/sina/users/{uid}/fromapi
 * 2、扩展数据：
 * 3、分析数据：
 *
 * @author wgybzb
 *
 */
public class SinaUsersServer {

	private static DateFormat sinaDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
	private final Component component;
	private final SinaUsersApplication application;

	public SinaUsersServer() {
		component = new Component();
		application = new SinaUsersApplication();
	}

	/**
	 * 主函数
	 */
	public static void main(String[] args) {

		SinaUsersServer server = new SinaUsersServer();
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
		throw new RuntimeException("Can not find JacksonCOnverter.");
	}

}
