package br.com.rafaelfborges.cifracesar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;;

/**
 * @author Rafael Fernando Borges
 * 
 * Desafio processo seletivo para o AceleraDev.
 */
public class CifraCesar {
	
	/*
	 * Método estático para decifrar a mensagem.
	 */
	public static String decifra(String numeroCasas, String texto) {
		int tamanhoIndice = Integer.parseInt(numeroCasas);
		char[] vetorChar = new char[texto.length()];
		
		for(int i = 0; i < texto.length(); i++) {
			int letra = 0;
			char caracter = texto.charAt(i);
			
			if((int)caracter >= 97 && (int)caracter <= 122) {
				if((int)caracter-tamanhoIndice < 97) {
					int diferenca = ((int)caracter-tamanhoIndice) - 97;
					letra = 123 + diferenca;
				} else {
					letra = (int)caracter - tamanhoIndice;
				}
			} else if((int)caracter >= 32 && (int)caracter <= 47) {
				letra = (int)caracter;
			} 
			caracter = (char)letra;
			vetorChar[i] = caracter;
		}
		return new String(vetorChar);
	}
	
	/*
	 * Método estático para gravar o arquivo. 
	 */
	public static void gravarArquivo(String data) {
		try {
			Writer file = new FileWriter("answer.json", false);
	        file.write(data);
	        file.close();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@SuppressWarnings({ "unchecked" })
	public static void main(String[] args) {
		try {
			
			/*
			 * Realizando requisição com a API. 
			 */
			String token = ""; //Token do usuário
			URL url = new URL("https://api.codenation.dev/v1/challenge/dev-ps/generate-data?token=" + token);
			HttpURLConnection conexaoHttp = (HttpURLConnection) url.openConnection();
			
			System.out.println("Resposta 'HTTP': " + conexaoHttp.getResponseCode());
			BufferedReader dadosResposta = new BufferedReader(new InputStreamReader(conexaoHttp.getInputStream()));
			String inputLine;
			StringBuffer resposta = new StringBuffer();
			while ((inputLine = dadosResposta.readLine()) != null) {
				resposta.append(inputLine);
			}
			dadosResposta.close();
			
			/*
			 * Pegando conteúdo da requisição e instanciando para um JSONObject.
			 */
			JSONParser jsonParser = new JSONParser();
			JSONObject dadosDesafio = (JSONObject) jsonParser.parse(resposta.toString());
			
			/*
			 * Gravando o JSON da requisição em arquivo.
			 */
			gravarArquivo(dadosDesafio.toJSONString());
	        
	        /*
	         * Lendo o arquivo JSON e sobrepondo a instância atual do objeto.
	         */
	        Reader arquivoJson = new FileReader("answer.json");
	        dadosDesafio = (JSONObject) jsonParser.parse(arquivoJson);
	        
	        /*
	         * Decifrando a mensagem, atualizando a chave 'decifrado' do objeto e atualizando o arquivo.
	         */
	        String numeroCasas = dadosDesafio.get("numero_casas").toString();
	        String textoCifrado = dadosDesafio.get("cifrado").toString();        
	        String textoDecifrado = decifra(numeroCasas, textoCifrado);
	        dadosDesafio.put("decifrado", textoDecifrado);
	        gravarArquivo(dadosDesafio.toJSONString());
	        	        
	        /*
	         * Gerando o resumo criptográfico SHA-1, atualizando a chave 'resumo_cripografico' e atualizando o arquivo.
	         */
	        String resumoCriptografico = DigestUtils.sha1Hex(textoDecifrado);
	        dadosDesafio.put("resumo_criptografico", resumoCriptografico);
	        gravarArquivo(dadosDesafio.toJSONString());
	        
	        /*
	         * Enviando o arquivo answer.json para API como multipart/form-data, com o campo 'file' de nome 'answer'.
	         */
	        CloseableHttpClient clienteHttp = HttpClients.createDefault();
	        HttpPost uploadArquivo = new HttpPost("https://api.codenation.dev/v1/challenge/dev-ps/submit-solution?token=" + token);
	        MultipartEntityBuilder construtorFormulario = MultipartEntityBuilder.create();
	        
	        File arquivo = new File("answer.json");
	        construtorFormulario.addBinaryBody(
	            "answer",
	            new FileInputStream(arquivo),
	            ContentType.APPLICATION_OCTET_STREAM,
	            arquivo.getName()
	        );

	        HttpEntity formulario = construtorFormulario.build();
	        uploadArquivo.setEntity(formulario);
	        CloseableHttpResponse respostaHttp = clienteHttp.execute(uploadArquivo);
	        HttpEntity mensagemResposta = respostaHttp.getEntity();
	        String respostaApi = EntityUtils.toString(mensagemResposta, "UTF-8");
	        System.out.println(respostaApi);
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
}

