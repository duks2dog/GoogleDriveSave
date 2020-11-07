package implememts;

import java.io.IOException;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import interfaces.UploadGoogleDriveService;

public class UploadGoogleDriveServiceImpl implements UploadGoogleDriveService {

	@Override
	public String fileUpload(FileContent mediaContent, File meta, Drive service) {
		String result = "";
		try {
			// ファイルアップロード
			File file = service.files().create(meta, mediaContent)
				    .setFields("id")
				    .execute();
			// 成功時はファイルIDが取得できる
			result = file.getId();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

}
