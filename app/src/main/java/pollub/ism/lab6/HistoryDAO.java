package pollub.ism.lab6;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface HistoryDAO {
    @Insert
    void insert(History history);

    @Query("SELECT * FROM History WHERE NAME= :selectedItem")
    List<History> findUpdatesByItemName(String selectedItem);


}
