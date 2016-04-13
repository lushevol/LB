package org.kylin.klb.entity.security;

import java.util.List;

public class Server {
	private String ip;
	private List<VirtualService> virtualServices;

	public static class VirtualService {
		private String virtualServiceIpName;
		private String realServerName;
		private String status;

		public String getRealServerName() {
			return this.realServerName;
		}

		public void setRealServerName(String realServerName) {
			this.realServerName = realServerName;
		}

		public String getVirtualServiceIpName() {
			return this.virtualServiceIpName;
		}

		public void setVirtualServiceIpName(String virtualServiceIpName) {
			this.virtualServiceIpName = virtualServiceIpName;
		}

		public String getStatus() {
			return this.status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

	}

	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public List<VirtualService> getVirtualServices() {
		return this.virtualServices;
	}

	public void setVirtualServices(List<VirtualService> virtualServices) {
		this.virtualServices = virtualServices;
	}
}
