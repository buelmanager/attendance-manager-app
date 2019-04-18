package com.buel.holyhelper.utils;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import com.buel.holyhelper.BuildConfig;
import com.buel.holyhelper.data.CommonData;
import com.buel.holyhelper.model.HolyModel;
import com.buel.holyhelper.view.SimpleListener;
import com.commonLib.MaterialDailogUtil;
import com.orhanobut.logger.LoggerHelper;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.core.content.FileProvider;

public class PoiUtil {

    public static List<HolyModel.memberModel> readAndShareMembers(
            Context context, Uri uri) {

        File xlsFile = new File(uri.getPath());
        Uri path = Uri.fromFile(xlsFile);
        List<HolyModel.memberModel> membersList = readXlsExcel(path.getPath());

        //LoggerHelper.d(membersList);
        //SuperToastUtil.toast(context, String.valueOf(membersList));
        return membersList;
    }

    public static void saveAndShareMembers(
            Context context,
            SimpleListener.OnCompleteListener onCompleteListener) {

        Map<String, HolyModel.memberModel> membersMap = CommonData.getMembersMap(); //CommonData.getHolyModel().memberModel;
        List<HolyModel.memberModel> membersList = new ArrayList<>();

        if(membersMap == null){
            Toast.makeText(context, "저장된 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(membersMap.size() <0 ){
            Toast.makeText(context, "저장된 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        for (Map.Entry<String, HolyModel.memberModel> eleDate : membersMap.entrySet()) {
            membersList.add(eleDate.getValue());
        }

        File xlsFile= PoiUtil.saveXlsExcel(context, membersList);
        MaterialDailogUtil.simpleYesNoDialog(context, "공유하시겠습니까?", s -> {
            if (s.equals("ok")) {

                onCompleteListener.onComplete();

                if (Build.VERSION.SDK_INT >= 24) { // Android Nougat ( 7.0 ) and later
                    Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider",xlsFile);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType( "application/excel");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.putExtra(Intent.EXTRA_STREAM,uri);
                    context.startActivity(Intent.createChooser(intent,"엑셀 내보내기"));
                } else {
                    Uri uri = Uri.fromFile(xlsFile);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("application/excel");
                    intent.putExtra(Intent.EXTRA_STREAM,uri);
                    context.startActivity(Intent.createChooser(intent,"엑셀 내보내기"));
                }

                // 미디어 스캐닝 (폴더내에 생성된 파일 보이도록 하는 코드)
                MediaScannerConnection.scanFile(context.getApplicationContext(), new String[]{xlsFile.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String s, Uri uri) {
                    }
                });
            }
        });
    }

    public static File saveXlsExcel(Context context, List<HolyModel.memberModel> memberExcelModels) {
        Workbook workbook = new HSSFWorkbook();

        Sheet sheet = workbook.createSheet(); // 새로운 시트 생성

        Row row = sheet.createRow(0); // 새로운 행 생성
        Cell cell;

        cell = row.createCell(0);
        cell.setCellValue("이름");

        cell = row.createCell(1);
        cell.setCellValue("생일");

        cell = row.createCell(2);
        cell.setCellValue("주소");

        cell = row.createCell(3);
        cell.setCellValue("전화");

        cell = row.createCell(4);
        cell.setCellValue("성별");

        cell = row.createCell(5);
        cell.setCellValue("직책");

        cell = row.createCell(6);
        cell.setCellValue("새신자");

        cell = row.createCell(7);
        cell.setCellValue("지역");

        cell = row.createCell(8);
        cell.setCellValue("인도자");

        cell = row.createCell(9);
        cell.setCellValue("부서명");

        cell = row.createCell(10);
        cell.setCellValue("소그룹");

        for (int i = 0; i < memberExcelModels.size(); i++) { // 데이터 엑셀에 입력
            row = sheet.createRow(i + 1);
            cell = row.createCell(0);
            cell.setCellValue(memberExcelModels.get(i).name);

            cell = row.createCell(1);
            cell.setCellValue(memberExcelModels.get(i).birth);

            cell = row.createCell(2);
            cell.setCellValue(memberExcelModels.get(i).address);

            cell = row.createCell(3);
            cell.setCellValue(memberExcelModels.get(i).phone);

            cell = row.createCell(4);
            cell.setCellValue(memberExcelModels.get(i).gender);

            cell = row.createCell(5);
            cell.setCellValue(memberExcelModels.get(i).position);

            cell = row.createCell(6);
            cell.setCellValue(memberExcelModels.get(i).isNew);

            cell = row.createCell(7);
            cell.setCellValue(memberExcelModels.get(i).town);

            cell = row.createCell(8);
            cell.setCellValue(memberExcelModels.get(i).leader);

            cell = row.createCell(9);
            cell.setCellValue(memberExcelModels.get(i).groupName);

            cell = row.createCell(10);
            cell.setCellValue(memberExcelModels.get(i).teamName);

        }

        File xlsFile = new File(context.getExternalFilesDir(null), CommonData.getHolyModel().name + "회원명부.xls");
        try {
            FileOutputStream os = new FileOutputStream(xlsFile);
            workbook.write(os); // 외부 저장소에 엑셀 파일 생성
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        LoggerHelper.d(xlsFile.getAbsolutePath() + "에 저장되었습니다");
        return xlsFile;
    }

    /**
     * XLS 파일을 분석하여 List<CustomerVo> 객체로 반환
     *
     * @param filePath
     * @return
     */
    @SuppressWarnings("resource")
    public static List<HolyModel.memberModel> readXlsExcel(String filePath) {

        // 반환할 객체를 생성
        List<HolyModel.memberModel> list = new ArrayList<>();

        FileInputStream fis = null;
        HSSFWorkbook workbook = null;

        try {

            filePath = filePath.replace("/document/primary:", Environment.getExternalStorageDirectory().toString()+"/");
            LoggerHelper.d("filePath : " + filePath);

            fis = new FileInputStream(filePath);
            // HSSFWorkbook은 엑셀파일 전체 내용을 담고 있는 객체
            workbook = new HSSFWorkbook(fis);

            // 탐색에 사용할 Sheet, Row, Cell 객체
            HSSFSheet curSheet;
            HSSFRow curRow;
            HSSFCell curCell;

            // Sheet 탐색 for문
            for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
                // 현재 Sheet 반환
                curSheet = workbook.getSheetAt(sheetIndex);
                // row 탐색 for문
                for (int rowIndex = 0; rowIndex < curSheet.getPhysicalNumberOfRows(); rowIndex++) {
                    // row 0은 헤더정보이기 때문에 무시
                    if (rowIndex != 0) {
                        // 현재 row 반환
                        curRow = curSheet.getRow(rowIndex);
                        String value;

                        // row의 첫번째 cell값이 비어있지 않은 경우 만 cell탐색
                        if (!"".equals(curRow.getCell(0).getStringCellValue())) {

                            HolyModel.memberModel mModel = new HolyModel.memberModel();

                            //LoggerHelper.d("curRow.getPhysicalNumberOfCells() : " + curRow.getPhysicalNumberOfCells());
                            // cell 탐색 for 문
                            for (int cellIndex = 0; cellIndex < curRow.getPhysicalNumberOfCells(); cellIndex++) {
                                curCell = curRow.getCell(cellIndex);

                                if (true) {
                                    //LoggerHelper.d(" curCell.getCellType() : " +  curCell.getCellType()) ;
                                    if(curCell.getCellType() == CellType.NUMERIC){
                                        value = (String.valueOf((int)curCell.getNumericCellValue()));
                                    }else{
                                        value = curCell.getStringCellValue();
                                    }

                                    if(value ==null){
                                        value = "";
                                    }

                                    //LoggerHelper.d("cellIndex : " + cellIndex + " //    value    :  " + value);
                                    // 현재 column index에 따라서 vo에 입력
                                    switch (cellIndex) {

                                        case 0: // 이름
                                            mModel.name = value;
                                            break;

                                        case 1: // 나이
                                            mModel.birth = value;
                                            break;

                                        case 2: // 주소
                                            mModel.address = value;
                                            break;

                                        case 3: // 전화번호
                                            mModel.phone = value;
                                            break;

                                        case 4: // 성별
                                            mModel.gender = value;
                                            break;

                                        case 5: // 직급
                                            mModel.position = value;
                                            break;

                                        case 6: // 새신자
                                            mModel.isNew = value;
                                            break;

                                        case 7: // 지역
                                            mModel.town = value;
                                            break;

                                        case 8: // 인도자
                                            mModel.leader = value;
                                            break;

                                        case 9: // 그룹
                                            mModel.groupName = value;
                                            break;
                                        case 10: // 팀
                                            mModel.teamName = value;
                                            break;

                                        case 11: // 이미지
                                            mModel.userPhotoUri = value;
                                            break;

                                        default:
                                            break;
                                    }
                                }
                            }
                            // cell 탐색 이후 vo 추가
                            list.add(mModel);
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            try {
                // 사용한 자원은 finally에서 해제
                if (workbook != null) workbook.close();
                if (fis != null) fis.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}