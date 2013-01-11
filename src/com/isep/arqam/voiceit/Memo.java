package com.isep.arqam.voiceit;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;

public class Memo {

	private String filePath;
	private String name;
	private int lengthAudio;
	
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
	
	public int getLengthAudio() {
		return lengthAudio;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
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
	
	//apagar memo
	public boolean delMemo(){
		File file = new File(filePath);
		Log.i("fp",filePath);
		boolean deleted = file.delete();
		return deleted;
	}
	
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

	//INFORMAÇÂO QUE APARECE NA LISTVIEW
	@Override
    public String toString() {
		int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(lengthAudio);
		int seconds = (int) TimeUnit.MILLISECONDS.toSeconds(lengthAudio);
		
        return this.name+" - "+minutes+":"+seconds;
    }
}
