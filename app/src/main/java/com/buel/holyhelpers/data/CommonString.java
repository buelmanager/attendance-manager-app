package com.buel.holyhelpers.data;


public class CommonString {

    //부 운영자 구독 ID
    public static final String SUB_ADMIN_SUBSCRIBE_01= "sub_admin_subscribe_01";

    //구글 라이선스
    public static final String GOOGLE_LISENCE_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAh71gGtFGjF94BVu+VlsT981iWMDUsFw0ccCTtc9veErUJmFovbmQaDNV6anTYLTftC4uRUvB3FAmsPrvzPDkrA8ERNqjsubu/SfTqGnk5VpgS83iE7o84nwGjgY8QvT9piJIl4Ne0VpJfDJNar+29kpW/gTrkSqrtkrpH/qM+c2ed5v7iFLgTolzuXiy2wkL/gz0dTEOW87mMPH5eILZ9FK0rhK5O06Pr5tw77BPlU/ItXI/QeLOmsB992bRPjl3+IpU6mXTy1+dYchBFbphOjyd22D2ebrURpdWGIVSrW1W4H0N4yj27CE36pBhySV1Dx5qVIdQc4SXB2vAgm6DGQIDAQAB";
    public static final String FIREBASE_SERVER_KEY = "AAAA_jYzvxw:APA91bFtI6SNXxEj6MTsht3TRqS0c6PWXFqHNNyFae1s10tm2mo3vHAoB0R24hq_XIVfHta_B8Y6DmSJgyZf6q23OxxtZ9gAunyIcnZ2RqWINCgAWDnS1ghHSe90CYLXT_TcFm6dwP96";
    //다음 주소 검색 주소
    public static final String DAUM_ADDRESS_URL = "http://skn.or.kr/daum_address.php";
    //public static final String DAUM_ADDRESS_URL = "https://firebasestorage.googleapis.com/v0/b/viewer-d7253.appspot.com/o/daum_address.php?alt=media&token=0334d2a9-5930-4723-b9ee-9db486fd428b";

    public static final String GROUP_NICK ="부서";
    public static final String TEAM_NICK ="소그룹";
    public static final String CORP_NICK ="교회";
    public static final String MEMBER_NICK ="교인/선교원";

    //STRING    목적_속성_행위
    public static final String INFO_TITLE_DONT_SEARCH_DATA = "검색 결과가 없습니다.";
    public static final String INFO_TITLE_DONT_ATTEND_DATA = "출석 정보가 없습니다.";
    public static final String INFO_LEVEL_HELPER_TITLE = "레벨과 포인트";
    public static final String INFO_HELPER_TITLE = "도움말";
    public static final String INFO_ATTEND_TITLE = "출석 체크";
    public static final String INFO_ANAL_TITLE = "홈/통계";
    public static final String INFO_SELECT_TITLE = "기관선택";

    public static final String INFO_TITLE_CONTROL_TEAM_OR_GROUP =GROUP_NICK+ "/" +TEAM_NICK +" 설정해 주세요.";
    public static final String INFO_TITLE_CONTROL_TEAM = TEAM_NICK + " 설정해 주세요.";
    public static final String INFO_TITLE_CONTROL_GROUP = GROUP_NICK + " 설정해 주세요.";
    public static final String INFO_TITLE_CONTROL_CORP = CORP_NICK + "정보를 먼저 설정해 주세요.";

    public static final String INFO_TITLE_SELECT_TEAM_OR_GROUP =GROUP_NICK+ "/" +TEAM_NICK +" 을/를 선택해 주세요.";
    public static final String INFO_TITLE_SELECT_TEAM = TEAM_NICK + " 을/를 선택해 주세요.";
    public static final String INFO_TITLE_SELECTL_GROUP = GROUP_NICK + " 을/를 선택해 주세요.";
    public static final String INFO_TITLE_SELECT_CORP = CORP_NICK +  " 을/를 선택해 주세요.";

    public static final String INFO_TITLE_ADD_TEAM_OR_GROUP =GROUP_NICK+ "/" +TEAM_NICK +" 을/를 추가해 주세요.";
    public static final String INFO_TITLE_ADD_TEAM = TEAM_NICK + " 을/를 추가해 주세요.";
    public static final String INFO_TITLE_ADD_GROUP = GROUP_NICK + " 을/를 추가해 주세요.";
    public static final String INFO_TITLE_ADD_CORP = CORP_NICK +  " 을/를 추가해해 주세요.";

