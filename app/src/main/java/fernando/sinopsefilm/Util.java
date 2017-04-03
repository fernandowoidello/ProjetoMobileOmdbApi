package fernando.sinopsefilm;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.net.Uri;
import java.util.ArrayList;
import java.util.List;
import android.os.AsyncTask;

import fernando.sinopsefilm.Model.Filme;

/**
 * Created by camil on 26/03/2017.
 */

public class Util

{
    public static String rawToJson(InputStream inputStream) {
        InputStream localStream = inputStream;
        String jsonString = "";
        Writer writer = new StringWriter();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(localStream, "UTF-8"));
            String line = reader.readLine();
            while (line != null) {
                writer.write(line);
                line = reader.readLine();
            }
            jsonString = writer.toString();
            writer.close();
            reader.close();
            localStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonString;
    }

    /**
     * Lê um arquivo da pasta raw (Resources) e converte o mesmo em String.
     *
     * @param jsonFile Arquivo JSON com os dados a serem convertidos
     * @return Os dados adicionados na classe Filme.
     */
    public static Filme convertJSONtoFilme(String jsonFile) {
        Filme filme = null;
        try {
            filme = new Filme();
            // List<Faixa> faixas = new ArrayList<>();
            JSONObject mainObject = new JSONObject(jsonFile);
            String titulo = mainObject.getString("Title");
            int ano = mainObject.getInt("Year");
            String nota = mainObject.getString("Rated");
            String lancamento = mainObject.getString("Released");
            String runtime = mainObject.getString("Runtime");
            String genero = mainObject.getString("Genre");
            String diretor = mainObject.getString("Director");
            String escritor = mainObject.getString("Writer");
            String atores = mainObject.getString("Actors");
            String plot = mainObject.getString("Plot");
            String linguagem = mainObject.getString("Language");
            String pais = mainObject.getString("Country");
            String poster = mainObject.getString("Poster");

            //JSONArray faixasJson = mainObject.getJSONArray("Faixas");

            //  for(int i = 0; i < faixasJson.length(); i++){
            //JSONObject localObj = faixasJson.getJSONObject(i);
            // String titulo = localObj.getString("Titulo");
            //String duracao = localObj.getString("Duracao");
            //String vocal = localObj.getString("Vocal");
            // faixas.add(new Faixa(titulo,duracao,vocal));
            //}
            filme.setTitle(titulo);
            filme.setYear(ano);
            filme.setRated(nota);
            filme.setReleased(lancamento);
            filme.setRuntime(runtime);
            filme.setGenre(genero);
            filme.setDirector(diretor);
            filme.setWriter(escritor);
            filme.setActors(atores);
            filme.setPlot(plot);
            filme.setLanguage(linguagem);
            filme.setCountry(pais);
            filme.setPoster(poster);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return filme;
    }


    //Responsavel por carregar o Objeto JSON
    public static String getJSONFromAPI(String titulo) {
        String retorno = "";
        try {
            URL apiEnd = new URL("http://www.omdbapi.com/?t=" + titulo);
            int codigoResposta;
            HttpURLConnection conexao;
            InputStream is;

            conexao = (HttpURLConnection) apiEnd.openConnection();
            conexao.setRequestMethod("GET");
            conexao.setReadTimeout(15000);
            conexao.setConnectTimeout(15000);
            conexao.connect(); //InvocationTargetException

            codigoResposta = conexao.getResponseCode();
            if (codigoResposta < HttpURLConnection.HTTP_BAD_REQUEST) {
                is = conexao.getInputStream();
            } else {
                is = conexao.getErrorStream();
            }

            retorno = rawToJson(is);
            is.close();
            conexao.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return retorno;
    }
}