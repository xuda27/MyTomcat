package utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javabeans.ServletMapper;
import javabeans.WebXmlObject;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;

public class ExecXml {
	@Test
	public void init() {
		String basePath = Config.getConfig().getProperty("path");
		// 扫描这个文件夹下面的所有目录，因为在服务器中没一个目录就是一个项目，都是需要去加载他们的web.xml 文件
		File fls = new File(basePath);
		// 获取当前目录下的所有子目录或文件
		File[] files = fls.listFiles();

		for (File fl : files) { // 循环读取每个项目下的配置文件，然后解析
			if (fl.isDirectory()) {
				parseXml(fl);
			} else {
				continue;
			}
		}

	}
	
	@SuppressWarnings("unchecked")
	private void parseXml(File fl) {

		File webFile = null; // web.xml 文件
		List<ServletMapper> list = null; // 存放每一个web.xml中的所有servlet信息
		ServletMapper mapper; // 存放每一个servlet
		Map<String, String> servletInfo = null ;
		Map<String, String> servletMapping = null;
		List<String> welcomes = null;
		
		
		webFile = new File(fl, "WEB-INF/web.xml");
		if (webFile.exists()) { // 如果存在，则是一个动态网站
			list = new ArrayList<ServletMapper>();
			//使用dom4j中的sax解析
			SAXReader reader  = new SAXReader();
			//设置命名空间地址
			Map<String,String> map = new HashMap<String,String>();
			map.put("design", "http://java.sun.com/xml/ns/javaee"); // 值就是命名空间
			reader.getDocumentFactory().setXPathNamespaceURIs(map);
			try {
				Document doc  = reader.read(webFile);
				// 取serlvet中的配置信息
				
				List<Element> nodes = doc.selectNodes("//design:servlet");
//				System.out.println(nodes.size());
				// 循环取出每一个servlet中的配置信息
				if(nodes.size()>0){
					servletInfo = new HashMap<String, String>();
					String servletName = null;
					String servletClass = null;
					for(Element et:nodes){
						servletName = et.selectSingleNode("design:servlet-name").getText().toString();
						servletClass = et.selectSingleNode("design:servlet-class").getText().toString();
						servletInfo.put(servletName, servletClass);
					}
				}
				
				// 取出servlet-mapping的配置信息
				nodes= doc.selectNodes("//design:servlet-mapping");
				
				// 循环取出每一个servlet中的配置信息
				if(nodes.size()>0){
					servletMapping = new HashMap<String, String>();
					String servletName = null;
					String urlPattern = null;
					for(Element et:nodes){
						servletName = et.selectSingleNode("design:servlet-name").getText().toString();
						urlPattern = et.selectSingleNode("design:url-pattern").getText().toString();
						servletMapping.put(servletName, urlPattern);
					}
				}
				
				// 取出welcome-file-list的配置信息
				nodes= doc.selectNodes("//design:welcome-file-list/design:welcome-file");
				
				// 循环取出每一个servlet中的配置信息
				if(nodes.size()>0){
					welcomes = new ArrayList<String>();
					for(Element et:nodes){
						welcomes.add(et.getTextTrim());
					}
				}
				
				//将servlet-mapping与servlet关联起来
				
				if(servletMapping != null && servletMapping.size()>0){
					Set<String> keys = servletMapping.keySet();
					for(String key:keys){
						if( servletInfo.containsKey(key)){
							mapper = new ServletMapper();
							mapper.setServletName(key);
							mapper.setServletClass(servletInfo.get(key));
							mapper.setServletUrl(servletMapping.get(key));
						}else{
							throw new RuntimeException(servletMapping.get(key) + "没有对应的处理类...");
						}
					}
				}
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}
		WebXmlObject wxo = new WebXmlObject();
		wxo.getWelcomeList().put(fl.getName(),welcomes);
		wxo.getWebObject().put(fl.getName(), list);
	}
}
