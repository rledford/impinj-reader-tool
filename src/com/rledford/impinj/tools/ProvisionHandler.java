package com.rledford.impinj.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Base64;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class ProvisionHandler {
	
	public static final String READER_URI = "https://{ip}:51505/provision";
	public static final String ITEMSENSE_URI = "https://{ip}/itemsense";
	public static final String PAYLOAD = "{\"BaseUrl\": {baseUrl}, \"AgentId\": {agentId}, \"ApiKey\": {apiKey}, \"ServerCertificate\": {serverCert}}";
	
	public static class ProvisionParams {
		public String readerIp;
		public String itemSenseIp;
		public String agentId;
		public String apiKey;
		public String pathToPemFile;
		public ThreadListener listener;
	}
	
	public static String quote(String str) {
		return "\"{str}\"".replace("{str}", str);
	}
	
	public static byte[] encode(byte[] bytes) {
		return Base64.getEncoder().encode(bytes);
	}
	
	public static byte[] encode(String str) {
		try {
			return  encode(str.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException unsupported) {
			return new byte[0];
		}
	}
	
	public static byte[] loadPemFile(String pathToPem) {
		try {
			Path path = Paths.get(pathToPem);
			return Files.readAllBytes(path);
		}
		catch (IOException err) {
			return null;
		}
	}
	
	public static void provisionAsync(ProvisionParams params) {
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				params.listener.onMessage("* provision reader *", true);
				String message = provision(params);
				params.listener.onMessage(message+"\n", false);
			}
		});
		thread.start();
	}
	
	public static String provision(ProvisionParams params) {
		URL url = null;
		HttpsURLConnection connection = null;
		OutputStream os;
		int responseCode = -1;
		byte[] pem = loadPemFile(params.pathToPemFile);
		
		if (pem == null) { return "Failed to load .pem file"; }
		
		String encodedPem = new String(encode(pem));
		String payload = PAYLOAD
				.replace("{baseUrl}", quote(ITEMSENSE_URI.replace("{ip}", params.itemSenseIp)))
				.replace("{agentId}", quote(params.agentId))
				.replace("{apiKey}", quote(params.apiKey.toString()))
				.replace("{serverCert}", quote(new String(encodedPem)));

		try {
			url = new URL(READER_URI.replace("{ip}", params.readerIp));
		} catch (MalformedURLException e) {
			System.out.println(e);
			return e.toString();
		}
		try {
			connection = (HttpsURLConnection)url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("charset", "utf-8");
			connection.setRequestProperty("Accept", "application/json");
			connection.setUseCaches(false);
			connection.setHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});
			
			os = connection.getOutputStream();
			os.write(payload.getBytes());
			os.flush();
			os.close();

			responseCode = connection.getResponseCode();
			
		} catch (IOException io) {
			System.out.println(io.getMessage());
			return io.getMessage();
		}
		
		if (connection != null) {
			if (responseCode == 200) {
				return "Reader provisioned successfully.";
			}
			if (responseCode > 200 && responseCode < 400) {
				try {
					BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
					String inputString;
					StringBuffer response = new StringBuffer();
					while ((inputString = in.readLine()) != null) {
						response.append(inputString);
					}
					in.close();
					System.out.println(response.toString());
					return response.toString();
				} catch (IOException io) {
					System.out.println(io.toString());
					return io.toString();
				}
			}
			if (responseCode >= 400) {
				if (responseCode == 401) { return "Authentication failed - check API key and PEM file"; }
				try {
					BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
					String inputString;
					StringBuffer response = new StringBuffer();
					while ((inputString = in.readLine()) != null) {
						response.append(inputString);
					}
					in.close();
					System.out.println(response.toString());
					return response.toString();
				} catch (IOException io) {
					System.out.println(io.toString());
					return io.toString();
				}
			}
		}
		
		return "Unable to connect to the reader";
	}
	
	/**
     * Disables the SSL certificate checking for new instances of {@link HttpsURLConnection} This has been created to
     * aid testing on a local box, not for use on production.
     */
    public static void disableSSLCertificateChecking() {
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                // Not implemented
            }

            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                // Not implemented
            }
        } };

        try {
            SSLContext sc = SSLContext.getInstance("TLS");

            sc.init(null, trustAllCerts, new java.security.SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
