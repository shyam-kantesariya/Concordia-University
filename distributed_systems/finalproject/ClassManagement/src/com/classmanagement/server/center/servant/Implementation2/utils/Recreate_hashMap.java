package com.classmanagement.server.center.servant.Implementation2.utils;


import com.classmanagement.models.Location;
import com.classmanagement.exceptions.InvalidJournalRecordType;
import com.classmanagement.server.center.servant.CenterServant;
import com.classmanagement.server.center.servant.Implementation2.SMS_implementation;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static com.classmanagement.configuration.Constants.REPLICA_ONE;
import static com.classmanagement.configuration.Constants.REPLICA_TWO;

public class Recreate_hashMap {
	private SMS_implementation centerServer;
	private String File_path;
	private CenterServant NewCentralServer;
	public Recreate_hashMap(String Path, Location location, String replicaId) throws IOException{
		this.File_path=Path;
		this.centerServer = new SMS_implementation(location.toString(), replicaId);
	}


	public SMS_implementation execute() throws IOException, Exception {
		BufferedReader in = new BufferedReader(
				new FileReader(this.File_path));
		String line = "";
		while ((line = in.readLine()) != null) {
			if (line.startsWith("ADD|S")) {
				line = line.replaceAll("ADD|", "");
				String[] studentData = line.split("(?<!,)[\\]]?\\s+[\\[]?");
				String RecordID=this.centerServer.createSRecord(studentData[1], studentData[2], studentData[3], studentData[4], studentData[5],"MTL1234");
				System.out.println(RecordID);
			} else if (line.startsWith("ADD|T")) {
				line = line.replaceAll("ADD|", "");
				String[] teacherData = line.split(" ");
				String RecordID=this.centerServer.createTRecord(teacherData[1], teacherData[2], teacherData[3],
						teacherData[4], teacherData[5], teacherData[6],"MTL0001");
				System.out.println(RecordID);
			} else if (line.startsWith("DELETE")) {
				line = line.replaceAll("DELETE|", "");
				String RecordID = line.substring(1,8);
				System.out.println(RecordID);
				String Record=this.centerServer.getRecordCounts("MTL0001");
				this.centerServer.delete(RecordID);
				String Record_last=this.centerServer.getRecordCounts("MTL0001");
				System.out.println(Record_last);
			} else if (line.startsWith("EDIT")) {
				line = line.replaceAll("EDIT|", "");
				String[] updateData = line.split(":");
				updateData[0] = line.substring(1,8);
				System.out.println(updateData.length);
				this.centerServer.editRecord(updateData[0], updateData[1], updateData[2], "MTL1234");
			}
		}
		in.close();
		return this.centerServer;
	}
}
