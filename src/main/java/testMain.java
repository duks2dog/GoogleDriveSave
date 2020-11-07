import java.io.IOException;
import java.security.GeneralSecurityException;

public class testMain {

	public static void main(String[] args) throws IOException {
		// GoogleDriveにファイルアップロード
		DriveSave ds = new DriveSave("C:\\Users\\shota\\Downloads\\2020_11_07_20_25_41(@konotarogomame)_1.jpg");
		try {
			ds.executeGoogleDriveApi("testdata.jpg", null);
		} catch (GeneralSecurityException gse) {
			gse.printStackTrace();
			System.out.println("GoogleDriveへの認証でエラーが起きています。");
		}

	}

}
