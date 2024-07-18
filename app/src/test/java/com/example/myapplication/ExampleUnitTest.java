package com.example.myapplication;


import static com.example.myapplication.allConstant.Allconstant.URL_SERVER;
import static com.example.myapplication.outile.EncodeurTableauFixe.decodeString;
import static com.example.myapplication.outile.EncodeurTableauFixe.encodeArray;

import com.example.myapplication.apiClass.RetrofitClient;
import com.example.myapplication.apiService.ApiService;
import com.example.myapplication.model.PaiementModel;
import com.example.myapplication.model.RouteResponse;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.BitSet;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws IOException, ParseException {
        //Pattern pattern = Pattern.compile(
        //        "^([^/]+)/([^ ]+) ([^ ]+) Ar recu de \\(([^)]+)\\) le ([^ ]+) a ([^ ]+)\\. Raison: ([^.]+)\\. Solde: ([^ ]+) Ar\\. Ref: ([^ ]+)$");
        String input = "1/2 100 100 100 Ar recu de Lalaina Rovasoa Ervone (0340689895) le 06/07/24 a 09:38. Raison: fsfefef. Solde : 61 925 Ar. Ref: 1369866461";
        String input2 = "d,qsk k,sqk,s";
        PaiementModel paiement = PaiementModel.parseFromString(input2);
        if(paiement==null){
            System.out.println("impossible de parser");
        }else{
            System.out.println("ID Réservation: " + paiement.getIdPaiement());
            System.out.println("Montant: " + paiement.getMontant());
            System.out.println("Nom Remettant: " + paiement.getNomRemetant());
            System.out.println("Numéro: " + paiement.getNumero());
            System.out.println("Date Paiement: " + paiement.getDatePaiement());
            System.out.println("Référence: " + paiement.getRef());
            System.out.println("key: " + paiement.getRefapp());
        }


    }

    public static void printIntArray(int[] array) {
        System.out.print("[");
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i]);
            if (i < array.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }
}