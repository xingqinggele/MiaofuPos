package com.example.miaofupos.newprojectview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.net.HttpRequest;
import com.example.miaofupos.net.OkHttpException;
import com.example.miaofupos.net.RequestParams;
import com.example.miaofupos.net.ResponseCallback;
import com.example.miaofupos.utils.BitMapUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 作者: qgl
 * 创建日期：2022/8/22
 * 描述:设备绑定、签字
 */
public class BindingEquipmentSigningActivity extends BaseActivity implements View.OnClickListener {
    private SignatureView mView;
    private String merchantNo;
    private TextView signing_tv;
    private Button btnClear,btnOk;
    private String snCode = "";
    private LinearLayout iv_back;
    private TextView cread_time;
    @Override
    protected int getLayoutId() {
        statusBarConfig(R.color.new_theme_color, false).init();
        return R.layout.binding_equipment_signing_activity;
    }

    @Override
    protected void initView() {
        FrameLayout frameLayout = findViewById(R.id.fl_view);
        mView = new SignatureView(this);
        frameLayout.addView(mView);
        mView.requestFocus();
        btnClear = findViewById(R.id.btn_clear);
        btnOk = findViewById(R.id.btn_ok);
        signing_tv = findViewById(R.id.signing_tv);
        iv_back = findViewById(R.id.iv_back);
        cread_time = findViewById(R.id.cread_time);
        snCode = getIntent().getStringExtra("snCode");
        merchantNo = getIntent().getStringExtra("mcId");
        posSignature(snCode);
        cread_time.setText("");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_clear:
                mView.clear();
                break;
            case R.id.btn_ok:
                Bitmap imageBitmap = mView.getCachebBitmap();
                String s = BitMapUtil.bitmap2Base64(imageBitmap,100,Bitmap.CompressFormat.PNG);
                Log.e("图片--->",s);
                posImg(s);
                break;
            case R.id.iv_back:
                    finish();
                    break;

        }
    }

    /**
     * 自定义签字区域
     */
    class SignatureView extends View {
        //画笔
        private Paint paint;

        //画布
        private Canvas cacheCanvas;

        //位图
        private Bitmap cachebBitmap;

        //图片保存路径
        private Path path;

        public Path getPath() {
            return path;
        }

        //位图缓存
        public Bitmap getCachebBitmap() {
            return cachebBitmap;
        }

        public SignatureView(Context context) {
            super(context);
            init();
        }

        /**
         * 初始化
         */
        private void init() {
            //设置画笔
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStrokeWidth(3);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLACK);
            path = new Path();

            //创建位图
            cachebBitmap = Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888);

            //用自定义位图构建画布
            cacheCanvas = new Canvas(cachebBitmap);
            //设置画布为白色
            cacheCanvas.drawColor(Color.WHITE);
        }

        /**
         * 清除画板，重置画笔
         */
        public void clear() {
            if (cacheCanvas != null) {
                paint.setColor(Color.WHITE);
                cacheCanvas.drawPaint(paint);
                paint.setColor(Color.BLACK);
                cacheCanvas.drawColor(Color.WHITE);
                invalidate();
            }
        }


        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawBitmap(cachebBitmap, 0, 0, null);
            canvas.drawPath(path, paint);
        }


        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {

            int curW = cachebBitmap != null ? cachebBitmap.getWidth() : 0;
            int curH = cachebBitmap != null ? cachebBitmap.getHeight() : 0;
            if (curW >= w && curH >= h) {
                return;
            }

            if (curW < w) curW = w;
            if (curH < h) curH = h;
            Bitmap newBitmap = Bitmap.createBitmap(curW, curH, Bitmap.Config.ARGB_8888);
            Canvas newCanvas = new Canvas();
            newCanvas.setBitmap(newBitmap);
            if (cachebBitmap != null) {
                newCanvas.drawBitmap(cachebBitmap, 0, 0, null);
            }
            cachebBitmap = newBitmap;
            cacheCanvas = newCanvas;
        }

        private float cur_x, cur_y;

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    cur_x = x;
                    cur_y = y;
                    path.moveTo(cur_x, cur_y);
                    break;
                }
                case MotionEvent.ACTION_MOVE: {
                    path.quadTo(cur_x, cur_y, x, y);
                    cur_x = x;
                    cur_y = y;
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    cacheCanvas.drawPath(path, paint);
                    path.reset();
                    break;
                }
            }
            invalidate();
            return true;
        }

    }

    @Override
    protected void initListener() {
        btnOk.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    /**
     * 上传图片
     * @param str
     */
    private void posImg(String str){
        loadDialog.show();
        RequestParams params = new RequestParams();
        params.put("merchantNo", merchantNo);
        params.put("photo", str);
        HttpRequest.posPhotoUpload(params, getToken(),new ResponseCallback() {
            //成功回调
            @Override
            public void onSuccess(Object responseObj) {
                loadDialog.dismiss();
                finish();
            }

            //失败回调
            @Override
            public void onFailure(OkHttpException failuer) {
                loadDialog.dismiss();

                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });
    }

    /**
     * 获取签字的提示语
     * @param posCode
     */
    private void posSignature(String posCode ){
        RequestParams params = new RequestParams();
        params.put("merchantNo", merchantNo);
        HttpRequest.posSignature(params, getToken(), new ResponseCallback() {
            //成功回调
            @Override
            public void onSuccess(Object responseObj) {
                setText(responseObj);
            }

            //失败回调
            @Override
            public void onFailure(OkHttpException failuer) {
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });
    }

    /**
     * 赋值
     * @param responseObj
     */
    private void setText(Object responseObj) {
        try {
            JSONObject object = new JSONObject(responseObj.toString());
            JSONObject data = object.getJSONObject("data");
            String result = "本人"+ "<font color='red'>" + data.getString("merchname") +"</font>"+"已充分知悉将使用众鑫POS产品：<br/>"+
                    "1、激活服务费：机具激活时支付"+ "<font color='red'>" +data.getString("serviceCharge")+"</font>"+"元；" +"<br/>"+
                    "2、通讯服务费：入网"+ "<font color='red'>"+data.getString("chargingTime")+"</font>"+"天后收取，"+ "<font color='red'>"+data.getString("flowCharge")+"</font>"+"元/年；"+"<br/>"+"3、交易手续费：以实际配置为准。"+"<br/>"+"如推销人员承诺其他事项，与众鑫助手无关。";
            signing_tv.setText(Html.fromHtml(result.replace("\"","<br/>")));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}