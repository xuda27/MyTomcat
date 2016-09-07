package javabeans;

import java.util.Map;

/**
 * 每个项目  servlet的映射对象
 * @author eden
 *
 */
public class ServletMapper {
	private String servletName; // servlet的名称
	private String servletClass; // servlet的class
	private String  servletUrl; // servlet的映射路径
	private Map<String,String> initParams; // 实例化参数
	public String getServletName() {
		return servletName;
	}
	public void setServletName(String servletName) {
		this.servletName = servletName;
	}
	public String getServletClass() {
		return servletClass;
	}
	public void setServletClass(String servletClass) {
		this.servletClass = servletClass;
	}
	public String getServletUrl() {
		return servletUrl;
	}
	public void setServletUrl(String servletUrl) {
		this.servletUrl = servletUrl;
	}
	public Map<String, String> getInitParams() {
		return initParams;
	}
	public void setInitParams(Map<String, String> initParams) {
		this.initParams = initParams;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((initParams == null) ? 0 : initParams.hashCode());
		result = prime * result
				+ ((servletClass == null) ? 0 : servletClass.hashCode());
		result = prime * result
				+ ((servletName == null) ? 0 : servletName.hashCode());
		result = prime * result
				+ ((servletUrl == null) ? 0 : servletUrl.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ServletMapper other = (ServletMapper) obj;
		if (initParams == null) {
			if (other.initParams != null)
				return false;
		} else if (!initParams.equals(other.initParams))
			return false;
		if (servletClass == null) {
			if (other.servletClass != null)
				return false;
		} else if (!servletClass.equals(other.servletClass))
			return false;
		if (servletName == null) {
			if (other.servletName != null)
				return false;
		} else if (!servletName.equals(other.servletName))
			return false;
		if (servletUrl == null) {
			if (other.servletUrl != null)
				return false;
		} else if (!servletUrl.equals(other.servletUrl))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ServletMapper [servletName=" + servletName + ", servletClass="
				+ servletClass + ", servletUrl=" + servletUrl + ", initParams="
				+ initParams + "]";
	}
	public ServletMapper(String servletName, String servletClass,
			String servletUrl, Map<String, String> initParams) {
		super();
		this.servletName = servletName;
		this.servletClass = servletClass;
		this.servletUrl = servletUrl;
		this.initParams = initParams;
	}
	public ServletMapper() {
		super();
	}
	
	
	
}
