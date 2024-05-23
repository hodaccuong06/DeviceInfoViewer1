package com.navico.deviceinfoviewer;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.core.text.HtmlCompat;

public class DeviceInfoViewer {

    private static final String TAG = "DeviceInfoViewer";

    // Phương thức hiển thị thông tin thiết bị trên màn hình
    public static void displayDeviceInfo(Activity activity) {
        if (activity == null) {
            Log.e(TAG, "Activity is null");
            return;
        }

        // Lấy thông tin thiết bị
        String deviceName = Build.MANUFACTURER + " " + Build.MODEL;
        String androidVersion = Build.VERSION.RELEASE;
        int apiLevel = Build.VERSION.SDK_INT;

        // Xác định loại thiết bị (tablet hoặc điện thoại)
        String deviceType = getDeviceType(activity);

        // Kiểm tra xem ứng dụng đang chạy trên máy thực hay máy ảo
        String emulatorType = isEmulator(activity) ? "Emulator" : "Real Device";

        // Tạo chuỗi thông tin về thiết bị
        String deviceInfo = deviceName + " - <b>Android:</b> " + androidVersion + "  - <b>API:</b> " + apiLevel + " - <b>Type:</b> " + deviceType + " - " + emulatorType;

        // Hiển thị thông tin thiết bị trên màn hình
        displayDeviceInfoText(activity, deviceInfo);
    }

    // Phương thức hiển thị chuỗi thông tin thiết bị trên TextView
    private static void displayDeviceInfoText(Activity activity, String deviceInfo) {
        TextView textView = new TextView(activity);
        // Sử dụng HtmlCompat.fromHtml() để chuyển đổi chuỗi HTML sang văn bản
        textView.setText(HtmlCompat.fromHtml(deviceInfo, HtmlCompat.FROM_HTML_MODE_LEGACY));
        textView.setTextColor(Color.BLACK);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        textView.setBackgroundColor(Color.TRANSPARENT);

        // Thiết lập tham số LayoutParams cho TextView
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        params.setMargins(10, 0, 10, 30);

        // Thêm TextView vào giao diện của Activity
        activity.addContentView(textView, params);
    }

    // Phương thức xác định loại thiết bị (tablet hoặc điện thoại)
    private static String getDeviceType(Context context) {
        if (isTablet(context)) {
            return "Tablet";
        } else {
            return "Phone";
        }
    }

    /**
     * Phương thức kiểm tra xem ứng dụng đang chạy trên máy thực hay máy ảo.
     *
     * @param context Context của ứng dụng.
     * @return True nếu ứng dụng đang chạy trên máy ảo, False nếu không.
     */
    private static boolean isEmulator(Context context) {
        // Lấy vân tay của thiết bị
        String fingerprint = Build.FINGERPRINT;
        // Lấy nhà sản xuất của thiết bị
        String manufacturer = Build.MANUFACTURER;
        // Lấy thương hiệu của thiết bị
        String brand = Build.BRAND;
        // Lấy model của thiết bị
        String model = Build.MODEL;
        // Lấy sản phẩm của thiết bị
        String product = Build.PRODUCT;

        // Kiểm tra các điều kiện để xác định xem thiết bị có phải là máy ảo hay không
        return manufacturer.equalsIgnoreCase("google") && brand.equalsIgnoreCase("google") // Xác định nhà sản xuất và thương hiệu là "google"
                // Kiểm tra vân tay bắt đầu bằng "google/sdk_gphone_" hoặc "google/sdk_gphone64_"
                && (fingerprint.startsWith("google/sdk_gphone_") || fingerprint.startsWith("google/sdk_gphone64_"))
                // Kiểm tra vân tay kết thúc bằng ":user/release-keys" hoặc ":userdebug/dev-keys"
                && (fingerprint.endsWith(":user/release-keys") || fingerprint.endsWith(":userdebug/dev-keys"))
                // Kiểm tra sản phẩm bắt đầu bằng "sdk_gphone_" hoặc "sdk_gphone64_"
                && (product.startsWith("sdk_gphone_") || product.startsWith("sdk_gphone64_"))
                // Kiểm tra model bắt đầu bằng "sdk_gphone_" hoặc "sdk_gphone64_"
                && (model.startsWith("sdk_gphone_") || model.startsWith("sdk_gphone64_"))
                // Kiểm tra vân tay bắt đầu bằng "generic"
                || fingerprint.startsWith("generic")
                // Kiểm tra vân tay bắt đầu bằng "unknown"
                || fingerprint.startsWith("unknown")
                // Kiểm tra model chứa "google_sdk"
                || model.contains("google_sdk")
                // Kiểm tra model chứa "Emulator"
                || model.contains("Emulator")
                // Kiểm tra model chứa "Android SDK built for x86"
                || model.contains("Android SDK built for x86")
                // Kiểm tra BOARD là "QC_Reference_Phone" và nhà sản xuất không phải là "Xiaomi"
                || "QC_Reference_Phone".equals(Build.BOARD) && !"Xiaomi".equalsIgnoreCase(manufacturer)
                // Kiểm tra nhà sản xuất chứa "Genymotion"
                || manufacturer.contains("Genymotion")
                // Kiểm tra HOST bắt đầu bằng "Build"
                || Build.HOST.startsWith("Build")
                // Kiểm tra nhà sản xuất là "generic" và DEVICE là "generic"
                || manufacturer.equalsIgnoreCase("generic") && Build.DEVICE.equalsIgnoreCase("generic")
                // Kiểm tra sản phẩm là "google_sdk"
                || product.equalsIgnoreCase("google_sdk")
                // Kiểm tra thuộc tính hệ thống "ro.kernel.qemu" có giá trị là "1"
                || "1".equals(System.getProperty("ro.kernel.qemu"));
    }


    // Phương thức xác định loại thiết bị (tablet hoặc điện thoại) dựa trên kích thước màn hình
    private static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }
}
