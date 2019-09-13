public class Medium {
	
	private boolean inUse;

	public Medium() {
		this.inUse = false;
	}

	public void setStatus(boolean status) {
		this.inUse = status;
	}

	public boolean inUse() {
		return this.inUse;
	}
}