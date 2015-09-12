package com.ilyoan.mytrainresv.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

public class MyHttp {
	private static final String TAG = "MyTrainResv";

	private static String URL_HOST = "smart.letskorail.com:9443";
	private static String URL_PREFIX = "https://" + URL_HOST + "/classes/com.korail.mobile";

	private static String URL_LOGIN = URL_PREFIX + ".login.Login";
	private static String URL_SEARCH = URL_PREFIX + ".seatMovie.ScheduleView";
	private static String URL_RESV = URL_PREFIX + ".certification.TicketReservation";

    private static final String ENCODING = "UTF-8";

    private String DEVICE = "AD";
    private String VERSION = "150718001";
    private String KEY = "korail01234567890";

	private HttpClient httpClient = null;

	public class MySSLSocketFactory extends SSLSocketFactory {
		SSLContext sslContext;


		public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException, NoSuchProviderException {
			super(truststore);

			this.sslContext = SSLContext.getInstance("SSLv3");

			TrustManager tm = new X509TrustManager() {
				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};

			this.sslContext.init(null, new TrustManager[] { tm }, null);
		}

		@Override
		public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
			return this.sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
		}

		@Override
		public Socket createSocket() throws IOException {
			return this.sslContext.getSocketFactory().createSocket();
		}
	}

	public MyHttp() {
		//this.httpClient = new DefaultHttpClient();
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null, null);

			SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 9443));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

			this.httpClient = new DefaultHttpClient(ccm, params);

			Log.d(TAG, "Http Client created");

		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
	}


	// Interface for search train result callback handler.
	public interface OnSearchTrainCallback {
		public void onResult(ArrayList<Train> trainList, String error);
	}

	// Interface for resv train result callback handler.
	public interface OnResvTrainCallback {
		public void onResult(boolean result, String error);
	}


    // -------------------------------------LOGIN---------------------------------------------

	// login to system.
	public void login(String id, String pw) {
		Log.v(TAG, "MyHttp.login(" + id + ")");

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		nameValuePairs.add(new BasicNameValuePair("Device", DEVICE));
		nameValuePairs.add(new BasicNameValuePair("Version", VERSION));
		nameValuePairs.add(new BasicNameValuePair("txtInputFlg", "2"));
		nameValuePairs.add(new BasicNameValuePair("txtMemberNo", id));
		nameValuePairs.add(new BasicNameValuePair("txtPwd", pw));

		try {
            String url = URL_LOGIN + buildParameter(nameValuePairs);
			Log.v(TAG, url);

			HttpTask httpTask = new HttpTask(
					this.httpClient,
					Method.GET,
                    url,
					this.onLoginResponse);

			httpTask.execute();
		} catch (Exception e) {
			Log.e(TAG, "HttpTask.login - exception: " + e.getMessage());
			e.printStackTrace();
		}
	}

	// login response handler.
	private final OnResponse onLoginResponse = new OnResponse() {
		@Override
		public void onResponse(int status, JSONObject json) {
			Log.d(TAG, "MyHttp.onLoginResponse - status: " + status);

            String result = "";
            String msg = "";
            String code = "";

            try {
                result = json.getString("strResult");
                code = json.getString("h_msg_cd");
                msg = json.getString("h_msg_txt");
                KEY = json.getString("Key");
            } catch (Exception e) {
                code = "";
                msg = e.getMessage();
            }
            if (code.equals("IRZ000001")) {
                Log.i(TAG, "MyHttp.onLoginResponse - login succeeded");
                MyTrainResv.showToast("로그인 성공");
                MyTrainResv.displayNoti("로그인", "로그인 성공");
            } else {
                MyTrainResv.showToast("로그인 실패");
                MyTrainResv.displayNoti("로그인", "로그인 실패: " + msg);
            }
		}
	};


    // -------------------------------------SEARCH---------------------------------------------

	// search train
	public void searchTrain(
            String stationFrom,
			String stationTo,
			String date,
			String timeFrom,
			boolean ktxOnly,
			OnSearchTrainCallback callback) {
		Log.d(TAG, "MyHttp.searchTrain() START");
		Log.v(TAG, "       stationFrom: " + stationFrom);
		Log.v(TAG, "         stationTo: " + stationTo);
		Log.v(TAG, "              date: " + date);
		Log.v(TAG, "          timeFrom: " + timeFrom);
		Log.v(TAG, "           ktxOnly: " + ktxOnly);

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("Device", DEVICE));
        nameValuePairs.add(new BasicNameValuePair("Version", VERSION));
        nameValuePairs.add(new BasicNameValuePair("Key", KEY));
		nameValuePairs.add(new BasicNameValuePair("txtGoStart", stationFrom));
		nameValuePairs.add(new BasicNameValuePair("txtGoEnd", stationTo));
		nameValuePairs.add(new BasicNameValuePair("txtGoAbrdDt", date));
		nameValuePairs.add(new BasicNameValuePair("txtGoHour", timeFrom));
		nameValuePairs.add(new BasicNameValuePair("selGoTrain", ktxOnly ? "00" : "05"));
		nameValuePairs.add(new BasicNameValuePair("radJobId", "1"));
        nameValuePairs.add(new BasicNameValuePair("txtMenuId", "11"));
        nameValuePairs.add(new BasicNameValuePair("txtPsgFlg_1", "1")); // 어른 수

		//HttpEntity param;
		try {
            String url = URL_SEARCH + buildParameter(nameValuePairs);
			//Log.v(TAG, "MyHttp.searchTrain - url: " + url);

			HttpTask httpTask = new HttpTask(
					this.httpClient,
					Method.GET,
					url,
					new OnSearchTrainResponse(callback));

			httpTask.execute();
		} catch (Exception e) {
			Log.e(TAG, "HttpTask.searchTrain - exception: " + e.getMessage());
			e.printStackTrace();
		}
	}

	class OnSearchTrainResponse implements OnResponse {
		OnSearchTrainCallback callback = null;

		public OnSearchTrainResponse(OnSearchTrainCallback callback) {
			this.callback = callback;
		}

		@Override
		public void onResponse(int status, JSONObject json) {
            Log.d(TAG, "MyHttp.onSearchTrainResponse - status: " + status);

            ArrayList<Train> result = null;

            String code = "";
            String msg = "";

            try {
                code = json.getString("h_msg_cd");
                msg = json.getString("h_msg_txt");

                if (code.equals("IRG000000")) {
                    JSONArray train_infos = json.getJSONObject("trn_infos").getJSONArray("trn_info");
                    result = new ArrayList<Train>();
                    int trains = train_infos.length();
                    for (int i = 0; i < trains; ++i) {
                        JSONObject train_info = train_infos.getJSONObject(i);
                        Log.v(TAG, train_info.toString());

                        Train train = new Train(
                                train_info.getString("h_trn_no"),
                                train_info.getString("h_trn_clsf_cd"),
                                train_info.getString("h_trn_gp_cd"),
                                train_info.getString("h_dpt_rs_stn_cd"),
                                train_info.getString("h_dpt_dt"),
                                train_info.getString("h_dpt_tm"),
                                train_info.getString("h_arv_rs_stn_cd"),
                                train_info.getString("h_arv_dt"),
                                train_info.getString("h_arv_tm"),
                                train_info.getString("h_run_dt"),
                                train_info.getString("h_spe_rsv_cd").equals("11"),
                                train_info.getString("h_gen_rsv_cd").equals("11"));

                        Log.v(TAG, train.toString());
                        result.add(train);
                    }
                    msg = null;
                }
            } catch (Exception e) {
                code = "";
                msg = e.getMessage();
            }

            if (this.callback != null) {
                this.callback.onResult(result, msg);
            }
		}
	}


    // -------------------------------------RESERVATION-------------------------------------------
	// reserve train
	public void resv(Train train, OnResvTrainCallback callback) {
		Log.v(TAG, "MyHttp.resv(" + train.toString() + ")");

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		// From this point, parameters are used for reservation.
        nameValuePairs.add(new BasicNameValuePair("Device", DEVICE));
        nameValuePairs.add(new BasicNameValuePair("Version", VERSION));
        nameValuePairs.add(new BasicNameValuePair("Key", KEY));

        nameValuePairs.add(new BasicNameValuePair("txtJobId", "1101")); // 일반예약

		nameValuePairs.add(new BasicNameValuePair("txtSeatAttCd1", "000")); // 좌석속성 ?
        nameValuePairs.add(new BasicNameValuePair("txtSeatAttCd2", "000")); // 순방향/역방향
        nameValuePairs.add(new BasicNameValuePair("txtSeatAttCd3", "000")); // 창/내
		nameValuePairs.add(new BasicNameValuePair("txtSeatAttCd4", "015")); // 요구속성
        nameValuePairs.add(new BasicNameValuePair("txtSeatAttCd5", "000")); // 노트/어린이단독
        nameValuePairs.add(new BasicNameValuePair("txtStndFlg", "N")); // 입석여부?
        nameValuePairs.add(new BasicNameValuePair("txtMenuId", "11"));
        nameValuePairs.add(new BasicNameValuePair("txtJrnyCnt", "1"));

		nameValuePairs.add(new BasicNameValuePair("txtJrnySqno1", "001"));
		nameValuePairs.add(new BasicNameValuePair("txtJrnyTpCd1", "11")); // 편도
		nameValuePairs.add(new BasicNameValuePair("txtDptDt1", train.fromDate));
		nameValuePairs.add(new BasicNameValuePair("txtDptRsStnCd1", train.fromStation));
		nameValuePairs.add(new BasicNameValuePair("txtDptTm1", train.fromTime));
		nameValuePairs.add(new BasicNameValuePair("txtArvRsStnCd1", train.toStation));
		nameValuePairs.add(new BasicNameValuePair("txtTrnNo1", train.no));
        nameValuePairs.add(new BasicNameValuePair("txtTrnGpCd1", train.group));
        nameValuePairs.add(new BasicNameValuePair("txtRunDt1", train.runDate));
		nameValuePairs.add(new BasicNameValuePair("txtTrnClsfCd1", train.type));
        nameValuePairs.add(new BasicNameValuePair("txtPsrmClCd1", train.hasNormal ? "1" : "2"));
        nameValuePairs.add(new BasicNameValuePair("txtSrcarNo1", "1")); // ?

        nameValuePairs.add(new BasicNameValuePair("txtTotPsgCnt", "1")); // 전체인원수
        nameValuePairs.add(new BasicNameValuePair("txtPsgTpCd1", "1")); // 어른
        nameValuePairs.add(new BasicNameValuePair("txtCompaCnt1", "1"));  // 1명
        nameValuePairs.add(new BasicNameValuePair("txtDiscKndCd1", "000")); // 할인없음


		try {
            String url = URL_RESV + buildParameter(nameValuePairs);
            Log.v(TAG, "MyHttp.resv - url: " + url);

			HttpTask httpTask = new HttpTask(
					this.httpClient,
					Method.GET,
					url,
					new OnReserveResponse(callback));

			httpTask.execute();
		} catch (Exception e) {
			Log.e(TAG, "HttpTask.resv - exception: " + e.getMessage());
			e.printStackTrace();
		}
	}

	class OnReserveResponse implements OnResponse {
		OnResvTrainCallback callback = null;

		public OnReserveResponse(OnResvTrainCallback callback) {
			this.callback = callback;
		}

		@Override
		public void onResponse(int status, JSONObject json) {
			Log.d(TAG, "MyHttp.onResvResponse - status: " + status);

            boolean result = false;
            String code = "";
            String msg = null;

            try {
                code = json.getString("h_msg_cd");
                msg = json.getString("h_msg_txt");

                if (code.equals("IRR000018")) {
                    result = true;
                    msg = null;
                } else {
                    result = false;
                }
            } catch (Exception e) {
                result = false;
                msg = e.getMessage();
            }

            if (this.callback != null) {
                this.callback.onResult(result, msg);
            }
		}
	}

	// Get status code of http response
	private final int getStatusCode(HttpResponse response) {
		return response.getStatusLine().getStatusCode();
	}

    private final String buildParameter(ArrayList<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        boolean first = true;
        for (NameValuePair pair: params) {
            sb.append(first ? "?" : "&");
            sb.append(pair.getName());
            sb.append("=");
            sb.append(pair.getValue());
            first = false;
        }
        return sb.toString();
    }

	// Get content string.
	private final String getContentString(HttpEntity entity) {
		return getContentString(entity, ENCODING);
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
				//Log.v(TAG, line);
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

	// Interface for http response handler.
	public interface OnResponse {
		public void onResponse(int status, JSONObject json);
	}

	// Http request method.
	public enum Method { GET, POST }

	// New version of Android enforces to use HttpTask.
	private class HttpTask extends AsyncTask<String, Void, HttpResponse> {
		private final Method method;
		private final String url;
		private OnResponse onResponse = null;
		private HttpEntity postEntity = null;
		private int status;
		private String contents;

		// HttpClient object. the object will be created in the parent class(MyHttp) for persistent connection.
		private HttpClient httpClient = null;

		// Create new HttpTask object.
		public HttpTask(HttpClient httpClient, Method method, String url, OnResponse onResponse) {
			this.httpClient = httpClient;
			this.method = method;
			this.url = url;
			this.onResponse = onResponse;
		}

		// Set post entity
		// TODO(ilyoan): looks like this is not working from some point.
		// take look at this. fortunately using GET method works.
		public void setPostEntity(List<NameValuePair> entities) {
			try {
				this.postEntity = new UrlEncodedFormEntity(entities, ENCODING);
			} catch (UnsupportedEncodingException e) {
				Log.e(TAG, "HttpTask.setPostEntity - exception: " + e.getMessage());
				e.printStackTrace();
			}
		}

		// Http request procedure running in background.
		@Override
		protected HttpResponse doInBackground(String... params) {
			HttpUriRequest request = null;
			HttpResponse response = null;

			// Create http request object depends on the method.
			if (this.method == Method.GET) {
				request = new HttpGet(this.url);
			} else {
				HttpPost post = new HttpPost(this.url);
				if (this.postEntity != null) {
					post.setEntity(this.postEntity);
				}
				request = post;
			}

			try {
				// Take http response
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
			// Returns the http response. onPostExecute() will be invoked by Android system.
			return response;
		}

		// Now we got Http response object. process with it. this function runs on main thread.
		@Override
		protected void onPostExecute(HttpResponse response) {
            JSONObject json = null;
            try {
                json = new JSONObject(this.contents);
            } catch (Exception e) {
                Log.e(TAG, "onPostExecute failed: " + e.getMessage());
            }
            this.onResponse.onResponse(this.status, json);
		}
	}
}
