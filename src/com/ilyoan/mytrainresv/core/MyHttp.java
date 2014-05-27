package com.ilyoan.mytrainresv.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

public class MyHttp {
	private static final String TAG = "MyTrainResv";
	private static String URL_LOGIN = "https://www.letskorail.com/korail/com/loginAction.do";
	private static String URL_SEARCH = "http://www.letskorail.com/ebizprd/EbizPrdTicketPr21111_i1.do";
	private static String URL_RESV = "http://www.letskorail.com/ebizprd/EbizPrdTicketPr12111_i1.do";
	private static String URL_RESV_REFERER = "http://www.letskorail.com/ebizprd/EbizPrdTicketPr21111_i1.do";
	private static String FP_LOGIN_SUCCESS_URL = "loginProc.do";
	private static String FP_SEARCH_ERROR_BEGIN = "<span class=\"point02\">";
	private static String FP_SEARCH_ERROR_END = "</span>";
	private static String FP_SEARCH_TRAIN_INFO_BEGIN = "new train_info(";
	private static String FP_SEARCH_TRAIN_INFO_END = ")";
	private static String FP_SEARCH_SEAT_SOLD_OUT = "btn_selloff.gif";
	private static String FP_SEARCH_SEAT_SPECIAL = "icon_apm_spe_yes.gif";
	private static String FP_SEARCH_SEAT_NORMAL = "icon_apm_yes.gif";
	private static String FP_RESV_TO_LOGIN = "login.do";
	private static String FP_RESV_CONFIRM_IMG = "tit_tick_emit01.gif";
	private static String FP_RESV_ERROR_BEGIN = "<span class=\"point02\">";
	private static String FP_RESV_ERROR_END = "</span>";

	private final HttpClient httpClient = new DefaultHttpClient();

