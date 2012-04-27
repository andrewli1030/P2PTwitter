package diesel.ali;

public class User {

	private String username;
	private String name;

	public User(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
