import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

import implememts.UploadGoogleDriveServiceImpl;
import interfaces.UploadGoogleDriveService;

/**
 * File Upload To Google Drive app
 * @author shota
 */
public class DriveSave {
    private static final String APPLICATION_NAME = "Google Drive Save";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIR_PATH = System.getProperty("user.home") + "\\tokens";

    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE_FILE);
    // クレデンシャルファイル
    private static final String CREDENTIAL_FILE_PATH = "/credentials.json";
    // ファイルパスリスト
    private List<String> filepathList = null;

    private UploadGoogleDriveService uploadGoogleDriveService;


    /**
     * クレデンシャルオブジェクトの生成
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // クレデンシャルファイルロード
        InputStream in = DriveSave.class.getResourceAsStream(CREDENTIAL_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIAL_FILE_PATH + " You must register Google Drive API.");
        }
        // OAuth認証
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // ユーザー認証のリクエスト
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setAccessType("offline")
        		.setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIR_PATH)))
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    /**
     * GoogleDriveへのファイルアップロード
     * @param saveFileName 保存ファイル名
     * @param saveDirectory ディレクトリ指定ある場合は入力(null可)<br>
     *         example:sample/photo
     * @return アップロード結果
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public boolean executeGoogleDriveApi(String saveFileName, String saveDirectory) throws IOException, GeneralSecurityException {
        // APIクライアントサービス.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        uploadGoogleDriveService = new UploadGoogleDriveServiceImpl();
        List<String> result = new ArrayList<String>();
        if (this.filepathList != null) {
        	for(String filepath : this.filepathList) {
        		// Google APIのFileクラス
            	File meta = new File();
            	// JavaのFileクラス
                java.io.File upFile = new java.io.File(filepath);
            	// アップロード用の情報を用意
            	// 1:メタデータ生成
                if (saveDirectory != null) {
                	meta.setName(saveDirectory + "/" + saveFileName);
                } else {
                	meta.setName(saveFileName);
                }
                // 2:mimeを設定
                String mimeType = Files.probeContentType(upFile.toPath());
                // 3:アップロードコンテンツ作成
                FileContent mediaCont = new FileContent(mimeType, upFile);

                // ファイルアップロード処理
                result.add(uploadGoogleDriveService.fileUpload(mediaCont, meta, service));
        	}
        }
        return result.size() >0;
    }

    //コンストラクタ
    public DriveSave(String filepath){
    	List<String> tmp = new ArrayList<String>();
    	tmp.add(filepath);
    	this.filepathList = tmp;
    }

    //コンストラクタ
    public DriveSave(ArrayList<String> filepaths){
    	this.filepathList = filepaths;
    }
}