package pollub.ism.lab6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.Calendar;
import java.util.Comparator;

import pollub.ism.lab6.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ArrayAdapter<CharSequence> adapter;
    private BazaMagazynowa bazaDanych;
    private String wybraneWarzywoNazwa = null;
    private Integer wybraneWarzywoIlosc = null;
    public enum OperacjaMagazynowa {SKLADUJ, WYDAJ};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter = ArrayAdapter.createFromResource(this, R.array.Asortyment, android.R.layout.simple_dropdown_item_1line);
        binding.spinner.setAdapter(adapter);
        bazaDanych = Room.databaseBuilder(getApplicationContext(), BazaMagazynowa.class, BazaMagazynowa.NAZWA_BAZY)
                .fallbackToDestructiveMigration().allowMainThreadQueries().build();

        if(bazaDanych.pozycjaMagazynowaDAO().size() == 0){
            String[] asortyment = getResources().getStringArray(R.array.Asortyment);
            for(String nazwa : asortyment){
                PozycjaMagazynowa pozycjaMagazynowa = new PozycjaMagazynowa();
                pozycjaMagazynowa.NAME = nazwa; pozycjaMagazynowa.QUANTITY = 0;
                bazaDanych.pozycjaMagazynowaDAO().insert(pozycjaMagazynowa);
            }
        }


        binding.addToDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zmienStan(OperacjaMagazynowa.SKLADUJ);
            }
        });

        binding.removeFromDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zmienStan(OperacjaMagazynowa.WYDAJ);
            }
        });

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                wybraneWarzywoNazwa = adapter.getItem(i).toString();
                aktualizuj();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Nie będziemy implementować, ale musi być
            }
        });
    }

    private void aktualizuj(){
        wybraneWarzywoIlosc = bazaDanych.pozycjaMagazynowaDAO().findQuantityByName(wybraneWarzywoNazwa);
        binding.state.setText("Stan magazynu dla " + wybraneWarzywoNazwa + " wynosi: " + wybraneWarzywoIlosc);
        binding.textInfo.setText(bazaDanych.historyDao().findUpdatesByItemName(wybraneWarzywoNazwa).toString());
        History history = bazaDanych.historyDao().findUpdatesByItemName(wybraneWarzywoNazwa).stream().max(Comparator.comparing(h -> h._id)).get();
        binding.unit.setText(history.toDate());
    }

    private void zmienStan(OperacjaMagazynowa operacja){


        Integer zmianaIlosci = null, nowaIlosc = null;

        try {
            zmianaIlosci = Integer.parseInt(binding.qunatity.getText().toString());
        }catch(NumberFormatException ex){
            return;
        }finally {
            binding.qunatity.setText("");
        }

        switch (operacja){
            case SKLADUJ: nowaIlosc = wybraneWarzywoIlosc + zmianaIlosci; break;
            case WYDAJ: nowaIlosc = wybraneWarzywoIlosc - zmianaIlosci; break;
        }

        PozycjaMagazynowa pozycjaMagazynowa = bazaDanych.pozycjaMagazynowaDAO().getAllByName(wybraneWarzywoNazwa);
        History history = new History();
        history.NAME = wybraneWarzywoNazwa;
        history.OLD_VALUE = pozycjaMagazynowa.QUANTITY;
        history.NEW_VALUE = nowaIlosc;
        history.UPDATED_AT = Calendar.getInstance().getTime().toString();
        bazaDanych.historyDao().insert(history);

        bazaDanych.pozycjaMagazynowaDAO().updateQuantityByName(wybraneWarzywoNazwa,nowaIlosc);

        aktualizuj();

    }
}
