

Feature

1. preview camera
2. record video from usb camera
3. play rtsp stream

Usage example

   This library provides powerful api.

1. 在app的build.gradle中加入以下配置
       repositories {    
           flatDir {        
               dirs 'libs'   // aar目录
             }
       }
   
2. 将aar文件拷贝到app/libs目录下
3. 在dependencies中加入aar引用
       compile(name: 'device_sdk-v1.0.0.0.x', ext: 'aar')
       //compile(name: 'libvlc-3.0.0-null', ext: 'aar')
   
4. manifests中加入
       <service android:name="com.wesine.device_sdk.service.HeartBeatService" />
       <receiver android:name="com.wesine.device_sdk.service.AlarmReceiver" />
   
5. 在Application 中加入
       @Override
           public void onCreate() {
               super.onCreate();
               Device.init(this);//设备串号校验
       	    startService(new Intent(this, HeartBeatService.class));// 定时清理产生数据
           }
   
6. 在Activity的加入
       @Override
           protected void onCreate(Bundle savedInstanceState) {
               super.onCreate(savedInstanceState);
               setContentView(R.layout.activity_main);
               player = (WesineGLSV) findViewById(R.id.player);
               cameraView = (CameraView) findViewById(R.id.cameraView);
               cameraUtil = CameraUtil.getCameraUtil();
               cameraUtil.setCameraView(cameraView);
       
               //vlcClient = VlcClient.getVlcClient();
               //vlcClient.init(this, "rtsp://122.205.5.5:8554/1.ts");
               //vlcClient.setWesineGLSV(player);
               //vlcClient.onCreat();
       
           }

       @Override
       protected void onResume() {
           //vlcClient.onResume();
           cameraUtil.onResume();
           super.onResume();
       }
    
       @Override
       protected void onPause() {
           //vlcClient.onPause();
           cameraUtil.onPause();
           super.onPause();
       }
    
       @Override
       protected void onDestroy() {
           //vlcClient.onDestroy();
           super.onDestroy();
       }
    
       public void record(View view) {
           cameraUtil.record();
       }
    
       public void stop(View view) {
           cameraUtil.stopRecording();
       }

       
    
       
    
    

资源占用

录视频资源占用比例：

cpu 8%-17%

内存 4MB

文件占用大小 30M/min
