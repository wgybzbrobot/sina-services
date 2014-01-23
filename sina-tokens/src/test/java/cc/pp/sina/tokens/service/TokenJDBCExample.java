package cc.pp.sina.tokens.service;

public class TokenJDBCExample {

	public static void main(String[] args) {
		TokenJDBC tokenJDBC = new TokenJDBC("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/pp_binds", "integration", "");
		System.out.println(tokenJDBC.getSinaUids());
	}

}
