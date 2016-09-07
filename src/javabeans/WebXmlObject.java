package javabeans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.Config;

/**
 * web.xml的解析对象
 * @author 
 *
 */
public class WebXmlObject {
	// 每个项目对应web.xml对象中的信息<web.xml web.xml的对象信息>
	public static Map<String, List<ServletMapper>> webObject = new HashMap<String, List<ServletMapper>>();
	
	//默认页面配置信息
	public static Map<String, List<String>> welcomeList = new HashMap<String,List<String>>();

	public Map<String, List<ServletMapper>> getWebObject() {
		return webObject;
	}

	public void setWebObject(Map<String, List<ServletMapper>> webObject) {
		this.webObject = webObject;
	}

	public Map<String, List<String>> getWelcomeList() {
		return welcomeList;
	}

	public void setWelcomeList(Map<String, List<String>> welcomeList) {
		this.welcomeList = welcomeList;
	}

	public void init(){
		String basePath = Config.getConfig().getProperty("path");
		System.out.println(basePath);
	}
	
	
	
}
