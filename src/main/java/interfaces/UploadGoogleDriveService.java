package interfaces;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

public interface  UploadGoogleDriveService {
    /**
     * GoogleDriveへのアップロード
     * @return boolean アップロード結果
     */
	public String fileUpload(FileContent mediaContent, File meta, Drive service);

}
