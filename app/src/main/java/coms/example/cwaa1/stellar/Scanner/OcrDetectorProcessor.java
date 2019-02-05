package coms.example.cwaa1.stellar.Scanner;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import coms.example.cwaa1.stellar.Scanner.cameraui.GraphicOverlay;

import static coms.example.cwaa1.stellar.Scanner.OcrCaptureActivity.TextBlockName;
import static coms.example.cwaa1.stellar.Scanner.OcrCaptureActivity.TextBlockObject;
import static coms.example.cwaa1.stellar.Scanner.OcrCaptureActivity.TextBlockObject1;
import static coms.example.cwaa1.stellar.Scanner.OcrCaptureActivity.TextBlockObject2;

public class OcrDetectorProcessor implements  Detector.Processor<TextBlock> {

    private GraphicOverlay<OcrGraphic> mGraphicOverlay;
    Activity activity;

    private StringBuilder mobiles = new StringBuilder();
    private StringBuilder emailss = new StringBuilder();
    private boolean emailDetected = false;
    private boolean mobileDetected = false;

    private Pattern p1;
    private Pattern p2;
    private Pattern p3;
    private Pattern p4;
    private Pattern p5;
    private Pattern p6;
    private Pattern p7;
    private Pattern p8;
    private Pattern p9;
    private Pattern p10;
    private Pattern p11;
    private Pattern p12;
    private Pattern p13;
    private Pattern p14;
    private Pattern p15;
    private Pattern p16;
    private Pattern p17;
    private Pattern p18;
    private Pattern p19;
    private Pattern p20;

    OcrDetectorProcessor(GraphicOverlay<OcrGraphic> ocrGraphicOverlay, Activity activity) {
        mGraphicOverlay = ocrGraphicOverlay;
        this.activity = activity;

        p1 = Pattern.compile("\\b[0-9]{10}\\b");                                           // 9876543210
        p2 = Pattern.compile("\\b[0-9]{5}\\s[0-9]{5}\\b");                                  // 98765 41230
        p3 = Pattern.compile("\\b[0-9]{3}\\s[0-9]{3}\\s[0-9]{4}\\b");                       // 987 654 3210
        p4 = Pattern.compile("\\b[0-9]{3}-[0-9]{3}-[0-9]{4}\\b");                           // 987-654-3210
        p5 = Pattern.compile("\\([0-9]{3}\\)\\s[0-9]{7}\\b");                               // (123) 4567890
        p6 = Pattern.compile("\\([0-9]{3}\\)\\s[0-9]{3}\\s[0-9]{4}\\b");                    // (123) 456 7890

        p7 = Pattern.compile("\\+[0-9]{1,3}-?[0-9]{10}\\b");                                 // +911234567890 or +91-1234567890
        p8 = Pattern.compile("\\+[0-9]{1,3}-?[0-9]{5}\\s[0-9]{5}\\b");                       // +9112345 67890 or +91-12345 67890
        p9 = Pattern.compile("\\+[0-9]{1,3}-?[0-9]{3}\\s[0-9]{3}\\s[0-9]{4}\\b");            // +91 123 456 7890 or +91-123 456 7890
        p10 = Pattern.compile("\\+[0-9]{1,3}-?[0-9]{3}-[0-9]{3}-[0-9]{4}\\b");               // +91123-456-7890 or +91-123-456-7890
        p11 = Pattern.compile("\\+[0-9]{1,3}-?\\([0-9]{3}\\)\\s[0-9]{7}\\b");                // +91(123) 4567890 or +91-(123) 4567890
        p12 = Pattern.compile("\\+[0-9]{1,3}-?\\([0-9]{3}\\)\\s[0-9]{3}\\s[0-9]{4}\\b");     // +91(123) 456 7890 or +91-(123) 456 7890

        p13 = Pattern.compile("\\b[0-9]{4}-[0-9]{3}-[0-9]{3}\\b");                           // 9876 543 210
        p14 = Pattern.compile("\\b[0-9]{4}-[0-9]{3}-[0-9]{3}\\b");                           // 9876-543-210
        p15 = Pattern.compile("\\([0-9]{4}\\)\\s[0-9]{6}\\b");                               // (1234) 567890
        p16 = Pattern.compile("\\([0-9]{4}\\)\\s[0-9]{3}\\s[0-9]{3}\\b");                    // (1234) 567 890

        p17 = Pattern.compile("\\+[0-9]{1,3}-?\\b[0-9]{4}-[0-9]{3}-[0-9]{3}\\b");             // +919876 543 210 or +91-9876 543 210
        p18 = Pattern.compile("\\+[0-9]{1,3}-?\\b[0-9]{4}-[0-9]{3}-[0-9]{3}\\b");             // +919876-543-210 or +91-9876-543-210
        p19 = Pattern.compile("\\+[0-9]{1,3}-?\\([0-9]{4}\\)\\s[0-9]{6}\\b");                 // +91(1234) 567890 or +91-(1234) 567890
        p20 = Pattern.compile("\\+[0-9]{1,3}-?\\([0-9]{4}\\)\\s[0-9]{3}\\s[0-9]{3}\\b");      // +91(1234) 567 890 or +91-(1234) 567 890
    }

