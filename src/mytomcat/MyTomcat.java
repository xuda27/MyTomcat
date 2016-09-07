package mytomcat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javabeans.WebXmlObject;

public class MyTomcat {
	public static void main(String[] args) throws IOException {
		ServerSocket ssk = new ServerSocket(8888);
		System.out.println("服务器启动。。。");
		Socket sk = null;
		
		new WebXmlObject(); // 读取配置文件
		while(true){
			sk = ssk.accept();
			System.out.println(sk.getRemoteSocketAddress() +"连接上来了");
			new Thread( new Session(sk)).start();
		}
	}
}
