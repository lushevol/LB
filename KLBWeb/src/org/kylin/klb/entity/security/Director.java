package org.kylin.klb.entity.security;

import java.util.List;

public class Director {
	private String localProt;
	private String localAddress;
	private String localScheduler;
	private String localFlags;
	private String localPersistent;
	private List<Remote> remotes;

	public List<Remote> getRemotes() {
		return this.remotes;
	}

	public void setRemotes(List<Remote> remotes) {
		this.remotes = remotes;
	}

	public String getLocalProt() {
		return this.localProt;
	}

	public void setLocalProt(String localProt) {
		this.localProt = localProt;
	}

	public String getLocalAddress() {
		return this.localAddress;
	}

	public void setLocalAddress(String localAddress) {
		this.localAddress = localAddress;
	}

	public String getLocalScheduler() {
		return this.localScheduler;
	}

	public void setLocalScheduler(String localScheduler) {
		this.localScheduler = localScheduler;
	}

	public String getLocalFlags() {
		return this.localFlags;
	}

	public void setLocalFlags(String localFlags) {
		this.localFlags = localFlags;
	}

	public String getLocalPersistent() {
		return this.localPersistent;
	}

	public void setLocalPersistent(String localPersistent) {
		this.localPersistent = localPersistent;
	}

	public static class Remote {
		private String remoteAddress;
		private String remoteForward;
		private String remoteWeight;
		private String remoteActiveConn;
		private String remoteInActConn;

		public String getRemoteAddress() {
			return this.remoteAddress;
		}

		public void setRemoteAddress(String remoteAddress) {
			this.remoteAddress = remoteAddress;
		}

		public String getRemoteForward() {
			return this.remoteForward;
		}

		public void setRemoteForward(String remoteForward) {
			this.remoteForward = remoteForward;
		}

		public String getRemoteWeight() {
			return this.remoteWeight;
		}

		public void setRemoteWeight(String remoteWeight) {
			this.remoteWeight = remoteWeight;
		}

		public String getRemoteActiveConn() {
			return this.remoteActiveConn;
		}

		public void setRemoteActiveConn(String remoteActiveConn) {
			this.remoteActiveConn = remoteActiveConn;
		}

		public String getRemoteInActConn() {
			return this.remoteInActConn;
		}

		public void setRemoteInActConn(String remoteInActConn) {
			this.remoteInActConn = remoteInActConn;
		}
	}
}
