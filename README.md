

#Feature

1. record video from usb camera

2. play rtsp stream


##Usage example

   This library provides powerful api.

1. 在app的build.gradle中加入以下配置

   ```
   repositories {    
       flatDir {        
           dirs 'libs'   // aar目录
         }
   }
   ```

   ​

2. 将aar文件拷贝到app/libs目录下

3. 在dependencies中加入aar引用

   ```
   compile(name: 'app-release', ext: 'aar')
   compile(name: 'libvlc-3.0.0-null', ext: 'aar')
   ```

   ​

4. 在Application 中加入

   ```
   @Override
       public void onCreate() {
           super.onCreate();
           Device.init(this);
       }
   ```

   ​

5. 在Activity的加入

   ```
   @Override
       protected void onCreate(Bundle savedInstanceState) {
           super.onCreate(savedInstanceState);
           setContentView(R.layout.activity_main);
           player = (WesineGLSV) findViewById(R.id.player);
           cameraView = (CameraView) findViewById(R.id.cameraView);
           cameraUtil = CameraUtil.getCameraUtil();
           cameraUtil.setCameraView(cameraView);

           vlcClient = VlcClient.getVlcClient();
           vlcClient.init(this, "rtsp://122.205.5.5:8554/1.ts");
           vlcClient.setWesineGLSV(player);
           vlcClient.onCreat();

       }


       @Override
       protected void onResume() {
           vlcClient.onResume();
           cameraUtil.onResume();
           super.onResume();
       }

       @Override
       protected void onPause() {
           vlcClient.onPause();
           cameraUtil.onPause();
           super.onPause();
       }

       @Override
       protected void onDestroy() {
           vlcClient.onDestroy();
           super.onDestroy();
       }

       public void record(View view) {
           cameraUtil.record();
       }

       public void stop(View view) {
           cameraUtil.stopRecording();
       }
   ```

   ​

   ​