    @Override
    public void receiveDetections(Detector.Detections<TextBlock> detections) {
        mGraphicOverlay.clear();
        SparseArray<TextBlock> items = detections.getDetectedItems();
        for (int i = 0; i < items.size(); ++i) {
            TextBlock item = items.valueAt(i);
            OcrGraphic graphic = new OcrGraphic(mGraphicOverlay, item);
            mGraphicOverlay.add(graphic);

            String email = isEmail(item.getValue());
            String phone = validatePhoneNumber(item.getValue());
            Log.v("email", "m: " + item.getValue());

            if (!emailDetected && !email.equalsIgnoreCase("")) {
                emailss = emailss.append(email).append("\n");
                emailDetected = true;
            }

            if (!mobileDetected && !phone.equalsIgnoreCase("")) {
                mobiles = mobiles.append(phone).append("\n");
                mobileDetected = true;
            }
        }

        if (mobileDetected && emailDetected) {
            Intent data = new Intent();
            data.putExtra(TextBlockObject, mobiles.toString());
            data.putExtra(TextBlockObject1, emailss.toString());
            data.putExtra(TextBlockObject2,TextBlockName);
            activity.setResult(CommonStatusCodes.SUCCESS, data);
            activity.finish();
        }
    }

    @Override
    public void release() {
        mGraphicOverlay.clear();
    }

    private String isEmail(String text) {
        Matcher m = Pattern.compile("\\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}\\b", Pattern.CASE_INSENSITIVE).matcher(text);
        if (m.find())
            return m.group();
        return "";
    }

    private String validatePhoneNumber(String phoneNo) {
        Matcher m1 = p1.matcher(phoneNo);
        if (m1.find()) return m1.group();

        Matcher m2 = p2.matcher(phoneNo);
        if (m2.find()) return m2.group();

        Matcher m3 = p3.matcher(phoneNo);
        if (m3.find()) return m3.group();

        Matcher m4 = p4.matcher(phoneNo);
        if (m4.find()) return m4.group();

        Matcher m5 = p5.matcher(phoneNo);
        if (m5.find()) return m5.group();

        Matcher m6 = p6.matcher(phoneNo);
        if (m6.find()) return m6.group();

        Matcher m7 = p7.matcher(phoneNo);
        if (m7.find()) return m7.group();

        Matcher m8 = p8.matcher(phoneNo);
        if (m8.find()) return m8.group();

        Matcher m9 = p9.matcher(phoneNo);
        if (m9.find()) return m9.group();

        Matcher m10 = p10.matcher(phoneNo);
        if (m10.find()) return m10.group();

        Matcher m11 = p11.matcher(phoneNo);
        if (m11.find()) return m11.group();

        Matcher m12 = p12.matcher(phoneNo);
        if (m12.find()) return m12.group();

        Matcher m13 = p13.matcher(phoneNo);
        if (m13.find()) return m13.group();

        Matcher m14 = p14.matcher(phoneNo);
        if (m14.find()) return m14.group();

        Matcher m15 = p15.matcher(phoneNo);
        if (m15.find()) return m15.group();

        Matcher m16 = p16.matcher(phoneNo);
        if (m16.find()) return m16.group();

        Matcher m17 = p17.matcher(phoneNo);
        if (m17.find()) return m17.group();

        Matcher m18 = p18.matcher(phoneNo);
        if (m18.find()) return m18.group();

        Matcher m19 = p19.matcher(phoneNo);
        if (m19.find()) return m19.group();

        Matcher m20 = p20.matcher(phoneNo);
        if (m20.find()) return m20.group();

        return "";
    }
}
