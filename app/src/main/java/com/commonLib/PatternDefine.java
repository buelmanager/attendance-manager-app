package com.commonLib;

/**
 * Created by 19001283 on 2018-07-19.
 */

/**
 * 패턴 정의
 * @author lsh
 *
 */
public class PatternDefine {
    /**
     *  전화번호 검색 패턴 02-472-2012 024722012
     */
    public static final String PATTERN_PHONE = "(\\(?\\d{2,3}\\)?-?\\d{3,4}-?\\d{4})|(\\d{9,11})";

    /**
     * 읍,면,동 구분
     */
    public static final String PATTERN_ADDRESS_UM_MYUN_DONG = "(\uC74D)|(\uBA74)|(\uB3D9)";

    /**
     * 시,군,구 구분
     */
    public static final String PATTERN_ADDRESS_SI_GUN_GU="(\uC2DC)|(\uAD70)|(\uAD6C)";

    /**
     * 통합 주소 검색 읍12,면12,동12,가12
     */
    public static final String PATTERN_ADDRESS_UM_MYUN_DONG_GA_BUNJI = "(\uC0B0\\d{1,5})|(\uBA74\\d{1,5})|(\uB3D9\\d{1,5})|(\uAC00\\d{1,5})|(\uB9AC\\d{1,5})";

    /**
     * 공백 제거 패턴
     */
    public static final String PATTERN_BLANK = "\\p{Space}";

    /**
     * 한글 구분
     */
    public static final String PATTERN_HANGUL = "[\\x{ac00}-\\x{d7af}]";

    /**
     * 기호중에서 - or ~ 둘중에 하나가 있는경우 구분
     */
    public static final String PATTERN_HYPHEN_OR_SWUNG_DASH ="(~|-)";

    /**
     * 시,도 - 시,군,구 구분
     */
    public static final String PATTERN_ADDRESS_SI_DO_SI_GUN_GU = "(" + PATTERN_HANGUL + "+(\uC2DC|\uB3C4)+" + PATTERN_HANGUL + "+(\uC2DC|\uAD70|\uAD6C))";

    /**
     * 도로명 주소 뒤에 붙는 글자
     * ex
     * 1. 부산광역시 중구 중구로 지하31  (신창동4가)  -> 지하
     */
    public static final String PATTERN_ROAD_SUFFIX_TEXT = "(\uC9C0\uD558)";

    /**
     * 도로명 주소 뒤에 붙는 패턴 ex)
     * <br> 앞에 접두사 로|길
     * <br> 1.숫자
     * <br> 2.숫자+번지
     * <br> 3.숫자(~|-)번지
     * <br> 4.숫자(~|-)번지
     * <br> 한글로 시작하는경우
     * <br> 1.한글+숫자
     * <br> 2.한글+숫자+번지
     * <br> 3.한글+숫자(~|-)번지
     * <br> 4.한글+숫자(~|-)번지
     */
    public static final String PATTERN_ROAD_SUFFIX =
            "(" +
                    "(" +
                    "(\\d{1,5})"+
                    "|(\\d{1,5}\uBC88\uC9C0)"+
                    "|(\\d{1,5}(~|-)\\d{1,5})" +
                    "|(\\d{1,5}(~|-)\\d{1,5}\uBC88\uC9C0)"+
                    ")"+
                    "|"+
                    "("+
                    "("+PATTERN_ROAD_SUFFIX_TEXT+"\\d{1,5})"+
                    "|("+PATTERN_ROAD_SUFFIX_TEXT+"\\d{1,5}\uBC88\uC9C0)"+
                    "|("+PATTERN_ROAD_SUFFIX_TEXT+"\\d{1,5}(~|-)\\d{1,5})" +
                    "|("+PATTERN_ROAD_SUFFIX_TEXT+"\\d{1,5}(~|-)\\d{1,5}\uBC88\uC9C0)"+
                    ")"+
                    ")";


    /**
     * 도로명 주소 패턴 ex) (한글|(숫자(~|-)숫자)|숫자)로|길
     */
    public static final String PATTERN_ROAD = "(([\\x{ac00}-\\x{d7af}]|(\\d{1,5}(~|-)\\d{1,5})|\\d{1,5})+(\uB85C|\uAE38))";


    /**
     * 한글 + 읍,면,동,가,리
     */
    public static final String PATTERN_ADDRESS_UM_MYUN_DONG_GA_RI = "([\\x{ac00}-\\x{d7af}]+((\uC74D)|(\uBA74)|(\uB3D9)|(\uAC00)|(\uB9AC)))";

    /**
     * 한글 + 읍,면,동,가
     */
    public static final String PATTERN_ADDRESS_UM_MYUN_DONG_GA = "([\\x{ac00}-\\x{d7af}]+((\uC74D)|(\uBA74)|(\uB3D9)|(\uAC00)))";

    /**
     * 한글 + 리
     */
    public static final String PATTERN_ADDRESS_RI = "([\\x{ac00}-\\x{d7af}]+(\uB9AC))";

    /**
     * 번지 112-11, 11
     */
    public static final String PATTERN_ADDRESS_BUNJI ="(\\d{1,5}(~|-)\\d{1,5}|\\d{1,5})";

    /**
     * 산번지 112-112
     */
    public static final String PATTERN_ADDRESS_SAN_BUNJI ="(\uC0B0(\\d{1,5}(~|-)\\d{1,5}|\\d{1,5}))";

    /**
     * 회사이름 앞,뒤 글자
     */
    public static final String PATTERN_COMPANY_PREFIX_AND_SUFFIX = "(\\(\uC8FC)\\)|(\\(\uC0AC)\\)|(\\(\uC7AC)\\)";

    /**
     * 특수문자
     */
    public static final String PATTERN_SPECIAL_LETTERS = "\\p{Punct}";

    /**
     * 시,도 & 시,군,구 & 도로명 주소 분류 패턴
     * @return
     */
    public static String toSidoSigunguRoad() {
        StringBuffer buffer = new StringBuffer();
        buffer = new StringBuffer();
        buffer.append(PATTERN_ADDRESS_SI_DO_SI_GUN_GU);
        buffer.append(PATTERN_ROAD);
        buffer.append(PATTERN_ROAD_SUFFIX);
        return buffer.toString();
    }

    /**
     * 시,군,구 & 도로명 주소 분류 패턴
     * @return
     */
    public static String toSigunguRoad() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(PATTERN_ADDRESS_SI_GUN_GU);
        buffer.append(PATTERN_ROAD);
        buffer.append(PATTERN_ROAD_SUFFIX);
        return buffer.toString();
    }

    /**
     * 도로명 주소 분류 패턴
     * @return
     */
    public static String toRoad(){
        StringBuffer buffer = new StringBuffer();
        buffer.append(PATTERN_ROAD);
        buffer.append(PATTERN_ROAD_SUFFIX);
        return buffer.toString();
    }

    /**
     * 통합 주소 검색 패턴
     * @return
     */
    public static final String toUnifiedAddress(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("(");
        buffer.append(PATTERN_ADDRESS_UM_MYUN_DONG_GA_RI);
        buffer.append(PATTERN_ADDRESS_BUNJI);
        buffer.append(")");
        buffer.append("|");
        buffer.append("(");
        buffer.append(PATTERN_ADDRESS_UM_MYUN_DONG_GA_RI);
        buffer.append(PATTERN_ADDRESS_SAN_BUNJI);
        buffer.append(")");
        return buffer.toString();
    }
}