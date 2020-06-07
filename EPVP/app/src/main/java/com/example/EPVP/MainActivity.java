package com.example.cameratest;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

//import com.baidu.mapapi.http.HttpClient;

//import com.baidu.location.BDAbstractLocationListener;

public class MainActivity extends AppCompatActivity {
    private Button btn_camera;
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    private LocationMode mCurrentMode;

    private static final String HEX_STRING = "0123456789ABCDEF";

    //server bianliang
    public String createFilePath = "";
    public String URL = "http://10.8.66.93:8080/ServletTest/ServletTest/UploadServlet";
    public String loc;
    public String dateNow;
    public String zhongwencanshu;
    public String guodu;
    public String videoName;
    //UI
    TextureMapView mMapView;
    BitmapDescriptor mCurrentMarker;
    BaiduMap mBaiduMap;

   // CompoundButton.OnCheckedChangeListener radioButtonListener;
    //Button requestLocButton;
    private boolean isFirstLoc = true;

    StringBuffer sb = new StringBuffer(256);


    //String result = "";
    //String url = "http://10.7.30.67:8080/home/taobaolist";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);


        //Map View
        mCurrentMode = LocationMode.NORMAL;
        mMapView = (TextureMapView) findViewById(R.id.mTexturemap);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                LocationMode.FOLLOWING,
                true,
                mCurrentMarker));

        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);

        //Location View
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(myListener);

        LocationClientOption option = new LocationClientOption();
        option.setIsNeedLocationPoiList(true);
        option.setIsNeedAddress(true);
        option.setScanSpan(1000);
        option.setOpenGps(true);
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setNeedDeviceDirect(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();



        btn_camera = (Button) findViewById(R.id.btn_camera);
        btn_camera.setOnClickListener(click);
        Log.i("MainActivity", "_________________________________________________start");
        //connectInternet();
        //Log.i("asdaa", result);
                /*在控制台输出result*/


    }


    private View.OnClickListener click = new View.OnClickListener(){
        @Override
        public void onClick(View view){

            Intent intent = null;
            switch(view.getId()) {
                case R.id.btn_camera:
                    intent = new Intent();
                    intent.setAction("android.media.action.VIDEO_CAPTURE");
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.putExtra("android.intent.extras.CAMERA_FACING", 0);


                    SimpleDateFormat sTimeFormat=new SimpleDateFormat("yyyy-MM-dd.hh:mm:ss");
                    dateNow=sTimeFormat.format(new Date());
                    createFilePath = "/sdcard/video"+dateNow+".3gp";
                        File file = new File(createFilePath);
                        if (file.exists()) {
                            file.delete();
                    }
                    Uri uri = Uri.fromFile(file);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(intent, 0);

                    //server part



        //register(loc,"fire");
//                    String enc = "UTF-8";
                    //register(loc,"fire");
                    break;
            }
            Log.i("MainActivity", "video mode"+sb.toString());
        }
    };

    private void register(String time, String location, String videoName) {
        String registerUrlStr = URL + "?time=" + time + "&location=" + location + "&videoName=" + videoName;
        new MyAsyncTask().execute(registerUrlStr);
    }

    /**
     * AsyncTask类的三个泛型参数：
     * （1）Param 在执行AsyncTask是需要传入的参数，可用于后台任务中使用
     * （2）后台任务执行过程中，如果需要在UI上先是当前任务进度，则使用这里指定的泛型作为进度单位
     * （3）任务执行完毕后，如果需要对结果进行返回，则这里指定返回的数据类型
     */
    public static class MyAsyncTask extends AsyncTask<String, Integer, String> {

       // private TextView tv; // 举例一个UI元素，后边会用到

        public MyAsyncTask() {
            //tv = v;
        }

//        @Override
//        protected void onPreExecute() {
//            Log.w("WangJ", "task onPreExecute()");
//        }

        /**
         * @param params
         */
        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            StringBuilder response = new StringBuilder();
            try {
                URL url = new URL(params[0]); // 声明一个URL
                connection = (HttpURLConnection) url.openConnection(); // 打开该URL连接
                connection.setRequestMethod("GET"); // 设置请求方法
                connection.setConnectTimeout(80000); // 超时时间
                connection.setReadTimeout(80000);
                InputStream in = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response.toString(); // 这里返回的结果就作为onPostExecute方法的入参
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // 如果在doInBackground方法，那么就会立刻执行本方法
            // 本方法在UI线程中执行，可以更新UI元素，典型的就是更新进度条进度，一般是在下载时候使用
        }

        /**
         * 运行在UI线程中，所以可以直接操作UI元素
         * @param s
         */
//        @Override
//        protected void onPostExecute(String s) {
//            Log.w("WangJ", "task onPostExecute()");
//            //tv.setText(s);
//        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(mLocationClient.isStarted()){
            Toast.makeText(this, sb.toString(), Toast.LENGTH_LONG).show();

        }
