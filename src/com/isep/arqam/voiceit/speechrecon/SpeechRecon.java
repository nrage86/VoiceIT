package com.isep.arqam.voiceit.speechrecon;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.isep.arqam.voiceit.R;
import com.isep.arqam.voiceit.Voiceit_main;
import com.isep.arqam.voiceit.dropbox.DropboxMain;

public class SpeechRecon extends Activity{

	ListView lv; //listView
	static final int check = 1111;
	String defaultName = null; //variável que armazena o nome por defeito recebido da activity anterior (MemoRecord)
	String newName = null; //vari��vel que armazena o novo nome dado pelo utilizador
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_recon);
        
        lv = (ListView)findViewById(R.id.wordsLv);
		
        //BOTAO PARA DITAR NOME DO MEMO
        Button b = (Button)findViewById(R.id.speakBt);
		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				//Verifica o estado da ligação à internet
				boolean isConnected = checkInternetConnection();
				
				if(isConnected==true){ //executa o reconhecimento de fala para dar o nome ao título
					Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
					i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
					i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Dite o nome...");
					startActivityForResult(i, check);
					finish();
				}else{ //deixa o ficheiro com o nome por defeito e salta para a Main Activity
					Intent i = new Intent(SpeechRecon.this, Voiceit_main.class);
					
					//TOAST - nome por defeito
					Context context = getApplicationContext();
					CharSequence text = "O memo foi gravado com um nome incremental";
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
					
					//Voltar à activity principal
					SpeechRecon.this.startActivity(i);
					finish();
				}
			}
		});
		
		//BOT��O QUE SALTA A OP����O DE DITAR NOME
		Button bDefault = (Button)findViewById(R.id.defaultBt);
		bDefault.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				//Verifica o estado da ligação à internet
				boolean isConnected = checkInternetConnection();
				
				if(isConnected==true){ //executa a activity de sincronização com Dropbox
					Intent myIntent = new Intent(SpeechRecon.this, DropboxMain.class);
					newName = getIntent().getStringExtra("memoNameDefault"); //neste contexto a vari��vel newName assume o nome por defeito
					myIntent.putExtra("memoName", newName); //envia o caminho do ficheiro para a activity DropboxTest
					myIntent.putExtra("DropboxTask", "upload");
					SpeechRecon.this.startActivity(myIntent); //inicia a activity DropboxMain
					finish();
				}else{ //salta para a Main Activity
					Intent i = new Intent(SpeechRecon.this, Voiceit_main.class);
					
					//TOAST - nome por defeito
					Context context = getApplicationContext();
					CharSequence text = "O memo foi gravado com um nome incremental";
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
					
					//Voltar à activity principal
					SpeechRecon.this.startActivity(i);
					finish();
				}
				
				
				
				

			}
		});
    }
    
    
    //Lida com os resultados do reconhecimento de voz	 
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == check && resultCode == RESULT_OK){
			ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			
			//preencher lista com elementos do array de resultados
			lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, results));
			
			//comportamento ao clicar num item da lista lv
			lv.setOnItemClickListener(new OnItemClickListener(){
				public void onItemClick(AdapterView<?> a, View v, int position, long id){
					Intent myIntent = new Intent(SpeechRecon.this, DropboxMain.class);
					
					//caminho do ficheiro antes de o utilizador ter ditado o nome, recebido da activity MemoRecord
					defaultName = getIntent().getStringExtra("memoNameDefault");
					
					//nome ditado pelo utilizador
					newName = Environment.getExternalStorageDirectory().getAbsolutePath();
					newName += "/"+lv.getItemAtPosition(position).toString()+".3gp";					
					
					//mudan��a de nome do ficheiro
					File file1 = new File(defaultName);
					File file2 = new File(newName);		
					file1.renameTo(file2);
					
					myIntent.putExtra("memoName", newName); //envia o caminho do ficheiro para a activity DropboxTest
					myIntent.putExtra("DropboxTask", "upload");
					SpeechRecon.this.startActivity(myIntent); //inicia a activity DropboxTest
				}
			});
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
    
    
    //FUNÇÃO QUE TESTA A EXISTÊNCIA DE LIGAÇÃO HÁ INTERNET
    private boolean checkInternetConnection() {
    	ConnectivityManager conMgr = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);
    	
    	if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()) {
    		return true;
    	} else {
    		return false;
    	}

    } 
}
