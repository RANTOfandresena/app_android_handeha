package com.example.myapplication;



import com.example.myapplication.model.PaiementModel;
import com.example.myapplication.outile.EncodeurTableauFixe;


import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws IOException, ParseException {
        String sms="600 Ar envoye a AINA MAMIRATRA HERIMANDOVA (0340264169) le 21/07/24 a 12:49. Frais: 50 Ar. Raison: ABw. Solde : 101 275 Ar. Ref: 1936783236";
        //sms="1/2 61 900 Ar recu de Lalaina Rovasoa Ervone (0340689895) le 06/07/24 a 09:38. Raison: 12. Solde : 61 925 Ar. Ref: 1369866461";
        PaiementModel paiement=PaiementModel.parseFromStringClient(sms);
        if (paiement != null) {
            System.out.println("Montant: " + paiement.getMontant());
            System.out.println("Nom Remettant: " + paiement.getNomRemetant());
            System.out.println("Numéro: " + paiement.getNumero());
            System.out.println("Date Paiement: " + paiement.getDatePaiement());
            System.out.println("Référence: " + paiement.getRef());
            System.out.println("Raison (Refapp): " + paiement.getRefapp());
        } else {
            System.out.println("Aucune correspondance trouvée.");
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