	public void login(String id, String pw) {
		Log.d(TAG, "MyHttp.login(" + id + ")");

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		// selInputFlg - 2: membership number, 4: cellphone number, 5: email.
		nameValuePairs.add(new BasicNameValuePair("selInputFlg", "2"));
		nameValuePairs.add(new BasicNameValuePair("UserId", id));
		nameValuePairs.add(new BasicNameValuePair("UserPwd", pw));
		nameValuePairs.add(new BasicNameValuePair("hidMemberFlg", "1"));
		nameValuePairs.add(new BasicNameValuePair("txtDv", pw.length() == 4 ? "1" : "2"));
		try {
			String param = getContentString(new UrlEncodedFormEntity(nameValuePairs), "UTF-8");
			String url = URL_LOGIN + "?" + param;
			Log.d(TAG, url);
			HttpTask httpTask = new HttpTask(this.httpClient, Method.GET, url, this.onLoginResponse);
			//httpTask.setPostEntity(nameValuePairs);
			httpTask.execute();
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, "HttpTask.login - exception: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private final OnResponse onLoginResponse = new OnResponse() {
		@Override
		public void onResponse(int status, String content) {
			Log.d(TAG, "MyHttp.onLoginResponse - status: " + status);
			Log.v(TAG, "MyHttp.onLoginResponse - content: " + content);
			if (content.contains(FP_LOGIN_SUCCESS_URL)) {
				Log.i(TAG, "MyHttp.onLoginResponse - login succeeded");
				MyTrainResv.showToast("로그인 성공");
			} else {
				Log.i(TAG, "MyHttp.onLoginResponse - login failed");
				MyTrainResv.showToast("로그인 실패");
			}
		}
	};

	public void searchTrain(String stationFrom,
			String stationTo,
			String date,
			String timeFrom,
			boolean ktxOnly) {
		Log.d(TAG, "MyHttp.searchTrain() START");
		Log.v(TAG, "       stationFrom: " + stationFrom);
		Log.v(TAG, "         stationTo: " + stationTo);
		Log.v(TAG, "              date: " + date);
		Log.v(TAG, "          timeFrom: " + timeFrom);
		Log.v(TAG, "           ktxOnly: " + ktxOnly);

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("txtGoStartCode", stationFrom));
		nameValuePairs.add(new BasicNameValuePair("txtGoEndCode", stationTo));
		nameValuePairs.add(new BasicNameValuePair("txtGoAbrdDt", date));
		nameValuePairs.add(new BasicNameValuePair("txtGoHour", timeFrom));
		nameValuePairs.add(new BasicNameValuePair("selGoTrain", ktxOnly ? "00" : "05"));
		nameValuePairs.add(new BasicNameValuePair("txtPsgCnt1", "1"));
		nameValuePairs.add(new BasicNameValuePair("chkStnNm", "N"));
		nameValuePairs.add(new BasicNameValuePair("radJobId", "1"));

		HttpEntity param;
		try {
			param = new UrlEncodedFormEntity(nameValuePairs);
			String url = URL_SEARCH + "?" + getContentString(param, "UTF-8");
			Log.d(TAG, "MyHttp.searchTrain - url: " + url);

			HttpTask httpTask = new HttpTask(this.httpClient, Method.GET, url, this.onSearchTrainResponse);
			httpTask.execute();
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, "HttpTask.searchTrain - exception: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private final OnResponse onSearchTrainResponse = new OnResponse() {
		@Override
		public void onResponse(int status, String content) {
			Log.d(TAG, "MyHttp.onSearchTrainResponse - status: " + status);
			//Log.v(TAG, "MyHttp.onLoginResponse - content: " + content);

			MyTrainResv myTrainResv = MyTrainResv.getInstance();
			myTrainResv.initTrainList();

			// error case
			ArrayList<Pair<String, Integer>> error = getStringBetweenFingerprint(
					content,
					FP_SEARCH_ERROR_BEGIN,
					FP_SEARCH_ERROR_END);
			if (error.size() > 0) {
				Log.w(TAG, "MyHttp.onSearchTrainResponse - error: " + error.get(0));
				MyTrainResv.showToast(error.get(0).first);
				return;
			}

			// train information
			ArrayList<Pair<String, Integer>> trainInfoList = getStringBetweenFingerprint(
					content,
					FP_SEARCH_TRAIN_INFO_BEGIN,
					FP_SEARCH_TRAIN_INFO_END);
			for (int i = 0; i < trainInfoList.size(); ++i) {
				// This string is about train information.
				String trainInfoStr = trainInfoList.get(i).first;
				// The train information is start at `index`.
				int index = trainInfoList.get(i).second;
				// Find matched icon for special seat.
				Pair<String, Integer> specialSeat = getFirstMatched(
						content, FP_SEARCH_SEAT_SOLD_OUT, FP_SEARCH_SEAT_SPECIAL, index);
				// Find matched icon for normal seat.
				Pair<String ,Integer> normalSeat =  getFirstMatched(
						content, FP_SEARCH_SEAT_SOLD_OUT, FP_SEARCH_SEAT_NORMAL, specialSeat.second + 1);
				// Create new train object.
				String[] trainInfo = trainInfoStr.replaceAll("\"", "").split(",");
				Train train = new Train(
						trainInfo[20].trim(),
						trainInfo[22].trim(),
						trainInfo[18].trim(),
						trainInfo[24].trim(),
						trainInfo[25].trim(),
						trainInfo[19].trim(),
						trainInfo[26].trim(),
						trainInfo[27].trim(),
						specialSeat.first == FP_SEARCH_SEAT_SPECIAL,
						normalSeat.first == FP_SEARCH_SEAT_NORMAL
						);
				Log.v(TAG, train.toString());
				myTrainResv.addTrain(train);
			}
			myTrainResv.onTrainList();
		}
	};


	public void resv(Train train) {
		Log.d(TAG, "MyHttp.resv(" + train.toString() + ")");

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		// From this point, parameters are used for reservation.
		nameValuePairs.add(new BasicNameValuePair("txtSeatAttCd1", "00")); // ??
		nameValuePairs.add(new BasicNameValuePair("txtSeatAttCd4", "15")); // ?
		nameValuePairs.add(new BasicNameValuePair("txtTotPsgCnt", "1"));
		nameValuePairs.add(new BasicNameValuePair("txtCompaCnt1", "1"));
		nameValuePairs.add(new BasicNameValuePair("txtJobId", "1101"));
		nameValuePairs.add(new BasicNameValuePair("txtJrnyCnt", "1"));
		nameValuePairs.add(new BasicNameValuePair("txtPsrmClCd1", train.hasNormal ? "1" : "2"));
		nameValuePairs.add(new BasicNameValuePair("txtJrnySqno1", "001"));
		nameValuePairs.add(new BasicNameValuePair("txtJrnyTpCd1", "11"));
		nameValuePairs.add(new BasicNameValuePair("txtDptDt1", train.fromDate));
		nameValuePairs.add(new BasicNameValuePair("txtDptRsStnCd1", train.fromStation));
		nameValuePairs.add(new BasicNameValuePair("txtDptTm1", train.fromTime));
		nameValuePairs.add(new BasicNameValuePair("txtArvRsStnCd1", train.toStation));
		nameValuePairs.add(new BasicNameValuePair("txtArvTm1", train.toTime));
		nameValuePairs.add(new BasicNameValuePair("txtTrnNo1", train.no));
		nameValuePairs.add(new BasicNameValuePair("txtTrnClsfCd1", train.type));
		nameValuePairs.add(new BasicNameValuePair("txtPsgTpCd1", "1"));

		try {
			String param = getContentString(new UrlEncodedFormEntity(nameValuePairs), "UTF-8");
			String url = URL_RESV + "?" + param;
			Log.d(TAG, url);
			HttpTask httpTask = new HttpTask(this.httpClient, Method.GET, url, this.onResvResponse);
			//httpTask.setPostEntity(nameValuePairs);
			httpTask.execute();
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, "HttpTask.resv - exception: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private final OnResponse onResvResponse = new OnResponse() {
		@Override
		public void onResponse(int status, String content) {
			Log.d(TAG, "MyHttp.onResvResponse - status: " + status);
			//Log.v(TAG, "MyHttp.onResvResponse - content: " + content);
			if (content.contains(FP_RESV_TO_LOGIN)) {
				Log.w(TAG, "MyHttp.onResvResponse - need login");
				MyTrainResv.showToast("로그인 필요");
			} else if (content.contains(FP_RESV_CONFIRM_IMG)) {
				Log.i(TAG, "MyHttp.onResvResponse - Success!!!!");
				MyTrainResv.showToast("예약 완료");
			} else {
				// error case
				ArrayList<Pair<String, Integer>> error = getStringBetweenFingerprint(
						content,
						FP_RESV_ERROR_BEGIN,
						FP_RESV_ERROR_END);
				if (error.size() > 0) {
					Log.w(TAG, "MyHttp.onSearchTrainResponse - error: " + error.get(0));
					MyTrainResv.showToast(error.get(0).first);
					return;
				}
			}
		}
	};



	private final ArrayList<Pair<String, Integer>> getStringBetweenFingerprint(
			String text, String begin, String end) {
		ArrayList<Pair<String, Integer>> res = new ArrayList<Pair<String, Integer>>();
		int beginIndex = 0;
		while (beginIndex != -1) {
			beginIndex = text.indexOf(begin, beginIndex);
			int endIndex = text.indexOf(end, beginIndex);
			if (beginIndex == -1 || endIndex == -1) break;
			res.add(new Pair<String, Integer>(
					text.substring(beginIndex + begin.length(), endIndex).trim(),
					beginIndex + begin.length()));
			beginIndex = endIndex;
		}
		return res;
	}

	private final Pair<String, Integer> getFirstMatched(String text, String a, String b, int beginIndex) {
		int indexA = text.indexOf(a, beginIndex);
		int indexB = text.indexOf(b, beginIndex);
		if (indexA == -1 && indexB == -1) {
			return new Pair<String, Integer>("", -1);
		}
		if (indexA == -1) {
			return new Pair<String, Integer>(b, indexB);
		} else if (indexB == -1) {
			return new Pair<String, Integer>(a, indexA);
		} else if (indexA < indexB) {
			return new Pair<String, Integer>(a, indexA);
		} else {
			return new Pair<String, Integer>(b, indexB);
		}
	}

	private final int getStatusCode(HttpResponse response) {
		return response.getStatusLine().getStatusCode();
	}

	private final String getContentString(HttpEntity entity) {
		return getContentString(entity, "UTF-8");
	}

	private final String getContentString(HttpEntity entity, String encoding) {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		try {
			InputStream content = entity.getContent();
			if (entity.getContentEncoding() != null) {
				encoding = entity.getContentEncoding().getValue();
			}
			br = new BufferedReader(new InputStreamReader(content, encoding));
			String line;
			while ((line = br.readLine()) != null) {
				Log.v(TAG, line);
				sb.append(line);
			}
		} catch (IllegalStateException e) {
			Log.e(TAG, "MyHttp.getContentString - exception: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(TAG, "MyHttp.getContentString - exception: " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}

	private interface OnResponse {
		public void onResponse(int status, String content);
	}

	public enum Method { GET, POST }

	private class HttpTask extends AsyncTask<String, Void, HttpResponse> {
		private final Method method;
		private final String url;
		private OnResponse onResponse = null;
		private HttpEntity postEntity = null;
		private int status;
		private String contents;

		private HttpClient httpClient = null;

		public HttpTask(HttpClient httpClient, Method method, String url, OnResponse onResponse) {
			this.httpClient = httpClient;
			this.method = method;
			this.url = url;
			this.onResponse = onResponse;
		}

		public void setPostEntity(List<NameValuePair> entities) {
			try {
				this.postEntity = new UrlEncodedFormEntity(entities);
			} catch (UnsupportedEncodingException e) {
				Log.e(TAG, "HttpTask.setPostEntity - exception: " + e.getMessage());
				e.printStackTrace();
			}
		}

		@Override
		protected HttpResponse doInBackground(String... params) {
			HttpUriRequest request = null;
			HttpResponse response = null;

			if (this.method == Method.GET) {
				request = new HttpGet(this.url);
			} else {
				HttpPost post = new HttpPost(this.url);
				if (this.postEntity != null) {
					post.setEntity(this.postEntity);
				}
				request = post;
			}
			request.addHeader("Content-Type", "application/xhtml+xml");
			request.addHeader("charset", "UTF-8");
			request.addHeader("Referer", URL_RESV_REFERER);

			try {
				response = this.httpClient.execute(request);
				this.status = getStatusCode(response);
				this.contents = getContentString(response.getEntity());
			} catch (ClientProtocolException e) {
				Log.e(TAG, "HttpTask failed: " + e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				Log.e(TAG, "HttpTask failed: " + e.getMessage());
				e.printStackTrace();
			}
			return response;
		}

		@Override
		protected void onPostExecute(HttpResponse response) {
			this.onResponse.onResponse(this.status, this.contents);
		}
	}
}

/* javascript train_info segnature
function train_info(
        txtGoAbrdDt,			// 0
        txtGoStartCode,
        txtGoEndCode,
        selGoTrain,
        selGoRoom,
        txtGoHour,				// 5
        txtGoTrnNo,
        useSeatFlg,
        useServiceFlg,
        selGoSeat,
        selGoSeat1,				// 10
        selGoSeat2,
        txtPsgCnt1,
        txtPsgCnt2,
        selGoService,
        h_trn_seq,				// 15
        h_chg_trn_dv_cd,
        h_chg_trn_seq,
        h_dpt_rs_stn_cd,
        h_arv_rs_stn_cd,
        h_trn_no,				// 20
        h_yms_apl_flg,
        h_trn_clsf_cd,
        h_run_dt,
        h_dpt_dt,
        h_dpt_tm,				// 25
        h_arv_dt,
        h_arv_tm,
        h_dlay_hr,
        h_rsv_wait_ps_cnt,
        h_dtour_flg,			// 30
        h_car_tp_cd,
        h_trn_cps_cd1,
        h_trn_cps_cd2,
        h_trn_cps_cd3,
        h_trn_cps_cd4,			// 35
        h_trn_cps_cd5,
        h_no_ticket_dpt_rs_stn_cd,
        h_no_ticket_arv_rs_stn_cd,
        h_nonstop_msg
   )
 */
