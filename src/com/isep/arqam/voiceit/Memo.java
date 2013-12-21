package com.isep.arqam.voiceit;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;

/**************************************************************************************************
 * Memo
 *************************************************************************************************/
public class Memo {
	private String filePath;
	private String name;
	private int lengthAudio;
	
	/**********************************************************************************************
	 * Memo
	 *********************************************************************************************/
	public Memo(String name_memo) {
		filePath = Environment.getExternalStorageDirectory()+"/"+name_memo+".3gp";
		name = name_memo;
		try {
			lengthAudio = determineAudioLength();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**********************************************************************************************
	 * getLengthAudio
	 *********************************************************************************************/
	public int getLengthAudio() {
		return lengthAudio;
	}

	/**********************************************************************************************
	 *  getFilePath
	 *********************************************************************************************/
	public String getFilePath() {
		return filePath;
	}

	/**********************************************************************************************
	 * setFilePath
	 *********************************************************************************************/
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**********************************************************************************************
	 * getName
	 *********************************************************************************************/
	public String getName() {
		return name;
	}

	/**********************************************************************************************
	 * setName
	 *********************************************************************************************/
	public void setName(String name) {
		this.name = name;
	}
	
	/**********************************************************************************************
	 * determineAudioLength
	 *********************************************************************************************/
	//DETERMINAR A DURAÇÃO DO AUDIO
	public int determineAudioLength() throws IOException{
		MediaPlayer mp = new MediaPlayer();
		FileInputStream fs;
		FileDescriptor fd;
		fs = new FileInputStream(filePath);
		fd = fs.getFD();
		mp.setDataSource(fd);
		mp.prepare();
		int length = mp.getDuration();
		mp.release();
		fs.close();	
		return length;
	}
	
	/**********************************************************************************************
	 * delMemo
	 *********************************************************************************************/
	//apagar memo
	public boolean delMemo(){
		File file = new File(filePath);
		Log.i("fp",filePath);
		boolean deleted = file.delete();
		return deleted;
	}
	
	/**********************************************************************************************
	 * renameMemo
	 *********************************************************************************************/
	//renomar memo
	public boolean renameMemo(String currentName, String newName){	
		File file1 = new File(currentName);
		File file2 = new File(newName);		
		if(file1.renameTo(file2)){
			return true;
		}else{
			return false;
		}
	}

	/**********************************************************************************************
	 * toString
	 *********************************************************************************************/
	//INFORMAÇÂO QUE APARECE NA LISTVIEW
	@Override
    public String toString() {
		int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(lengthAudio);
		int seconds = (int) TimeUnit.MILLISECONDS.toSeconds(lengthAudio);
        return this.name+" - "+minutes+":"+seconds;
    }
}
