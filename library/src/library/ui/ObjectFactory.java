package library.ui;

import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.Properties;

import library.service.Singleton;

public class ObjectFactory {

	public static Object getObject(String name) throws Exception {
		Properties props = new Properties();
		props.load(new FileReader("conf.properties"));
		Class claz = Class.forName(props.getProperty(name));
		try {
			Object o = claz.newInstance();
			return o;
		} catch (IllegalAccessException iae) {
			if (claz.getAnnotation(Singleton.class) != null) {
				Singleton singleton = (Singleton) claz.getAnnotation(Singleton.class);
				String mname = singleton.methodname();
				Method method = claz.getMethod(mname);
				return method.invoke(claz);
			}
			System.out.println("Not Found");
			throw new Exception();
		}
	}

}
