package diesel.ali;

public class Status {

	private User user;
	private String statusText;

	public Status(User user, String statusText) {
		this.setUser(user);
		this.setStatusText(statusText);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getStatusText() {
		return statusText;
	}

	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.user.toString() + ": " + this.statusText;
	}
}
