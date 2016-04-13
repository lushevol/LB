package org.kylin.klb.sysInfo;

public class InnerClass {
	private static void staticMethod() {
	}

	private void instanceMethod() {
		Inner3 inner3 = new Inner3();

		InnerClass.Inner3.Inner4 inner4 = new InnerClass.Inner3.Inner4();
	}

	public static class Inner1 {
		public Inner1() {
			// InnerClass.access$0();
		}
	}

	static class Inner2 {
	}

	private static class Inner3 {
		public static class Inner4 {
		}
	}
}
