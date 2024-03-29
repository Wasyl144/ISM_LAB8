package pollub.ism.lab6;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {PozycjaMagazynowa.class, History.class}, version = BazaMagazynowa.WERSJA, exportSchema = false)
public abstract class BazaMagazynowa extends RoomDatabase {
    public static final String NAZWA_BAZY = "Stoisko z warzywami";
    public static final int WERSJA = 7;

    public abstract PozycjaMagazynowaDAO pozycjaMagazynowaDAO();
    public abstract HistoryDAO historyDao();


}
