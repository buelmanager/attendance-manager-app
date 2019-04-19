package com.commonLib;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by blue7 on 2018-05-09.
 */

public class Common {
    public static String PACKAGE_NAME;
    public static int[] VORDIPLOM_COLORS = {
            Color.rgb(140, 234, 255), Color.rgb(255, 140, 157),
            Color.rgb(192, 255, 140), Color.rgb(255, 247, 140), Color.rgb(255, 208, 140)
    };

    public static int[] VORDIPLOM_RED = {
            Color.rgb(255, 140, 157)
    };

    public static int[] VORDIPLOM_BLUE = {
            Color.rgb(140, 234, 255)
    };

    public static String trim(String str) {
        return str.replaceAll("\\p{Z}","");
    }


    /**
     * removeAllFragments
     *
     * @param fragmentManager
     */
    public static void removeAllFragments(FragmentManager fragmentManager) {
        //Here we are clearing back stack fragment entries
        int backStackEntry = fragmentManager.getBackStackEntryCount();
        if (backStackEntry > 0) {
            for (int i = 0; i < backStackEntry; i++) {
                fragmentManager.popBackStackImmediate();
            }
        }

        //Here we are removing all the fragment that are shown here
        if (fragmentManager.getFragments() != null && fragmentManager.getFragments().size() > 0) {
            for (int i = 0; i < fragmentManager.getFragments().size(); i++) {
                Fragment mFragment = fragmentManager.getFragments().get(i);
                if (mFragment != null) {
                    fragmentManager.beginTransaction().remove(mFragment).commit();
                    // this will clear the back stack and displays no animation on the screen
                    fragmentManager.popBackStackImmediate(mFragment.getTag(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            }
        }

    }

    /**
     * map을 리스트 형태로 정렬한다.
     * @param map
     * @return
     */
    public static List sortByValue(final Map map) {

        List<String> list = new ArrayList();
        list.addAll(map.keySet());

        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                Object v1 = map.get(o1);
                Object v2 = map.get(o2);
                return ((Comparable) v2).compareTo(v1);
            }
        });

        //Collections.reverse(list); // 주석시 오름차순
        return list;
    }

    /**
     * 이메일 포맷 체크
     *
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        boolean isNormal = m.matches();
        return isNormal;
    }

    public static String addZero(Integer integer) {
        String tempNum;
        if (integer >= 10)
            tempNum = String.valueOf(integer);
        else
            tempNum = "0" + String.valueOf(integer);
        return tempNum;
    }

    public static String currentTimestamp() {
        /*Date and Time Pattern	          Result
        "yyyy.MM.dd G 'at' HH:mm:ss z"	  2001.07.04 AD at 12:08:56 PDT
        "EEE, MMM d, ''yy"	              Wed, Jul 4, '01
        "h:mm a"	                      12:08 PM
        "hh 'o''clock' a, zzzz"	          12 o'clock PM, Pacific Daylight Time
        "K:mm a, z"	                      0:08 PM, PDT
        "yyyyy.MMMMM.dd GGG hh:mm aaa"	  02001.July.04 AD 12:08 PM
        "EEE, d MMM yyyy HH:mm:ss Z"	  Wed, 4 Jul 2001 12:08:56 -0700
        "yyMMddHHmmssZ"	                  010704120856-0700
        "yyyy-MM-dd'T'HH:mm:ss.SSSZ"	  2001-07-04T12:08:56.235-0700*/

        java.util.Date today = Calendar.getInstance().getTime();
        //SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddTHHmmss");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String current = formatter.format(today);

        return current;
    }

    /**
     * 현재의 view를 리턴
     *
     * @param context
     * @return
     */
    public static View getRootView(Context context) {
        return ((Activity) context).getWindow().getDecorView();
    }

    ;

    /**
     * ViewPager의 현재 fragment를 리턴
     *
     * @param pager
     * @param adapter
     * @return
     */
    public static Fragment getCurrentFragment(ViewPager pager,
                                              FragmentPagerAdapter adapter) {
        try {
            Method m = adapter
                    .getClass()
                    .getSuperclass()
                    .getDeclaredMethod("makeFragmentName", int.class,
                            long.class);
            Field f = adapter.getClass().getSuperclass()
                    .getDeclaredField("mFragmentManager");
            f.setAccessible(true);
            FragmentManager fm = (FragmentManager) f.get(adapter);
            m.setAccessible(true);
            String tag = null;
            tag = (String) m.invoke(null, pager.getId(),
                    (long) pager.getCurrentItem());
            return fm.findFragmentByTag(tag);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap getBitmapFromUri(Context context, Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                context.getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);

        int width = options.outWidth;
        int height = options.outHeight;

        float sampleRatio = getSampleRatio(width, height);

        options.inJustDecodeBounds = false;
        options.inSampleSize = (int) sampleRatio;

        Bitmap resizedBitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
        parcelFileDescriptor.close();

        return resizedBitmap;
    }

    private static float getSampleRatio(int width, int height) {
        final int targetWidth = 1280;
        final int targetHeight = 1280;

        float ratio;

        if (width > height) {
            //landscape
            if (width > targetWidth) {
                ratio = (float) width / (float) targetWidth;
            } else {
                ratio = 1f;
            }
        } else {
            //portarit
            if (height > targetHeight) {
                ratio = (float) height / (float) targetHeight;
            } else {
                ratio = 1f;
            }
        }
        return Math.round(ratio);
    }


    public static String getMacAdress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(Integer.toHexString(b & 0xFF) + ":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            //handle exception
        }
        return "";
    }


    public static void sendDirectCall(String pNum, Activity context) {
        // 사용자의 OS 버전이 마시멜로우 이상인지 체크한다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            /**
             * 사용자 단말기의 권한 중 "전화걸기" 권한이 허용되어 있는지 확인한다.
             * Android는 C언어 기반으로 만들어졌기 때문에 Boolean 타입보다 Int 타입을 사용한다.
             */
            int permissionResult = context.checkSelfPermission(Manifest.permission.CALL_PHONE);
            /**
             * 패키지는 안드로이드 어플리케이션의 아이디이다.
             * 현재 어플리케이션이 CALL_PHONE에 대해 거부되어있는지 확인한다.
             */
            if (permissionResult == PackageManager.PERMISSION_DENIED) {
                if ( context.shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog.setTitle("권한이 필요합니다.")
                            .setMessage("이 기능을 사용하기 위해서는 단말기의 \"전화걸기\" 권한이 필요합니다. 계속 하시겠습니까?")
                            .setPositiveButton("네", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        // CALL_PHONE 권한을 Android OS에 요청한다.
                                        context.requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1000);
                                    }
                                }
                            })
                            .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(context, "기능을 취소했습니다", Toast.LENGTH_SHORT).show();
                                }
                            }).create().show();
                }

                // 최초로 권한을 요청할 때
                else {
                    // CALL_PHONE 권한을 Android OS에 요청한다.
                    context.requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1000);
                }
            }
            // CALL_PHONE의 권한이 있을 때
            else {
                // 즉시 실행
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + pNum));
                context.startActivity(intent);
            }
        }
        // 마시멜로우 미만의 버전일 때
        else {
            // 즉시 실행
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + pNum));
            context.startActivity(intent);
        }
    }

    public static void hideKeyboard(Activity activity) {
        View view = Common.getRootView(activity);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
