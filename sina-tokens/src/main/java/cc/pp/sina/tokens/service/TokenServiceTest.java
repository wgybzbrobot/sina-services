package cc.pp.sina.tokens.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TokenServiceTest {

	public static void main(String[] args) throws IOException {

		TokenService tokenService = new TokenService();

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File("pp_sina_uids")));) {
			for (Long uid : tokenService.getSinaUids()) {
				bw.append(uid.toString());
				bw.newLine();
			}
		}
	}

}