    public static final String DEFINITION_NAME_DEFAULT = "기타";
    public static final String DEFINITION_NAME_NICK = "이름";
    public static final String DEFINITION_NAME_TEAM_NICK = "명칭/이름";
    public static final String DEFINITION_NAME_LEADER = "리더자";
    public static final String DEFINITION_NAME_U_NUMBER_NICK = "고유번호";

    public static final String INFO_TITLE_DONT_LIST_DATA = "리스트가 없습니다.";

    //STRING    목적_속성_행위
    public static final String GUIDE_FLOATING_BUTTON_ATTEND = "아래의 '출석체크' 버튼을 클릭하여 출석체크를 하실 수 있습니다.";
    public static final String GUIDE_FLOATING_BUTTON_SELECT = "아래의 '설정/관리' 버튼을 클릭하여 선택하실 수 있습니다.";
    public static final String GUIDE_FLOATING_BUTTON_ADD = "아래의 '추가' 버튼을 클릭하여 추가하실 수 있습니다.";
    public static final String GUIDE_HELPER_MEMBER_RECUCLER_VIEW_ATTENDANCE = "<strong>† 출석/결석 체크</strong><br> " +
            "멤버 버튼내의 '좌측 체크버튼' 은 출석,<br> '우측 체크버튼' 은 결석입니다." + "<br><br>" +
            "결석은 결석체크를 클릭해야<br> 서버에 저장이 됩니다." + "<br><br>" +
            "<strong>† 날짜와 예배시간 변경</strong><br> " +
            "하단의 달력버튼을 클릭하면 원하시는 날짜와 예배시간을 선택할수 있습니다." + "<br><br>" +
            "기본은 '날짜'는 '현재 날짜'이고<br> 기본 '예배시간'은 '낮' 시간입니다.<br><br>" +

            "<strong>† 출석체크 저장기능</strong><br> " +
            "한번 체크한 출석체크는 저장되어 화면에 표시됩니다.";
    public static final String GUIDE_HELPER_MEMBER_RECUCLER_VIEW_ADMIN = "<strong>† 수정/삭제</strong><br> " +
            "상단의 설정버튼을 클릭하여 수정 및 삭제가 가능합니다." + "<br><br>" +
            "<strong>† 멤버 추가</strong><br> " +
            "하단의 멤버추가 버튼을 통해서 멤버를 추가할수 있습니다.";

    public static final String GUIDE_HELPER_BRIEFING_PAGE = "<strong>† 설정 관련</strong><br> " +
            "좌측 메뉴 또는 하단 퀵메뉴의 <br>선택/관리 메뉴를 선택하여<br> "+CORP_NICK+"/그룹/팀 설정을 먼저하세요." + "<br><br>" +
            "<strong>† 날짜/예배시간 변경</strong><br> " +
            "하단의 달력 버튼을 클릭하여<br> 원하시는 '달' 과 '요일' 및 '예배시간' 을 <br>선택할수 있습니다. <br><br>" +
            "날짜를 선택 시 해당 '일'의 요일을 <br> 기준으로월간/주간 통계가 보여집니다.";

    public static final String PERSON_DATA_CONTROL_INFO = "개인정보 수집 및 이용 동의\n" +
            "\n" +
            "브엘는 아래의 목적으로 개인정보를 수집 및 이용하며, 회원의 개인정보를 안전하게 취급하는데 최선을 다합니다.\n" +
            "\n" +
            "[필수] 서비스 기본기능의 제공\n" +
            "\n" +
            "목적항목보유기간서비스 내 이용자 식별 및 회원관리휴대폰번호, 구글/페이스북 가입시 사용하는 계정(이하 계정), 닉네임회원 탈퇴 후 지체없이\n" +
            "서비스 이용에 필요한 최소한의 개인정보로 동의 거부 시 서비스 이용이 제한됩니다.\n" +
            "\n" +
            "더 자세한 내용에 대해서는 개인정보 처리방침을 참고하시기 바랍니다. \n\n\n" +

