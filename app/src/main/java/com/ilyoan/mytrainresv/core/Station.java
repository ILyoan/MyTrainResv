package com.ilyoan.mytrainresv.core;

import java.util.ArrayList;

public class Station {
	private static Station instance = null;

	private final ArrayList<StationInfo> stations = new ArrayList<StationInfo>();

	private Station() {
		this.stations.add(new StationInfo("0001", "서울"));
		this.stations.add(new StationInfo("0104", "용산"));
		this.stations.add(new StationInfo("0002", "영등포"));
		this.stations.add(new StationInfo("0501", "광명"));
		this.stations.add(new StationInfo("0003", "수원"));
		this.stations.add(new StationInfo("0502", "천안아산"));
		this.stations.add(new StationInfo("0297", "오송"));
		this.stations.add(new StationInfo("0010", "대전"));
		this.stations.add(new StationInfo("0507", "김천구미"));
		this.stations.add(new StationInfo("0015", "동대구"));
		this.stations.add(new StationInfo("0508", "신경주"));
		this.stations.add(new StationInfo("0509", "울산"));
		this.stations.add(new StationInfo("0020", "부산"));
		this.stations.add(new StationInfo("0030", "익산"));
		this.stations.add(new StationInfo("0042", "광주"));
		this.stations.add(new StationInfo("0036", "광주송정"));
		this.stations.add(new StationInfo("0041", "목포"));
		this.stations.add(new StationInfo("0053", "여수EXPO"));

		this.stations.add(new StationInfo("0000", "--- KTX ---"));

		this.stations.add(new StationInfo("0342", "가수원"));
		this.stations.add(new StationInfo("0476", "가야"));
		this.stations.add(new StationInfo("0150", "가평"));
		this.stations.add(new StationInfo("0309", "각계"));
		this.stations.add(new StationInfo("0172", "간현"));
		this.stations.add(new StationInfo("0481", "갈촌"));
		this.stations.add(new StationInfo("0028", "강경"));
		this.stations.add(new StationInfo("0115", "강릉"));
		this.stations.add(new StationInfo("0151", "강촌"));
		this.stations.add(new StationInfo("0482", "개양"));
		this.stations.add(new StationInfo("0219", "개태사"));
		this.stations.add(new StationInfo("0160", "개포"));
		this.stations.add(new StationInfo("0216", "거제"));
		this.stations.add(new StationInfo("0433", "거촌"));
		this.stations.add(new StationInfo("0184", "건천"));
		this.stations.add(new StationInfo("0208", "경강"));
		this.stations.add(new StationInfo("0024", "경산"));
		this.stations.add(new StationInfo("0021", "경주"));
		this.stations.add(new StationInfo("0468", "경화"));
		this.stations.add(new StationInfo("0218", "계룡"));
		this.stations.add(new StationInfo("0240", "고막원"));
		this.stations.add(new StationInfo("0122", "고한"));
		this.stations.add(new StationInfo("0049", "곡성"));
		this.stations.add(new StationInfo("0259", "공전"));
		this.stations.add(new StationInfo("0370", "관촌"));
		this.stations.add(new StationInfo("0491", "광곡"));
		this.stations.add(new StationInfo("0501", "광명"));
		this.stations.add(new StationInfo("0068", "광양"));
		this.stations.add(new StationInfo("0145", "광운대"));
		this.stations.add(new StationInfo("0042", "광주"));
		this.stations.add(new StationInfo("0036", "광주송정"));
		this.stations.add(new StationInfo("0082", "광천"));
		this.stations.add(new StationInfo("0241", "괴목"));
		this.stations.add(new StationInfo("0050", "구례구"));
		this.stations.add(new StationInfo("0013", "구미"));
		this.stations.add(new StationInfo("0019", "구포"));
		this.stations.add(new StationInfo("0329", "구학"));
		this.stations.add(new StationInfo("0323", "국수"));
		this.stations.add(new StationInfo("0061", "군북"));
		this.stations.add(new StationInfo("0505", "군산"));
		this.stations.add(new StationInfo("0043", "극락강"));
		this.stations.add(new StationInfo("0736", "금강"));
		this.stations.add(new StationInfo("0239", "금곡"));
		this.stations.add(new StationInfo("0732", "금릉"));
		this.stations.add(new StationInfo("0187", "기장"));
		this.stations.add(new StationInfo("0246", "김유정"));
		this.stations.add(new StationInfo("0031", "김제"));
		this.stations.add(new StationInfo("0012", "김천"));
		this.stations.add(new StationInfo("0507", "김천구미"));
		this.stations.add(new StationInfo("0461", "나원"));
		this.stations.add(new StationInfo("0201", "나전"));
		this.stations.add(new StationInfo("0037", "나주"));
		this.stations.add(new StationInfo("0164", "나한정"));
		this.stations.add(new StationInfo("0452", "남문구"));
		this.stations.add(new StationInfo("0131", "남문산"));
		this.stations.add(new StationInfo("0317", "남성현"));
		this.stations.add(new StationInfo("0048", "남원"));
		this.stations.add(new StationInfo("0186", "남창"));
		this.stations.add(new StationInfo("0152", "남춘천"));
		this.stations.add(new StationInfo("0497", "남평"));
		this.stations.add(new StationInfo("0361", "노안"));
		this.stations.add(new StationInfo("0027", "논산"));
		this.stations.add(new StationInfo("0391", "능곡"));
		this.stations.add(new StationInfo("0132", "능주"));
		this.stations.add(new StationInfo("0266", "다시"));
		this.stations.add(new StationInfo("0176", "단성"));
		this.stations.add(new StationInfo("0096", "단양"));
		this.stations.add(new StationInfo("0247", "달천"));
		this.stations.add(new StationInfo("0417", "대광리"));
		this.stations.add(new StationInfo("0023", "대구"));
		this.stations.add(new StationInfo("0148", "대성리"));
		this.stations.add(new StationInfo("0310", "대신"));
		this.stations.add(new StationInfo("0430", "대야"));
		this.stations.add(new StationInfo("0010", "대전"));
		this.stations.add(new StationInfo("0083", "대천"));
		this.stations.add(new StationInfo("0233", "덕산"));
		this.stations.add(new StationInfo("0168", "덕소"));
		this.stations.add(new StationInfo("0052", "덕양"));
		this.stations.add(new StationInfo("0209", "덕하"));
		this.stations.add(new StationInfo("0111", "도계"));
		this.stations.add(new StationInfo("0077", "도고온천"));
		this.stations.add(new StationInfo("0095", "도담"));
		this.stations.add(new StationInfo("0403", "도라산"));
		this.stations.add(new StationInfo("0015", "동대구"));
		this.stations.add(new StationInfo("0410", "동두천"));
		this.stations.add(new StationInfo("0189", "동래"));
		this.stations.add(new StationInfo("0450", "동백산"));
		this.stations.add(new StationInfo("0366", "동산"));
		this.stations.add(new StationInfo("0364", "동익산"));
		this.stations.add(new StationInfo("0437", "동점"));
		this.stations.add(new StationInfo("0113", "동해"));
		this.stations.add(new StationInfo("0173", "동화"));
		this.stations.add(new StationInfo("0615", "두정"));
		this.stations.add(new StationInfo("0205", "득량"));
		this.stations.add(new StationInfo("0059", "마산"));
		this.stations.add(new StationInfo("0147", "마석"));
		this.stations.add(new StationInfo("0038", "망상"));
		this.stations.add(new StationInfo("0249", "매곡"));
		this.stations.add(new StationInfo("0235", "명봉"));
		this.stations.add(new StationInfo("0041", "목포"));
		this.stations.add(new StationInfo("0074", "목행"));
		this.stations.add(new StationInfo("0229", "몽탄"));
		this.stations.add(new StationInfo("0236", "무안"));
		this.stations.add(new StationInfo("0114", "묵호"));
		this.stations.add(new StationInfo("0401", "문산"));
		this.stations.add(new StationInfo("0224", "물금"));
		this.stations.add(new StationInfo("0244", "미평"));
		this.stations.add(new StationInfo("0120", "민둥산"));
		this.stations.add(new StationInfo("0017", "밀양"));
		this.stations.add(new StationInfo("0062", "반성"));
		this.stations.add(new StationInfo("0738", "백마고지"));
		this.stations.add(new StationInfo("0167", "백산"));
		this.stations.add(new StationInfo("0258", "백양리"));
		this.stations.add(new StationInfo("0034", "백양사"));
		this.stations.add(new StationInfo("0089", "벌교"));
		this.stations.add(new StationInfo("0451", "범일"));
		this.stations.add(new StationInfo("0198", "별어곡"));
		this.stations.add(new StationInfo("0069", "보성"));
		this.stations.add(new StationInfo("0434", "봉성"));
		this.stations.add(new StationInfo("0175", "봉양"));
		this.stations.add(new StationInfo("0105", "봉화"));
		this.stations.add(new StationInfo("0008", "부강"));
		this.stations.add(new StationInfo("0020", "부산"));
		this.stations.add(new StationInfo("0190", "부전"));
		this.stations.add(new StationInfo("0464", "부조"));
		this.stations.add(new StationInfo("0807", "부천"));
		this.stations.add(new StationInfo("0222", "북영천"));
		this.stations.add(new StationInfo("0064", "북천"));
		this.stations.add(new StationInfo("0166", "분천"));
		this.stations.add(new StationInfo("0185", "불국사"));
		this.stations.add(new StationInfo("0312", "사곡"));
		this.stations.add(new StationInfo("0255", "사릉"));
		this.stations.add(new StationInfo("0193", "사방"));
		this.stations.add(new StationInfo("0121", "사북"));
		this.stations.add(new StationInfo("0143", "사상"));
		this.stations.add(new StationInfo("0018", "삼랑진"));
		this.stations.add(new StationInfo("0044", "삼례"));
		this.stations.add(new StationInfo("0250", "삼산"));
		this.stations.add(new StationInfo("0213", "삼탄"));
		this.stations.add(new StationInfo("0080", "삽교"));
		this.stations.add(new StationInfo("0272", "상동"));
		this.stations.add(new StationInfo("0635", "상봉"));
		this.stations.add(new StationInfo("0156", "상주"));
		this.stations.add(new StationInfo("0257", "상천"));
		this.stations.add(new StationInfo("0341", "서경주"));
		this.stations.add(new StationInfo("0275", "서광주"));
		this.stations.add(new StationInfo("0025", "서대전"));
		this.stations.add(new StationInfo("0833", "서빙고"));
		this.stations.add(new StationInfo("0001", "서울"));
		this.stations.add(new StationInfo("0243", "서정리"));
		this.stations.add(new StationInfo("0086", "서천"));
		this.stations.add(new StationInfo("0325", "석불"));
		this.stations.add(new StationInfo("0108", "석포"));
		this.stations.add(new StationInfo("0199", "선평"));
		this.stations.add(new StationInfo("0248", "성환"));
		this.stations.add(new StationInfo("0411", "소요산"));
		this.stations.add(new StationInfo("0142", "소정리"));
		this.stations.add(new StationInfo("0188", "송정"));
		this.stations.add(new StationInfo("0455", "수영"));
		this.stations.add(new StationInfo("0003", "수원"));
		this.stations.add(new StationInfo("0051", "순천"));
		this.stations.add(new StationInfo("0161", "승부"));
		this.stations.add(new StationInfo("0508", "신경주"));
		this.stations.add(new StationInfo("0263", "신기"));
		this.stations.add(new StationInfo("0182", "신녕"));
		this.stations.add(new StationInfo("0223", "신동"));
		this.stations.add(new StationInfo("0078", "신례원"));
		this.stations.add(new StationInfo("0369", "신리"));
		this.stations.add(new StationInfo("0174", "신림"));
		this.stations.add(new StationInfo("0416", "신망리"));
		this.stations.add(new StationInfo("0281", "신창"));
		this.stations.add(new StationInfo("0465", "신창원"));
		this.stations.add(new StationInfo("0265", "신탄리"));
		this.stations.add(new StationInfo("0009", "신탄진"));
		this.stations.add(new StationInfo("0032", "신태인"));
		this.stations.add(new StationInfo("0245", "심천"));
		this.stations.add(new StationInfo("0116", "쌍용"));
		this.stations.add(new StationInfo("0503", "아산"));
		this.stations.add(new StationInfo("0324", "아신"));
		this.stations.add(new StationInfo("0202", "아우라지"));
		this.stations.add(new StationInfo("0311", "아포"));
		this.stations.add(new StationInfo("0192", "안강"));
		this.stations.add(new StationInfo("0100", "안동"));
		this.stations.add(new StationInfo("0135", "안양"));
		this.stations.add(new StationInfo("0230", "약목"));
		this.stations.add(new StationInfo("0171", "양동"));
		this.stations.add(new StationInfo("0486", "양보"));
		this.stations.add(new StationInfo("0269", "양수"));
		this.stations.add(new StationInfo("0731", "양원"));
		this.stations.add(new StationInfo("0463", "양자동"));
		this.stations.add(new StationInfo("0091", "양평"));
		this.stations.add(new StationInfo("0053", "여수EXPO"));
		this.stations.add(new StationInfo("0139", "여천"));
		this.stations.add(new StationInfo("0195", "연당"));
		this.stations.add(new StationInfo("0220", "연무대"));
		this.stations.add(new StationInfo("0026", "연산"));
		this.stations.add(new StationInfo("0415", "연천"));
		this.stations.add(new StationInfo("0011", "영동"));
		this.stations.add(new StationInfo("0002", "영등포"));
		this.stations.add(new StationInfo("0117", "영월"));
		this.stations.add(new StationInfo("0098", "영주"));
		this.stations.add(new StationInfo("0103", "영천"));
		this.stations.add(new StationInfo("0075", "예당"));
		this.stations.add(new StationInfo("0119", "예미"));
		this.stations.add(new StationInfo("0079", "예산"));
		this.stations.add(new StationInfo("0162", "예천"));
		this.stations.add(new StationInfo("0134", "오근장"));
		this.stations.add(new StationInfo("0141", "오산"));
		this.stations.add(new StationInfo("0297", "오송"));
		this.stations.add(new StationInfo("0047", "오수"));
		this.stations.add(new StationInfo("0067", "옥곡"));
		this.stations.add(new StationInfo("0154", "옥산"));
		this.stations.add(new StationInfo("0892", "옥수"));
		this.stations.add(new StationInfo("0022", "옥천"));
		this.stations.add(new StationInfo("0076", "온양온천"));
		this.stations.add(new StationInfo("0484", "완사"));
		this.stations.add(new StationInfo("0836", "왕십리"));
		this.stations.add(new StationInfo("0014", "왜관"));
		this.stations.add(new StationInfo("0618", "외고산"));
		this.stations.add(new StationInfo("0159", "용궁"));
		this.stations.add(new StationInfo("0347", "용동"));
		this.stations.add(new StationInfo("0169", "용문"));
		this.stations.add(new StationInfo("0104", "용산"));
		this.stations.add(new StationInfo("0733", "운천"));
		this.stations.add(new StationInfo("0509", "울산"));
		this.stations.add(new StationInfo("0084", "웅천"));
		this.stations.add(new StationInfo("0215", "원동"));
		this.stations.add(new StationInfo("0479", "원북"));
		this.stations.add(new StationInfo("0092", "원주"));
		this.stations.add(new StationInfo("0217", "월내"));
		this.stations.add(new StationInfo("0383", "율촌"));
		this.stations.add(new StationInfo("0072", "음성"));
		this.stations.add(new StationInfo("0101", "의성"));
		this.stations.add(new StationInfo("0264", "의정부"));
		this.stations.add(new StationInfo("0055", "이양"));
		this.stations.add(new StationInfo("0300", "이원"));
		this.stations.add(new StationInfo("0030", "익산"));
		this.stations.add(new StationInfo("0921", "인천공항"));
		this.stations.add(new StationInfo("0227", "일광"));
		this.stations.add(new StationInfo("0040", "일로"));
		this.stations.add(new StationInfo("0395", "일산"));
		this.stations.add(new StationInfo("0204", "일신"));
		this.stations.add(new StationInfo("0165", "임기"));
		this.stations.add(new StationInfo("0362", "임성리"));
		this.stations.add(new StationInfo("0046", "임실"));
		this.stations.add(new StationInfo("0402", "임진강"));
		this.stations.add(new StationInfo("0212", "입실"));
		this.stations.add(new StationInfo("0197", "자미원"));
		this.stations.add(new StationInfo("0446", "장락"));
		this.stations.add(new StationInfo("0035", "장성"));
		this.stations.add(new StationInfo("0504", "장항"));
		this.stations.add(new StationInfo("0454", "재송"));
		this.stations.add(new StationInfo("0414", "전곡"));
		this.stations.add(new StationInfo("0006", "전의"));
		this.stations.add(new StationInfo("0045", "전주"));
		this.stations.add(new StationInfo("0158", "점촌"));
		this.stations.add(new StationInfo("0262", "정동진"));
		this.stations.add(new StationInfo("0200", "정선"));
		this.stations.add(new StationInfo("0033", "정읍"));
		this.stations.add(new StationInfo("0093", "제천"));
		this.stations.add(new StationInfo("0267", "제천순환"));
		this.stations.add(new StationInfo("0088", "조성"));
		this.stations.add(new StationInfo("0007", "조치원"));
		this.stations.add(new StationInfo("0126", "좌천"));
		this.stations.add(new StationInfo("0138", "주덕"));
		this.stations.add(new StationInfo("0815", "주안"));
		this.stations.add(new StationInfo("0234", "중리"));
		this.stations.add(new StationInfo("0071", "증평"));
		this.stations.add(new StationInfo("0308", "지탄"));
		this.stations.add(new StationInfo("0170", "지평"));
		this.stations.add(new StationInfo("0511", "진례"));
		this.stations.add(new StationInfo("0066", "진상"));
		this.stations.add(new StationInfo("0480", "진성"));
		this.stations.add(new StationInfo("0056", "진영"));
		this.stations.add(new StationInfo("0063", "진주"));
		this.stations.add(new StationInfo("0278", "진주수목원"));
		this.stations.add(new StationInfo("0140", "진해"));
		this.stations.add(new StationInfo("0057", "창원"));
		this.stations.add(new StationInfo("0512", "창원중앙"));
		this.stations.add(new StationInfo("0751", "천마산"));
		this.stations.add(new StationInfo("0005", "천안"));
		this.stations.add(new StationInfo("0502", "천안아산"));
		this.stations.add(new StationInfo("0109", "철암"));
		this.stations.add(new StationInfo("0016", "청도"));
		this.stations.add(new StationInfo("0090", "청량리"));
		this.stations.add(new StationInfo("0155", "청리"));
		this.stations.add(new StationInfo("0253", "청소"));
		this.stations.add(new StationInfo("0070", "청주"));
		this.stations.add(new StationInfo("0276", "청주공항"));
		this.stations.add(new StationInfo("0149", "청평"));
		this.stations.add(new StationInfo("0412", "초성리"));
		this.stations.add(new StationInfo("0449", "추전"));
		this.stations.add(new StationInfo("0133", "추풍령"));
		this.stations.add(new StationInfo("0106", "춘양"));
		this.stations.add(new StationInfo("0153", "춘천"));
		this.stations.add(new StationInfo("0073", "충주"));
		this.stations.add(new StationInfo("0396", "탄현"));
		this.stations.add(new StationInfo("0102", "탑리"));
		this.stations.add(new StationInfo("0714", "태금"));
		this.stations.add(new StationInfo("0123", "태백"));
		this.stations.add(new StationInfo("0125", "태화강"));
		this.stations.add(new StationInfo("0110", "통리"));
		this.stations.add(new StationInfo("0146", "퇴계원"));
		this.stations.add(new StationInfo("0400", "파주"));
		this.stations.add(new StationInfo("0085", "판교"));
		this.stations.add(new StationInfo("0256", "평내호평"));
		this.stations.add(new StationInfo("0130", "평촌"));
		this.stations.add(new StationInfo("0004", "평택"));
		this.stations.add(new StationInfo("0058", "포항"));
		this.stations.add(new StationInfo("0097", "풍기"));
		this.stations.add(new StationInfo("0065", "하동"));
		this.stations.add(new StationInfo("0238", "하양"));
		this.stations.add(new StationInfo("0129", "한림정"));
		this.stations.add(new StationInfo("0413", "한탄강"));
		this.stations.add(new StationInfo("0196", "함백"));
		this.stations.add(new StationInfo("0060", "함안"));
		this.stations.add(new StationInfo("0029", "함열"));
		this.stations.add(new StationInfo("0157", "함창"));
		this.stations.add(new StationInfo("0039", "함평"));
		this.stations.add(new StationInfo("0127", "해운대"));
		this.stations.add(new StationInfo("0390", "행신"));
		this.stations.add(new StationInfo("0107", "현동"));
		this.stations.add(new StationInfo("0211", "호계"));
		this.stations.add(new StationInfo("0081", "홍성"));
		this.stations.add(new StationInfo("0210", "화명"));
		this.stations.add(new StationInfo("0183", "화본"));
		this.stations.add(new StationInfo("0054", "화순"));
		this.stations.add(new StationInfo("0128", "황간"));
		this.stations.add(new StationInfo("0136", "횡천"));
		this.stations.add(new StationInfo("0458", "효문"));
		this.stations.add(new StationInfo("0191", "효자"));
		this.stations.add(new StationInfo("0274", "효천"));
		this.stations.add(new StationInfo("0343", "흑석리"));
		this.stations.add(new StationInfo("0178", "희방사"));
	}

	public static Station getInstance() {
		if (instance == null) {
			instance = new Station();
		}
		return instance;
	}

	public String getCode(String name) {
		for (int i = 0; i < this.stations.size(); ++i) {
			if (this.stations.get(i).name().equals(name)) {
				return this.stations.get(i).code();
			}
		}
		return "0000";
	}

	public String getName(String code) {
		for (int i = 0; i < this.stations.size(); ++i) {
			if (this.stations.get(i).code().equals(code)) {
				return this.stations.get(i).name();
			}
		}
		return "";
	}

	public ArrayList<String> names() {
		ArrayList<String> res = new ArrayList<String>();
		for (int i = 0; i < this.stations.size(); ++i) {
			res.add(this.stations.get(i).name());
		}
		return res;
	}

	public ArrayList<String> codes() {
		ArrayList<String> res = new ArrayList<String>();
		for (int i = 0; i < this.stations.size(); ++i) {
			res.add(this.stations.get(i).code());
		}
		return res;
	}

	public class StationInfo {
		public String code;
		public String name;

		public StationInfo(String code, String name) {
			this.code = code;
			this.name = name;
		}

		public String code() {
			return this.code;
		}

		public String name() {
			return this.name;
		}
	}
}
