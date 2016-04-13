package org.kylin.modules.web;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.zip.GZIPOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;

public class WebUtils {
	public static final long ONE_YEAR_SECONDS = 31536000L;

	public static void setDownloadableHeader(HttpServletResponse response,
			String fileName) {
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ fileName + "\"");
	}

	public static void setLastModifiedHeader(HttpServletResponse response,
			long lastModifiedDate) {
		response.setDateHeader("Last-Modified", lastModifiedDate);
	}

	public static void setEtag(HttpServletResponse response, String etag) {
		response.setHeader("ETag", etag);
	}

	public static void setExpiresHeader(HttpServletResponse response,
			long expiresSeconds) {
		response.setDateHeader("Expires", System.currentTimeMillis()
				+ expiresSeconds * 1000L);

		response.setHeader("Cache-Control", "max-age=" + expiresSeconds);
	}

	public static void setNoCacheHeader(HttpServletResponse response) {
		response.setDateHeader("Expires", 0L);

		response.setHeader("Cache-Control", "max-age=0");
	}

	public static boolean checkAccetptGzip(HttpServletRequest request) {
		String acceptEncoding = request.getHeader("Accept-Encoding");

		return StringUtils.contains(acceptEncoding, "gzip");
	}

	public static OutputStream buildGzipOutputStream(
			HttpServletResponse response) throws IOException {
		response.setHeader("Content-Encoding", "gzip");
		return new GZIPOutputStream(response.getOutputStream());
	}

	public static boolean checkIfModifiedSince(HttpServletRequest request,
			HttpServletResponse response, long lastModified) {
		long ifModifiedSince = request.getDateHeader("If-Modified-Since");
		if ((ifModifiedSince != -1L)
				&& (lastModified < ifModifiedSince + 1000L)) {
			response.setStatus(304);
			return false;
		}
		return true;
	}

	public static boolean checkIfNoneMatchEtag(HttpServletRequest request,
			HttpServletResponse response, String etag) {
		String headerValue = request.getHeader("If-None-Match");
		if (headerValue != null) {
			boolean conditionSatisfied = false;
			if (!headerValue.equals("*")) {
				StringTokenizer commaTokenizer = new StringTokenizer(
						headerValue, ",");
				do {
					String currentToken = commaTokenizer.nextToken();
					if (currentToken.trim().equals(etag))
						conditionSatisfied = true;
					if (conditionSatisfied)
						response.setStatus(304);
				} while (commaTokenizer.hasMoreTokens());
			} else {
				conditionSatisfied = true;
			}

			if (conditionSatisfied) {
				response.setStatus(304);
				response.setHeader("ETag", etag);
				return false;
			}
		}
		return true;
	}

	public static Map<String, String> getParametersStartingWith(
			HttpServletRequest request, String prefix) {
		return org.springframework.web.util.WebUtils.getParametersStartingWith(
				request, prefix);
	}
}
