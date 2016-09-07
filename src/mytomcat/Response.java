package mytomcat;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import utils.Config;
import javabeans.ServletMapper;
import javabeans.WebXmlObject;

/**
 * 响应对象
 * @author eden
 *
 */
public class Response {
	private OutputStream out;
	private String projectName = null;
	public Response(OutputStream out){
		this.out = out;
	}
	
	/**
	 * path= /a/servlet /a/web/servlet /a /a/ http://127.0.0.1:8080/a/web/servlet? http://127.0.0.1:8080/a/
	 * @param path
	 */
	public void redirect(String path){ // /wowo
//		System.out.println("redirect"+path);
		String flag = path.substring(1); // wowo
		String url = null;
		
		if(flag.contains("/") && flag.indexOf("/")!=flag.length()-1){
			projectName = path.substring(0,flag.indexOf("/"));
			url = flag.substring(flag.indexOf("/"));// 请求的资源地址
			
			// 查看当前项目中有没有配置web.xml
			List<ServletMapper> servletMapper = WebXmlObject.webObject.get(projectName);
			if(servletMapper == null || servletMapper.size()<=0){ // 说明此项目没有配置servlet信息
				String fileInfo = Config.getConfig().getProperty("path") +"\\"+flag.replace("/", "\\");
				System.out.println(fileInfo);
				if(flag.endsWith(".jpg")){
					send(fileInfo,"application/x-jpg");
				}else if(flag.endsWith(".jpeg") || flag.endsWith(".jpe") ){
					send(fileInfo,"image/jpeg");
				}else if(flag.endsWith(".gif")){
					send(fileInfo,"image/gif");
				}else if(flag.endsWith(".css")){
					send(fileInfo,"text/css");
				}else if(flag.endsWith(".js")){
					send(fileInfo,"application/x-javascript");
				}else if(flag.endsWith(".jsp")){
					send(fileInfo,"text/html");
				}else{
					sendFile(getFileToString(fileInfo));
				}
				
			}else{ // 说明有配置项
				boolean bl = false;
				for(ServletMapper mapper : servletMapper){
					if(url.equals(mapper.getServletUrl())){// 说明要调用当前这个servlet的处理类
						
						 bl = true;
						 break;
					}
				}
				
				if(!bl){ // 如果没有找到，则当成静态资源读取返回，则处理方式跟下面的else的处理方式一样
					searchDefaultPage(flag);
				}
			}
		}else{ // 如果项目名后面没有东西，则访问该项目的web.xml中默认页面  //a  a/  wowo/ wowo 
			searchDefaultPage(flag);
		}
	}
	
	/**
	 * 检索默认路径并返回
	 * @param flag
	 */
	private void searchDefaultPage(String flag) {
		projectName = flag.replace("/", ""); // wowo
		Map<String,List<String>> welcomeList = WebXmlObject.welcomeList;
		if(welcomeList!=null && welcomeList.size()>0){
			//判断此项目有没有在web.xml中配置默认访问页面
			if(welcomeList.containsKey(projectName)){// 说明这个项目在web.xml中配置默认访问页面
				List<String> list = welcomeList.get(projectName);
				
				String fileInfo = null;
				for(String str:list){
					fileInfo = this.getFileToString(Config.getConfig().getProperty("path")+"\\"+projectName+"\\"+str);
					if(fileInfo == null){
						continue;
					}else{
						break;
					}
				}
				if(fileInfo != null){
					sendFile(fileInfo);
				}else{
					sendTomcatDefaultPage();
				}
			}else{// 没有在web.xml配置默认页面
				sendTomcatDefaultPage();
			}
		}else{// 没有配置
			sendTomcatDefaultPage();
		}
	}
	
	private void sendTomcatDefaultPage() {
		//首先读取服务器配置的默认页面
		String url = Config.getConfig().getProperty("defaultPage"); // defaultPage = index.html,index.htm,index.jsp
		
		if(url.contains(",")){
			//String urls = url.split(",");
			String[] urls = url.split(",");
			String filePage = null;
			int i=0,len=0;
			for(i=0,len=urls.length;i<len;i++){ // wowo/
				String path = Config.getConfig().getProperty("path")+"\\"+projectName+"\\"+urls[i];
				filePage = getFileToString(path);
				if(filePage != null){// 说明已经读到
					break;
				}
			}
			if(i>=len){ // 说明不是从中间跳出的循环，也就是没有找到服务器配置的默认页面
				error404();
			}else{
				sendFile(filePage);
			}
		}else{
			//到项目中查找该页面存在
			String filePage = getFileToString(Config.getConfig().getProperty("path")+"\\"+projectName +"\\"+url);
			if(filePage!=null){
				sendFile(filePage);
			}else{
				error404();
			}
		}
		
	}
	
	/**
	 * 读取指定文件
	 * @param path：要读取的文件
	 * @return:如果文件存在，则返回该文件的字符串，如果不存在返回null
	 */
	private String getFileToString(String path){ // D:\mytomcat\webapps\wowo\index.html
		File f1 = new File(path);
		if(f1.exists()){
			FileInputStream fis = null;
			try {
				StringBuffer sbf = new StringBuffer();
				int len= -1;
				byte [] bt = new byte[1024];
				fis =  new FileInputStream(f1);
				
				while( (len=fis.read(bt))!=-1){
					sbf.append(new String(bt,0,len));
				}
				return sbf.toString();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				if(fis!=null){
					try {
						fis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}else{
			return null;
		}
		return null;
	}

	/**
	 * 发送页面文件 支持的格式 html css js htm
	 * @param str 要发送的内容
	 */
	private void sendFile(String str){
//		String ms ="HTTP/1.1 200 OK\r\nContent-Type:text/html\r\nContent-Length:"+ str.getBytes().toString()+"\r\n\r\n"+str;
		String ms ="HTTP/1.1 200 OK\r\nContent-Type:text/html;charset=utf-8\r\n\r\n"+str;
		try {
			out.write(ms.getBytes());
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param str 路径
	 * @param ContentType 
	 */
	private void send(String str,String ContentType){
//		String ms ="HTTP/1.1 200 OK\r\nContent-Type:text/html\r\nContent-Length:"+ str.getBytes().toString()+"\r\n\r\n"+str;
		File f1 = new File(str);
		if(f1.exists()){
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(f1);
				byte[] bt = new byte[fis.available()];
				fis.read(bt);
				String ms ="HTTP/1.1 200 OK\r\nContent-Type:"+ContentType+"\r\n\r\n";
				out.write(ms.getBytes());
				out.write(bt);
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				if(fis != null){
					try {
						fis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}else{
			error404();
		}
	}
	
	
	private void error404(){
		System.out.println("404...");
		try {
			String err = "<h1>File Not Found</h1>";
			String message = "HTTP/1.1 404 File Not Found\r\nContent-Type:text/html;charset=utf-8\r\nContent-Length:"+err.length()+"\r\n\r\n"+err;
			out.write(message.getBytes());
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
