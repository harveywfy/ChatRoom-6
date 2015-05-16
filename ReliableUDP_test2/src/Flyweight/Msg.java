package Flyweight;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Msg {

	private int id; //��ʾ�������

	//private int sendCount = 0; // ���ʹ���
	private long lastSendTime; // ���һ�η��͵�ʱ��
	
	public Msg(int id) {
		this.id = id;
	}

	public Msg(byte[] udpData) {
		try {
			ByteArrayInputStream bins = new ByteArrayInputStream(udpData);
			DataInputStream dins = new DataInputStream(bins);

			this.id = dins.readInt();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public byte[] toByte() {
		try {
			ByteArrayOutputStream bous = new ByteArrayOutputStream();
			DataOutputStream dous = new DataOutputStream(bous);

			dous.writeInt(this.id);
			dous.flush();

			return bous.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setID(int id) {
		this.id = id;
	}

	public int getID() {
		return id;
	}
	
//	public int getSendCount() {
//		return sendCount;
//	}
//
//	public void setSendCount(int sendCount) {
//		this.sendCount = sendCount;
//	}

	public long getLastSendTime() {
		return lastSendTime;
	}

	public void setLastSendTime(long lastSendTime) {
		this.lastSendTime = lastSendTime;
	}
}
