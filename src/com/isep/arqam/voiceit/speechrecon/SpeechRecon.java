package com.isep.arqam.voiceit.speechrecon;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.isep.arqam.voiceit.R;
import com.isep.arqam.voiceit.dropbox.DropboxMain;;

public class SpeechRecon extends Activity{

	ListView lv; //listView
	static final int check = 1111;
	String defaultName = null; //variável que armazena o nome por defeito recebido da activity anterior (MemoRecord)
	String newName = null; //variável que armazena o novo nome dado pelo utilizador
	
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
				// TODO Auto-generated method stub
				Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
				i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
				i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Dite o nome...");
				startActivityForResult(i, check);
			}
		});
		
		//BOTÃO QUE SALTA A OPÇÃO DE DITAR NOME
		Button bDefault = (Button)findViewById(R.id.defaultBt);
		bDefault.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent myIntent = new Intent(SpeechRecon.this, DropboxMain.class);
				newName = getIntent().getStringExtra("memoNameDefault"); //neste contexto a variável newName assume o nome por defeito
				myIntent.putExtra("memoName", newName); //envia o caminho do ficheiro para a activity DropboxTest
				myIntent.putExtra("DropboxTask", "upload");
				SpeechRecon.this.startActivity(myIntent); //inicia a activity DropboxTest
			}
		});
    }
    
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
					
					//mudança de nome do ficheiro
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
}
