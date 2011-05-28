package com.ramblingwood.minecraft.jsonapi.streams;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.simpleForBukkit.JSONObject;

import com.ramblingwood.minecraft.jsonapi.JSONServer;

public class StreamingResponse extends InputStream {
	private ArrayList<? extends JSONAPIStream> stack;
	private String callback;
	private int pos = 0;
	
	public StreamingResponse(ArrayList<? extends JSONAPIStream> arr, String callback) {
		stack = arr;
	}
	
	public String nextLine () {
		while(pos >= stack.size()) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		pos++;

		return JSONServer.callback(callback, makeResponseObj(stack.get(pos-1))).concat("\r\n");
	}
	
	private String makeResponseObj (JSONAPIStream ja) {
		JSONObject o = new JSONObject();
		o.put("result", "success");
		o.put("source", ja.getSourceName());
		o.put("success", ja);
		
		String ret = o.toJSONString();
		return ret;
	}
	
	public int read() throws IOException {
		return -1;
	}
}