            "--------------------------------------" +
            "개인정보처리방침이란?\n" +
            "\n" +
            "브엘(이하 \"회사\")는 이용자의 동의를 기반으로 개인정보를 수집·이용 및 제공하고 있으며, 이용자의 권리 (개인정보자기결정권)를 적극적으로 보장합니다.\n" +
            "회사는 정보통신서비스제공자가 준수하여야 하는 대한민국의 관계 법령 및 개인정보보호 규정, 가이드라인을 준수하고 있습니다.\n" +
            "“개인정보처리방침”이란 이용자의 소중한 개인정보를 보호하여 안심하고 서비스를 이용할 수 있도록 회사가 서비스를 운영함에 있어 준수해야 할 지침을 말합니다.\n" +
            "\n" +
            "개인정보의 수집\n" +
            "\n" +
            "모든 이용자는 회사가 제공하는 서비스를 이용할 수 있고, 회원가입을 통해 더욱 다양한 서비스를 제공받을 수 있습니다.\n" +
            "이용자의 개인정보를 수집하는 경우에는 반드시 사전에 이용자에게 해당 사실을 알리고 동의를 구하도록 하겠습니다.\n" +
            "\n" +
            "어떻게 개인정보를 수집할까요?회원가입 및 서비스 이용 과정에서 이용자가 개인정보 수집에 대해 동의를 하고 직접 정보를 입력하는 경우제휴 서비스 또는 단체 등으로부터 개인정보를 제공받은 경우고객센터를 통한 상담 과정에서 웹페이지, 메일, 팩스, 전화 등온·오프라인에서 진행되는 이벤트/행사 등 참여1. 서비스 제공에 필요한 최소한의 개인정보를 수집하고 있습니다.필수정보 : 해당 서비스의 본질적 기능을 수행하기 위한 정보(조건 상황에 따라 반드시 필요한 정보 포함)선택정보 : 서비스 본질적 기능 이외에 부가적인 기능을 제공하기 위해 수집하는 정보 (선택 정보를 입력하지 않은 경우에도 서비스 이용 제한은 없습니다.)2. 이용자의 동의 없이 민감정보를 수집하지 않습니다.민감정보란? : 이용자의 사생활을 침해할 우려가 있는 정보 (인종, 사상 및 신조, 정치적 성향이나 범죄기록, 의료정보 등)어떤 개인정보를 수집할까요?\n" +
            "\n" +
            "회원 가입 시 아래와 같은 개인정보가 수집 됩니다.\n" +
            "\n" +
            "1. 전화번호, 계정, 닉네임, 프로필사진, 홀리헬퍼서비스 친구목록, \n2. 계정, 닉네임, 홀리헬퍼서비스 \n" +
            "\n" +
            "서비스의 안정성 확보, 안전한 서비스 제공, 법률 및 서비스 이용약관 위반 행위 제한 등의 목적으로 서비스를 이용하는 과정에서 정보가 자동으로 생성 또는 수집 될 수 있습니다.\n" +
            "\n" +
            "서비스 이용기록, 접속 로그, 거래기록, IP 정보, 쿠키, 불량 및 부정 이용기록, 모바일 기기정보 (모델명, 이동통신사 정보, OS정보, 화면사이즈, 언어 및 국가정보, 광고 ID, 디바이스 식별정보 등)서비스 및 서비스 어플리케이션에 대한 불법/부정 접근 행위 및 관련 기록, 서비스 어플리케이션에 대한 접근 시도 기록, 서비스 및 서비스 어플리케이션의 안전한 동작 환경 확인에 필요한 정보\n" +
            "\n" +
            "또한, 고객 상담을 위해 추가수집되는 개인정보는 아래와 같습니다.\n" +
            "\n" +
            "(필수) 전화번호, 계정\n" +
            "\n" +
            "개인정보의 이용어떠한 목적으로 개인정보를 이용할까요?\n" +
            "\n" +
            "이용자의 개인정보를 다음과 같은 목적으로만 이용하며, 목적이 변경될 경우에는 반드시 사전에 이용자에게 동의를 구하도록 하겠습니다.\n" +
            "\n" +
            " \n" +
            "1. 서비스 로그인기록보존 근거: 통신비밀보호법보존 기간: 3개월2. 소비자의 불만 또는 분쟁처리에 관한 기록보존 근거 : 전자상거래 등에서의 소비자보호에 관한 법률보존 기간 : 3년3. 계약 또는 청약철회 등에 관한 기록/대금결제 및 재화 등의 공급에 관한 기록보존 근거 : 전자상거래 등에서의 소비자보호에 관한 법률보존 기간 : 5년4. 표시/광고에 관한 기록보존 근거 : 전자상거래 등에서의 소비자보호에 관한 법률보존 기간 : 6개월5. 세법이 규정하는 모든 거래에 관한 장부 및 증빙서류보존 근거 : 국세기본법보존 기간 : 5년6. 전자금융 거래에 관한 기록보존 근거 : 전자금융거래법보존 기간 : 5년\n" +
            "\n" +
            "기타사항이용자의 권리를 다음과 같이 보호하고 있습니다.\n" +
            "\n" +
            "이용자 및 법정대리인은 언제든지 수집 정보에 대하여 동의 열람, 정정, 동의 철회 및 삭제, 처리정지 등을 요청할 수 있습니다. 다만, 동의 철회․삭제시 서비스의 일부 또는 전부 이용이 제한될 수 있습니다.\n" +
            "\n" +
            "① 개인정보 열람∙제공 요청\n" +
            "\n" +
            "브엘에서 처리하고 있는 이용자의 개인정보에 대한 열람∙제공을 요구할 수 있습니다.회원정보는' 모바일 앱(App) > 메뉴 > 내정보'에서 확인 가능합니다.서비스를 통한 조회가 되지 않는 개인정보를 열람하고자 하는 경우에는 고객센터 연락 주시면 특별한 사유가 없는 한 10일 이내(근무일 기준)에 조치를 합니다. 다만, 열람이 늦어지거나 불가능한 사유가 발생할 경우 신속하게 알려드리도록 합니다.\n" +
            "\n" +
            "② 개인정보 정정∙삭제 요청\n" +
            "\n" +
            "브엘에서 처리하고 있는 이용자의 개인정보에 대한 정정∙삭제를 요구할 수 있습니다.회원정보는' 모바일 앱(App) > 메뉴 > 내정보' 또는 고객센터를 이용하여 처리 가능합니다. 이를 위하여, 본인확인 등의 절차가 진행될 수 있습니다.\n" +
            "\n" +
            "③ 개인정보 동의 철회 및 삭제, 처리 정지\n" +
            "\n" +
            "고객센터를 이용하여 수집 및 이용된 정보에 대한 동의 철회 및 삭제, 처리 정지를 요청하실 수 있습니다. 다만, 이러한 요청시, 서비스의 일부 또는 전부 이용이 제한될 수 있으며, 다른 법령에 따라 수집하는 정보의 경우에는 동의 철회, 삭제, 처리 정지가 어려울 수 있습니다.이용자가 개인정보의 오류에 대해 정정 요청 시 정정이 완료될 때 까지 해당 정보를 이용 또는 제공하지 않습니다. 오류가 포함된 개인정보를 제 3자에게 이미 제공한 경우에는 정정 내용을 제공받은 자에게 통지하여 조치할 수 있도록 하겠습니다.쿠키(cookie)를 설치, 운영하고 있고 이용자는 이를 거부할 수 있습니다.쿠키란 무엇일까요?\n" +
            "\n" +
            "쿠키란 웹사이트를 운영하는데 이용되는 서버가 이용자의 브라우저에 보내는 아주 작은 텍스트 파일로서 이용자의 컴퓨터에 저장됩니다.\n" +
            "\n" +
            "쿠키를 왜 사용하나요?\n" +
            "\n" +
            "회사는 개인화되고 맞춤화된 서비스를 제공하기 위해서 이용자의 정보를 저장하고 수시로 불러오는 쿠키를 사용합니다. 이용자가 웹사이트에 방문할 경우 웹 사이트 서버는 이용자의 디바이스에 저장되어 있는 쿠키의 내용을 읽어 이용자의 환경설정을 유지하고 맞춤화된 서비스를 제공하게 됩니다. 쿠키는 이용자가 웹 사이트를 방문할 때, 웹 사이트 사용을 설정한대로 접속하고 편리하게 사용할 수 있도록 돕습니다. 또한, 이용자의 웹사이트 방문 기록, 이용 형태를 통해서 최적화된 광고 등 맞춤형 정보를 제공하기 위해 활용됩니다.\n" +
            "\n" +
            "쿠키를 수집하지 못하게 거부하고 싶다면?\n" +
            "\n" +
            "쿠키는 개인을 식별하는 정보를 자동적/능동적으로 수집하지 않으며, 이용자는 쿠키 설치에 대한 선택권을 가지고 있습니다. 따라서, 이용자는 웹 브라우저에서 옵션을 설정함으로써 모든 쿠키를 허용하거나, 쿠키가 저장될 때마다 확인을 거치거나, 모든 쿠키의 저장을 거부할 수도 있습니다.\n" +
            "다만 쿠키 설치를 거부할 경우 웹 사용이 불편해지며 로그인이 필요한 일부 서비스 이용에 어려움이 있을 수 있습니다.\n" +
            "\n" +
            "설정 방법의 예Internet Explorer의 경우 : 웹 브라우저 상단의 도구 메뉴 > 인터넷 옵션 > 개인정보 > 설정Chrome의 경우 : 웹 브라우저 우측의 설정 메뉴 > 화면 하단의 고급 설정 표시 > 개인정보의 콘텐츠 설정 버튼 > 쿠키이용자의 소중한 개인정보 보호를 위해 다음의 노력을 하고 있습니다.\n" +
            "\n" +
            "회사는 이용자의 개인정보를 가장 소중한 가치로 여기고 개인정보를 취급함에 있어서 다음과 같은 노력을 다하고 있습니다.\n" +
            "\n" +
            "이용자의 개인정보를 암호화하고 있습니다.- 회사는 이용자의 개인정보를 암호화된 통신구간을 이용하여 전송하고, 비밀번호 등 중요정보는 암호화하여 보관하고 있습니다.해킹이나 컴퓨터 바이러스로부터 보호하기 위하여 노력하고 있습니다.- 회사는 해킹이나 컴퓨터 바이러스 등에 의해 이용자의 개인정보가 유출되거나 훼손되는 것을 막기 위해 외부로부터 접근이 통제된 구역에 시스템을 설치하고 있습니다.- 해커 등의 침입을 탐지하고 차단할 수 있는 시스템을 설치하여 24시간 감시하고 있으며, 백신 프로그램을 설치하여 시스템이 최신 악성코드나 바이러스에 감염되지 않도록 노력하고 있습니다.- 또한 새로운 해킹/보안 기술에 대해 지속적으로 연구하여 서비스에 적용하고 있습니다.소중한 개인정보에 접근할 수 있는 사람을 최소화하고 있습니다.- 회사는 개인정보를 취급하는 직원을 최소화합니다.- 또한 개인정보를 보관하는 데이터베이스 시스템과 개인정보를 처리하는 시스템에 대한 비밀번호의 생성과 변경, 그리고 접근할 수 있는 권한에 대한 체계적인 기준을 마련하고 지속적인 감사를 실시하고 있습니다.임직원에게 이용자의 개인정보 보호에 대해 정기적인 교육을 실시하고 있습니다.- 개인정보를 취급하는 모든 임직원들을 대상으로 개인정보보호 의무와 보안에 대한 정기적인 교육을 실시하고 있습니다.개인정보보호를 위한 전담조직을 운영하고 있습니다.\n" +
            "\n" +
            "회사가 제공하는 서비스(또는 사업)를 이용하시면서 발생한 모든 개인정보 보호 관련 문의, 불만처리, 피해구제 등에 관한 사항을 개인정보 보호책임자 및 고객지원센터로 문의하실 수 있으며, 브엘는 이런 문의에 대해 지체 없이 답변 및 처리 할 것입니다.\n" +
            "\n" +
            "개인정보 보호책임자 및 연락처개인정보 보호책임자이름 : 원철희, 직위 : CEO, 부서 : 기획팀: 010-6557-7258, 메일 : buelmanager@gmail.com개인정보 민원처리 담당부서부서 : 고객센터전화 : 010-6557-7258, 메일 : buelmanager@gmail.com\n" +
            "\n" +
            "또한 개인정보가 침해되어 이에 대한 신고나 상담이 필요하신 경우에는 아래 기관에 문의하셔서 도움을 받으실 수 있습니다.\n" +
            "\n" +
            "개인정보침해신고센터(국번 없이)118http://privacy.kisa.or.kr대검찰청 사이버수사과(국번없이)1301http://www.spo.go.kr경찰청 사이버안전국(국번 없이)182http://cyberbureau.police.go.kr기타. 부가 방침\n" +
            "\n" +
            "회사는 위 내용에 대한 추가, 삭제 및 수정이 있을 경우 변경사항의 시행 7일 전 공지사항을 통해 이용자에게 설명 드리겠습니다. 그러나 이용자의 소중한 권리 또는 의무에 중요한 내용 변경은 최소 30일전에 말씀 드리도록 하겠습니다. 또한 관련 법령이나 회사 정책 등으로 인하여 불가피하게 처리방침을 변경해야 하는 경우, 공지사항을 통해 빠르게 알려드리고 있으니 참고하여 주시기 바랍니다.\n" +
            "\n" +
            "개인정보 처리 방침은 시행일로부터 적용됩니다.\n" +
            "\n" +
            "개인정보처리방침 공고일자: 2019. 02. 27.\n" +
            "\n" +
            "개인정보처리방침 시행일자: 2019. 03. 06.\n" +
            "\n" +
            "\n" +
            "* 변경 전 개인정보처리방침 보기\n\n\n"+

            "다음의 사항에 대한 앱 푸시 알림을 제공합니다.\n" +
            "\n" +
            "고객을 위한 이벤트 소식 알림할인 쿠폰 지급에 대한 알림\n" +
            "\n" +
            "위 사항 외에도 고객을 위한 다양한 알림을 제공합니다.";

}
