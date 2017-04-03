package fernando.sinopsefilm;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.MalformedJsonException;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import fernando.sinopsefilm.Model.Filme;

public class MainActivity extends AppCompatActivity
{
    private TextView textJSON;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void CarregaSinopse(View v)
    {
        textJSON = (TextView) findViewById(R.id.textFilm);
        String t = textJSON.getText().toString();
        new RequisicaoFilme().execute(t);

    }

    private class RequisicaoFilme extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... params) {
            String retorno = "";
            try {
                URL apiEnd = new URL("http://www.omdbapi.com/?t=" + params[0]);
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

                retorno = Util.rawToJson(is);
                is.close();
                conexao.disconnect();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return retorno;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Filme filme = Util.convertJSONtoFilme(s);
            TextView textSinopse = (TextView) findViewById(R.id.textSinopse);
            textSinopse.setText(filme.getPlot());
        }
    }

}

