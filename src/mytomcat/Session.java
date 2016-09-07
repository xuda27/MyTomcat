package mytomcat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Session implements Runnable {
	private Socket sk;
	private InputStream is;
	private OutputStream os;
	private Request request;
	
	public Session(Socket sk){
		this.sk = sk;
		try {
			is = sk.getInputStream();
			os = sk.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		request = new Request(is); //  创建一个请求
		String url = request.getUrl();
		
		Response response = new Response(os);
		
		response.redirect(url);
	}

	private void out500(Exception e){
		String protocol = request.getProtocolVersion()+" 500 Error\r\n\r\n";
		protocol+=e.getMessage();
		
		try {
			os.write(protocol.getBytes());
			os.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		}finally{
			if(os != null){
				try {
					os.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if(is!= null){
				try {
					is.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
			if(sk!=null){
				try {
					sk.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
		}
		
	}
}
