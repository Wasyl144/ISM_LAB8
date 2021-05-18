package pollub.ism.lab6;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "History")
public class History {
    @PrimaryKey(autoGenerate = true)
    public int _id;
    public String NAME;
    public int OLD_VALUE;
    public int NEW_VALUE;
    public String UPDATED_AT;


    @Override
    public String toString() {
        return "History{" +
                "NAME='" + NAME + '\'' +
                ", OLD_VALUE=" + OLD_VALUE +
                ", NEW_VALUE=" + NEW_VALUE +
                ", UPDATED_AT='" + UPDATED_AT + '\'' +
                '}';
    }

    public String toDate() {
        return ", UPDATED_AT='" + UPDATED_AT + '\'';
    }
}
