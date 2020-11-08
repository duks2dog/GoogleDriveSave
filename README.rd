# GoogleDriveSave
 このアプリはtanimoto shotaが自分用に作った、引数で与えられたファイルをGoogleDriveへ
 アップロードするTLCoopのサブアプリです。
 このアプリ単体でも利用は可能です。自分用に改造して使っていただくのは問題ありませんが、
 GoogleDevelopperからCredentialsを取得しなければなりません。
 
# 仕組み

* **resources**ディレクトリにGoogleDevelopperから取得した**Credentials.json**を配置してください。
* GoogleDriveのOAuth認証接続を構築後、ファイルに必要な情報をセットにしてアップロードを実行します。

<pre>
├─gradle
│  └─wrapper
├─src
│  ├─main
│  │  ├─java
│  │  │  ├─implememts
│  │  │  └─interfaces
│  │  └─resources
│  └─test
└─tokens
</pre>