//        if(mLocationClient.isStarted()){
//            Toast.makeText(this, "Location Client: Successful !", Toast.LENGTH_SHORT).show();
//        }
        System.out.println(requestCode+" here return code");
        String filePath = createFilePath;
        String serverURL ="http://10.8.66.93:8080/ServletTest/ServletTest/UploadServlet";

        UploadFile uf = new UploadFile();
        uf.upload(serverURL,filePath);
        System.out.println("先or后");
        //doGET 上传
        try {
            String param = URLEncoder.encode(zhongwencanshu ,"utf-8").replaceAll("\\+","%20");
            guodu = loc+param;
            //重新构造一个文件名
            videoName = dateNow+".3gp";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        register(dateNow,guodu,videoName);
        System.out.println("99999999999");

        super.onActivityResult(requestCode,resultCode,data);
    }






    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    .direction(location.getDirection())
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .build();

            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                //设置缩放中心点；缩放比例；
                builder.target(ll).zoom(18.0f);
                //给地图设置状态
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }

            loc ="latitude%20"+ location.getLatitude()+"%20lontitude%20"+location.getLongitude();
            //中文参数
            zhongwencanshu = location.getCountry()+" "+location.getCity()+" "+location.getDistrict()+" "+location .getStreet()+" "+location.getAddrStr();
//            loc = location.getLocationDescribe();
//            loc = toBrowserCode(loc);

//            try {
//                loc= URLEncoder.encode(loc, "UTF-8");
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
            sb.append(location.getTime());
            sb.append("\nlocType description : ");// *****对应的定位类型说明*****
            sb.append(location.getLocTypeDescription());
            sb.append("\nlatitude : ");// 纬度
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");// 经度
            sb.append(location.getLongitude());
            sb.append("\nradius : ");// 半径
            sb.append(location.getRadius());
            sb.append("\nCountryCode : ");// 国家码
            sb.append(location.getCountryCode());
            sb.append("\nCountry : ");// 国家名称
            sb.append(location.getCountry());
            sb.append("\ncitycode : ");// 城市编码
            sb.append(location.getCityCode());
            sb.append("\ncity : ");// 城市
            sb.append(location.getCity());
            sb.append("\nDistrict : ");// 区
            sb.append(location.getDistrict());
            sb.append("\nStreet : ");// 街道
            sb.append(location .getStreet());
            sb.append("\naddr : ");// 地址信息
            sb.append(location.getAddrStr());
            sb.append("\nUserIndoorState: ");// 返回用户室内外判断结果
            sb.append(location.getUserIndoorState());
            sb.append("\nDirection(not all devices have value): ");
            sb.append(location.getDirection());// 方向
            sb.append("\nlocationdescribe: ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            sb.append("\nPoi: ");// POI信息
            if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                for (int i = 0; i < location.getPoiList().size(); i++) {
                    Poi poi = (Poi) location.getPoiList().get(i);
                    sb.append(poi.getName() + ";");
                }
            }
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 速度 单位：km/h
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());// 卫星数目
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 海拔高度 单位：米
                sb.append("\ngps status : ");
                sb.append(location.getGpsAccuracyStatus());// *****gps质量判断*****
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                // 运营商信息
                if (location.hasAltitude()) {// *****如果有海拔高度*****
                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());// 单位：米
                }
                sb.append("\noperationers : ");// 运营商信息
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }

            //logMsg(sb.toString());


           // Toast.makeText(this, "Camera Closed", Toast.LENGTH_SHORT).show();
        }
        //Log.i("BaiduLocationApiDem", sb.toString());
    }

    public static String toBrowserCode(String word) {
        byte[] bytes = word.getBytes();

        //不包含中文，不做处理
        if (bytes.length == word.length())
            return word;

        StringBuilder browserUrl = new StringBuilder();
        String tempStr = "";

        for (int i = 0; i < word.length(); i++) {
            char currentChar = word.charAt(i);

            //不需要处理
            if ((int) currentChar <= 256) {

                if (tempStr.length() > 0) {
                    byte[] cBytes = tempStr.getBytes();

                    for (int j = 0; j < cBytes.length; j++) {
                        browserUrl.append('%');
                        browserUrl.append(HEX_STRING.charAt((cBytes[j] & 0xf0) >> 4));
                        browserUrl.append(HEX_STRING.charAt((cBytes[j] & 0x0f) >> 0));
                    }
                    tempStr = "";
                }

                browserUrl.append(currentChar);
            } else {
                //把要处理的字符，添加到队列中
                tempStr += currentChar;
            }
        }
        return browserUrl.toString();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

//Log.i("BaiduLocationApiDem", sb.toString());
//    }
}
